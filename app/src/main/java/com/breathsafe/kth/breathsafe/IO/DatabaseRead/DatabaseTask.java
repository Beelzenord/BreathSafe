package com.breathsafe.kth.breathsafe.IO.DatabaseRead;

import android.app.Activity;
import android.os.AsyncTask;

import com.breathsafe.kth.breathsafe.Database.Repository;
import com.breathsafe.kth.breathsafe.Exceptions.CancelTaskException;
import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Database.DatabaseTables;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class DatabaseTask {


    /**
     * Reads from a file asynchronously. If the task is canceled the result be set to
     * CancelTaskException. After the task is done, onFileRead will be called on the activity.
     */
    public static class Read extends AsyncTask<Void, Void, Result> {
        private Activity activity;
        private Object data;
        private int tag;
        public Read (Activity activity, Object data, int tag) {
            this.activity = activity;
            this.data = data;
            this.tag = tag;
        }
    @Override
    protected Result doInBackground(Void... strings) {
        Result result = null;
        Repository repository;
        try {
            if (isCancelled())
                throw new CancelTaskException();
            switch (tag) {
                case DatabaseTables.AIR_POLLUTION:
                    repository = Repository.getInstance(activity);
                    List<AirPollution> list1 = repository.airPollutionDoa().getAirPollution();
                    result = new Result(tag, list1);
                    break;


                case DatabaseTables.LOCATION_CATEGORY:
                    repository = Repository.getInstance(activity);
                    List<LocationCategory> list2 = repository.locationCategoryDoa().getAllLocationCategory();
                    result = new Result(tag, list2);
//                      repository.locationCategoryDoa().insertAsList((List<LocationCategory>)data);
                    break;


                case DatabaseTables.LOCATION:
                    System.out.println("gettings locations");
                    Repository repository2 = Repository.getInstance(activity);
                    List<Location> list3 = repository2.locationDoa().getAllLocations();
                    result = new Result(tag, list3);
                    break;


            }
            if (isCancelled())
                throw new CancelTaskException();
//            if (j != null)
//                result = new Result(tag, j);
//            else
//                result = new Result(tag, new NullPointerException());

        } catch (CancelTaskException e) {
            e.printStackTrace();
            result = new Result(tag, e);
        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(tag, e);
        }
        finally {

        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        ((MainActivity)activity).onDatabaseReadComplete(result);
    }
}

/**
 * The result of the file read. Tag is to identify what was read.
 */
public static class Result {
    public int tag;
    public Object msg;
    public Exception ex;
    public Result(int tag, Object msg) {
        this.tag = tag;
        this.msg = msg;
        ex = null;
    }
    public Result(int tag, Exception ex) {
        this.tag = tag;
        this.ex = ex;
        msg = null;
    }
}
}
