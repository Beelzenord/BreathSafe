package com.breathsafe.kth.breathsafe.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import java.util.List;

public class StoreToDatabase {
    private static final String TAG = "StoreToDatabase";

    public class StoreLocationCategoryOld extends AsyncTask<Void, Void, Boolean> {
        private Repository repository;
        private Context context;
        private List<LocationCategory> locationCategories;
        public StoreLocationCategoryOld(Context context, List<LocationCategory> locationCategoryList){
            this.context = context;
            this.locationCategories = locationCategoryList;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            repository = Repository.getInstance(this.context);

            repository.locationCategoryDoa().insertAsList(locationCategories);
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

    public static class StoreLocationCategory extends AsyncTask<Void, Void, Boolean> {
        private Repository repository;
        private Context context;
        private List<LocationCategory> locationCategories;
        public StoreLocationCategory(Context context, List<LocationCategory> locationCategoryList){
            this.context = context;
            this.locationCategories = locationCategoryList;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            long startSavingLocationCategories = System.currentTimeMillis();
            repository = Repository.getInstance(this.context);
            int count = repository.locationCategoryDoa().countNumberOfEntities();
            if (count == locationCategories.size()) {
                repository.locationCategoryDoa().updateAsList(locationCategories);
            }
            else {
                long id;
                for (LocationCategory lc : locationCategories) {
                    id = repository.locationCategoryDoa().insert(lc);
                    if (id < 0) {
                        repository.locationCategoryDoa().update(lc);
                    }
                }
            }
            Log.d(TAG, "timer: end of LocationCategories: " + System.currentTimeMillis());
            Log.d(TAG, "Time to save locationcategores to db: " + (System.currentTimeMillis() - startSavingLocationCategories));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    public static class StoreLocation extends AsyncTask<Void, Void, Boolean> {
        private Repository repository;
        private Context context;
        private List<Location> locations;
        public StoreLocation(Context context, List<Location> locations){
            this.context = context;
            this.locations = locations;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            long startOfDatabaseSave = System.currentTimeMillis();
            repository = Repository.getInstance(this.context);
            int count = repository.locationDoa().countNumberOfEntities();
            Log.d(TAG, "number of location in db: " + count);
            if (count == locations.size()) {
                repository.locationDoa().updateAsList(locations);
            }
            /** this takes very long but shouldn't happen more than a few times a year **/
            else {
                long id;
                for (Location l : locations) {
                    id = repository.locationDoa().insert(l);
                    if (id < 0) {
                        repository.locationDoa().update(l);
                    }
                }
            }
            Log.d(TAG, "timer: end of Location: " + System.currentTimeMillis());
            Log.d(TAG, "run: Time to save to database: " + (System.currentTimeMillis() - startOfDatabaseSave));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    public static class StoreAirPollution extends AsyncTask<Void, Void, Boolean> {
        private Repository repository;
        private Context context;
        private List<AirPollution> airPollutions;
        public StoreAirPollution(Context context, List<AirPollution> airPollutions){
            this.context = context;
            this.airPollutions = airPollutions;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            long startOfDatabaseSave = System.currentTimeMillis();
            repository = Repository.getInstance(this.context);
            int count = repository.airPollutionDoa().countNumberOfEntities();
            Log.d(TAG, "number of air pollution in db: " + count);
            if (count == airPollutions.size()) {
                repository.airPollutionDoa().updateAsList(airPollutions);
            }
            /** this takes very long **/
            else {
                long id;
                for (AirPollution a : airPollutions) {
                    id = repository.airPollutionDoa().insert(a);
                    if (id < 0) {
                        repository.airPollutionDoa().update(a);
                    }
                }
            }
            Log.d(TAG, "timer: end of AirPollution: " + System.currentTimeMillis());
            Log.d(TAG, "run: Time to save to database: " + (System.currentTimeMillis() - startOfDatabaseSave));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    public static class DeleteAllAndStoreAirPollution extends AsyncTask<Void, Void, Boolean> {
        private Repository repository;
        private Context context;
        private List<AirPollution> airPollutions;
        public DeleteAllAndStoreAirPollution(Context context, List<AirPollution> airPollutions){
            this.context = context;
            this.airPollutions = airPollutions;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            long startOfDatabaseSave = System.currentTimeMillis();
            repository = Repository.getInstance(this.context);
            repository.airPollutionDoa().deleteAllAirPollution();
            repository.airPollutionDoa().insertAsList(airPollutions);
            /** this takes very long **/
            /*else {
                long id;
                for (AirPollution a : airPollutions) {
                    id = repository.airPollutionDoa().insert(a);
                    if (id < 0) {
                        repository.airPollutionDoa().update(a);
                    }
                }
            }*/
            Log.d(TAG, "timer: end of AirPollution: " + System.currentTimeMillis());
            Log.d(TAG, "run: Time to save to database: " + (System.currentTimeMillis() - startOfDatabaseSave));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

}
