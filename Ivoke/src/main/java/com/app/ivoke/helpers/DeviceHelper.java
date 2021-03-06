package com.app.ivoke.helpers;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.app.ivoke.Common;
import com.app.ivoke.controllers.main.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class DeviceHelper {

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PE_FROM_NOTIFICATION = "Ivoke.Notification";

    /*
     *  Return boolean
     *  Verify if GooglePlay is supported.
     *
     *  Created: Cristian Oliveira
     */
    public static boolean checkGooglePlayServices(Activity pActivityCaller) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(pActivityCaller);
        if (resultCode != ConnectionResult.SUCCESS) {
            //if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            //    GooglePlayServicesUtil.getErrorDialog(resultCode, pActivityCaller,
            //            PLAY_SERVICES_RESOLUTION_REQUEST).show();
            //}
            return false;
        }
        return true;
    }

    public static boolean hasInternetConnection()
    {
         ConnectivityManager cm =
                    (ConnectivityManager) Common.appContext.getSystemService(Common.appContext.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean hasGpsOrInternetLocalization(Activity pActivityCaller)
    {
        LocationManager locationManager = (LocationManager) pActivityCaller.getSystemService(Context.LOCATION_SERVICE);
         // getting GPS status
         boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
         // getting network status
         boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
       
       return isGPSEnabled||isNetworkEnabled;
    }

    public static void showGpsConfiguration(Activity pActivity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        pActivity.startActivity(intent);
    }

    public static void showNetworkConfiguration(Activity pActivity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        pActivity.startActivity(intent);
    }


}
