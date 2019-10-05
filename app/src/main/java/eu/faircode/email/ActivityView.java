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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ActivityView extends ActivityBilling implements FragmentManager.OnBackStackChangedListener {
    private String startup;

    private View view;

    private View content_separator;
    private View content_pane;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ScrollView drawerContainer;
    private RecyclerView rvAccount;
    private ImageButton ibExpanderUnified;
    private RecyclerView rvUnified;
    private RecyclerView rvFolder;
    private RecyclerView rvMenu;
    private ImageButton ibExpanderExtra;
    private RecyclerView rvMenuExtra;
    private Group grpUnified;

    private boolean exit = false;
    private boolean searching = false;

    static final int REQUEST_UNIFIED = 1;
    static final int REQUEST_WHY = 2;
    static final int REQUEST_THREAD = 3;
    static final int REQUEST_OUTBOX = 4;
    static final int REQUEST_ERROR = 5;
    static final int REQUEST_UPDATE = 6;
    static final int REQUEST_WIDGET = 7;

    static final String ACTION_VIEW_FOLDERS = BuildConfig.APPLICATION_ID + ".VIEW_FOLDERS";
    static final String ACTION_VIEW_MESSAGES = BuildConfig.APPLICATION_ID + ".VIEW_MESSAGES";
    static final String ACTION_SEARCH = BuildConfig.APPLICATION_ID + ".SEARCH";
    static final String ACTION_VIEW_THREAD = BuildConfig.APPLICATION_ID + ".VIEW_THREAD";
    static final String ACTION_EDIT_FOLDER = BuildConfig.APPLICATION_ID + ".EDIT_FOLDER";
    static final String ACTION_EDIT_ANSWERS = BuildConfig.APPLICATION_ID + ".EDIT_ANSWERS";
    static final String ACTION_EDIT_ANSWER = BuildConfig.APPLICATION_ID + ".EDIT_ANSWER";
    static final String ACTION_EDIT_RULES = BuildConfig.APPLICATION_ID + ".EDIT_RULES";
    static final String ACTION_EDIT_RULE = BuildConfig.APPLICATION_ID + ".EDIT_RULE";

    private static final long EXIT_DELAY = 2500L; // milliseconds
    static final long UPDATE_INTERVAL = (BuildConfig.BETA_RELEASE ? 4 : 12) * 3600 * 1000L; // milliseconds

    @Override
    @SuppressLint("MissingSuperCall")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, false);

        if (savedInstanceState != null)
            searching = savedInstanceState.getBoolean("fair:searching");

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        startup = prefs.getString("startup", "unified");

        view = LayoutInflater.from(this).inflate(R.layout.activity_view, null);
        setContentView(view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        content_separator = findViewById(R.id.content_separator);
        content_pane = findViewById(R.id.content_pane);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Helper.resolveColor(this, R.attr.colorDrawerScrim));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        drawerContainer = findViewById(R.id.drawer_container);

        // Accounts
        rvAccount = drawerContainer.findViewById(R.id.rvAccount);
        rvAccount.setLayoutManager(new LinearLayoutManager(this));
        final AdapterNavAccount aadapter = new AdapterNavAccount(this, this);
        rvAccount.setAdapter(aadapter);

        // Unified system folders
        ibExpanderUnified = drawerContainer.findViewById(R.id.ibExpanderUnified);
        ibExpanderUnified.setVisibility(View.GONE);

        grpUnified = drawerContainer.findViewById(R.id.grpUnified);
        grpUnified.setVisibility(View.GONE);

        rvUnified = drawerContainer.findViewById(R.id.rvUnified);
        rvUnified.setLayoutManager(new LinearLayoutManager(this));
        final AdapterNavUnified uadapter = new AdapterNavUnified(this, this);
        rvUnified.setAdapter(uadapter);

        boolean unified_system = prefs.getBoolean("unified_system", false);
        ibExpanderUnified.setImageLevel(unified_system ? 0 /* less */ : 1 /* more */);
        grpUnified.setVisibility(unified_system ? View.VISIBLE : View.GONE);

        ibExpanderUnified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean unified_system = !prefs.getBoolean("unified_system", false);
                prefs.edit().putBoolean("unified_system", unified_system).apply();
                ibExpanderUnified.setImageLevel(unified_system ? 0 /* less */ : 1 /* more */);
                grpUnified.setVisibility(unified_system ? View.VISIBLE : View.GONE);
            }
        });

        // Navigation folders
        rvFolder = drawerContainer.findViewById(R.id.rvFolder);
        rvFolder.setLayoutManager(new LinearLayoutManager(this));
        final AdapterNavFolder fadapter = new AdapterNavFolder(this, this);
        rvFolder.setAdapter(fadapter);

        rvMenu = drawerContainer.findViewById(R.id.rvMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        final AdapterNavMenu madapter = new AdapterNavMenu(this, this);
        rvMenu.setAdapter(madapter);

        // Extra menus
        ibExpanderExtra = drawerContainer.findViewById(R.id.ibExpanderExtra);

        rvMenuExtra = drawerContainer.findViewById(R.id.rvMenuExtra);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvMenuExtra.setLayoutManager(llm);
        final AdapterNavMenu eadapter = new AdapterNavMenu(this, this);
        rvMenuExtra.setAdapter(eadapter);

        final Drawable d = getDrawable(R.drawable.divider);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, llm.getOrientation()) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int pos = parent.getChildAdapterPosition(view);
                NavMenuItem menu = eadapter.get(pos);
                outRect.set(0, 0, 0, menu != null && menu.isSeparated() ? d.getIntrinsicHeight() : 0);
            }
        };
        itemDecorator.setDrawable(d);
        //rvMenuExtra.addItemDecoration(itemDecorator);

        boolean minimal = prefs.getBoolean("minimal", false);
        ibExpanderExtra.setImageLevel(minimal ? 1 /* more */ : 0 /* less */);
        rvMenuExtra.setVisibility(minimal ? View.GONE : View.VISIBLE);

        ibExpanderExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean minimal = !prefs.getBoolean("minimal", false);
                prefs.edit().putBoolean("minimal", minimal).apply();
                ibExpanderExtra.setImageLevel(minimal ? 1 /* more */ : 0 /* less */);
                rvMenuExtra.setVisibility(minimal ? View.GONE : View.VISIBLE);
                if (!minimal)
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            drawerContainer.fullScroll(View.FOCUS_DOWN);
                        }
                    });
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        // Fixed menus

        PackageManager pm = getPackageManager();
        final List<NavMenuItem> menus = new ArrayList<>();

        final NavMenuItem navOperations = new NavMenuItem(R.drawable.baseline_dns_24, R.string.menu_operations, new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(drawerContainer);
                onMenuOperations();
            }
        });

        //menus.add(navOperations);

//        menus.add(new NavMenuItem(R.drawable.baseline_list_24, R.string.title_log, new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(drawerContainer);
//                onShowLog();
//            }
//        }));

        menus.add(new NavMenuItem(R.drawable.baseline_reply_24, R.string.menu_answers, new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(drawerContainer);
                onMenuAnswers();
            }
        }));

        menus.add(new NavMenuItem(R.drawable.baseline_settings_applications_24, R.string.menu_setup, new Runnable() {
            @Override
            public void run() {
                drawerLayout.closeDrawer(drawerContainer);
                onMenuSetup();
            }
        }));

        madapter.set(menus);

        // Collapsible menus

        List<NavMenuItem> extra = new ArrayList<>();
//        extra.add(new NavMenuItem(R.drawable.baseline_help_24, R.string.menu_legend, new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(drawerContainer);
//                onMenuLegend();
//            }
//        }));

//        extra.add(new NavMenuItem(R.drawable.baseline_question_answer_24, R.string.menu_faq, new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(drawerContainer);
//                onMenuFAQ();
//            }
//        }, new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(drawerContainer);
//                onDebugInfo();
//            }
//        }).setExternal(true));

//        if (Helper.getIntentIssue(this).resolveActivity(pm) != null)
//            extra.add(new NavMenuItem(R.drawable.baseline_feedback_24, R.string.menu_issue, new Runnable() {
//                @Override
//                public void run() {
//                    drawerLayout.closeDrawer(drawerContainer);
//                    onMenuIssue();
//                }
//            }).setExternal(true));

//        extra.add(new NavMenuItem(R.drawable.baseline_account_box_24, R.string.menu_privacy, new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(drawerContainer);
//                onMenuPrivacy();
//            }
//        }));

//        extra.add(new NavMenuItem(R.drawable.baseline_info_24, R.string.menu_about, new Runnable() {
//            @Override
//            public void run() {
//                onMenuAbout();
//            }
//        }, new Runnable() {
//            @Override
//            public void run() {
//                if (!Helper.isPlayStoreInstall()) {
//                    drawerLayout.closeDrawer(drawerContainer);
//                    checkUpdate(true);
//                }
//            }
//        }).setSeparated());

//        extra.add(new NavMenuItem(R.drawable.baseline_monetization_on_24, R.string.menu_pro, new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(drawerContainer);
//                startActivity(new Intent(ActivityView.this, ActivityBilling.class));
//            }
//        }));

//        if ((getIntentInvite(this).resolveActivity(pm) != null))
//            extra.add(new NavMenuItem(R.drawable.baseline_people_24, R.string.menu_invite, new Runnable() {
//                @Override
//                public void run() {
//                    drawerLayout.closeDrawer(drawerContainer);
//                    onMenuInvite();
//                }
//            }).setExternal(true));

//        if ((Helper.isPlayStoreInstall() || BuildConfig.DEBUG) &&
//                getIntentRate(this).resolveActivity(pm) != null)
//            extra.add(new NavMenuItem(R.drawable.baseline_star_24, R.string.menu_rate, new Runnable() {
//                @Override
//                public void run() {
//                    drawerLayout.closeDrawer(drawerContainer);
//                    onMenuRate();
//                }
//            }).setExternal(true));

//        if (getIntentOtherApps().resolveActivity(pm) != null)
//            extra.add(new NavMenuItem(R.drawable.baseline_get_app_24, R.string.menu_other, new Runnable() {
//                @Override
//                public void run() {
//                    drawerLayout.closeDrawer(drawerContainer);
//                    onMenuOtherApps();
//                }
//            }).setExternal(true));

        //eadapter.set(extra);

        // Live data

        DB db = DB.getInstance(this);

        db.account().liveAccountsEx(false).observe(this, new Observer<List<TupleAccountEx>>() {
            @Override
            public void onChanged(@Nullable List<TupleAccountEx> accounts) {
                if (accounts == null)
                    accounts = new ArrayList<>();
                aadapter.set(accounts);
            }
        });

        db.folder().liveUnified().observe(this, new Observer<List<EntityFolderUnified>>() {
            @Override
            public void onChanged(List<EntityFolderUnified> folders) {
                if (folders == null)
                    folders = new ArrayList<>();
                ibExpanderUnified.setVisibility(folders.size() > 0 ? View.VISIBLE : View.GONE);
                boolean unified_system = prefs.getBoolean("unified_system", false);
                grpUnified.setVisibility(unified_system && folders.size() > 0 ? View.VISIBLE : View.GONE);
                uadapter.set(folders);
            }
        });

        db.folder().liveNavigation().observe(this, new Observer<List<TupleFolderNav>>() {
            @Override
            public void onChanged(List<TupleFolderNav> folders) {
                if (folders == null)
                    folders = new ArrayList<>();
                fadapter.set(folders);
            }
        });

        db.operation().liveStats().observe(this, new Observer<TupleOperationStats>() {
            @Override
            public void onChanged(TupleOperationStats stats) {
                navOperations.setWarning(stats != null && stats.errors != null && stats.errors > 0);
                navOperations.setCount(stats == null ? 0 : stats.pending);
                madapter.notifyDataSetChanged();
            }
        });

        // Initialize

        if (content_pane != null) {
            content_separator.setVisibility(View.GONE);
            content_pane.setVisibility(View.GONE);
        }

        if (getSupportFragmentManager().getFragments().size() == 0 &&
                !getIntent().hasExtra(Intent.EXTRA_PROCESS_TEXT))
            init();

        if (savedInstanceState != null)
            drawerToggle.setDrawerIndicatorEnabled(savedInstanceState.getBoolean("fair:toggle"));

        checkFirst();
        checkCrash();

        Shortcuts.update(this, this);
    }

    private void init() {
        Bundle args = new Bundle();

        FragmentBase fragment;
        switch (startup) {
            case "accounts":
                fragment = new FragmentAccounts();
                args.putBoolean("settings", false);
                break;
            case "folders":
                fragment = new FragmentFolders();
                break;
            default:
                fragment = new FragmentMessages();
        }

        fragment.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        for (Fragment existing : fm.getFragments())
            fragmentTransaction.remove(existing);
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("unified");
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("fair:toggle", drawerToggle.isDrawerIndicatorEnabled());
        outState.putBoolean("fair:searching", searching);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        IntentFilter iff = new IntentFilter();
        iff.addAction(ACTION_VIEW_FOLDERS);
        iff.addAction(ACTION_VIEW_MESSAGES);
        iff.addAction(ACTION_SEARCH);
        iff.addAction(ACTION_VIEW_THREAD);
        iff.addAction(ACTION_EDIT_FOLDER);
        iff.addAction(ACTION_EDIT_ANSWERS);
        iff.addAction(ACTION_EDIT_ANSWER);
        iff.addAction(ACTION_EDIT_RULES);
        iff.addAction(ACTION_EDIT_RULE);
        lbm.registerReceiver(receiver, iff);

        checkUpdate(false);
        checkIntent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.unregisterReceiver(receiver);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerContainer))
            drawerLayout.closeDrawer(drawerContainer);
        else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (exit || count > 1)
                super.onBackPressed();
            else if (!backHandled()) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getOriginalContext());
                boolean double_back = prefs.getBoolean("double_back", true);
                if (searching || !double_back)
                    super.onBackPressed();
                else {
                    exit = true;
                    ToastEx.makeText(this, R.string.app_exit, Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, EXIT_DELAY);
                }
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
            finish();
        else {
            if (drawerLayout.isDrawerOpen(drawerContainer))
                drawerLayout.closeDrawer(drawerContainer);
            drawerToggle.setDrawerIndicatorEnabled(count == 1);

            if (content_pane != null) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_pane);
                content_separator.setVisibility(fragment == null ? View.GONE : View.VISIBLE);
                content_pane.setVisibility(fragment == null ? View.GONE : View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void checkFirst() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("first", true))
            new FragmentDialogFirst().show(getSupportFragmentManager(), "first");
    }

    private void checkCrash() {
        new SimpleTask<Long>() {
            @Override
            protected Long onExecute(Context context, Bundle args) throws Throwable {
                File file = new File(context.getCacheDir(), "crash.log");
                if (file.exists()) {
                    StringBuilder sb = new StringBuilder();
                    try {
                        String line;
                        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                            while ((line = in.readLine()) != null)
                                sb.append(line).append("\r\n");
                        }

                        return Log.getDebugInfo(context, R.string.title_crash_info_remark, null, sb.toString()).id;
                    } finally {
                        file.delete();
                    }
                }

                return null;
            }

            @Override
            protected void onExecuted(Bundle args, Long id) {
                if (id != null)
                    startActivity(
                            new Intent(ActivityView.this, ActivityCompose.class)
                                    .putExtra("action", "edit")
                                    .putExtra("id", id));
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                ToastEx.makeText(ActivityView.this,
                        Helper.formatThrowable(ex, false), Toast.LENGTH_LONG).show();
            }
        }.execute(this, new Bundle(), "crash:log");
    }

    private void checkUpdate(boolean always) {
        if (Helper.isPlayStoreInstall() || !Helper.hasValidFingerprint(this))
            return;

        long now = new Date().getTime();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!always && !prefs.getBoolean("updates", true))
            return;
        if (!always && prefs.getLong("last_update_check", 0) + UPDATE_INTERVAL > now)
            return;
        prefs.edit().putLong("last_update_check", now).apply();

        Bundle args = new Bundle();
        args.putBoolean("always", always);

        new SimpleTask<UpdateInfo>() {
            @Override
            protected UpdateInfo onExecute(Context context, Bundle args) throws Throwable {
                StringBuilder response = new StringBuilder();
                HttpsURLConnection urlConnection = null;
                try {
                    URL latest = new URL(BuildConfig.GITHUB_LATEST_API);
                    urlConnection = (HttpsURLConnection) latest.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(false);
                    urlConnection.connect();

                    int status = urlConnection.getResponseCode();
                    InputStream inputStream = (status == HttpsURLConnection.HTTP_OK
                            ? urlConnection.getInputStream() : urlConnection.getErrorStream());

                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = br.readLine()) != null)
                        response.append(line);

                    if (status == HttpsURLConnection.HTTP_FORBIDDEN) {
                        // {"message":"API rate limit exceeded for ...","documentation_url":"https://developer.github.com/v3/#rate-limiting"}
                        JSONObject jmessage = new JSONObject(response.toString());
                        if (jmessage.has("message"))
                            throw new IllegalArgumentException(jmessage.getString("message"));
                        throw new IOException("HTTP " + status + ": " + response.toString());
                    }
                    if (status != HttpsURLConnection.HTTP_OK)
                        throw new IOException("HTTP " + status + ": " + response.toString());

                    JSONObject jroot = new JSONObject(response.toString());

                    if (!jroot.has("tag_name") || jroot.isNull("tag_name"))
                        throw new IOException("tag_name field missing");
                    if (!jroot.has("html_url") || jroot.isNull("html_url"))
                        throw new IOException("html_url field missing");
                    if (!jroot.has("assets") || jroot.isNull("assets"))
                        throw new IOException("assets section missing");

                    // Get update info
                    UpdateInfo info = new UpdateInfo();
                    info.tag_name = jroot.getString("tag_name");
                    info.html_url = jroot.getString("html_url");

                    // Check if new release
                    JSONArray jassets = jroot.getJSONArray("assets");
                    for (int i = 0; i < jassets.length(); i++) {
                        JSONObject jasset = jassets.getJSONObject(i);
                        if (jasset.has("name") && !jasset.isNull("name")) {
                            String name = jasset.getString("name");
                            if (name.endsWith(".apk")) {
                                Log.i("Latest version=" + info.tag_name);
                                if (BuildConfig.VERSION_NAME.equals(info.tag_name))
                                    return null;
                                else
                                    return info;
                            }
                        }
                    }

                    return null;
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }

            @Override
            protected void onExecuted(Bundle args, UpdateInfo info) {
                boolean always = args.getBoolean("always");
                if (info == null) {
                    if (always)
                        ToastEx.makeText(ActivityView.this, BuildConfig.VERSION_NAME, Toast.LENGTH_LONG).show();
                    return;
                }

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(ActivityView.this, "update")
                                .setSmallIcon(R.drawable.baseline_get_app_24)
                                .setContentTitle(getString(R.string.title_updated, info.tag_name))
                                .setAutoCancel(true)
                                .setShowWhen(false)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                                .setVisibility(NotificationCompat.VISIBILITY_SECRET);

                Intent update = new Intent(Intent.ACTION_VIEW, Uri.parse(info.html_url));
                if (update.resolveActivity(getPackageManager()) != null) {
                    PendingIntent piUpdate = PendingIntent.getActivity(
                            ActivityView.this, REQUEST_UPDATE, update, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(piUpdate);
                }

                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(Helper.NOTIFICATION_UPDATE, builder.build());
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                if (args.getBoolean("always"))
                    if (ex instanceof IllegalArgumentException || ex instanceof IOException)
                        ToastEx.makeText(ActivityView.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                    else
                        Helper.unexpectedError(getSupportFragmentManager(), ex);
            }
        }.execute(this, args, "update:check");
    }

    private void checkIntent() {
        Intent intent = getIntent();

        if (intent.getBooleanExtra("refresh", false)) {
            intent.removeExtra("refresh");
            setIntent(intent);
            ServiceSynchronize.process(this, true);
        }

        String action = intent.getAction();
        Log.i("View intent=" + intent + " action=" + action);
        if (action != null) {
            intent.setAction(null);
            setIntent(intent);

            if (action.startsWith("unified")) {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                    getSupportFragmentManager().popBackStack("unified", 0);

                if (action.contains(":")) {
                    Intent clear = new Intent(this, ServiceUI.class)
                            .setAction(action.replace("unified", "clear"));
                    startService(clear);
                }

            } else if ("why".equals(action)) {
                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                    getSupportFragmentManager().popBackStack("unified", 0);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ActivityView.this);
                boolean why = prefs.getBoolean("why", false);
                if (!why || BuildConfig.DEBUG) {
                    prefs.edit().putBoolean("why", true).apply();
                    Helper.viewFAQ(this, 2);
                }

            } else if ("outbox".equals(action))
                onMenuOutbox();

            else if (action.startsWith("thread")) {
                intent.putExtra("thread", action.split(":", 2)[1]);
                onViewThread(intent);

            } else if (action.equals("widget"))
                onViewThread(intent);
        }

        if (intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)) {
            CharSequence csearch = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            String search = (csearch == null ? null : csearch.toString());
            if (!TextUtils.isEmpty(search)) {
                searching = true;
                FragmentMessages.search(
                        ActivityView.this, ActivityView.this, getSupportFragmentManager(),
                        -1, false, search);
            }

            intent.removeExtra(Intent.EXTRA_PROCESS_TEXT);
            setIntent(intent);
        }
    }

    private Intent getIntentOtherApps() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Helper.isPlayStoreInstall()
                ? Helper.PLAY_APPS_URI : Helper.GITHUB_APPS_URI));
        return intent;
    }

    private void onMenuFolders(long account) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("unified", 0);

        Bundle args = new Bundle();
        args.putLong("account", account);

        FragmentFolders fragment = new FragmentFolders();
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("folders");
        fragmentTransaction.commit();
    }

    private void onMenuOutbox() {
        Bundle args = new Bundle();

        new SimpleTask<EntityFolder>() {
            @Override
            protected EntityFolder onExecute(Context context, Bundle args) {
                DB db = DB.getInstance(context);
                EntityFolder outbox = db.folder().getOutbox();
                return outbox;
            }

            @Override
            protected void onExecuted(Bundle args, EntityFolder outbox) {
                if (outbox == null)
                    return;

                if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
                    getSupportFragmentManager().popBackStack("unified", 0);

                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(ActivityView.this);
                lbm.sendBroadcast(
                        new Intent(ActivityView.ACTION_VIEW_MESSAGES)
                                .putExtra("account", -1L)
                                .putExtra("folder", outbox.id)
                                .putExtra("type", outbox.type));
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                Helper.unexpectedError(getSupportFragmentManager(), ex);
            }
        }.execute(this, args, "menu:outbox");
    }

    private void onMenuOperations() {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("operations", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new FragmentOperations()).addToBackStack("operations");
        fragmentTransaction.commit();
    }

    private void onMenuAnswers() {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("answers", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new FragmentAnswers()).addToBackStack("answers");
        fragmentTransaction.commit();
    }

    private void onMenuSetup() {
        startActivity(new Intent(ActivityView.this, ActivitySetup.class));
    }

    private void onMenuLegend() {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("legend", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new FragmentLegend()).addToBackStack("legend");
        fragmentTransaction.commit();
    }

    private void onMenuTest() {
        Helper.view(this, Uri.parse(Helper.TEST_URI), false);
    }

    private void onMenuFAQ() {
        Helper.viewFAQ(this, 0);
    }

    private void onMenuIssue() {
        startActivity(Helper.getIntentIssue(this));
    }

    private void onMenuPrivacy() {
        Bundle args = new Bundle();
        args.putString("name", "PRIVACY.md");
        FragmentDialogMarkdown fragment = new FragmentDialogMarkdown();
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "privacy");
    }

    private void onMenuAbout() {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("about", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new FragmentAbout()).addToBackStack("about");
        fragmentTransaction.commit();
    }

    private void onMenuInvite() {
        startActivity(getIntentInvite(this));
    }

    private void onMenuRate() {
        new FragmentDialogRate().show(getSupportFragmentManager(), "rate");
    }

    private void onMenuOtherApps() {
        Helper.view(this, getIntentOtherApps());
    }

    private void onDebugInfo() {
        new SimpleTask<Long>() {
            @Override
            protected Long onExecute(Context context, Bundle args) throws IOException {
                return Log.getDebugInfo(context, R.string.title_debug_info_remark, null, null).id;
            }

            @Override
            protected void onExecuted(Bundle args, Long id) {
                startActivity(new Intent(ActivityView.this, ActivityCompose.class)
                        .putExtra("action", "edit")
                        .putExtra("id", id));
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                if (ex instanceof IllegalArgumentException)
                    ToastEx.makeText(ActivityView.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                else
                    ToastEx.makeText(ActivityView.this, ex.toString(), Toast.LENGTH_LONG).show();
            }

        }.execute(this, new Bundle(), "debug:info");
    }

    private void onShowLog() {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("logs", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new FragmentLogs()).addToBackStack("logs");
        fragmentTransaction.commit();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String action = intent.getAction();

                if (ACTION_VIEW_FOLDERS.equals(action))
                    onViewFolders(intent);
                else if (ACTION_VIEW_MESSAGES.equals(action))
                    onViewMessages(intent);
                else if (ACTION_SEARCH.equals(action))
                    onSearchMessages(intent);
                else if (ACTION_VIEW_THREAD.equals(action))
                    onViewThread(intent);
                else if (ACTION_EDIT_FOLDER.equals(action))
                    onEditFolder(intent);
                else if (ACTION_EDIT_ANSWERS.equals(action))
                    onEditAnswers(intent);
                else if (ACTION_EDIT_ANSWER.equals(action))
                    onEditAnswer(intent);
                else if (ACTION_EDIT_RULES.equals(action))
                    onEditRules(intent);
                else if (ACTION_EDIT_RULE.equals(action))
                    onEditRule(intent);
            }
        }
    };

    private void onViewFolders(Intent intent) {
        long account = intent.getLongExtra("id", -1);
        onMenuFolders(account);
    }

    private void onViewMessages(Intent intent) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("messages", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Bundle args = new Bundle();
        args.putString("type", intent.getStringExtra("type"));
        args.putLong("account", intent.getLongExtra("account", -1));
        args.putLong("folder", intent.getLongExtra("folder", -1));

        FragmentMessages fragment = new FragmentMessages();
        fragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("messages");
        fragmentTransaction.commit();
    }

    private void onSearchMessages(Intent intent) {
        long folder = intent.getLongExtra("folder", -1);
        String query = intent.getStringExtra("query");
        FragmentMessages.search(
                this, this, getSupportFragmentManager(),
                folder, false, query);
    }

    private void onViewThread(Intent intent) {
        boolean found = intent.getBooleanExtra("found", false);

        if (!found && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getSupportFragmentManager().popBackStack("thread", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Bundle args = new Bundle();
        args.putLong("account", intent.getLongExtra("account", -1));
        args.putString("thread", intent.getStringExtra("thread"));
        args.putLong("id", intent.getLongExtra("id", -1));
        args.putBoolean("found", found);

        FragmentMessages fragment = new FragmentMessages();
        fragment.setArguments(args);

        int pane;
        if (content_pane == null)
            pane = R.id.content_frame;
        else {
            pane = R.id.content_pane;
            content_separator.setVisibility(View.VISIBLE);
            content_pane.setVisibility(View.VISIBLE);
            args.putBoolean("pane", true);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(pane, fragment).addToBackStack("thread");
        fragmentTransaction.commit();
    }

    private void onEditFolder(Intent intent) {
        FragmentFolder fragment = new FragmentFolder();
        fragment.setArguments(intent.getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("folder");
        fragmentTransaction.commit();
    }

    private void onEditAnswers(Intent intent) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new FragmentAnswers()).addToBackStack("answers");
        fragmentTransaction.commit();
    }

    private void onEditAnswer(Intent intent) {
        FragmentAnswer fragment = new FragmentAnswer();
        fragment.setArguments(intent.getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("answer");
        fragmentTransaction.commit();
    }

    private void onEditRules(Intent intent) {
        FragmentRules fragment = new FragmentRules();
        fragment.setArguments(intent.getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("rules");
        fragmentTransaction.commit();
    }

    private void onEditRule(Intent intent) {
        FragmentRule fragment = new FragmentRule();
        fragment.setArguments(intent.getExtras());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).addToBackStack("rule");
        fragmentTransaction.commit();
    }

    private class UpdateInfo {
        String tag_name; // version
        String html_url;
    }

    private static Intent getIntentInvite(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.title_try)).append("\n\n");
        sb.append(BuildConfig.INVITE_URI).append("\n\n");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        return intent;
    }

    private static Intent getIntentRate(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
        if (intent.resolveActivity(context.getPackageManager()) == null)
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
        return intent;
    }

    public static class FragmentDialogFirst extends FragmentDialogBase {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext())
                    .setMessage(getString(R.string.title_hint_sync))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                            prefs.edit().putBoolean("first", false).apply();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
    }

    public static class FragmentDialogRate extends FragmentDialogBase {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext())
                    .setMessage(R.string.title_issue)
                    .setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Helper.viewFAQ(getContext(), 0);
                        }
                    })
                    .setNegativeButton(R.string.title_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Helper.view(getContext(), getIntentRate(getContext()));
                        }
                    })
                    .create();
        }
    }
}
