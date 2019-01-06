package com.breathsafe.kth.breathsafe.Utilities;


import android.app.Activity;

import com.breathsafe.kth.breathsafe.JsonParsers.AirJsonParser;
import com.breathsafe.kth.breathsafe.JsonParsers.LocationJsonParser;

import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import org.json.JSONException;

import java.util.List;

/**
 * Helper class to relieve code of MainActivity.
 */
public class OnTaskCompleteHelper {

    /**
     * Parse airpollution json data to a list of AirPollution.
     * @param activity The calling activity .
     * @param s The json documents.
     * @return A List<AirPollution>.
     */
    public static List<AirPollution> onAirTaskComplete(Activity activity, String s) {
        try {
            List<AirPollution> list = AirJsonParser.parseAirLuftdaten(activity.getResources(), s);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse location category json data to a list of LocationCategory.
     * @param activity The calling activity.
     * @param s The json document.
     * @return A List<LocationCategory>
     */
    public static List<LocationCategory> onLocationCategoryTaskComplete(Activity activity, String s) {
        try {
            List<LocationCategory> list = LocationJsonParser.parseLocationCategoriesStockholmApi(activity.getResources(), s);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null ;
        }
    }

    /**
     * Parses a json document with Location data.
     * @param s The json document.
     * @param categoryName The name of the LocationCategory.
     * @param categoryID The id of the LocationCategory.
     * @return A List<Location>
     */
    public static List<Location> onSpecificLocationTaskComplete(String s, String categoryName, String categoryID) {
        try {
            List<Location> list = LocationJsonParser.parseLocationStockholmApi(s, categoryName, categoryID);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
