package com.breathsafe.kth.breathsafe.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import java.util.List;

public class StoreToDatabase extends AsyncTask<Void,Void,Boolean> {
    private Repository repository;
    private Context context;
    private List<LocationCategory> locationCategories;
    public StoreToDatabase(Context context, List<LocationCategory> locationCategoryList){
        this.context = context;
        this.locationCategories = locationCategoryList;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        repository = Repository.getInstance(this.context);
        repository.locationCategoryDoa().insert(locationCategories);
        if(repository.locationCategoryDoa().countNumberOfEntities()!=0){
            return true;
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
