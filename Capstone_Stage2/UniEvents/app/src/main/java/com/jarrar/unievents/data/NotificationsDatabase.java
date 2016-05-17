package com.jarrar.unievents.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by TOSHIBA on 5/8/2016.
 */
public class NotificationsDatabase {

    private EventsDatabase dbHelper;

    private SQLiteDatabase database;

    public final static String notificationsTable = "notifications";

    public final static String notificationText = "notification_text";

    public NotificationsDatabase(Context context) {
        dbHelper = new EventsDatabase(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public void saveNotification(String notification) {
        ContentValues values = new ContentValues();
        values.put(notificationText, notification);
        database.insert(notificationsTable, null, values);
    }

    public ArrayList<String> getNotifications() {
        Cursor cursor = database.rawQuery("select " + notificationText + " from " + notificationsTable, null);
        ArrayList<String> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String text = cursor.getString(cursor
                        .getColumnIndex(notificationText));
                if (!result.contains(text))
                    result.add(text);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }

    public void deleteNotification(String notification) {
        database.delete(notificationsTable, notificationText + " = ?",
                new String[]{String.valueOf(notification)});
    }
}
