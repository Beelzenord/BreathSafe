package com.breathsafe.kth.breathsafe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private Button searchButton;
    private Button goToMapButton;
    private Button goToFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Fragments", "MainFragment");
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);
        goToFragment = (Button) view.findViewById(R.id.go_to_favourites_button);
        goToFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).setmViewPagerint(0);
            }
        });
        searchButton = (Button)view.findViewById(R.id.main_search_text);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity)getActivity()).setmViewPagerint(1);
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

        return view;
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
                ((MainActivity)getActivity()).startSearchActivity();
                break;
            case R.id.menu_search_select_item :
                ((MainActivity)getActivity()).startSearchActivity();
                break;
            case R.id.menu_search_select_category :

                break;
        }
        return true;
    }


}
