package com.breathsafe.kth.breathsafe.Search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.breathsafe.kth.breathsafe.Database.RetrieveFavorites;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter ;

    private List<Location> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     //   return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.favorite_recyclerView);
        RetrieveFavorites retrieveFavorites = new RetrieveFavorites(getContext());

        try {
            list = retrieveFavorites.execute().get();

            System.out.println("list " +list.size());

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


            favoritesAdapter = new FavoritesAdapter(getContext(),list);
            recyclerView.setAdapter(favoritesAdapter);



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;

    }
}
