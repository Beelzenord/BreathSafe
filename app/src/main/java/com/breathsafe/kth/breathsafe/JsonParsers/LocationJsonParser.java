package com.breathsafe.kth.breathsafe.JsonParsers;

import android.content.res.Resources;

import com.breathsafe.kth.breathsafe.CoordinatesConverterRT90.CoordinateHandler;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationJsonParser {

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
                    stringDateToLongDate(timeUpdated), coords[0], coords[1], nowTimeInMillis());
            list.add(location);
        }
        return list;
    }

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
                    stringDateToLongDate(timeUpdated), coords[0], coords[1], nowTimeInMillis());
            list.add(location);
        }
        return list;
    }

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
                    stringDateToLongDate(timeUpdated), categoryId, categoryName, nowTimeInMillis());
            list.add(lc);
        }
        return list;
    }

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

    private static long nowTimeInMillis() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }
}
