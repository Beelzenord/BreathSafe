package com.breathsafe.kth.breathsafe;


import android.content.Intent;

import com.breathsafe.kth.breathsafe.JsonParsers.AirJsonParser;
import com.breathsafe.kth.breathsafe.JsonParsers.LocationJsonParser;

import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.AirPollutionData;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.Model.LocationCategoryData;

import org.json.JSONException;

import java.util.List;

public class OnTaskCompleteHelper {
    public static void onAirTaskComplete(MainActivity activity, String s) {
        try {
            AirPollutionData airPollusionData = AirPollutionData.getInstance();
            List<AirPollution> list = AirJsonParser.parseAirLuftdaten(activity.getResources(), s);
            airPollusionData.setList(list);
            System.out.println("list size AP " + list.size());
           // Intent intent = new Intent(activity, MapsActivity.class);
          //  activity.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean onLocationCategoryTaskComplete(MainActivity activity, String s) {
        try {
            LocationCategoryData locationCategoryData = LocationCategoryData.getInstance();
            List<LocationCategory> list = LocationJsonParser.parseLocationCategoriesStockholmApi(activity.getResources(), s);
            locationCategoryData.setList(list);
            System.out.println("list size Loc " + list.size());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
