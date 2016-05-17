package com.jarrar.unievents.data;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by TOSHIBA on 5/14/2016.
 */
public class WidgetService extends RemoteViewsService {
    private Context context;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        context = this.getApplicationContext();
        return (new EventsViewsFactory(context, intent));
    }
}