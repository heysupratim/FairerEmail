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

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class WidgetUnifiedRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int appWidgetId;

    private long account;
    private boolean unseen;
    private boolean flagged;
    private List<TupleMessageWidget> messages = new ArrayList<>();

    WidgetUnifiedRemoteViewsFactory(final Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.i("Widget factory create id=" + appWidgetId);
    }

    @Override
    public void onDataSetChanged() {
        Log.i("Widget factory changed id=" + appWidgetId);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean threading = prefs.getBoolean("threading", true);
        account = prefs.getLong("widget." + appWidgetId + ".account", -1L);
        unseen = prefs.getBoolean("widget." + appWidgetId + ".unseen", false);
        flagged = prefs.getBoolean("widget." + appWidgetId + ".flagged", false);

        messages.clear();

        DB db = DB.getInstance(context);
        List<TupleMessageWidget> wmessages = db.message().getWidgetUnified(threading, unseen, flagged);
        for (TupleMessageWidget wmessage : wmessages)
            if (account < 0 || wmessage.account == account)
                messages.add(wmessage);
    }

    @Override
    public void onDestroy() {
        Log.i("Widget factory destroy id=" + appWidgetId);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_widget_unified);

        if (position >= messages.size())
            return views;

        try {
            TupleMessageWidget message = messages.get(position);

            Intent thread = new Intent(context, ActivityView.class);
            thread.putExtra("account", message.account);
            thread.putExtra("thread", message.thread);
            thread.putExtra("id", message.id);
            views.setOnClickFillInIntent(R.id.llMessage, thread);

            String froms = MessageHelper.formatAddressesShort(message.from);
            if (message.unseen > 1)
                froms = context.getString(R.string.title_name_count, froms, Integer.toString(message.unseen));

            SpannableString ssFrom = new SpannableString(froms);
            SpannableString ssTime = new SpannableString(Helper.getRelativeTimeSpanString(context, message.received));
            SpannableString ssSubject = new SpannableString(TextUtils.isEmpty(message.subject) ? "" : message.subject);
            SpannableString ssAccount = new SpannableString(TextUtils.isEmpty(message.accountName) ? "" : message.accountName);

            if (!message.ui_seen) {
                ssFrom.setSpan(new StyleSpan(Typeface.BOLD), 0, ssFrom.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssTime.setSpan(new StyleSpan(Typeface.BOLD), 0, ssTime.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssSubject.setSpan(new StyleSpan(Typeface.BOLD), 0, ssSubject.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ssAccount.setSpan(new StyleSpan(Typeface.BOLD), 0, ssAccount.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }

            views.setTextViewText(R.id.tvFrom, ssFrom);
            views.setTextViewText(R.id.tvTime, ssTime);
            views.setTextViewText(R.id.tvSubject, ssSubject);
            views.setTextViewText(R.id.tvAccount, ssAccount);
            views.setViewVisibility(R.id.tvAccount, account < 0 ? View.VISIBLE : View.GONE);
        } catch (Throwable ex) {
            Log.e(ex);
        }

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (position >= messages.size())
            return -1;
        return messages.get(position).id;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
