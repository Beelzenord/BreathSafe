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
    private int callbackActivity;
    private final String TAG = "SearchActivity";
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private SelectLocationFragment selectLocationFragment;

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

    public void setmViewPagerIntCategory(int nr, String category, String categoryIdentifier) {
        selectLocationFragment.setCategory(category,categoryIdentifier);
        mViewPager.setCurrentItem(nr);
    }
    public void exitThisActivity() {
        Intent intent = null;
        switch (callbackActivity) {
            case Constants.SEARCH_ACTIVITY_CALLBACK_MAINACTIVITY:
                Log.i(TAG,"Starting Main Activity");
                intent = new Intent(this, MainActivity.class);
                break;
            case Constants.SEARCH_ACTIVITY_CALLBACK_MAPACTIVITY:
                Log.i(TAG,"Starting Map Activity");
                intent = new Intent(this, MapActivity.class);
                break;
        }
        if (intent != null) {
            Log.i(TAG,"Setting Result");
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }



}
