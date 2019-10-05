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

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentBase extends Fragment {
    private String title = null;
    private String subtitle = " ";
    private boolean finish = false;

    private long message = -1;
    private long attachment = -1;

    private static final int REQUEST_ATTACHMENT = 51;
    private static final int REQUEST_ATTACHMENTS = 52;

    static final String ACTION_STORE_ATTACHMENT = BuildConfig.APPLICATION_ID + ".STORE_ATTACHMENT";
    static final String ACTION_STORE_ATTACHMENTS = BuildConfig.APPLICATION_ID + ".STORE_ATTACHMENTS";

    protected void setTitle(int resid) {
        setTitle(getString(resid));
    }

    protected void setTitle(String title) {
        this.title = title;
        updateSubtitle();
    }

    protected void setSubtitle(int resid) {
        setSubtitle(getString(resid));
    }

    protected void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        updateSubtitle();
    }

    @Override
    public void startActivity(Intent intent) {
        try {
            if (Helper.hasAuthentication(getContext()))
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            super.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.e(ex);
            ToastEx.makeText(getContext(), getString(R.string.title_no_viewer, intent.getAction()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            if (Helper.hasAuthentication(getContext()))
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            super.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException ex) {
            Log.e(ex);
            ToastEx.makeText(getContext(), getString(R.string.title_no_viewer, intent.getAction()), Toast.LENGTH_LONG).show();
        }
    }

    protected void finish() {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED))
            getFragmentManager().popBackStack();
        else
            finish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("Save instance " + this);
        int before = Helper.getSize(outState);
        outState.putString("fair:subtitle", subtitle);
        super.onSaveInstanceState(outState);
        int after = Helper.getSize(outState);
        Log.i("Saved instance " + this + " size=" + before + "/" + after);

        Map<String, String> crumb = new HashMap<>();
        crumb.put("name", this.getClass().getName());
        crumb.put("before", Integer.toString(before));
        crumb.put("after", Integer.toString(after));
        for (String key : outState.keySet()) {
            Object value = outState.get(key);
            crumb.put(key, value == null ? "" : value.getClass().getName());
        }
        Log.breadcrumb("onSaveInstanceState", crumb);

        for (String key : outState.keySet())
            Log.i("Saved " + this + " " + key + "=" + outState.get(key));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("Create " + this + " saved=" + (savedInstanceState != null));
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            subtitle = savedInstanceState.getString("fair:subtitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Create view " + this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("Activity " + this + " saved=" + (savedInstanceState != null));
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.i("Resume " + this);
        super.onResume();
        updateSubtitle();
        if (finish) {
            getFragmentManager().popBackStack();
            finish = false;
        }

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        IntentFilter iff = new IntentFilter();
        iff.addAction(ACTION_STORE_ATTACHMENT);
        iff.addAction(ACTION_STORE_ATTACHMENTS);
        lbm.registerReceiver(receiver, iff);
    }

    @Override
    public void onPause() {
        Log.i("Pause " + this);
        super.onPause();

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getContext());
        lbm.unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("Result class=" + this.getClass().getSimpleName() +
                " request=" + requestCode + " result=" + resultCode);
        Log.logExtras(data);
        super.onActivityResult(requestCode, resultCode, data);

        try {
            switch (requestCode) {
                case REQUEST_ATTACHMENT:
                    if (resultCode == RESULT_OK && data != null)
                        onSaveAttachment(data);
                    break;
                case REQUEST_ATTACHMENTS:
                    if (resultCode == RESULT_OK && data != null)
                        onSaveAttachments(data);
                    break;
            }
        } catch (Throwable ex) {
            Log.e(ex);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focused = getActivity().getCurrentFocus();
        if (focused != null)
            im.hideSoftInputFromWindow(focused.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("Config " + this);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDestroy() {
        Log.i("Destroy " + this);
        super.onDestroy();
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(!isPane() && hasMenu);
    }

    private void updateSubtitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && !isPane()) {
            ActionBar actionbar = activity.getSupportActionBar();
            if (actionbar != null) {
                actionbar.setTitle(title == null ? getString(R.string.app_name) : title);
                actionbar.setSubtitle(subtitle);
            }
        }
    }

    private boolean isPane() {
        Bundle args = getArguments();
        return (args != null && args.getBoolean("pane"));
    }

    boolean hasPermission(String name) {
        ActivityBase activity = (ActivityBase) getActivity();
        if (activity == null)
            return false;
        return activity.hasPermission(name);
    }

    void addBackPressedListener(ActivityBase.IBackPressedListener listener) {
        ((ActivityBase) getActivity()).addBackPressedListener(listener, getViewLifecycleOwner());
    }

    void addBillingListener(ActivityBilling.IBillingListener listener) {
        ((ActivityBilling) getActivity()).addBillingListener(listener, getViewLifecycleOwner());
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                String action = intent.getAction();

                if (ACTION_STORE_ATTACHMENT.equals(action))
                    onStoreAttachment(intent);
                if (ACTION_STORE_ATTACHMENTS.equals(action))
                    onStoreAttachments(intent);
            }
        }
    };

    private void onStoreAttachment(Intent intent) {
        attachment = intent.getLongExtra("id", -1);
        Intent create = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        create.addCategory(Intent.CATEGORY_OPENABLE);
        create.setType(intent.getStringExtra("type"));
        create.putExtra(Intent.EXTRA_TITLE, intent.getStringExtra("name"));
        if (create.resolveActivity(getContext().getPackageManager()) == null)
            ToastEx.makeText(getContext(), R.string.title_no_saf, Toast.LENGTH_LONG).show();
        else
            startActivityForResult(Helper.getChooser(getContext(), create), REQUEST_ATTACHMENT);
    }

    private void onStoreAttachments(Intent intent) {
        message = intent.getLongExtra("id", -1);
        Intent tree = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        //tree.putExtra("android.content.extra.SHOW_ADVANCED", true);
        if (tree.resolveActivity(getContext().getPackageManager()) == null)
            ToastEx.makeText(getContext(), R.string.title_no_saf, Toast.LENGTH_LONG).show();
        else
            startActivityForResult(Helper.getChooser(getContext(), tree), REQUEST_ATTACHMENTS);
    }

    private void onSaveAttachment(Intent data) {
        Bundle args = new Bundle();
        args.putLong("id", attachment);
        args.putParcelable("uri", data.getData());

        new SimpleTask<Void>() {
            @Override
            protected Void onExecute(Context context, Bundle args) throws Throwable {
                long id = args.getLong("id");
                Uri uri = args.getParcelable("uri");

                if ("file".equals(uri.getScheme())) {
                    Log.w("Save attachment uri=" + uri);
                    throw new IllegalArgumentException(context.getString(R.string.title_no_stream));
                }

                DB db = DB.getInstance(context);
                EntityAttachment attachment = db.attachment().getAttachment(id);
                if (attachment == null)
                    return null;
                File file = attachment.getFile(context);

                ParcelFileDescriptor pfd = null;
                OutputStream os = null;
                InputStream is = null;
                try {
                    pfd = context.getContentResolver().openFileDescriptor(uri, "w");
                    os = new FileOutputStream(pfd.getFileDescriptor());
                    is = new FileInputStream(file);

                    byte[] buffer = new byte[Helper.BUFFER_SIZE];
                    int read;
                    while ((read = is.read(buffer)) != -1)
                        os.write(buffer, 0, read);
                } finally {
                    try {
                        if (pfd != null)
                            pfd.close();
                    } catch (Throwable ex) {
                        Log.w(ex);
                    }
                    try {
                        if (os != null)
                            os.close();
                    } catch (Throwable ex) {
                        Log.w(ex);
                    }
                    try {
                        if (is != null)
                            is.close();
                    } catch (Throwable ex) {
                        Log.w(ex);
                    }
                }

                return null;
            }

            @Override
            protected void onExecuted(Bundle args, Void data) {
                ToastEx.makeText(getContext(), R.string.title_attachment_saved, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                if (ex instanceof IllegalArgumentException || ex instanceof FileNotFoundException)
                    ToastEx.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                else
                    Helper.unexpectedError(getFragmentManager(), ex);
            }
        }.execute(this, args, "attachment:save");
    }

    private void onSaveAttachments(Intent data) {
        Bundle args = new Bundle();
        args.putLong("id", message);
        args.putParcelable("uri", data.getData());

        new SimpleTask<Void>() {
            @Override
            protected Void onExecute(Context context, Bundle args) throws Throwable {
                long id = args.getLong("id");
                Uri uri = args.getParcelable("uri");

                DB db = DB.getInstance(context);
                DocumentFile tree = DocumentFile.fromTreeUri(context, uri);
                List<EntityAttachment> attachments = db.attachment().getAttachments(id);
                for (EntityAttachment attachment : attachments) {
                    File file = attachment.getFile(context);

                    String name = Helper.sanitizeFilename(attachment.name);
                    if (TextUtils.isEmpty(name))
                        name = Long.toString(attachment.id);
                    DocumentFile document = tree.createFile(attachment.type, name);
                    if (document == null)
                        throw new FileNotFoundException(uri + ":" + name);

                    ParcelFileDescriptor pfd = null;
                    OutputStream os = null;
                    InputStream is = null;
                    try {
                        pfd = context.getContentResolver().openFileDescriptor(document.getUri(), "w");
                        os = new FileOutputStream(pfd.getFileDescriptor());
                        is = new FileInputStream(file);

                        byte[] buffer = new byte[Helper.BUFFER_SIZE];
                        int read;
                        while ((read = is.read(buffer)) != -1)
                            os.write(buffer, 0, read);
                    } finally {
                        try {
                            if (pfd != null)
                                pfd.close();
                        } catch (Throwable ex) {
                            Log.w(ex);
                        }
                        try {
                            if (os != null)
                                os.close();
                        } catch (Throwable ex) {
                            Log.w(ex);
                        }
                        try {
                            if (is != null)
                                is.close();
                        } catch (Throwable ex) {
                            Log.w(ex);
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onExecuted(Bundle args, Void data) {
                ToastEx.makeText(getContext(), R.string.title_attachments_saved, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onException(Bundle args, Throwable ex) {
                Helper.unexpectedError(getFragmentManager(), ex);
            }
        }.execute(this, args, "attachments:save");
    }
}
