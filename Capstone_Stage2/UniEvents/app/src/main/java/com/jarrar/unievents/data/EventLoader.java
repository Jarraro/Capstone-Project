package com.jarrar.unievents.data;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

/**
 * Helper for loading a list of events or a single event.
 */
public class EventLoader extends CursorLoader {
    public static EventLoader newAllEventsInstance(Context context) {
        return new EventLoader(context, EventsContract.Events.buildDirUri());
    }

//    public static EventLoader newInstanceForEventId(Context context, long eventId) {
//        return new EventLoader(context, EventsContract.Events.buildEventUri(eventId));
//    }

    public static EventLoader loadFromUri(Context context, Uri uri) {
        return new EventLoader(context, uri);
    }

    private EventLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, EventsContract.Events.DEFAULT_SORT);
    }

    public interface Query {
        String[] PROJECTION = {
                EventsContract.Events._ID,
                EventsContract.Events.PHOTO_URL,
                EventsContract.Events.THUMB_URL,
                EventsContract.Events.TITLE,
                EventsContract.Events.DESCRIPTON,
                EventsContract.Events.DATE,
                EventsContract.Events.TARGET,
                EventsContract.Events.LOCATION,
        };

        int _ID = 0;
        int PHOTO_URL = 1;
        int THUMB_URL = 2;
        int TITLE = 3;
        int DESCRIPTON = 4;
        int DATE = 5;
        int TARGET = 6;
        int LOCATION = 7;
    }
}
