package com.breathsafe.kth.breathsafe.IO;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Database.LocationCategoryDoa;
import com.breathsafe.kth.breathsafe.Database.Repository;
import com.breathsafe.kth.breathsafe.Database.StoreToDatabase;
import com.breathsafe.kth.breathsafe.Exceptions.CancelTaskException;
import com.breathsafe.kth.breathsafe.IO.Network.LocationDownloadThread;
import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;
import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;
import com.breathsafe.kth.breathsafe.Model.LocationData;
import com.breathsafe.kth.breathsafe.OnTaskCompleteHelper;
import com.breathsafe.kth.breathsafe.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




public class DatabaseSynchronizer extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "DatabaseSynchronizer";
    private Activity activity;

    private long startOfPool;

    private static List<LocationCategory> locationCategories;
    private static List<Location>[] locations;
    private static int position;

    public DatabaseSynchronizer(Activity activity) {
        this.activity = activity;

    }

    private void saveLocationsToDB(List<Location> wholeList) {
        StoreToDatabase.StoreLocation storeLC = new StoreToDatabase.StoreLocation(activity, wholeList);
        storeLC.execute();
    }

    private void saveLocationCategoriesToDB() {
        StoreToDatabase.StoreLocationCategory storeLC = new StoreToDatabase.StoreLocationCategory(activity, locationCategories);
        storeLC.execute();
    }

    private Location getLocationUsingId(List<Location> list, String id) {
        for (Location location : list) {
            if (location.getId().equals(id))
                return location;
        }
        return null;
    }

    private void downloadLocations() throws ExecutionException, InterruptedException {
        position = -1;
        int cores = Runtime.getRuntime().availableProcessors();
        Log.d(TAG, "downloadLocations: cores: " + cores);
        int alive = 1000;
        TimeUnit time = TimeUnit.MILLISECONDS;
        String prefix = activity.getString(R.string.api_stockholm_specific_prefix);
        String suffix = activity.getString(R.string.api_stockholm_specific_midfix) +
                        activity.getString(R.string.stockholm_api_key);
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(locationCategories.size());
        ExecutorService executorService = new ThreadPoolExecutor(cores, cores, alive, time, arrayBlockingQueue);
        Collection<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < locationCategories.size(); i++) {
            LocationDownloadThread thread = new LocationDownloadThread(locations, i, locationCategories.get(i), prefix, suffix);
            Future future = executorService.submit(thread);
            futures.add(future);
        }
        for (Future f : futures) {
            f.get();
        }
        Log.d(TAG, "downloadLocations: DONE");
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        long startOfAll = System.currentTimeMillis();
        Log.d(TAG, "timer: start of all: " + startOfAll);
        System.out.println("URL PATH : " + activity.getResources().getString(R.string.api_stockholm_all_categories));
        String apicall = activity.getResources().getString(R.string.api_stockholm_all_categories) +
                activity.getResources().getString(R.string.stockholm_api_key);

        InputStream in = null;
        HttpURLConnection http = null;
        BufferedReader br = null;

        StringBuilder urlStr = new StringBuilder();
        URL url = null;
        try {
            urlStr.append(apicall);
            Log.i(TAG, urlStr.toString());
            url = new URL(urlStr.toString());
            http = (HttpURLConnection) url.openConnection();
            in = http.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            String msg = sb.toString();
            locationCategories = OnTaskCompleteHelper.onLocationCategoryTaskComplete(activity, msg);
            LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
            locationCategoryData.setList(locationCategories);

            locations = (List<Location>[]) new ArrayList[locationCategories.size()];
            startOfPool = System.currentTimeMillis();
            downloadLocations();

            long startOfMerge = System.currentTimeMillis();
            Log.d(TAG, "run: Time to download all: " + (startOfMerge - startOfPool));
            List<Location> wholeList = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();
            for (int i = 0; i < locations.length; i++) {
                List<Location> partList = locations[i];
                for (Location l : partList) {
                    if (keys.contains(l.getId())) {
                        Location dupLocation = getLocationUsingId(wholeList, l.getId());
                        if (dupLocation != null) {
                            dupLocation.addCategory(l.getFirstCategory());
                        }
                    }
                    else {
                        wholeList.add(l);
                        keys.add(l.getId());
                    }
                }
            }
            Log.d(TAG, "downloadLocations: Time to merge: " + (System.currentTimeMillis() - startOfMerge));

            ArrayList<String> keys2 = new ArrayList<>();
            for (Location l : wholeList) {
                if (keys2.contains(l.getId())) {
                    Log.d(TAG, "run: duplicate " + l.getId() + " " + l.getName());
                }
                keys2.add(l.getId());
            }

            LocationData locationData = LocationData.getInstance();
            locationData.setList(wholeList);

            saveLocationCategoriesToDB();
            saveLocationsToDB(wholeList);
            Log.d(TAG, "timer: end of all: " + (System.currentTimeMillis() - startOfAll));
        } catch (Exception e) {
            Log.d(TAG, "run: threadpool rip : " + e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * When the task is done call onDownloadComplete.
     * @param result The result of the download.
     */
    @Override
    protected void onPostExecute(Boolean result) {
        ((MainActivity)activity).onDBSynchronizeComplete(result);
    }
}
