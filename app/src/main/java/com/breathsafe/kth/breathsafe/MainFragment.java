package com.breathsafe.kth.breathsafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.breathsafe.kth.breathsafe.Database.DatabaseTables;
import com.breathsafe.kth.breathsafe.IO.DatabaseRead.DatabaseTask;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationData;

import java.util.ArrayList;
import java.util.List;

/**
 * The opening screen of the app. Contains the list of favorites, buttons for search and map.
 * And a menu for settings, refresh locations, and About.
 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private Button searchButton;
    private Button goToMapButton;

    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter ;
    private boolean hasLoaded = false;
    private List<Location> list;
    private long timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Fragments", "MainFragment");
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.favorite_recyclerView);
        searchButton = (Button)view.findViewById(R.id.main_search_text);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startSearchActivity();
            }
        });
        goToMapButton = view.findViewById(R.id.go_to_map_button);
        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).startMapActivity();
            }
        });

        list = new ArrayList<Location>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        favoritesAdapter = new FavoritesAdapter(getContext(), list);
        recyclerView.setAdapter(favoritesAdapter);
        hasLoaded = true;
        return view;
    }

    /**
     * Menu for this fragment.
     * @param menu The menu to use.
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
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
            case R.id.menu_main_settings :
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_main_refresh_location :
                ((MainActivity)getActivity()).startDatabaseSynchronizer();
                break;
            case R.id.menu_main_about :
                Intent intent2 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }

    /**
     * When the user goes to this fragment, update the list of favorites.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("Fragments", "SearchCategoryFragment is visible");
            updateList(LocationData.getInstance().getList());
        }
    }

    /**
     * Updates the list of favorites.
     * @param newList The new list to show.
     */
    public void updateList(List<Location> newList) {
        if (hasLoaded) {
            list.clear();
            for (Location l : newList) {
                if (l.isFavorite()) {
                    if (!list.contains(l))
                        list.add(l);
                }
            }
            favoritesAdapter.notifyDataSetChanged();
            Log.i(TAG, "updateList: start: " + (System.currentTimeMillis() - ((MainActivity)getActivity()).startOfApp));
            Log.i(TAG, "updateList: timer: " + (System.currentTimeMillis() - timer));
        }
    }

    /**
     * When the user goes to this fragment, update the list of favorites from the database.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume:");
        timer = System.currentTimeMillis();
        DatabaseTask.Read favorites = new DatabaseTask.Read(getActivity(), null, DatabaseTables.LOCATION_FAVORITES);
        favorites.execute();
    }
}
