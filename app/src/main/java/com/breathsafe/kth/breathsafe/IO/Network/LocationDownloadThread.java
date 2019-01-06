package com.breathsafe.kth.breathsafe.IO.Network;

import android.util.Log;

import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Utilities.OnTaskCompleteHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to download all Locations which is part of a specific LocationCategory.
 * A list of Locations is created and put into the array of List<Location>.
 * This array is used by many different threads so only the position in the array
 * specified by the position attribute may be used, otherwise a ConcurrentModificationException
 * may occur.
 */
public class LocationDownloadThread implements Runnable {
    private static final String TAG = "LocationDownloadThread";
    private List<Location>[] list;
    private int position;
    private LocationCategory locationCategory;
    private String prefix;
    private String suffix;

    public LocationDownloadThread(List<Location>[] list, int position, LocationCategory locationCategory, String prefix, String suffix) {
        this.list = list;
        this.position = position;
        this.locationCategory = locationCategory;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public void run() {
        InputStream in = null;
        HttpURLConnection http = null;
        BufferedReader br = null;

        StringBuilder urlStr = new StringBuilder();
        URL url = null;
        try {
            urlStr.append(prefix);
            urlStr.append(locationCategory.getId());
            urlStr.append(suffix);

            Log.i(TAG, urlStr.toString());
            url = new URL(urlStr.toString());
            System.out.println("CALLING API: " + locationCategory.getId());
            http = (HttpURLConnection) url.openConnection();
            in = http.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            String msg = sb.toString();


            List<Location> data = OnTaskCompleteHelper.onSpecificLocationTaskComplete(msg, locationCategory.getSingularName(), locationCategory.getId());
            list[position] = data;
        } catch (Exception e) {
            list[position] = new ArrayList<>();
        }
    }

}
