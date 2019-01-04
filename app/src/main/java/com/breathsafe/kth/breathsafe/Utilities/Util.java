package com.breathsafe.kth.breathsafe.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.AirPollutionData;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.R;

import java.util.List;


public class Util {
    private static final String TAG = "UtilClass";

    public static boolean isThresholdReachedToDownloadPlaces(Context context) {
        int networkState = findInternetConnection(context);
        if (networkState == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.no_network_available), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "isThresholdReachedToDownloadPlaces: no network");
            return false;
        }
        List<LocationCategory> list = LocationCategoryData.getInstance().getList();
        if (list.size() == 0) {
            Log.i(TAG, "isThresholdReachedToDownloadPlaces: list size = 0");
            return true;
        }
        if (networkState == 1) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String s = sharedPreferences.getString(context.getResources().getString(R.string.sync_frequency_locations_wifi_key),
                    context.getResources().getString(R.string.pref_sync_locations_frequency_default_wifi));
            Long threshold = Long.parseLong(s);
            LocationCategory lc = list.get(0);
            Log.i(TAG, "isThresholdReachedToDownloadPlaces: threshold: " + threshold);
            Log.i(TAG, "isThresholdReachedToDownloadPlaces: difference: " + (System.currentTimeMillis() - lc.getRetrieved()));
            if ((System.currentTimeMillis() - lc.getRetrieved()) > threshold) {
                Log.i(TAG, "startDatabaseSynchronizer: wifi do download");
                return true;
            }
        }
        else if (networkState == 2) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String s = sharedPreferences.getString(context.getResources().getString(R.string.sync_frequency_locations_key),
                    context.getResources().getString(R.string.pref_sync_locations_frequency_default));
            Long threshold = Long.parseLong(s);
            LocationCategory lc = list.get(0);
            Log.i(TAG, "isThresholdReachedToDownloadPlaces: threshold: " + threshold);
            Log.i(TAG, "isThresholdReachedToDownloadPlaces: difference: " + (System.currentTimeMillis() - lc.getRetrieved()));
            if ((System.currentTimeMillis() - lc.getRetrieved()) > threshold) {
                Log.i(TAG, "isThresholdReachedToDownloadPlaces: do download");
                return true;
            }
        }
        return false;
    }

    public static boolean isThresholdReachedToDownloadAirPollution(Context context) {
        int networkState = findInternetConnection(context);
        if (networkState == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.no_network_available), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "isThresholdReachedToDownloadAirPollution: no network");
            return false;
        }

        List<AirPollution> airPollutions = AirPollutionData.getInstance().getList();
        if (airPollutions.size() == 0) {
            Log.i(TAG, "isThresholdReachedToDownloadAirPollution: No AirPollution data in database");
            return true;
        }
        if (networkState == 1) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String s = sharedPreferences.getString(context.getResources().getString(R.string.sync_frequency_airpollution_wifi_key),
                    context.getResources().getString(R.string.pref_sync_airpollution_frequency_default_wifi));
            Long threshold = Long.parseLong(s);
            AirPollution first = airPollutions.get(0);
            Log.i(TAG, "isThresholdReachedToDownloadAirPollution: threshold: " + threshold);
            Log.i(TAG, "isThresholdReachedToDownloadAirPollution: difference: " + (System.currentTimeMillis() - first.getRetrieved()));
            if ((System.currentTimeMillis() - first.getRetrieved()) > threshold) {
                Log.i(TAG, "isThresholdReachedToDownloadAirPollution: do download airpollutions on wifi");
                return true;
            }
        }
        else if (networkState == 2) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String s = sharedPreferences.getString(context.getResources().getString(R.string.sync_frequency_airpollution_key),
                    context.getResources().getString(R.string.pref_sync_airpollution_frequency_default));
            Long threshold = Long.parseLong(s);
            AirPollution first = airPollutions.get(0);
            Log.i(TAG, "isThresholdReachedToDownloadAirPollution: threshold: " + threshold);
            Log.i(TAG, "isThresholdReachedToDownloadAirPollution: difference: " + (System.currentTimeMillis() - first.getRetrieved()));
            if ((System.currentTimeMillis() - first.getRetrieved()) > threshold) {
                Log.i(TAG, "isThresholdReachedToDownloadAirPollution: do download airpollutions");
                return true;
            }
        }
        Log.i(TAG, "isThresholdReachedToDownloadAirPollution: use old airpollution data");
        return false;
    }

    /**
     * Checks if device is connected to the internet. Returns 0 if there is no connection to the internet.
     * Returns 1 if there is a wifi connection. Returns 2 if there is no wifi connection but the device
     * is still connected to the internet using for example 3G or 4G.
     * @param context The context that holds the internet connection.
     * @return The type of internet connection established or 0 if the device is not connected to the internet.
     */
    public static int findInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return 1;
            return 2;
        }
        return 0;
    }


}
