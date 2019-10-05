package eu.faircode.email;

/*
    This file is part of FairEmail.

    FairEmail is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FairEmail is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FairEmail.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2018-2019 by Marcel Bokhorst (M66B)
*/

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.webkit.CookieManager;

import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationEx extends Application {
    private Thread.UncaughtExceptionHandler prev = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(getLocalizedContext(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.logMemory(this, "App create version=" + BuildConfig.VERSION_NAME);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean crash_reports = prefs.getBoolean("crash_reports", false);

        prev = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                if (!crash_reports && Log.isOwnFault(ex)) {
                    Log.e(ex);

                    if (BuildConfig.BETA_RELEASE ||
                            !Helper.isPlayStoreInstall())
                        Log.writeCrashLog(ApplicationEx.this, ex);

                    if (prev != null)
                        prev.uncaughtException(thread, ex);
                } else {
                    Log.w(ex);
                    System.exit(1);
                }
            }
        });

        Log.setupBugsnag(this);

        upgrade(this);

        createNotificationChannels();

        if (Helper.hasWebView(this))
            CookieManager.getInstance().setAcceptCookie(false);

        MessageHelper.setSystemProperties(this);
        ContactInfo.init(this);

        WorkerWatchdog.init(this);
        WorkerCleanup.queue(this);
    }

    @Override
    public void onTrimMemory(int level) {
        Log.logMemory(this, "Trim memory level=" + level);
        Map<String, String> crumb = new HashMap<>();
        crumb.put("level", Integer.toString(level));
        crumb.put("free", Integer.toString(Log.getFreeMemMb()));
        Log.breadcrumb("trim", crumb);
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Log.logMemory(this, "Low memory");
        Map<String, String> crumb = new HashMap<>();
        crumb.put("free", Integer.toString(Log.getFreeMemMb()));
        Log.breadcrumb("low", crumb);
        super.onLowMemory();
    }

    static void upgrade(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int version = prefs.getInt("version", BuildConfig.VERSION_CODE);
        Log.i("Upgrading from " + version + " to " + BuildConfig.VERSION_CODE);

        SharedPreferences.Editor editor = prefs.edit();

        if (version < 468) {
            editor.remove("notify_trash");
            editor.remove("notify_archive");
            editor.remove("notify_reply");
            editor.remove("notify_flag");
            editor.remove("notify_seen");

        } else if (version < 601) {
            editor.putBoolean("contact_images", prefs.getBoolean("autoimages", true));
            editor.remove("autoimages");

        } else if (version < 612) {
            if (prefs.getBoolean("autonext", false))
                editor.putString("onclose", "next");
            editor.remove("autonext");

        } else if (version < 693) {
            editor.remove("message_swipe");
            editor.remove("message_select");

        } else if (version < 696) {
            String theme = prefs.getString("theme", "light");
            if ("grey".equals(theme))
                editor.putString("theme", "grey_dark");

            if (prefs.contains("ascending")) {
                editor.putBoolean("ascending_list", prefs.getBoolean("ascending", false));
                editor.remove("ascending");
            }

        } else if (version < 701) {
            if (prefs.getBoolean("suggest_local", false)) {
                editor.putBoolean("suggest_sent", true);
                editor.remove("suggest_local");
            }

        } else if (version < 703) {
            if (!prefs.getBoolean("style_toolbar", true)) {
                editor.putBoolean("compose_media", false);
                editor.remove("style_toolbar");
            }

        } else if (version < 709) {
            if (prefs.getBoolean("swipe_reversed", false)) {
                editor.putBoolean("reversed", true);
                editor.remove("swipe_reversed");
            }
        }


        if (BuildConfig.DEBUG && false) {
            editor.remove("app_support");
            editor.remove("notify_archive");
            editor.remove("message_swipe");
            editor.remove("message_select");
            editor.remove("folder_actions");
            editor.remove("folder_sync");
        }

        editor.putInt("version", BuildConfig.VERSION_CODE);

        editor.apply();
    }

    private void createNotificationChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Sync
            NotificationChannel service = new NotificationChannel(
                    "service", getString(R.string.channel_service),
                    NotificationManager.IMPORTANCE_MIN);
            service.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            service.setShowBadge(false);
            service.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            nm.createNotificationChannel(service);

            // Send
            NotificationChannel send = new NotificationChannel(
                    "send", getString(R.string.channel_send),
                    NotificationManager.IMPORTANCE_DEFAULT);
            send.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            send.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            nm.createNotificationChannel(send);

            // Notify
            NotificationChannel notification = new NotificationChannel(
                    "notification", getString(R.string.channel_notification),
                    NotificationManager.IMPORTANCE_HIGH);
            notification.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notification.enableLights(true);
            nm.createNotificationChannel(notification);

            // Update
            if (!Helper.isPlayStoreInstall()) {
                NotificationChannel update = new NotificationChannel(
                        "update", getString(R.string.channel_update),
                        NotificationManager.IMPORTANCE_HIGH);
                update.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                update.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                nm.createNotificationChannel(update);
            }

            // Warn
            NotificationChannel warning = new NotificationChannel(
                    "warning", getString(R.string.channel_warning),
                    NotificationManager.IMPORTANCE_HIGH);
            warning.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            nm.createNotificationChannel(warning);

            // Error
            NotificationChannel error = new NotificationChannel(
                    "error",
                    getString(R.string.channel_error),
                    NotificationManager.IMPORTANCE_HIGH);
            error.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            nm.createNotificationChannel(error);

            // Contacts grouping
            NotificationChannelGroup group = new NotificationChannelGroup(
                    "contacts",
                    getString(R.string.channel_group_contacts));
            nm.createNotificationChannelGroup(group);
        }
    }

    static Context getLocalizedContext(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean english = prefs.getBoolean("english", false);

        if (english) {
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(Locale.US);
            return context.createConfigurationContext(config);
        } else
            return context;
    }
}
