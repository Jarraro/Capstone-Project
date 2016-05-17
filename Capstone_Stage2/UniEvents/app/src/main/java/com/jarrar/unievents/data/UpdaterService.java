package com.jarrar.unievents.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.jarrar.unievents.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdaterService extends IntentService {
    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.jarrar.unievents.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.jarrar.unievents.intent.extra.REFRESHING";

    public UpdaterService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, "Not online, not refreshing.");
            return;
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        // Don't even inspect the intent, we only do one thing, and that's fetch content.
        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = EventsContract.Events.buildDirUri();

        // Delete all items
        cpo.add(ContentProviderOperation.newDelete(dirUri).build());

        try {
            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
            if (array == null) {
                throw new JSONException("Invalid parsed item array");
            }

            for (int i = 0; i < array.length(); i++) {
                ContentValues values = new ContentValues();
                JSONObject object = array.getJSONObject(i);
                values.put(EventsContract.Events._ID, object.getString("id"));
                values.put(EventsContract.Events.PHOTO_URL, object.getString("photo"));
                values.put(EventsContract.Events.THUMB_URL, object.getString("thumb"));
                values.put(EventsContract.Events.TITLE, object.getString("title"));
                values.put(EventsContract.Events.DESCRIPTON, object.getString("description"));
                values.put(EventsContract.Events.DATE, object.getString("date"));
                values.put(EventsContract.Events.TARGET, object.getString("target"));
                values.put(EventsContract.Events.LOCATION, object.getString("location"));
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            getContentResolver().applyBatch(EventsContract.CONTENT_AUTHORITY, cpo);

        } catch (JSONException | RemoteException | OperationApplicationException e) {
            Log.e(TAG, "Error updating content.", e);
        }

        sendStickyBroadcast(
                new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }
}
