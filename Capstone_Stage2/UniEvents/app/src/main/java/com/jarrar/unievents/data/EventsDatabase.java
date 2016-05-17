package com.jarrar.unievents.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jarrar.unievents.data.EventsProvider.Tables;

/**
 * Created by TOSHIBA on 5/1/2016.
 */
public class EventsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "uniEvents.db";
    private static final int DATABASE_VERSION = 1;

    public EventsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.EVENTS + " ("
                + EventsContract.EventsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventsContract.EventsColumns.PHOTO_URL + " TEXT,"
                + EventsContract.EventsColumns.THUMB_URL + " TEXT,"
                + EventsContract.EventsColumns.TITLE + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.DESCRIPTON + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.DATE + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.TARGET + " TEXT NOT NULL,"
                + EventsContract.EventsColumns.LOCATION + " TEXT NOT NULL"
                + ")" );

        // Notifications simple table
        db.execSQL("CREATE TABLE notifications ("
                + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "notification_text" + " TEXT NOT NULL"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}