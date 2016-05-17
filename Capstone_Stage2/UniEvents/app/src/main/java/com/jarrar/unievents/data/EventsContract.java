package com.jarrar.unievents.data;

import android.net.Uri;

/**
 * Created by TOSHIBA on 5/1/2016.
 */
public class EventsContract {

    public static final String CONTENT_AUTHORITY = "com.jarrar.unievents";
    public static final Uri BASE_URI = Uri.parse("content://com.jarrar.unievents");

    interface EventsColumns {
        String _ID = "_id";
        String PHOTO_URL = "photo_url";
        String THUMB_URL = "thumb_url";
        String TITLE = "title";
        String DESCRIPTON = "description";
        String DATE = "date";
        String TARGET = "target";
        String LOCATION = "location";

    }

    public static class Events implements EventsColumns {

        public static final String DEFAULT_SORT = DATE + " DESC";
        public static final String TABLE_NAME = "events";

        /**
         * Matches: /events/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("events").build();
        }

        /**
         * Matches: /events/[_id]/
         */
        public static Uri buildEventUri(long _id) {
            return BASE_URI.buildUpon().appendPath("events").appendPath(Long.toString(_id)).build();
        }

        /**
         * Read events ID events detail URI.
         */
        public static long getEventId(Uri eventUri) {
            return Long.parseLong(eventUri.getPathSegments().get(1));
        }
    }

    private EventsContract() {
    }
}
