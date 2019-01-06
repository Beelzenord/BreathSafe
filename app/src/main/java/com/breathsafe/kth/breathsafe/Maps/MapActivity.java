package com.breathsafe.kth.breathsafe.Maps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.AsyncTaskCallback;
import com.breathsafe.kth.breathsafe.Utilities.CalculateAirPollutionData;
import com.breathsafe.kth.breathsafe.Utilities.Constants;
import com.breathsafe.kth.breathsafe.Database.StoreToDatabase;
import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.AirPollutionData;
import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Utilities.OnTaskCompleteHelper;
import com.breathsafe.kth.breathsafe.R;
import com.breathsafe.kth.breathsafe.Search.SearchActivity;
import com.breathsafe.kth.breathsafe.Utilities.Util;

import java.util.List;

import static com.breathsafe.kth.breathsafe.Utilities.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

/**
 * An activity to show google maps with all selected Locations.
 */
public class MapActivity extends AppCompatActivity implements AsyncTaskCallback {
    private static final String TAG = "MapActivityTagger";
    private static final int NETWORK_AIR = 5;

    public boolean locationPermissionGranted;
    public boolean gpsEnabled;
    private MapsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        locationPermissionGranted = false;
        gpsEnabled = false;
    }

    /**
     * Starts the fragment that show the google map.
     */
    private void startMapsFragment(){
        fragment = MapsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.maps_container, fragment, getString(R.string.fragment_user_list));
        transaction.commit();
    }

    /**
     * Tries to start the map if the app already has location permission.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        if (InitLocation.isServicesOK(this)) {
            if (InitLocation.getLocationPermission(this)) {
                Log.i(TAG, "onResume: start maps fragments");
                startMapsFragment();
            }
        }
    }

    /**
     * Checks if the use has selected any locations to show.
     * Checks if database has updated AirPollution data. If not
     * download new data.
     */
    public void checkForAirPollution() {
        List<Location> list = DisplayOnMapList.getInstance().getList();
        if (list.size() == 0) {
            Log.i(TAG, "createAirTask: list is <= 0");
            fragment.refreshMap();
            return;
        }

        if (Util.isThresholdReachedToDownloadAirPollution(this))
            startNetworkTask();
        else
            useOldAirPollutionData();
    }

    private void useOldAirPollutionData() {
        Log.i(TAG, "useOldAirPollutionData: ");
        List<AirPollution> list = AirPollutionData.getInstance().getList();
        for (Location l : DisplayOnMapList.getInstance().getList()) {
            double res = CalculateAirPollutionData.weightedPM1andPM2(list, l);
//            Log.i(TAG, "on database data: averageAQI: " + res);
            l.setAverageAQI(res);
        }
        fragment.refreshMap();
    }

    /**
     * Callback for when the app needs to ask the user for locations permission.
     * If granted, open map. If not close the activity.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: GRANTED");
                    startMapsFragment();
                }
                else {
                    Log.i(TAG, "onRequestPermissionsResult: NOT GRANTED");
                    showToast("Permission is needed to show the map");
                    finish();
                }
            }
        }
    }

    /**
     * Callback for when the app asks for GPS enable.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called : req: " + requestCode);
        Log.d(TAG, "onActivityResult: called : res: " + resultCode);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(locationPermissionGranted){
                    Log.i(TAG, "onActivityResult: " + "nr2");
                    startMapsFragment();
                    break;
                }
                else{
                    InitLocation.getLocationPermission(this);
                    break;
                }
            }
            case Constants.SEARCH_ACTIVITY_LOCATION: {
                Log.i(TAG, "onActivityResult: onResume takes care of starting the map..");
                break;
            }
        }
    }

    /**
     * Starts a AsyncTask to download new AirPollution data.
     */
    private void startNetworkTask() {
        Log.i(TAG, "startNetworkTask: ");
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.api_luftdaten_lat_lat_area));
        sb.append(getResources().getString(R.string.api_luftdaten_default_lat));
        sb.append(",");
        sb.append(getResources().getString(R.string.api_luftdaten_default_lng));
        sb.append(",");
        sb.append(getResources().getString(R.string.api_luftdaten_default_radius));
        NetworkTask networkTask = new NetworkTask(this, sb.toString(), NETWORK_AIR);
        networkTask.execute();
    }

    /**
     * Callback for when the app downloads new airpollution data.
     * Refreshes the map when done.
     * @param result The data retrieved.
     */
    @Override
    public void onDownloadComplete(NetworkTask.Result result) {
        if (result.msg != null) {
            List<AirPollution> list = OnTaskCompleteHelper.onAirTaskComplete(this, (String)result.msg);
            Log.i(TAG, "onDownloadComplete: size: " + list.size());

            for (Location l : DisplayOnMapList.getInstance().getList()) {
                double res = CalculateAirPollutionData.weightedPM1andPM2(list, l);
                Log.i(TAG, "onDownloadComplete: averageAQI: " + res);
                l.setAverageAQI(res);
            }
            fragment.refreshMap();
            AirPollutionData.getInstance().setList(list);
            StoreToDatabase.DeleteAllAndStoreAirPollution storeAirPollution = new StoreToDatabase.DeleteAllAndStoreAirPollution(this, list);
            storeAirPollution.execute();
        }
        else {
            Log.i(TAG, "onDownloadComplete: luftdaten exception");
            result.ex.printStackTrace();
        }
    }

    /**
     * Adds a Location to favorites and updates the database.
     * @param currentLocation
     */
    public void addToFavorites(Location currentLocation) {
        StringBuilder sb = new StringBuilder();
        sb.append(currentLocation.getName());
        sb.append(" ");
        if (!currentLocation.isFavorite()) {
            currentLocation.setFavorite(true);
            StoreToDatabase.UpdateLocation updateLocation = new StoreToDatabase.UpdateLocation(this, currentLocation);
            updateLocation.execute();
            sb.append(getResources().getString(R.string.saved_to_favorites));
            showToast(sb.toString());
        }
        else {
            sb.append(getResources().getString(R.string.already_in_favorites));
            showToast(sb.toString());
        }
    }

    /**
     * Starts the search activity
     */
    public void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Constants.SEARCH_ACTIVITY_CALLBACK_ACTIVITY, Constants.SEARCH_ACTIVITY_CALLBACK_MAPACTIVITY);
        startActivityForResult(intent, Constants.SEARCH_ACTIVITY_LOCATION);
    }

    /**
     * Shows a short toast message in the middle of the screen.
     * @param msg The message to show.
     */
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
