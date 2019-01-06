package com.breathsafe.kth.breathsafe.Maps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.Utilities.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static com.breathsafe.kth.breathsafe.Utilities.Constants.ERROR_DIALOG_REQUEST;

public class InitLocation {
    private static final String TAG = "InitLocation";

    /**
     * Builds an alert message for when the The GPS in not active.
     * @param activity
     */
    public static void buildAlertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Enable GPS to show your own location, do you want to enable it?")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        Toast.makeText(activity, "This application requires GPS to work properly", Toast.LENGTH_LONG).show();
//                        activity.finish();
//                        builder.
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Checks if maps are enabled. Builds an alert if it is not.
     * @param activity
     * @return
     */
    public static boolean isMapsEnabled(Activity activity){
        final LocationManager manager = (LocationManager) activity.getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps(activity);
            return false;
        }
        return true;
    }

    /**
     * Checks if the app has Locations permission and asks for permission if not.
     * @param activity The activity to use.
     * @return True if app has permission, else false.
     */
    public static boolean getLocationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "getLocationPermission: locationPermissionGranted = true");
            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.i(TAG, "getLocationPermission: locationPermissionGranted = false");
            return false;
        }
    }

    /**
     * Checks if google API service are present and working.
     * @param activity The activity to use.
     * @return True if it is working.
     */
    public static boolean isServicesOK(Activity activity){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(activity, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public static boolean checkMapServices(Activity activity){
        if(isServicesOK(activity)){
            Log.i(TAG, "checkMapServices: isServices is OK");
//            return true;
            if(isMapsEnabled(activity)){
                return true;
            }
        }
        return false;
    }

}
