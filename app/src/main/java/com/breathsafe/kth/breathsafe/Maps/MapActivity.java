package com.breathsafe.kth.breathsafe.Maps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.breathsafe.kth.breathsafe.AsyncTaskCallback;
import com.breathsafe.kth.breathsafe.CalculateAirPollutionData;
import com.breathsafe.kth.breathsafe.Constants;
import com.breathsafe.kth.breathsafe.Database.StoreToDatabase;
import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.AirPollutionData;
import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.OnTaskCompleteHelper;
import com.breathsafe.kth.breathsafe.R;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import static com.breathsafe.kth.breathsafe.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MapActivity extends AppCompatActivity implements AsyncTaskCallback {
    private static final String TAG = "MapActivityTagger";
    private static final int NETWORK_AIR = 5;
    private static final double defaultLat = 59.33937606387055;
    private static final double defaultLng = 18.04011602003701;
    private static final int defaultRadius = 30;
    private static final int thirtyMinutesInMillis = 18000000;

    public boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    MapsThingFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        createAirTask(); // for test air pollution

    }

    public void checkForAirPollution() {
        List<Location> list = DisplayOnMapList.getInstance().getList();
        if (list.size() <= 0) {
            Log.i(TAG, "createAirTask: list is <= 0");
            return;
        }
        List<AirPollution> airPollutions = AirPollutionData.getInstance().getList();
        if (airPollutions.size() <= 0) {
            Log.i(TAG, "checkForAirPollution: No AirPollution data in database");
            startNetworkTask();
            return;
        }
        AirPollution first = airPollutions.get(0);
        if ((System.currentTimeMillis() - first.getRetrieved()) > thirtyMinutesInMillis) {
            Log.i(TAG, "checkForAirPollution: Air Pollution data in database is not up to date");
            startNetworkTask();
            return;
        }
        Log.i(TAG, "checkForAirPollution: using database AirPollution ");
        handleAirPollutionData(airPollutions);
    }

    private void handleAirPollutionData(List<AirPollution> list) {
        for (Location l : DisplayOnMapList.getInstance().getList()) {
            double res = CalculateAirPollutionData.weightedPM1andPM2(list, l);
            Log.i(TAG, "onDownloadComplete: averageAQI: " + res);
            l.setAverageAQI(res);
        }
        fragment.setAir(list);
    }

    private void startNetworkTask() {
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.api_luftdaten_lat_lat_area));
        sb.append(defaultLat);sb.append(",");
        sb.append(defaultLng);sb.append(",");
        sb.append(defaultRadius);
        NetworkTask networkTask = new NetworkTask(this, sb.toString(), NETWORK_AIR);
        networkTask.execute();
    }

    private void createAirTask() {
        List<Location> list = DisplayOnMapList.getInstance().getList();
        if (list.size() <= 0) {
            Log.i(TAG, "createAirTask: list is <= 0");
            return;
        }
        Location location = list.get(list.size()-1);
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.api_luftdaten_lat_lat_area));
        sb.append(location.getLatitude());sb.append(",");
        sb.append(location.getLongitude());sb.append(",");
        sb.append(30);
        NetworkTask networkTask = new NetworkTask(this, sb.toString(), NETWORK_AIR);
        networkTask.execute();
    }

    private void inflateUserListFragment(){
//        hideSoftKeyboard();
        fragment = MapsThingFragment.newInstance();
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(getString(R.string.intent_user_list), mUserList);
//        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.replace(R.id.mapper_container, fragment, getString(R.string.fragment_user_list));
        transaction.addToBackStack(getString(R.string.fragment_user_list));
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (InitLocation.checkMapServices(this)) {
            InitLocation.getLocationPermission(this);
            if (locationPermissionGranted) {
                // TO correct things
                inflateUserListFragment();
            }
            else {
                InitLocation.getLocationPermission(this);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    Log.d(TAG, "onRequestPermissionsResult: " + "nr1");
                    inflateUserListFragment();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(locationPermissionGranted){
                    Log.d(TAG, "onActivityResult: " + "nr2");
//                    getChatrooms();
                }
                else{

                    InitLocation.getLocationPermission(this);
                }
            }
        }
    }

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
            StoreToDatabase.StoreAirPollution storeAirPollution = new StoreToDatabase.StoreAirPollution(this, list);
            storeAirPollution.execute();
            fragment.setAir(list);
        }
        else {
            Log.i(TAG, "onDownloadComplete: luftdaten exception");
            result.ex.printStackTrace();
        }
    }
}
