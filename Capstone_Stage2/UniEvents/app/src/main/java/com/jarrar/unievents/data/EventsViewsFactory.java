package com.jarrar.unievents.data;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jarrar.unievents.R;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 5/14/2016.
 */
public class EventsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context = null;
    private int appWidgetId;
    private ArrayList<WidgetListItem> listItemList = new ArrayList<>();
    private Cursor cursor;
    Intent mIntent;

    public EventsViewsFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;
        Log.d("Widget", "Widget Factory running");
        mIntent = intent;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        CursorLoader mCursorLoader = new CursorLoader(
                context,
                EventsContract.Events.buildDirUri(),
                PROJECTION,
                null,
                null,
                null);
        mCursorLoader.registerListener(431, new android.content.Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(android.content.Loader<Cursor> loader, Cursor data) {
                cursor = data;
                Log.d("Widget", "onLoadComplete");
                populateListItems();
            }
        });
        mCursorLoader.startLoading();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_list_item);
        WidgetListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.textViewTitle, listItem.title);
        Log.d("Title", listItem.title);
        remoteView.setTextViewText(R.id.textViewDate, listItem.date);
        remoteView.setTextViewText(R.id.textViewLocation, listItem.location);
        Log.d("Widget", "Return RemoteView");
        return remoteView;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void populateListItems() {

        Log.d("Widget", "Trying Populate items");
        if (cursor == null) {
            Log.d("Widget", "populate returned - cursor null");
            return;
        }
        if (!cursor.moveToFirst()) {
            cursor.close();
            Log.d("Widget", "populate returned - cursor empty");
            return;
        }
        else if(cursor.moveToFirst()) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                Log.d("Widget", "populating items successfully ");
                WidgetListItem listItem = new WidgetListItem();
                Log.d("Widget", "populating item " + cursor.getString(0));
                listItem.title = cursor.getString(1);
                listItem.date = cursor.getString(2);
                listItem.location = cursor.getString(3);
                listItemList.add(listItem);
            }
        cursor.close();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.events_list);
    }

    public class WidgetListItem {
        public String title, date, location;
    }

    String[] PROJECTION = {
            EventsContract.Events._ID,
            EventsContract.Events.TITLE,
            EventsContract.Events.DATE,
            EventsContract.Events.LOCATION,
    };
}