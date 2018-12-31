package com.breathsafe.kth.breathsafe.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Model.Location;

public class LinkAsFavorite extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    private Location location;
    private String foreinKey;
    private final String TAG = "LinkAsFavorite";
    public LinkAsFavorite(Context context,String categoryID, Location location) {
        this.context = context;
        this.foreinKey = categoryID;
        this.location = location;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        location.setChildId(foreinKey);
        Repository repository = Repository.getInstance(this.context);
        repository.locationDoa().update(location);
        Log.i(TAG,"Foreign Key " + this.foreinKey);
        Log.i(TAG,"Successfully updated " + location.toString());
        Location l = repository.locationDoa().getById(location.getId());
        Log.i(TAG,"retrieved " + l.toString());
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        super.onPostExecute(aBoolean);
    }
}
