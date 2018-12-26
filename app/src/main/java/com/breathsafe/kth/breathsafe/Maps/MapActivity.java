package com.breathsafe.kth.breathsafe.Maps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.Constants;
import com.breathsafe.kth.breathsafe.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.breathsafe.kth.breathsafe.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MapActivity extends AppCompatActivity {
    private static final String TAG = "MapActivityTagger";

    public boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    private double lat;
    private double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();

        lat = getIntent().getDoubleExtra("latitude",0);
        lon = getIntent().getDoubleExtra("longitude",0);

        Log.d(TAG, "latidude : " + lat + " longitide : " + lon);




    }
    private void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation: getting the devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{

            if(locationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                         if(task.isSuccessful()){
                             Log.d(TAG,"onComplete: found Location");
                             Location currentLocation = (Location) task.getResult();

                         }
                         else{
                             Log.d(TAG,"onComplete: current Location is null");
                             Toast.makeText(MapActivity.this,"unable to get current location", Toast.LENGTH_SHORT).show();
                         }

                    }
                });

            }

        }
        catch(SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getLocalizedMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG,"moveCamera: moving the camera to lat: " + latLng.latitude + ", lng: " + latLng.longitude );

    }
    private void inflateUserListFragment(){
//        hideSoftKeyboard();

        MapsThingFragment fragment = MapsThingFragment.newInstance(new LatLng(lat,lon));
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(getString(R.string.intent_user_list), mUserList);
//        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.replace(R.id.container, fragment, getString(R.string.fragment_user_list));
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
}
