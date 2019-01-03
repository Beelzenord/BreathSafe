package com.breathsafe.kth.breathsafe.Search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.breathsafe.kth.breathsafe.Database.RetrieveFavorites;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationData;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter ;
    private final String TAG  = "FavoritesFragment";

    private RetrieveFavorites retrieveFavorites;
    private boolean hasLoaded = false;
    private List<Location> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     //   return super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG,"onCreate:");
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.favorite_recyclerView);
        retrieveFavorites = new RetrieveFavorites(getContext());

//        try {
//            list = retrieveFavorites.execute().get();
            list = new ArrayList<Location>();
            // System.out.println("list " +list.size());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            favoritesAdapter = new FavoritesAdapter(getContext(), list);
            recyclerView.setAdapter(favoritesAdapter);
            hasLoaded = true;



//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.i("Fragments", "SearchCategoryFragment is visible");
            if (hasLoaded)
                updateList();
        }
    }

    private void updateList() {
        for (Location l : LocationData.getInstance().getList()) {
            if (l.isFavorite()) {
                if (!list.contains(l))
                    list.add(l);
            }
        }
        favoritesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume:");
       // favoritesAdapter = new FavoritesAdapter(getContext(),list);
        /*try {
            list = retrieveFavorites.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //recyclerView.setAdapter(favoritesAdapter);
        super.onResume();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroy : " );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop : " );
    }
}
