package com.jarrar.unievents.gcm;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.jarrar.unievents.backend.registration.Registration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by JARRAR on 26/04/2016.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {
    private static Registration regService = null;

    private GoogleCloudMessaging gcm;
    private Context context;
    private static final String SENDER_ID = "913905248369";

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (regService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://uni-events-1296.appspot.com/_ah/api/");
            regService = builder.build();
        }

        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            String regId = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regId;

            regService.register(regId).execute();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }

        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {
        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
        Log.d("REGISTRATION", msg);
    }
}