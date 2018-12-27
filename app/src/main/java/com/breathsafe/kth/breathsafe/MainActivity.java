package com.breathsafe.kth.breathsafe;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Database.DatabaseTables;
import com.breathsafe.kth.breathsafe.IO.DatabaseRead.DatabaseTask;
import com.breathsafe.kth.breathsafe.IO.DatabaseSynchronizer;
import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.AirPollutionData;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;
import com.breathsafe.kth.breathsafe.Model.LocationData;
import com.breathsafe.kth.breathsafe.Search.PagerAdapter;
import com.breathsafe.kth.breathsafe.Search.SearchCategoryFragment;
import com.breathsafe.kth.breathsafe.Search.SelectCategoryFragment;
import com.breathsafe.kth.breathsafe.Search.SelectLocationFragment;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AsyncTaskCallback {
    private static final String TAG = "MainActivity";
    private static final int AIR_TASK = 0;
    private static final int LOCATION_CATEGORY_TASK = 1;
    private static final int LOCATION_TASK = 2;
    private static final int LOCATION_SPECIFIC_TASK = 3;
    private long prev;
    private boolean searchButtonPressed;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private SelectLocationFragment selectLocationFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: hello0");
        searchButtonPressed = false;


        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);

//        System.out.println("URL PATH : " + getResources().getString(R.string.api_stockholm_all_categories));
//        String apicall = getResources().getString(R.string.api_stockholm_all_categories) +
//                getResources().getString(R.string.stockholm_api_key);
//        NetworkTask networkTask = new NetworkTask(this, apicall, LOCATION_CATEGORY_TASK);
//        networkTask.execute();


        startReadFromDatabase();


    }

    private void startReadFromDatabase() {
        prev = System.currentTimeMillis();
        DatabaseTask.Read databaseTask = new DatabaseTask.Read(this, null, DatabaseTables.LOCATION_CATEGORY);
        databaseTask.execute();
        DatabaseTask.Read location = new DatabaseTask.Read(this, null, DatabaseTables.LOCATION);
        location.execute();
        DatabaseTask.Read airPollution = new DatabaseTask.Read(this, null, DatabaseTables.AIR_POLLUTION);
        airPollution.execute();
    }

    private void startDatabaseSynchronizer() {
        DatabaseSynchronizer databaseSynchronizer = new DatabaseSynchronizer(this);
        databaseSynchronizer.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: hello1");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: hello2");
    }

    @Override
    public void onDownloadComplete(NetworkTask.Result result) {
        if (result.msg != null) {
            switch (result.tag) {
                case AIR_TASK:
                    OnTaskCompleteHelper.onAirTaskComplete(this, (String) result.msg);
                    break;
                case LOCATION_CATEGORY_TASK:
                    long now = Calendar.getInstance().getTimeInMillis();
                    System.out.println("millisc: " + (now - prev));
                    OnTaskCompleteHelper.onLocationCategoryTaskComplete(this, (String)result.msg);
                    LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
                    for (LocationCategory lc : locationCategoryData.getList())
                        System.out.println(lc.getSingularName());
//                    StoreToDatabase storeToDatabase = new StoreToDatabase(this,locationCategoryData.getList());
//                    storeToDatabase.execute(); //if executed the database holds values for location categories
                    Log.i(TAG, "onDownloadComplete: stored categories");
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

    public void onDatabaseReadComplete(DatabaseTask.Result result) {
        if (result.msg != null) {
            switch(result.tag) {
                case DatabaseTables.AIR_POLLUTION: {
                    long now = System.currentTimeMillis();
                    List<AirPollution> airPollutions = (List<AirPollution>)result.msg;
                    AirPollutionData.getInstance().setList(airPollutions);
                    Log.i(TAG, "onDatabaseReadComplete: time to read AirPollutions: " + (now - prev));
                    Log.i(TAG, "onDatabaseReadComplete: size of AirPollutions: " + airPollutions.size());
                    break;
                }
                case DatabaseTables.LOCATION_CATEGORY: {
                    long now = Calendar.getInstance().getTimeInMillis();
                    List<LocationCategory> list = (List<LocationCategory>)result.msg;
                    LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
                    locationCategoryData.setList(list);
                    Log.i(TAG, "onDatabaseReadComplete: time to read LocationCategory" + (now - prev));
                    Log.i(TAG, "onDatabaseReadComplete: size of LocationCategory: " + list.size());
                    if (list.size() <= 0) {
                        Log.i(TAG, "onDatabaseReadComplete: No data in database, starting DatabaseSynchronizer");
                        startDatabaseSynchronizer();
                    }
                    break;
                }

                case DatabaseTables.LOCATION: {
                    long now = Calendar.getInstance().getTimeInMillis();
                    List<Location> list = (List<Location>)result.msg;
                    LocationData.getInstance().setList(list);
                    Log.i(TAG, "onDatabaseReadComplete: time to read Location: " + (now-prev));
                    Log.i(TAG, "onDatabaseReadComplete: size of Location: " + list.size());
                    break;
                }
            }
        }
        else {
            Log.i(TAG, "onDatabaseReadComplete: could not read from database");
            result.ex.printStackTrace();
        }
    }



    public void onDBSynchronizeComplete(Boolean result) {
        if (result && searchButtonPressed) {
            //TODO: start search activity
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "MainFragment");
        adapter.addFragment(new SearchCategoryFragment(), "SearchCategory");
        selectLocationFragment = new SelectLocationFragment();
        adapter.addFragment(selectLocationFragment, "SelectLocation");
        adapter.addFragment(new SelectCategoryFragment(), "SelectCategory");
        viewPager.setAdapter(adapter);
    }

    public void setmViewPagerint(int nr) {
        mViewPager.setCurrentItem(nr);
    }
    public void setmViewPagerIntCategory(int nr, String category) {
        selectLocationFragment.setCategory(category);
        mViewPager.setCurrentItem(nr);
    }

    public void startMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }




}

