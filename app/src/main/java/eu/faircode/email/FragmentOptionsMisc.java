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

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;

public class FragmentOptionsMisc extends FragmentBase implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SwitchCompat swDoubleBack;
    private Spinner spBiometricsTimeout;
    private SwitchCompat swEnglish;
    private SwitchCompat swWatchdog;
    private SwitchCompat swUpdates;
    private SwitchCompat swExperiments;
    private TextView tvExperimentsHint;
    private SwitchCompat swCrashReports;
    private SwitchCompat swDebug;
    private Button btnCleanup;

    private TextView tvProcessors;
    private TextView tvMemoryClass;
    private TextView tvLastCleanup;
    private TextView tvUuid;

    private Group grpDebug;

    private final static String[] RESET_OPTIONS = new String[]{
            "double_back", "biometrics_timeout", "english", "watchdog", "updates", "experiments", "crash_reports", "debug"
    };

    private final static String[] RESET_QUESTIONS = new String[]{
            "welcome", "crash_reports_asked",
            "show_html_confirmed", "print_html_confirmed",
            "identities_asked", "edit_ref_confirmed", "delete_ref_confirmed"
    };

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setSubtitle(R.string.title_setup);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_options_misc, container, false);

        // Get controls

        swDoubleBack = view.findViewById(R.id.swDoubleBack);
        spBiometricsTimeout = view.findViewById(R.id.spBiometricsTimeout);
        swEnglish = view.findViewById(R.id.swEnglish);
        swWatchdog = view.findViewById(R.id.swWatchdog);
        swUpdates = view.findViewById(R.id.swUpdates);
        swExperiments = view.findViewById(R.id.swExperiments);
        tvExperimentsHint = view.findViewById(R.id.tvExperimentsHint);
        swCrashReports = view.findViewById(R.id.swCrashReports);
        swDebug = view.findViewById(R.id.swDebug);
        btnCleanup = view.findViewById(R.id.btnCleanup);

        tvProcessors = view.findViewById(R.id.tvProcessors);
        tvMemoryClass = view.findViewById(R.id.tvMemoryClass);
        tvLastCleanup = view.findViewById(R.id.tvLastCleanup);
        tvUuid = view.findViewById(R.id.tvUuid);

        grpDebug = view.findViewById(R.id.grpDebug);

        setOptions();

        // Wire controls

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        swDoubleBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("double_back", checked).apply();
            }
        });

        spBiometricsTimeout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int[] values = getResources().getIntArray(R.array.biometricsTimeoutValues);
                prefs.edit().putInt("biometrics_timeout", values[position]).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                prefs.edit().remove("biometrics_timeout").apply();
            }
        });

        swEnglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("english", checked).commit(); // apply won't work here
                restart();
            }
        });

        swWatchdog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("watchdog", checked).apply();
                WorkerWatchdog.init(getContext());
            }
        });

        swUpdates.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("updates", checked).apply();
                if (!checked) {
                    NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.cancel(Helper.NOTIFICATION_UPDATE);
                }
            }
        });

        tvExperimentsHint.setPaintFlags(tvExperimentsHint.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvExperimentsHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.viewFAQ(getContext(), 125);
            }
        });

        swExperiments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("experiments", checked).apply();
            }
        });

        swCrashReports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit()
                        .remove("crash_reports_asked")
                        .putBoolean("crash_reports", checked)
                        .apply();
                Log.setCrashReporting(checked);
            }
        });

        swDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                prefs.edit().putBoolean("debug", checked).apply();
                grpDebug.setVisibility(checked || BuildConfig.DEBUG ? View.GONE : View.GONE);
                ServiceSynchronize.reload(getContext(), "debug=" + checked);
            }
        });

        btnCleanup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCleanup();
            }
        });

        setLastCleanup(prefs.getLong("last_cleanup", -1));

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            setOptions();
            if ("last_cleanup".equals(key))
                setLastCleanup(prefs.getLong(key, -1));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_options_misc, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_default:
                onMenuDefault();
                return true;
            case R.id.menu_reset_questions:
                onMenuResetQuestions();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onMenuDefault() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        for (String option : RESET_OPTIONS)
            editor.remove(option);
        editor.apply();
        ToastEx.makeText(getContext(), R.string.title_setup_done, Toast.LENGTH_LONG).show();
    }

    private void onMenuResetQuestions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        for (String option : RESET_QUESTIONS)
            editor.remove(option);
        for (String key : prefs.getAll().keySet())
            if (key.endsWith(".show_images"))
                editor.remove(key);
        editor.apply();
        ToastEx.makeText(getContext(), R.string.title_setup_done, Toast.LENGTH_LONG).show();
    }

    private void onCleanup() {
        new SimpleTask<Void>() {
            @Override
            protected void onPreExecute(Bundle args) {
                btnCleanup.setEnabled(false);
                ToastEx.makeText(getContext(), R.string.title_executing, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onPostExecute(Bundle args) {
                btnCleanup.setEnabled(true);
                ToastEx.makeText(getContext(), R.string.title_completed, Toast.LENGTH_LONG).show();
            }

            @Override
            protected Void onExecute(Context context, Bundle args) {
                WorkerCleanup.cleanup(context, true);
                return null;
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                Helper.unexpectedError(getFragmentManager(), ex);
            }
        }.execute(this, new Bundle(), "cleanup:run");
    }

    private void setOptions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        swDoubleBack.setChecked(prefs.getBoolean("double_back", true));

        int biometrics_timeout = prefs.getInt("biometrics_timeout", 2);
        int[] biometricTimeoutValues = getResources().getIntArray(R.array.biometricsTimeoutValues);
        for (int pos = 0; pos < biometricTimeoutValues.length; pos++)
            if (biometricTimeoutValues[pos] == biometrics_timeout) {
                spBiometricsTimeout.setSelection(pos);
                break;
            }

        swEnglish.setChecked(prefs.getBoolean("english", false));
        swWatchdog.setChecked(prefs.getBoolean("watchdog", true));
        swUpdates.setChecked(prefs.getBoolean("updates", true));
        swUpdates.setVisibility(
                Helper.isPlayStoreInstall() || !Helper.hasValidFingerprint(getContext())
                        ? View.GONE : View.VISIBLE);
        swExperiments.setChecked(prefs.getBoolean("experiments", false));
        swCrashReports.setChecked(prefs.getBoolean("crash_reports", false));
        swDebug.setChecked(prefs.getBoolean("debug", false));

        tvProcessors.setText(getString(R.string.title_advanced_processors, Runtime.getRuntime().availableProcessors()));

        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        int class_mb = am.getMemoryClass();
        tvMemoryClass.setText(getString(R.string.title_advanced_memory_class, class_mb + " MB"));
        tvUuid.setText(prefs.getString("uuid", null));

        grpDebug.setVisibility(swDebug.isChecked() || BuildConfig.DEBUG ? View.GONE : View.GONE);
    }

    private void setLastCleanup(long time) {
        java.text.DateFormat DTF = Helper.getDateTimeInstance(getContext());
        tvLastCleanup.setText(
                getString(R.string.title_advanced_last_cleanup,
                        time < 0 ? "-" : DTF.format(time)));
    }

    private void restart() {
        Intent intent = new Intent(getContext(), ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Runtime.getRuntime().exit(0);
    }
}
