package com.breathsafe.kth.breathsafe;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.breathsafe.kth.breathsafe.Database.DatabaseTables;
import com.breathsafe.kth.breathsafe.Database.LocationCategoryDoa;
import com.breathsafe.kth.breathsafe.Database.Repository;
import com.breathsafe.kth.breathsafe.Database.StoreToDatabase;
//import com.breathsafe.kth.breathsafe.IO.DatabaseRead.DatabaseTask;
import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.Network.NetworkGetAllLocation;
import com.breathsafe.kth.breathsafe.Network.NetworkTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import static com.breathsafe.kth.breathsafe.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int AIR_TASK = 0;
    private static final int LOCATION_CATEGORY_TASK = 1;
    private static final int LOCATION_TASK = 2;
    private static final int LOCATION_SPECIFIC_TASK = 3;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private FusedLocationProviderClient mClient;
    private long prev;

    private Button favorites;
    private Button search;
    private Button settings;
    private EditText editText;

    private List<LocationCategory> locationCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favorites = (Button) findViewById(R.id.favID);
        search = (Button) findViewById(R.id.searchBtnId);
        editText = (EditText) findViewById(R.id.searchId);
        settings = (Button) findViewById(R.id.settingsID);
        settings.setOnClickListener(this::gotoSettings);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute 
                    geoLocate();
                }
                return false;
            }
        });

        mClient = LocationServices.getFusedLocationProviderClient(this);
       //  Intent intent = new Intent(this, MapActivity.class);
       //  startActivity(intent);
        prev = Calendar.getInstance().getTimeInMillis();


        System.out.println("URL PATH : " + getResources().getString(R.string.api_stockholm_all_categories));
        String apicall = getResources().getString(R.string.api_stockholm_all_categories) +
                getResources().getString(R.string.stockholm_api_key);

        NetworkTask networkTask = new NetworkTask(this, apicall, LOCATION_CATEGORY_TASK);
        networkTask.execute();


//        DatabaseTask.Read databaseTask = new DatabaseTask.Read(this, null, DatabaseTables.LOCATION_CATEGORY);
//        databaseTask.execute();
    }


    private void geoLocate() {

        Log.d(TAG,"geoLocate: geolocating ");

        String searchString = editText.getText().toString();
        Geocoder geocoder = new Geocoder(this);

        List<Address> list = new ArrayList<>();

        try{
                list = geocoder.getFromLocationName(searchString,1);

        }catch(IOException e){
            Log.e(TAG,"geoLocate: IOException: " + e.getMessage());
        }
        if(list.size() >0){
            Address address = list.get(0);

            Log.d(TAG,"geoLocate: found a location " + address.getLocality());

            Log.d(TAG,"geoLocate: found a location " + address.toString());

            Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();

            double lat = address.getLatitude();
            double lon = address.getLongitude();
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("latitude",lat);
            intent.putExtra("longitude",lon);
            startActivity(intent);
        }
    }

    public boolean isServiceOK(){
        Log.d(TAG,"isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an fixable error occurred");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
           // return false;
        }
        else{
            Toast.makeText(this,"Nothing to do",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLastknownLocation() {
        Log.d(TAG, "getLastKnownLocation: called. ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                  if(task.isSuccessful()){
                      Location location = task.getResult();

                  }
            }
        });
    }


    public void onDownloadComplete(NetworkTask.Result result) {
        if (result.msg != null) {
            switch (result.tag) {
                case AIR_TASK:
                    OnTaskCompleteHelper.onAirTaskComplete(this, (String) result.msg);
                    break;
                case LOCATION_CATEGORY_TASK:
                    long now = Calendar.getInstance().getTimeInMillis();
                    System.out.println("millisc: " + (now - prev));
                    System.out.println(result.msg);
                    locationCategoryList = OnTaskCompleteHelper.onLocationCategoryTaskComplete(this, (String)result.msg);
                    LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
                    for (LocationCategory lc : locationCategoryData.getList())
                        System.out.println(lc.getSingularName());
                    StoreToDatabase storeToDatabase = new StoreToDatabase(this,locationCategoryData.getList());
                    storeToDatabase.execute(); //if executed the database holds values for location categories
                   // locationCategoryList  = locationCategoryData.getList();
                    System.out.println("the list " + this.locationCategoryList.size());
                    Log.d(TAG, "onDownloadComplete: stored categories");
                    break;
                case LOCATION_TASK:

                    break;
                case LOCATION_SPECIFIC_TASK:

                    break;
                default:
                    break;
            }
        }
        else {
            Log.i("Exception", "Could not download data: " + result.ex.toString());
        }
    }
    private void gotoSettings(View view){
        Intent intent = new Intent(this, FilterSearchActivity.class);
        Bundle bundle = new Bundle();

       // OnTaskCompleteHelper.onLocationCategoryTaskComplete(this, (String)result.msg);
       // LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
        if(this.locationCategoryList!=null){
            System.out.println("SIZE " + this.locationCategoryList.size());
            bundle.putParcelableArrayList("dataCategories",(ArrayList<LocationCategory>) this.locationCategoryList);
            intent.putExtras(bundle);
            startActivity(intent);
        }




    }

/*    public void onDatabaseReadComplete(DatabaseTask.Result result) {
        if (result.msg != null) {
            switch(result.tag) {
                case DatabaseTables.AIR_POLLUTION:
                    break;
                case DatabaseTables.LOCATION_CATEGORY: {
                    long now = Calendar.getInstance().getTimeInMillis();
                    System.out.println("millis: " + (now - prev));


                    List<LocationCategory> list = (List<LocationCategory>)result.msg;
                    for (LocationCategory lc : list) {
                        Log.d(TAG, lc.getSingularName());
                    }
                }
            }
        }
        else {
            Log.d(TAG, "onDatabaseReadComplete: could not read from database");
        }
    }*/


}
