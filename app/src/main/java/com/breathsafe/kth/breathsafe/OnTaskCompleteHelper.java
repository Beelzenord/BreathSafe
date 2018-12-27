package com.breathsafe.kth.breathsafe;


import android.app.Activity;

import com.breathsafe.kth.breathsafe.JsonParsers.AirJsonParser;
import com.breathsafe.kth.breathsafe.JsonParsers.LocationJsonParser;

import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import org.json.JSONException;

import java.util.List;

public class OnTaskCompleteHelper {
    public static List<AirPollution> onAirTaskComplete(Activity activity, String s) {
        try {
//            AirPollutionData airPollusionData = AirPollutionData.getInstance();
            List<AirPollution> list = AirJsonParser.parseAirLuftdaten(activity.getResources(), s);
//            airPollusionData.setList(list);
            System.out.println("list size AP " + list.size());
           // Intent intent = new Intent(activity, MapsActivity.class);
            return list;
          //  activity.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<LocationCategory> onLocationCategoryTaskComplete(Activity activity, String s) {
        try {
//            LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
            List<LocationCategory> list = LocationJsonParser.parseLocationCategoriesStockholmApi(activity.getResources(), s);
//            locationCategoryData.setList(list);
            System.out.println("list size Loc " + list.size());

            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

    public static List<Location> onSpecificLocationTaskComplete(Activity activity, String s) {
        try {
//            LocationData locationData = LocationData.getInstance();
            List<Location> list = LocationJsonParser.parseLocationStockholmApi(activity.getResources(), s);
//            locationData.setList(list);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Location> onSpecificLocationTaskComplete(String s, String categoryName) {
        try {
//            LocationData locationData = LocationData.getInstance();
            List<Location> list = LocationJsonParser.parseLocationStockholmApi(s, categoryName);
//            locationData.setList(list);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
