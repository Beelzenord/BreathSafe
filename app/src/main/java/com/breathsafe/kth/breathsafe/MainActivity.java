package com.breathsafe.kth.breathsafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Database.StoreToDatabase;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.Network.NetworkTask;

//import static com.breathsafe.kth.breathsafe.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int AIR_TASK = 0;
    private static final int LOCATION_CATEGORY_TASK = 1;
    private static final int LOCATION_TASK = 2;
    private static final int LOCATION_SPECIFIC_TASK = 3;

    public boolean locationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("URL PATH : " + getResources().getString(R.string.api_stockholm_all_categories));
        String apicall = getResources().getString(R.string.api_stockholm_all_categories) +
                getResources().getString(R.string.stockholm_api_key);
        NetworkTask networkTask = new NetworkTask(this, apicall, LOCATION_CATEGORY_TASK);

        networkTask.execute();
    }



    public void onDownloadComplete(NetworkTask.Result result) {
        if (result.msg != null) {
            switch (result.tag) {
                case AIR_TASK:
                    OnTaskCompleteHelper.onAirTaskComplete(this, (String) result.msg);
                    break;
                case LOCATION_CATEGORY_TASK:
                  //  OnTaskCompleteHelper.onLocationCategoryTaskComplete(this, (String)result.msg);

                    OnTaskCompleteHelper.onLocationCategoryTaskComplete(this, (String)result.msg);
                    LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
                    for (LocationCategory lc : locationCategoryData.getList())
                        System.out.println(lc.getSingularName());
                   // StoreToDatabase storeToDatabase = new StoreToDatabase(this,locationCategoryData.getList());
                   // storeToDatabase.execute(); if executed the database holds values for location categories

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


}
