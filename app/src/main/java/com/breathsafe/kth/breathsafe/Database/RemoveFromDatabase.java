package com.breathsafe.kth.breathsafe.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.breathsafe.kth.breathsafe.Model.Location;

public class RemoveFromDatabase extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    private Location location;

    public RemoveFromDatabase(Context context, Location location) {
        this.context = context;
        this.location = location;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Repository repository = Repository.getInstance(this.context);
        location.setChildId(null);
        System.out.println("LOCATION " + location.getName());
        repository.locationDoa().update(location);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
