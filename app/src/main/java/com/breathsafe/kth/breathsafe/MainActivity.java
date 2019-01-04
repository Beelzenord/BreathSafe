package com.breathsafe.kth.breathsafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.Comments.LoginActivity;
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
import com.breathsafe.kth.breathsafe.Search.SearchActivity;
import com.breathsafe.kth.breathsafe.Utilities.Constants;
import com.breathsafe.kth.breathsafe.Utilities.Util;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AsyncTaskCallback {
    private static final String TAG = "MainActivity";
    private static final int AIR_TASK = 0;
    private static final int LOCATION_CATEGORY_TASK = 1;
    private static final int LOCATION_TASK = 2;
    private static final int LOCATION_SPECIFIC_TASK = 3;
    private long prev;
    private long startDBSynchronizer;
    public long startOfApp;
    private boolean searchButtonPressed;
    private boolean databaseSynchronizerIsRunning;
    private int loadingDatabaseLocationsCounter;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startOfApp = System.currentTimeMillis();
        Log.i(TAG, "onCreate: ");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        if (true)
            return;

        searchButtonPressed = false;
        databaseSynchronizerIsRunning = false;

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(mViewPager);

        openOrCreateDatabase(getResources().getString(R.string.database), MODE_PRIVATE, null);
        startReadFromDatabase();
    }



    private void startReadFromDatabase() {
        prev = System.currentTimeMillis();
//        DatabaseTask.Read favorites = new DatabaseTask.Read(this, null, DatabaseTables.LOCATION_FAVORITES);
//        favorites.execute();
        DatabaseTask.Read databaseTask = new DatabaseTask.Read(this, null, DatabaseTables.LOCATION_CATEGORY);
        databaseTask.execute();
        DatabaseTask.Read location = new DatabaseTask.Read(this, null, DatabaseTables.LOCATION);
        location.execute();
        DatabaseTask.Read airPollution = new DatabaseTask.Read(this, null, DatabaseTables.AIR_POLLUTION);
        airPollution.execute();
    }

    public void startDatabaseSynchronizer() {
        if (!databaseSynchronizerIsRunning) {
            DatabaseSynchronizer databaseSynchronizer = new DatabaseSynchronizer(this);
            databaseSynchronizer.execute();
            databaseSynchronizerIsRunning = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.i(TAG, "onResume: ");
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

                    Log.i(TAG, "onDatabaseReadComplete: time to read LocationCategory: " + (now - prev));
                    Log.i(TAG, "onDatabaseReadComplete: size of LocationCategory: " + list.size());
                    if (Util.isThresholdReachedToDownloadPlaces(this)) {
                        Log.i(TAG, "onDatabaseReadComplete: starting DatabaseSynchronizer");
                        startDBSynchronizer = System.currentTimeMillis();
                        startDatabaseSynchronizer();
                    }
                    else if (searchButtonPressed) {
                        SearchActivity.setLocationCategoriesLoaded();
                        Log.i(TAG, "onDatabaseReadComplete: setLocationCategoriesLoaded");
                    }
                    break;
                }

                case DatabaseTables.LOCATION: {
                    long now = Calendar.getInstance().getTimeInMillis();
                    List<Location> list = (List<Location>)result.msg;
                    Log.i(TAG, "onDatabaseReadComplete: time to read Location: " + (now-prev));
                    Log.i(TAG, "onDatabaseReadComplete: size of Location: " + list.size());
                    for (Location l : list) {
                        DatabaseTask.Read databaseTask = new DatabaseTask.Read(this, l, DatabaseTables.LOCATION_AND_CATEGORY_RELATION);
                        databaseTask.execute();
                    }
                    SearchActivity.hello("From mainactivity");
                    LocationData.getInstance().setList(list);
                    loadingDatabaseLocationsCounter = 0;
                    break;
                }
                case DatabaseTables.LOCATION_AND_CATEGORY_RELATION: {
                    loadingDatabaseLocationsCounter++;
                    if (loadingDatabaseLocationsCounter >= LocationData.getInstance().getList().size()) {
                        if (searchButtonPressed) {
                            SearchActivity.setLocationsLoaded();
                            Log.i(TAG, "onDatabaseReadComplete: SearchActivity.setLocationsLoaded()");
                        }
//                        else
//                            mainFragment.updateList();
                        Log.i(TAG, "Everything loaded: " + (System.currentTimeMillis() - prev));
                    }
                    break;
                }
                case DatabaseTables.LOCATION_FAVORITES: {
                    Log.i(TAG, "onDatabaseReadComplete: LOCATION_FAVORITES time: " + (System.currentTimeMillis() - prev));
                    List<Location> favorites = (List<Location>)result.msg;
                    mainFragment.updateList(favorites);
                }
            }
        }
        else {
            Log.i(TAG, "onDatabaseReadComplete: could not read from database");
//            result.ex.printStackTrace();
            Log.i(TAG, "onDatabaseReadComplete: " + result.tag);
        }
    }

    /**
     * When another activity returns data to this activity.
     * If the requestCode is SELECT_LOCATION then this activity will try to show information
     * about the selected location.
     * @param requestCode The request code.
     * @param resultCode The result code.
     * @param result Contains the information sent from the other activity to this activity.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.SEARCH_ACTIVITY_LOCATION:
                    Log.i(TAG, "onActivityResult: ");
                    startMapActivity();
                    break;
            }
        }
    }

    public void onDBSynchronizeComplete(Boolean result) {
        Log.i(TAG, "onDBSynchronizeComplete: timer: " + (System.currentTimeMillis() - startDBSynchronizer));
        Log.i(TAG, "onDBSynchronizeComplete: since start: " + (System.currentTimeMillis() - prev));
        databaseSynchronizerIsRunning = false;
        if (result && searchButtonPressed) {
            SearchActivity.setLocationCategoriesLoaded();
            SearchActivity.setLocationsLoaded();
        }
        else if(result)
            mainFragment.updateList(LocationData.getInstance().getList());
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new FavoritesFragment(),"FavoritesFragment");
        mainFragment = new MainFragment();
        adapter.addFragment(mainFragment, "MainFragment");
        viewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
    }

    public void setmViewPagerint(int nr) {
        mViewPager.setCurrentItem(nr);
    }

    public void startMapActivity() {
        Log.i(TAG, "startMapActivity: ");
        Constants.setStart();
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    /**
     * MainFragment call this method when the search button is pressed.
     */
    public void startSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra(Constants.SEARCH_ACTIVITY_CALLBACK_ACTIVITY, Constants.SEARCH_ACTIVITY_CALLBACK_MAINACTIVITY);
        startActivityForResult(intent, Constants.SEARCH_ACTIVITY_LOCATION);
    }

    /**
     * Shows a short toast message in the middle of the screen.
     * @param msg The message to show.
     */
    private void showToast(String msg) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onDownloadComplete(NetworkTask.Result result) {
        Log.i(TAG, "onDownloadComplete: is this ever called?");
        if (result.msg != null) {
            switch (result.tag) {
                case AIR_TASK: break;
                case LOCATION_CATEGORY_TASK: break;
                case LOCATION_TASK: break;
                case LOCATION_SPECIFIC_TASK: break;
                default: break;
            }
        }
        else {
            Log.i("Exception", "Could not download data: " + result.ex.toString());
        }
    }
}

