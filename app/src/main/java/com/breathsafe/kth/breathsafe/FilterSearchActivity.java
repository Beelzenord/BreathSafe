package com.breathsafe.kth.breathsafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.breathsafe.kth.breathsafe.Adapters.CustomAdapter;
import com.breathsafe.kth.breathsafe.Adapters.ListItem;
import com.breathsafe.kth.breathsafe.Adapters.LocationCategoryAdapter;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {
    private Spinner spinner;
    private List<LocationCategory> locationCategories;
    private RecyclerView recyclerView;
    private List<ListItem> items;
    private CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);
        locationCategories = new ArrayList<LocationCategory>();
        spinner = (Spinner) findViewById(R.id.spinner);
        Bundle bundle = getIntent().getExtras();
        ArrayList<LocationCategory> arraylist = bundle.getParcelableArrayList("dataCategories");


        //The spinner for the list of categories
        final LocationCategoryAdapter locationAdapter = new LocationCategoryAdapter(this,0,arraylist,this);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(locationAdapter);


        setUpHandler();


        //its corresponding recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

         items = new ArrayList<ListItem>();
         for(int i = 0 ; i < 10 ;i++){
             ListItem it = new ListItem(
                     "Item " + (i+1),"Description"
             );
             items.add(it);
         }
         customAdapter = new CustomAdapter(this,locationCategories);

         recyclerView.setAdapter(customAdapter);










    }

    private void setUpHandler() {
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ViewParent viewParent = view.getParent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }

    public void feedBack(LocationCategory item, boolean isTicked){
       if(isTicked){
           locationCategories.add(item);

           recyclerView.setAdapter(customAdapter);
       }
       else{
           locationCategories.remove(item);
           recyclerView.setAdapter(customAdapter);
       }
    }
}
