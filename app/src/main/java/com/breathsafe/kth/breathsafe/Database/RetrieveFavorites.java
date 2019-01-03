package com.breathsafe.kth.breathsafe.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.breathsafe.kth.breathsafe.Model.Categories;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;

public class RetrieveFavorites extends AsyncTask<Void,Void,List<Location>> {
    private Context context;

    public RetrieveFavorites(Context context) {
        this.context = context;
    }

    @Override
    protected List<Location> doInBackground(Void... voids) {
        Repository repository = Repository.getInstance(this.context);
        List<Location> locations = repository.locationDoa().getFavorites();


        if(locations!=null){
            for(Location l : locations){
                //LocationCategory locationCategory = repository.locationCategoryDoa().getSingle(l.getChildId());
             //   Categories categories = new Categories(locationCategory.getSingularName());
             //   l.setCategories(categories);
                List<String> tmp = l.getCategoryNames();

                if(tmp!=null){
                    for(String s : tmp){
                        System.out.println("CATEGORY: " + s);
                    }
                }

            }
            return locations;
        }
        else{
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Location> locations) {
        super.onPostExecute(locations);
    }
}
