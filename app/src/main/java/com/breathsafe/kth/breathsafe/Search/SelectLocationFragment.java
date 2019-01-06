package com.breathsafe.kth.breathsafe.Search;

import android.content.Context;
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

import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationData;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to let the user search and select a Location.
 */
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
    private DisplayOnMapList displayOnMapList;
    private String category;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Fragments", "onCreateView SelectLocationFragment");
        View view = inflater.inflate(R.layout.select_item_fragment, container, false);
        setHasOptionsMenu(true);
        category = "";
        actualSearchText = "";
        actualSearchTextPrev = "";
        locationData = LocationData.getInstance();
        displayOnMapList = DisplayOnMapList.getInstance();
        searchText = view.findViewById(R.id.search2_search_text);

        addTextWatcher();
        createRecycler(view);
        return view;
    }
    /**
     * Updates the list of Locations instantly when the user types in the search field.
     */
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

    /**
     * Update the list of items when the user goes to this fragment.
     * @param isVisibleToUser If this fragment is visible.
     */
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
     * @param item The menu name to handle.
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
     * Creates the recycler which contains the location. Creates the adapter used with the recycler.
     * @param view The view of this fragment.
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

    /**
     * Updates the list of Locations.
     */
    public void updateList() {
        mAdapter.setList(newList(), category);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * When an item is clicked it is picked out the location to the be display in the MapActivity.
     * @param view The view the click came from.
     * @param position The position in the list of the name clicked.
     */
    @Override
    public void onItemClick(View view, int position) {
        Location location = mAdapter.getItem(position);
        if (location.getName().equals("All")) {
            Log.i(TAG, "Clicked All");
            if (selectedCategoryLocations.size() == locationData.getList().size()) {
                Toast.makeText(getContext(), "You may only select All locations if you first select a category", Toast.LENGTH_LONG).show();
            }
            else {
                Log.i(TAG, "onItemClick: selectedCategoryLocations: " + selectedCategoryLocations.size());
                Log.i(TAG, "onItemClick: locationData: " + locationData.getList().size());
                List<Location> list = mAdapter.getList();
                if (list.size() > 1) {
                    for (int i = 1; i < list.size(); i++)
                        displayOnMapList.add(list.get(i));
                    ((SearchActivity) getActivity()).exitThisActivity();
                }
            }
        }
        else {
            Log.i(TAG, "Name: " + location.getName());
            Log.i(TAG, "Lat: " + location.getLatitude());
            Log.i(TAG, "Lng: " + location.getLongitude());
            displayOnMapList.add(location);
            ((SearchActivity)getActivity()).exitThisActivity();
        }
    }
    /**
     * Creates a new List of Locations to show to the user.
     * This list will only include matches of when the user typed in the search field.
     * @return The new list of Locations to display to the user.
     */
    private List<Location> newList() {
        long now = System.currentTimeMillis();
        if (selectedCategoryLocations == null || category.equalsIgnoreCase("All"))
            selectedCategoryLocations = LocationData.getInstance().getList();
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

    /**
     * Sets the category that the user selected and creates a list
     * of Locations for that category.
     * @param category The category selected.
     */
    public void setCategory(String category) {
        this.category = category;
        Log.i(TAG, "setCategory: " + category);
        if (category.equalsIgnoreCase("All")) {
            selectedCategoryLocations = locationData.getList();
        }
        else {
            List<Location> list = locationData.getList();
            selectedCategoryLocations = new ArrayList<>();
            for (Location l : list) {
                if (l.containsCategoryName(category))
                    selectedCategoryLocations.add(l);
            }
        }
    }

}
