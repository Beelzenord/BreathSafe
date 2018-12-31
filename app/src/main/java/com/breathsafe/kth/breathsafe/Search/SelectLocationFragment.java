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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.breathsafe.kth.breathsafe.Database.LinkAsFavorite;
import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;
import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.Model.LocationData;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;

public class SelectLocationFragment extends Fragment implements SelectLocationAdapter.ItemClickListener {
    private static final String TAG = "SelectLocationFragment";
    private RecyclerView mRecyclerView;
    private SelectLocationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LocationData locationData;
    private EditText searchText;
    private String actualSearchText;
    private String actualSearchTextPrev;
    private List<Location> selectedCategoryLocations;

    private String locatorForCategory;

    private DisplayOnMapList displayOnMapList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Fragments", "onCreateView SelectLocationFragment");
        View view = inflater.inflate(R.layout.select_item_fragment, container, false);
        setHasOptionsMenu(true);
        actualSearchText = "";
        actualSearchTextPrev = "";
        locationData = LocationData.getInstance();
        displayOnMapList = DisplayOnMapList.getInstance();
        searchText = view.findViewById(R.id.search2_search_text);
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
                if (!actualSearchText.equals(actualSearchTextPrev)) {
                    actualSearchTextPrev = actualSearchText;
                    updateList();
                }
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
            Log.i("Fragments", "SelectLocationFragment is visible");
            updateList();
            searchText.requestFocus();
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
            case R.id.menu_search_select_item :
                ((SearchActivity)getActivity()).setmViewPagerIntCategory(1, "All");
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
        mRecyclerView = view.findViewById(R.id.search2_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SelectLocationAdapter(getActivity(), new ArrayList<Location>());
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
        Location location = mAdapter.getItem(position);
        if (location.getName().equals("All")) {
            Log.i(TAG, "Clicked All");
            if (selectedCategoryLocations.size() == locationData.getList().size()) {
                Toast.makeText(getContext(), "You may only select All locations if you first select a category", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.i(TAG, "onItemClick: selectedCategoryLocations: " + selectedCategoryLocations.size());
                Log.i(TAG, "onItemClick: locationData: " + locationData.getList().size());
                for (Location l : selectedCategoryLocations) {
                    displayOnMapList.add(l);
                }
                ((SearchActivity)getActivity()).exitThisActivity();
            }
        }
        else {
            Log.i(TAG, "Name: " + location.getName());
            Log.i(TAG, "Lat: " + location.getLatitude());
            Log.i(TAG, "Lng: " + location.getLongitude());
            Log.i(TAG, "child: " + location.getChildId() + " " + this.locatorForCategory) ;
            displayOnMapList.add(location);

          //  location.setChildId(this.locatorForCategory);
            //Link With Foreign key
            LinkAsFavorite linkAsFavorite = new LinkAsFavorite(getContext(),this.locatorForCategory,location);

            linkAsFavorite.execute();

         //   ((MainActivity)getActivity()).startMapActivity();
//            ((SearchActivity)getActivity()).setmViewPagerint(1);
//            ((MainActivity)getActivity()).startMapActivity();
            ((SearchActivity)getActivity()).exitThisActivity();
        }
    }

    private List<Location> newList() {
        long now = System.currentTimeMillis();

        if (selectedCategoryLocations != null && selectedCategoryLocations.size() > 0) {
            List<Location> clone = new ArrayList<>();
            Location all = new Location("All");
            clone.add(all);
            for (Location l : selectedCategoryLocations) {
                if (l.getName().toLowerCase().contains(actualSearchText.toLowerCase()))
                    clone.add(l);
            }
            Log.i(TAG, "newList: Time to create clone: " + (System.currentTimeMillis() - now));
            return clone;
        }
        else {
            Log.i(TAG, "newList: else Time to create clone: " + (System.currentTimeMillis() - now));
            return new ArrayList<Location>();
        }
    }

    public void onKeyTypedInSearchField(View view) {
        updateList();
    }

    public void setCategory(String category) {
        long start = System.currentTimeMillis();
        if (category.equalsIgnoreCase("All")) {
            selectedCategoryLocations = locationData.getList();
        }
        else {
            List<Location> list = locationData.getList();
            Log.i(TAG, "setCategory: locationData size: " + list.size());
            selectedCategoryLocations = new ArrayList<>();
            for (Location l : list) {
                if (l.containsCategory(category))
                    selectedCategoryLocations.add(l);
            }
        }
        Log.i(TAG, "setCategory: timer: " + (System.currentTimeMillis() - start));
       // this.locatorForCategory = identifier;
       // Log.i(TAG,"list category from fragment " + selectedCategoryLocations.size() + " identifier " + identifier);
    }

    public void setCategory(String category, String categoryIdentifier) {
        this.locatorForCategory = categoryIdentifier;
        long start = System.currentTimeMillis();
        if (category.equalsIgnoreCase("All")) {
            selectedCategoryLocations = locationData.getList();
        }
        else {
            List<Location> list = locationData.getList();
            Log.i(TAG, "setCategory: locationData size: " + list.size());
            selectedCategoryLocations = new ArrayList<>();
            for (Location l : list) {
                if (l.containsCategory(category))
                    selectedCategoryLocations.add(l);
            }
        }
        Log.i(TAG, "setCategory: timer: " + (System.currentTimeMillis() - start));
    }
}
