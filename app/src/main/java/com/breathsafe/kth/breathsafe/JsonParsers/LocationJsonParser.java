package com.breathsafe.kth.breathsafe.JsonParsers;

import android.content.res.Resources;

import com.breathsafe.kth.breathsafe.Utilities.CoordinatesConverterRT90.CoordinateHandler;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses data from Stockholm open API.
 */
public class LocationJsonParser {

    /**
     * Parses a list of Locations from the Stockholm open API to a list of locations.
     * The coordinates are of types RT90 which are converted to longitude and latitude using the
     * CoordinateHandler.
     * @param res The resource object used.
     * @param s The string containing the json object retrieved.
     * @return A List<Location> containing only the desired information.
     * @throws JSONException If the data cannot be parsed.
     */
    public static List<Location> parseLocationStockholmApi(Resources res, String s) throws JSONException {
        CoordinateHandler ch = new CoordinateHandler();
        JSONArray jsonArray = new JSONArray(s);
        List<Location> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject j1 = jsonArray.getJSONObject(i);
            String timeCreated = j1.getString("TimeCreated");
            String timeUpdated = j1.getString("TimeUpdated");
            String id = j1.getString("Id");
            String name = j1.getString("Name");
            JSONObject coordiates = j1.getJSONObject("GeographicalPosition");
            int x = coordiates.getInt("X");
            int y = coordiates.getInt("Y");
            double[] coords = ch.gridToGeodetic(x, y);

            Location location = new Location(id, new ArrayList(), new ArrayList(), name, stringDateToLongDate(timeCreated),
                    stringDateToLongDate(timeUpdated), coords[0], coords[1], System.currentTimeMillis());
            list.add(location);
        }
        return list;
    }

    /**
     * Parses a list of Locations from the Stockholm open API to a list of locations.
     * The coordinates are of types RT90 which are converted to longitude and latitude using the
     * CoordinateHandler.
     * @param s The string containing the json object retrieved.
     * @param categoryName The name of the category which held this Location.
     * @param categoryID The id of the category which held this location.
     * @return A List<Location> containing only the desired information.
     * @throws JSONException If the data cannot be parsed.
     */
    public static List<Location> parseLocationStockholmApi(String s, String categoryName, String categoryID) throws JSONException {
        CoordinateHandler ch = new CoordinateHandler();
        JSONArray jsonArray = new JSONArray(s);
        List<Location> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject j1 = jsonArray.getJSONObject(i);
            String timeCreated = j1.getString("TimeCreated");
            String timeUpdated = j1.getString("TimeUpdated");
            String id = j1.getString("Id");
            String name = j1.getString("Name");
            JSONObject coordiates = j1.getJSONObject("GeographicalPosition");
            int x = coordiates.getInt("X");
            int y = coordiates.getInt("Y");
            double[] coords = ch.gridToGeodetic(x, y);
            List<String> category = new ArrayList<>();
            category.add(categoryName);
            List<String> categoryIDlist = new ArrayList<>();
            categoryIDlist.add(categoryID);
            Location location = new Location(id, category, categoryIDlist, name, stringDateToLongDate(timeCreated),
                    stringDateToLongDate(timeUpdated), coords[0], coords[1], System.currentTimeMillis());
            list.add(location);
        }
        return list;
    }

    /**
     * Parses a list of Location categories from the Stockholm open API to a list of LocationCategories.
     * @param res The resource object used.
     * @param s The string containing the json object retrieved.
     * @return A List<Location> containing only the desired information.
     * @throws JSONException If the data cannot be parsed.
     */
    public static List<LocationCategory> parseLocationCategoriesStockholmApi(Resources res, String s) throws JSONException {
        CoordinateHandler ch = new CoordinateHandler();
        JSONArray jsonArray = new JSONArray(s);
        List<LocationCategory> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject j1 = jsonArray.getJSONObject(i);
            String timeCreated = j1.getString("TimeCreated");
            String timeUpdated = j1.getString("TimeUpdated");
            String id = j1.getString("Id");
            String singularName = j1.getString("SingularName");
            JSONObject categoryInfo = j1.getJSONObject("ServiceUnitTypeGroupInfo");
            int categoryId = categoryInfo.getInt("Id");
            String categoryName = categoryInfo.getString("Name");

            LocationCategory lc = new LocationCategory(singularName, id, stringDateToLongDate(timeCreated),
                    stringDateToLongDate(timeUpdated), categoryId, categoryName, System.currentTimeMillis());
            list.add(lc);
        }
        return list;
    }

    /**
     * Converts a date on a string to a long representation of the date
     * in milliseconds since 1970-01-01.
     * @param date The date to be converted.
     * @return The date in type milliseconds since 1970-01-01.
     */
    private static long stringDateToLongDate(String date) {
        try {
            String[] s1 = date.split("\\(");
            String[] s2 = s1[1].split("\\+");
            String s = s2[0];
            long l = Long.parseLong(s);
            return l;
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
