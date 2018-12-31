package com.breathsafe.kth.breathsafe.Search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;


public class SearchCategoryFragment extends Fragment implements SearchCategoryAdapter.ItemClickListener {
    private static final String TAG = "SearchCategoryFragment";
    private RecyclerView mRecyclerView;
    private SearchCategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LocationCategoryData locationCategoryData;
    private EditText searchText;
    private String actualSearchText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Fragments", "onCreateView SearchCategoryFragment");
        View view = inflater.inflate(R.layout.search_category_fragment, container, false);
        setHasOptionsMenu(true);
        actualSearchText = "";
        locationCategoryData = LocationCategoryData.getInstance();
        searchText = view.findViewById(R.id.search1_search_text);
        addTextWatcher();
        createRecycler(view);
        return view;
    }

    private void addTextWatcher() {
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualSearchText = s.toString();
                Log.i(TAG, "onTextChanged: " + s);
                updateList();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("Fragments", "SearchCategoryFragment is visible");
            updateList();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handles a click on the menu.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_search_category :
                break;
            case R.id.menu_search_select_item ://org:2
                ((MainActivity)getActivity()).setmViewPagerint(3);
                break;
            case R.id.menu_search_select_category :

                break;
        }
        return true;
    }
    /**
     * Creates the recycler which contains the locations. Creates the adapter used with the recycler.
     */
    private void createRecycler(View view) {
        mRecyclerView = view.findViewById(R.id.search1_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<LocationCategory> list = locationCategoryData.getList();
        mAdapter = new SearchCategoryAdapter(getActivity(), newList());
        /*if (list != null && list.size() > 0) {
            List<LocationCategory> clone = new ArrayList<>();
            for (LocationCategory lc : list)
                clone.add(lc);
            mAdapter = new SelectLocationAdapter(getActivity(), clone);
        }
        else
            mAdapter = new SelectLocationAdapter(getActivity(), new ArrayList<LocationCategory>());*/

        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateList() {
//        if (mAdapter.hasEmptyList()) {
//            List<LocationCategory> list = locationCategoryData.getList();
//            if (list != null) {
                mAdapter.setList(newList());
                mAdapter.notifyDataSetChanged();
//            }
//        }
    }

    /**
     * When a location is clicked it is picked out and sent back to the main activity.
     * This activity closes.
     * @param view The view the click came from.
     * @param position The position in the list of the item clicked.
     */
    @Override
    public void onItemClick(View view, int position) {
        LocationCategory locationCategory = mAdapter.getLocationCategory(position);
        if (locationCategory.getSingularName().equals("All")) {
            Log.i(TAG, "Clicked All");
        }
        else {
            Log.i(TAG, "Clicked item: " + locationCategory.getSingularName());//org 2
            ((MainActivity)getActivity()).setmViewPagerIntCategory(3, locationCategory.getSingularName(),locationCategory.getId());
        }
//        Location location = mAdapter.getLocation(position);
//        result.putExtra(MainActivity.LOCATION_RESULT, (Parcelable) location);

//        setResult(Activity.RESULT_OK, result);
//        finish();
    }

    private List<LocationCategory> newList() {
        List<LocationCategory> list = locationCategoryData.getList();
        if (list.size() > 0) {
            List<LocationCategory> clone = new ArrayList<>();
            LocationCategory all = new LocationCategory("All");
            clone.add(all);
            for (LocationCategory lc : list) {
                if (lc.getSingularName().toLowerCase().contains(actualSearchText.toLowerCase()))
                    clone.add(lc);
            }
            return clone;
        }
        else {
            return new ArrayList<LocationCategory>();
        }
    }

    public void onKeyTypedInSearchField(View view) {
        updateList();

    }

}
