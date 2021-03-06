package com.app.ivoke.libraries;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.app.ivoke.Common;
import com.app.ivoke.R;
import com.app.ivoke.helpers.DebugHelper;
import com.app.ivoke.helpers.DeviceHelper;
import com.app.ivoke.helpers.SettingsHelper;
import com.app.ivoke.objects.defaults.DefaultWebCallback;
import com.app.ivoke.objects.interfaces.IAsyncCallBack;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;

public class GCMManager {
    DebugHelper dbg = new DebugHelper("GCMManager");

    public static final String PREF_REGISTRATION_ID = "com.app.ivoke.registration_id";
    public static final String PREF_APP_VERSION     = "com.app.ivoke.version";

    GoogleCloudMessaging gcm;
    String gcm_registration_id;
    Context context;

    Common common;

    DefaultWebCallback callback;
    RegistrationAsync asyncRegister = new RegistrationAsync();

    AtomicInteger msgId = new AtomicInteger();

    public String getRegistrationId(Context pContext) {
        context = pContext;

        dbg.method("getRegistrationId");
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PREF_REGISTRATION_ID, "");
        if (registrationId!=null) {
            dbg.log("Not registration");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PREF_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            dbg.log("App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return SettingsHelper.getSharedPreference(context);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    public void registerInBackground(DefaultWebCallback pCallback) {
        callback = pCallback;
        asyncRegister.execute(null, null, null);
    }

    public void sendMessageTo(String pRegistrationId, String message)
    {
         Bundle data = new Bundle();
         data.putString("registration_ids", pRegistrationId);
         data.putString("my_message", "Hello World");
    //     data.putString("my_action",
    //             "com.google.android.gcm.demo.app.ECHO_NOW");

    }

    public class RegistrationAsync extends AsyncTask<Object, Object, Object>
    {
        @Override
        protected Object doInBackground(Object... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                gcm_registration_id = gcm.register(context.getString(R.string.google_project_ivoke_id));
                msg = "Device registered, registration ID=" + gcm_registration_id;
                
                
                // You should send the registration ID to your server over HTTP,
                // so it can use GCM/HTTP or CCS to send messages to your app.
                // The request to your server should be authenticated if your app
                // is using accounts.
                sendRegistrationIdToBackend();

                // For this demo: we don't need to send it because the device
                // will send upstream messages to a server that echo back the
                // message using the 'from' address in the message.
                // Persist the regID - no need to register again.
                storeRegistrationId(context, gcm_registration_id);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
                // If there is an error, don't just keep trying to register.
                // Require the user to click a button again, or perform
                // exponential back-off.
            }
            dbg.log("msg:"+msg);
            
            callback.onPreComplete(gcm_registration_id);
            
            return msg;
        }


        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            callback.onCompleteTask(gcm_registration_id);
        }
        /**
         * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
         * or CCS to send messages to your app. Not needed for this demo since the
         * device sends upstream messages to a server that echoes back the message
         * using the 'from' address in the message.
         */
        private void sendRegistrationIdToBackend() {

            //Common.setDeviceRegistrationId(gcm_registration_id);

        }

        /**
         * Stores the registration ID and app versionCode in the application's
         * {@code SharedPreferences}.
         *
         * @param context application's context.
         * @param regId registration ID
         */
        private void storeRegistrationId(Context context, String regId) {
            int appVersion = getAppVersion(context);
            SharedPreferences.Editor editor = SettingsHelper.getEditor(context);

            editor.putString(PREF_REGISTRATION_ID, regId);
            editor.putInt(PREF_APP_VERSION, appVersion);
            editor.commit();
        }
    }

    public class SendMessageAsync extends AsyncTask<Object, Object, Object>
    {
         Bundle data;
         public SendMessageAsync(Bundle pParameters)
         {
             data = pParameters;
         }

         @Override
         protected String doInBackground(Object... params) {
             String msg = "";
             try {
                     String id = Integer.toString(msgId.incrementAndGet());
                     String gp_id = context.getString(R.string.google_project_ivoke_id);
                     
                     gcm.send(gp_id + "@gcm.googleapis.com", id, data);
                     msg = "Sent message";
             } catch (IOException ex) {
                 msg = "Error :" + ex.getMessage();
             }
             return msg;
         }

    }
}
