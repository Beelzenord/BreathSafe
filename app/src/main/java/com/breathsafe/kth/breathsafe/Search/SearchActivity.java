package com.breathsafe.kth.breathsafe.Search;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Constants;
import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.R;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private int callbackActivity;
    private static boolean locationCategoriesLoaded;
    private static boolean locationsLoaded;

    private static SearchCategoryFragment searchCategoryFragment;
    private static SelectLocationFragment selectLocationFragment;

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.search_container);
        setupViewPager(mViewPager);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            callbackActivity = extras.getInt(Constants.SEARCH_ACTIVITY_CALLBACK_ACTIVITY);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

       // adapter.addFragment(new FavoritesFragment(),"FavoritesFragment");
        searchCategoryFragment = new SearchCategoryFragment();
        adapter.addFragment(searchCategoryFragment, "SearchCategory");
        selectLocationFragment = new SelectLocationFragment();
        adapter.addFragment(selectLocationFragment, "SelectLocation");
//        adapter.addFragment(new SelectCategoryFragment(), "SelectCategory");
        viewPager.setAdapter(adapter);
    }

    public void setmViewPagerint(int nr) {
        mViewPager.setCurrentItem(nr);
    }

    public void setmViewPagerIntCategory(int nr, String category) {
        selectLocationFragment.setCategory(category);
        mViewPager.setCurrentItem(nr);
    }

    /*public void setmViewPagerIntCategory(int nr, String category, String categoryIdentifier) {
        selectLocationFragment.setCategory(category,categoryIdentifier);
        mViewPager.setCurrentItem(nr);
    }*/
    public void exitThisActivity() {
        Intent intent = null;
        switch (callbackActivity) {
            case Constants.SEARCH_ACTIVITY_CALLBACK_MAINACTIVITY:
                intent = new Intent(this, MainActivity.class);
                break;
            case Constants.SEARCH_ACTIVITY_CALLBACK_MAPACTIVITY:
                Constants.setStart();
                intent = new Intent(this, MapActivity.class);
                break;
        }
        if (intent != null) {
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    public static void hello(String s) {
        Log.i(TAG, "hello: " + s);
    }



    public static void setLocationCategoriesLoaded() {
        if (searchCategoryFragment != null) {
            if (searchCategoryFragment.isVisible())
                searchCategoryFragment.updateList();
        }
    }

    public static void setLocationsLoaded() {
        if (selectLocationFragment != null) {
            Log.i(TAG, "setLocationsLoaded: ");
            if (selectLocationFragment.isVisible()) {
                Log.i(TAG, "setLocationsLoaded: ");
                selectLocationFragment.updateList();
            }
        }
    }

    public static boolean isLocationCategoriesLoaded() {
        return locationCategoriesLoaded;
    }

    public static boolean isLocationsLoaded() {
        return locationsLoaded;
    }
}
