package com.breathsafe.kth.breathsafe.JsonParsers;

import android.content.res.Resources;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Model.AirPollusion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AirJsonParser {

    public static List<AirPollusion> parseAirLuftdaten(Resources res, String s) throws JSONException {
        JSONArray jsonArray = new JSONArray(s);
        List<AirPollusion> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject j1 = jsonArray.getJSONObject(i);
            JSONObject sensor = j1.getJSONObject("sensor");
            JSONObject sensor_type = sensor.getJSONObject("sensor_type");
            String name = sensor_type.getString("name");
            if (!name.equals("SDS011"))
                continue;
            Log.i("JsonParser", "Continue: " + name);
            JSONObject location = j1.getJSONObject("location");
            JSONArray sensordatavalues = j1.getJSONArray("sensordatavalues");
            double p1 = 0, p2 = 0;
            for (int j = 0; j < sensordatavalues.length(); j++) {
                JSONObject j2 = sensordatavalues.getJSONObject(j);
                if (j2.get("value_type").equals("P1"))
                    p1 = j2.getDouble("value");
                else if (j2.get("value_type").equals("P2"))
                    p2 = j2.getDouble("value");
            }
            if (p1 > 500 || p2 > 500)
                continue;
            double lat = location.getDouble("latitude");
            double lng = location.getDouble("longitude");
            String country = location.getString("country");
            int id = sensor.getInt("id");
            AirPollusion airPollusion = new AirPollusion(id, name, country, lat, lng, p1, p2, nowTimeInMillis());
            list.add(airPollusion);
        }
        return list;
    }

    private static long nowTimeInMillis() {
        Calendar c = Calendar.getInstance();
        return c.getTimeInMillis();
    }
}
