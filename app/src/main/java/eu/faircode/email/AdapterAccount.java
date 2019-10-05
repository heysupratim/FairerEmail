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

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class AdapterAccount extends RecyclerView.Adapter<AdapterAccount.ViewHolder> {
    private Fragment parentFragment;
    private boolean settings;

    private Context context;
    private LifecycleOwner owner;
    private LayoutInflater inflater;

    private int colorUnread;
    private int textColorSecondary;

    private List<TupleAccountEx> items = new ArrayList<>();

    private NumberFormat NF = NumberFormat.getNumberInstance();
    private DateFormat DTF;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private View view;
        private View vwColor;
        private ImageView ivPrimary;
        private ImageView ivNotify;
        private TextView tvName;
        private ImageView ivSync;
        private TextView tvUser;
        private ImageView ivState;
        private TextView tvHost;
        private TextView tvLast;
        private TextView tvIdentity;
        private TextView tvDrafts;
        private TextView tvWarning;
        private TextView tvError;
        private Button btnHelp;
        private Group grpSettings;

        private TwoStateOwner powner = new TwoStateOwner(owner, "AccountPopup");

        ViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.clItem);
            vwColor = itemView.findViewById(R.id.vwColor);
            ivSync = itemView.findViewById(R.id.ivSync);
            ivPrimary = itemView.findViewById(R.id.ivPrimary);
            ivNotify = itemView.findViewById(R.id.ivNotify);
            tvName = itemView.findViewById(R.id.tvName);
            tvUser = itemView.findViewById(R.id.tvUser);
            ivState = itemView.findViewById(R.id.ivState);
            tvHost = itemView.findViewById(R.id.tvHost);
            tvLast = itemView.findViewById(R.id.tvLast);
            tvIdentity = itemView.findViewById(R.id.tvIdentity);
            tvDrafts = itemView.findViewById(R.id.tvDrafts);
            tvWarning = itemView.findViewById(R.id.tvWarning);
            tvError = itemView.findViewById(R.id.tvError);
            btnHelp = itemView.findViewById(R.id.btnHelp);
            grpSettings = itemView.findViewById(R.id.grpSettings);
        }

        private void wire() {
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            btnHelp.setOnClickListener(this);
        }

        private void unwire() {
            view.setOnClickListener(null);
            view.setOnLongClickListener(null);
            btnHelp.setOnClickListener(null);
        }

        private void bindTo(TupleAccountEx account) {
            view.setActivated(account.tbd != null);
            //vwColor.setBackgroundColor(account.color == null ? Color.TRANSPARENT : account.color);
            //vwColor.setVisibility(ActivityBilling.isPro(context) ? View.VISIBLE : View.INVISIBLE);

            ivSync.setImageResource(account.synchronize ? R.drawable.baseline_sync_24 : R.drawable.baseline_sync_disabled_24);

            ivPrimary.setVisibility(account.primary ? View.VISIBLE : View.GONE);
            ivNotify.setVisibility(account.notify ? View.VISIBLE : View.GONE);

            if (settings)
                tvName.setText(account.name);
            else {
                if (account.unseen > 0)
                    tvName.setText(context.getString(R.string.title_name_count, account.name, NF.format(account.unseen)));
                else
                    tvName.setText(account.name);

                tvName.setTypeface(account.unseen > 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                tvName.setTextColor(account.unseen > 0 ? colorUnread : textColorSecondary);
            }

            tvUser.setText(account.user);

            if ("connected".equals(account.state))
                ivState.setImageResource(R.drawable.baseline_cloud_24);
            else if ("connecting".equals(account.state))
                ivState.setImageResource(R.drawable.baseline_cloud_queue_24);
            else if ("closing".equals(account.state))
                ivState.setImageResource(R.drawable.baseline_close_24);
            else
                ivState.setImageResource(R.drawable.baseline_cloud_off_24);
            ivState.setVisibility(account.synchronize ? View.VISIBLE : View.INVISIBLE);

            tvHost.setText(String.format("%s:%d", account.host, account.port));
            tvLast.setText(context.getString(R.string.title_last_connected,
                    account.last_connected == null ? "-" : DTF.format(account.last_connected)));

            tvIdentity.setVisibility(account.identities > 0 || !settings ? View.GONE : View.VISIBLE);
            tvDrafts.setVisibility(account.drafts || !settings ? View.GONE : View.VISIBLE);

            tvWarning.setText(account.warning);
            tvWarning.setVisibility(account.warning == null || !settings ? View.GONE : View.VISIBLE);

            tvError.setText(account.error);
            tvError.setVisibility(account.error == null ? View.GONE : View.VISIBLE);
            btnHelp.setVisibility(account.error == null ? View.GONE : View.VISIBLE);

            grpSettings.setVisibility(settings ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnHelp)
                Helper.viewFAQ(context, 22);
            else {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION)
                    return;

                TupleAccountEx account = items.get(pos);
                if (account.tbd != null)
                    return;

                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                lbm.sendBroadcast(
                        new Intent(settings ? ActivitySetup.ACTION_EDIT_ACCOUNT : ActivityView.ACTION_VIEW_FOLDERS)


                                .putExtra("id", account.id)
                                .putExtra("pop", account.pop));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int pos = getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION)
                return false;

            final TupleAccountEx account = items.get(pos);
            if (account.tbd != null)
                return false;

            PopupMenuLifecycle popupMenu = new PopupMenuLifecycle(context, powner, view);

            popupMenu.getMenu().add(Menu.NONE, 0, 0, account.name).setEnabled(false);

            popupMenu.getMenu().add(Menu.NONE, R.string.title_enabled, 1, R.string.title_enabled)
                    .setCheckable(true).setChecked(account.synchronize);

            if (!account.pop && account.notify && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = EntityAccount.getNotificationChannelId(account.id);
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel channel = nm.getNotificationChannel(channelId);
                if (channel != null)
                    popupMenu.getMenu().add(Menu.NONE, R.string.title_edit_channel, 2, R.string.title_edit_channel);
            }

            if (!account.pop && settings)
                popupMenu.getMenu().add(Menu.NONE, R.string.title_copy, 3, R.string.title_copy);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.string.title_enabled:
                            onActionSync(!item.isChecked());
                            return true;

                        case R.string.title_edit_channel:
                            onActionEditChannel();
                            return true;

                        case R.string.title_copy:
                            onActionCopy();
                            return true;

                        default:
                            return false;
                    }
                }

                private void onActionSync(boolean sync) {
                    Bundle args = new Bundle();
                    args.putLong("id", account.id);
                    args.putBoolean("sync", sync);

                    new SimpleTask<Boolean>() {
                        @Override
                        protected Boolean onExecute(Context context, Bundle args) {
                            long id = args.getLong("id");
                            boolean sync = args.getBoolean("sync");

                            DB db = DB.getInstance(context);
                            if (!sync) {
                                db.account().setAccountWarning(id, null);
                                db.account().setAccountError(id, null);
                            }
                            db.account().setAccountSynchronize(id, sync);

                            return sync;
                        }

                        @Override
                        protected void onExecuted(Bundle args, Boolean sync) {
                            ServiceSynchronize.reload(context, "account set sync=" + sync);
                        }

                        @Override
                        protected void onException(Bundle args, Throwable ex) {
                            Helper.unexpectedError(parentFragment.getFragmentManager(), ex);
                        }
                    }.execute(context, owner, args, "account:enable");
                }

                @TargetApi(Build.VERSION_CODES.O)
                private void onActionEditChannel() {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName())
                            .putExtra(Settings.EXTRA_CHANNEL_ID, EntityAccount.getNotificationChannelId(account.id));
                    context.startActivity(intent);
                }

                private void onActionCopy() {
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                    lbm.sendBroadcast(
                            new Intent(ActivitySetup.ACTION_EDIT_ACCOUNT)
                                    .putExtra("id", account.id)
                                    .putExtra("copy", true));
                }
            });

            popupMenu.show();

            return true;
        }
    }

    AdapterAccount(final Fragment parentFragment, boolean settings) {
        this.parentFragment = parentFragment;
        this.settings = settings;

        this.context = parentFragment.getContext();
        this.owner = parentFragment.getViewLifecycleOwner();
        this.inflater = LayoutInflater.from(context);

        this.colorUnread = Helper.resolveColor(context, R.attr.colorUnread);
        this.textColorSecondary = Helper.resolveColor(context, android.R.attr.textColorSecondary);

        this.DTF = Helper.getDateTimeInstance(context, DateFormat.SHORT, DateFormat.SHORT);

        setHasStableIds(true);

        owner.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroyed() {
                Log.i(AdapterAccount.this + " parent destroyed");
                AdapterAccount.this.parentFragment = null;
            }
        });
    }

    public void set(@NonNull List<TupleAccountEx> accounts) {
        Log.i("Set accounts=" + accounts.size());

        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffCallback(items, accounts), false);

        items = accounts;

        diff.dispatchUpdatesTo(new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
                Log.i("Inserted @" + position + " #" + count);
            }

            @Override
            public void onRemoved(int position, int count) {
                Log.i("Removed @" + position + " #" + count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                Log.i("Moved " + fromPosition + ">" + toPosition);
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                Log.i("Changed @" + position + " #" + count);
            }
        });
        diff.dispatchUpdatesTo(this);
    }

    private class DiffCallback extends DiffUtil.Callback {
        private List<TupleAccountEx> prev = new ArrayList<>();
        private List<TupleAccountEx> next = new ArrayList<>();

        DiffCallback(List<TupleAccountEx> prev, List<TupleAccountEx> next) {
            this.prev.addAll(prev);
            this.next.addAll(next);
        }

        @Override
        public int getOldListSize() {
            return prev.size();
        }

        @Override
        public int getNewListSize() {
            return next.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            TupleAccountEx f1 = prev.get(oldItemPosition);
            TupleAccountEx f2 = next.get(newItemPosition);
            return f1.id.equals(f2.id);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            TupleAccountEx f1 = prev.get(oldItemPosition);
            TupleAccountEx f2 = next.get(newItemPosition);
            return f1.uiEquals(f2);
        }
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).id;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_account, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.unwire();

        TupleAccountEx account = items.get(position);
        holder.bindTo(account);

        holder.wire();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        holder.powner.recreate();
    }
}
