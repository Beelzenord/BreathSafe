package com.breathsafe.kth.breathsafe.Database;

import android.content.Context;
import android.os.AsyncTask;

public class RetrieveAirPollution extends AsyncTask<Void,Void,Void> {
    private Context context;

    public RetrieveAirPollution(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Repository repository = Repository.getInstance(this.context);
        System.out.println("RetrieveAirPollution " + repository.airPollutionDoa().getAirPollution().size());
        return null;
    }
}
