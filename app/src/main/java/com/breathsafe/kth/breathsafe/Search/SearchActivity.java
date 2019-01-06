package com.breathsafe.kth.breathsafe.Search;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Utilities.Constants;
import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.R;

/**
 * An Activity to let the user search for location.
 * There are multiple fragments to let the user choose what to
 * search for, Locations or LocationCategories.
 * This activity can return to either the MainActivity or the
 * MapActivity depending on which started the SearchActivity.
 */
public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private int callbackActivity;

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

    /**
     * Setup for the view pager to enable multiple fragments.
     * @param viewPager The view pager to set up.
     */
    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        searchCategoryFragment = new SearchCategoryFragment();
        adapter.addFragment(searchCategoryFragment, "SearchCategory");
        selectLocationFragment = new SelectLocationFragment();
        adapter.addFragment(selectLocationFragment, "SelectLocation");
        viewPager.setAdapter(adapter);
    }

    /**
     * Set the view to a specific page.
     * @param nr The place of the page to switch to.
     */
    public void setmViewPagerint(int nr) {
        mViewPager.setCurrentItem(nr);
    }

    /**
     * Set the view to a specific page using a specific category.
     * @param nr The place of the page to switch to.
     * @param category The specific category to use.
     */
    public void setmViewPagerIntCategory(int nr, String category) {
        selectLocationFragment.setCategory(category);
        mViewPager.setCurrentItem(nr);
    }

    /**
     * Exist this activity and returns to a specific activity.
     */
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

    /**
     * If the went to this activity before data was loaded to the model from either
     * the database or the web, this method will be called to the UI updates when data
     * is finally loaded.
     */
    public static void setLocationCategoriesLoaded() {
        if (searchCategoryFragment != null) {
            if (searchCategoryFragment.isVisible())
                searchCategoryFragment.updateList();
        }
    }

    /**
     * If the went to this activity before data was loaded to the model from either
     * the database or the web, this method will be called to the UI updates when data
     * is finally loaded.
     */
    public static void setLocationsLoaded() {
        if (selectLocationFragment != null) {
            Log.i(TAG, "setLocationsLoaded: ");
            if (selectLocationFragment.isVisible()) {
                Log.i(TAG, "setLocationsLoaded: ");
                selectLocationFragment.updateList();
            }
        }
    }

   /* public static boolean isLocationCategoriesLoaded() {
        return locationCategoriesLoaded;
    }

    public static boolean isLocationsLoaded() {
        return locationsLoaded;
    }*/
}
