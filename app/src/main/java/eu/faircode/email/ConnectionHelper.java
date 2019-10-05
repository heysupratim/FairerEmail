package eu.faircode.email;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.preference.PreferenceManager;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

public class ConnectionHelper {
    // https://dns.watch/
    private static final String DEFAULT_DNS = "84.200.69.80";

    // Roam like at home
    // https://en.wikipedia.org/wiki/European_Union_roaming_regulations
    private static final List<String> RLAH_COUNTRY_CODES = Collections.unmodifiableList(Arrays.asList(
            "AT", // Austria
            "BE", // Belgium
            "BG", // Bulgaria
            "HR", // Croatia
            "CY", // Cyprus
            "CZ", // Czech Republic
            "DK", // Denmark
            "EE", // Estonia
            "FI", // Finland
            "FR", // France
            "DE", // Germany
            "GR", // Greece
            "HU", // Hungary
            "IS", // Iceland
            "IE", // Ireland
            "IT", // Italy
            "LV", // Latvia
            "LI", // Liechtenstein
            "LT", // Lithuania
            "LU", // Luxembourg
            "MT", // Malta
            "NL", // Netherlands
            "NO", // Norway
            "PL", // Poland
            "PT", // Portugal
            "RO", // Romania
            "SK", // Slovakia
            "SI", // Slovenia
            "ES", // Spain
            "SE", // Sweden
            "GB" // United Kingdom
    ));

    static class NetworkState {
        private Boolean connected = null;
        private Boolean suitable = null;
        private Boolean unmetered = null;
        private Boolean roaming = null;

        boolean isConnected() {
            return (connected != null && connected);
        }

        boolean isSuitable() {
            return (suitable != null && suitable);
        }

        boolean isUnmetered() {
            return (unmetered != null && unmetered);
        }

        boolean isRoaming() {
            return (roaming != null && roaming);
        }

        public void update(NetworkState newState) {
            connected = newState.connected;
            unmetered = newState.unmetered;
            suitable = newState.suitable;
        }
    }

    static NetworkState getNetworkState(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean metered = prefs.getBoolean("metered", true);
        boolean roaming = prefs.getBoolean("roaming", true);
        boolean rlah = prefs.getBoolean("rlah", true);

        NetworkState state = new NetworkState();
        try {
            Boolean isMetered = isMetered(context);
            state.connected = (isMetered != null);
            state.unmetered = (isMetered != null && !isMetered);
            state.suitable = (isMetered != null && (metered || !isMetered));

            if (state.connected && !roaming) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    NetworkInfo ani = (cm == null ? null : cm.getActiveNetworkInfo());
                    if (ani != null)
                        state.roaming = ani.isRoaming();
                } else {
                    Network active = (cm == null ? null : cm.getActiveNetwork());
                    if (active != null) {
                        NetworkCapabilities caps = cm.getNetworkCapabilities(active);
                        if (caps != null)
                            state.roaming = !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING);
                    }
                }

                if (state.roaming != null && state.roaming && rlah)
                    try {
                        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (tm != null) {
                            String sim = tm.getSimCountryIso();
                            String network = tm.getNetworkCountryIso();
                            Log.i("Country SIM=" + sim + " network=" + network);
                            if (sim != null && network != null &&
                                    RLAH_COUNTRY_CODES.contains(sim) &&
                                    RLAH_COUNTRY_CODES.contains(network))
                                state.roaming = false;
                        }
                    } catch (Throwable ex) {
                        Log.w(ex);
                    }
            }
        } catch (Throwable ex) {
            Log.e(ex);
        }

        return state;
    }

    private static Boolean isMetered(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            NetworkInfo ani = (cm == null ? null : cm.getActiveNetworkInfo());
            if (ani == null || !ani.isConnected())
                return null;
            return cm.isActiveNetworkMetered();
        }

        Network active = (cm == null ? null : cm.getActiveNetwork());
        if (active == null) {
            Log.i("isMetered: no active network");
            return null;
        }

        // onLost [... state: DISCONNECTED/DISCONNECTED ... available: true]
        NetworkInfo ani = cm.getNetworkInfo(active);
        if (ani == null || !ani.isConnected())
            return null;

        NetworkCapabilities caps = cm.getNetworkCapabilities(active);
        if (caps == null) {
            Log.i("isMetered: active no caps");
            return null; // network unknown
        }

        Log.i("isMetered: active caps=" + caps);

        if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN) &&
                !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            Log.i("isMetered: no internet");
            return null;
        }

        if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)) {
            Log.i("isMetered: active restricted");
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)) {
            Log.i("isMetered: active background");
            return null;
        }

        if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)) {
            boolean unmetered = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            Log.i("isMetered: active not VPN unmetered=" + unmetered);
            return !unmetered;
        }

        // VPN: evaluate underlying networks

        boolean underlying = false;
        Network[] networks = cm.getAllNetworks();
        for (Network network : networks) {
            caps = cm.getNetworkCapabilities(network);
            if (caps == null) {
                Log.i("isMetered: no underlying caps");
                continue; // network unknown
            }

            Log.i("isMetered: underlying caps=" + caps);

            if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                Log.i("isMetered: underlying no internet");
                continue;
            }

            if (!caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)) {
                Log.i("isMetered: underlying restricted");
                continue;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P &&
                    !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)) {
                Log.i("isMetered: underlying background");
                continue;
            }

            if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)) {
                underlying = true;
                Log.i("isMetered: underlying is connected");

                if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)) {
                    Log.i("isMetered: underlying is unmetered");
                    return false;
                }
            }
        }

        if (!underlying) {
            Log.i("isMetered: no underlying network");
            return null;
        }

        // Assume metered
        Log.i("isMetered: underlying assume metered");
        return true;
    }

    static boolean airplaneMode(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    static String getDnsServer(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return DEFAULT_DNS;

        LinkProperties props = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            for (Network network : cm.getAllNetworks()) {
                NetworkInfo ni = cm.getNetworkInfo(network);
                if (ni != null && ni.isConnected()) {
                    props = cm.getLinkProperties(network);
                    Log.i("Old props=" + props);
                    break;
                }
            }
        else {
            Network active = cm.getActiveNetwork();
            if (active == null)
                return DEFAULT_DNS;
            props = cm.getLinkProperties(active);
            Log.i("New props=" + props);
        }

        if (props == null)
            return DEFAULT_DNS;

        List<InetAddress> dns = props.getDnsServers();
        if (dns.size() == 0)
            return DEFAULT_DNS;
        else
            return dns.get(0).getHostAddress();
    }

    static boolean lookupMx(Address[] addresses, Context context) throws UnknownHostException {
        boolean ok = true;

        if (addresses != null)
            for (Address address : addresses)
                try {
                    String email = ((InternetAddress) address).getAddress();
                    if (email == null || !email.contains("@"))
                        continue;
                    String domain = email.split("@")[1];
                    Lookup lookup = new Lookup(domain, Type.MX);
                    SimpleResolver resolver = new SimpleResolver(ConnectionHelper.getDnsServer(context));
                    lookup.setResolver(resolver);
                    Log.i("Lookup MX=" + domain + " @" + resolver.getAddress());

                    lookup.run();
                    if (lookup.getResult() == Lookup.HOST_NOT_FOUND ||
                            lookup.getResult() == Lookup.TYPE_NOT_FOUND) {
                        Log.i("Lookup MX=" + domain + " result=" + lookup.getErrorString());
                        throw new UnknownHostException(context.getString(R.string.title_no_server, domain));
                    } else if (lookup.getResult() != Lookup.SUCCESSFUL)
                        ok = false;
                } catch (UnknownHostException ex) {
                    throw ex;
                } catch (Throwable ex) {
                    Log.e(ex);
                    ok = false;
                }

        return ok;
    }

    static InetAddress lookupMx(String domain, Context context) {
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            SimpleResolver resolver = new SimpleResolver(getDnsServer(context));
            lookup.setResolver(resolver);
            Log.i("Lookup MX=" + domain + " @" + resolver.getAddress());

            lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                Record[] answers = lookup.getAnswers();
                if (answers != null && answers.length > 0 && answers[0] instanceof MXRecord) {
                    MXRecord mx = (MXRecord) answers[0];
                    return InetAddress.getByName(mx.getTarget().toString(true));
                }
            }
        } catch (Throwable ex) {
            Log.w(ex);
        }

        return null;
    }

    static boolean isSameDomain(String host1, String host2) {
        String domain1 = getDomain(host1);
        String domain2 = getDomain(host2);
        return Objects.equals(domain1, domain2);
    }

    private static String getDomain(String host) {
        if (host != null) {
            String[] h = host.split("\\.");
            if (h.length >= 2)
                return h[h.length - 2] + "." + h[h.length - 1];
        }
        return host;
    }
}
