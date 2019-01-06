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

import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Fragment to let the user search and select a LocationCategory.
 */
public class SearchCategoryFragment extends Fragment implements SearchCategoryAdapter.ItemClickListener {
    private static final String TAG = "SearchCategoryFragment";
    private RecyclerView mRecyclerView;
    private SearchCategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText searchText;
    private String actualSearchText;
    private boolean hasLoaded = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Fragments", "onCreateView SearchCategoryFragment");
        View view = inflater.inflate(R.layout.search_category_fragment, container, false);
        setHasOptionsMenu(true);
        actualSearchText = "";
        searchText = view.findViewById(R.id.search1_search_text);
        addTextWatcher();
        createRecycler(view);
        hasLoaded = true;
        return view;
    }

    /**
     * Updates the list of LocationCategories instantly when the user types in the search field.
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

    /**
     * Update the list of items when the user goes to this fragment.
     * @param isVisibleToUser If this fragment is visible.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("Fragments", "SearchCategoryFragment is visible");
            if (hasLoaded) {
                updateList();
            }
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
     * Creates the recycler which contains the location categories. Creates the adapter used with the recycler.
     * @param view The view of this fragment.
     */
    private void createRecycler(View view) {
        mRecyclerView = view.findViewById(R.id.search1_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchCategoryAdapter(getActivity(), newList());
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Updates the list of LocationCategories.
     */
    public void updateList() {
        mAdapter.setList(newList());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * When an item is clicked it is picked out the category is used to find location in the next fragment.
     * @param view The view the click came from.
     * @param position The position in the list of the name clicked.
     */
    @Override
    public void onItemClick(View view, int position) {
        LocationCategory locationCategory = mAdapter.getLocationCategory(position);
        if (locationCategory.getSingularName().equals("All")) {
            Log.i(TAG, "Clicked All");
            ((SearchActivity)getActivity()).setmViewPagerIntCategory(1, "All");
        }
        else {
            Log.i(TAG, "Clicked name: " + locationCategory.getSingularName());
            ((SearchActivity)getActivity()).setmViewPagerIntCategory(1, locationCategory.getSingularName());
        }
    }

    /**
     * Creates a new List of LocationCategories to show to the user.
     * This list will only include matches of when the user typed in the search field.
     * @return The new list of LocationCategories to display to the user.
     */
    private List<LocationCategory> newList() {
        List<LocationCategory> list = LocationCategoryData.getInstance().getList();
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

}
