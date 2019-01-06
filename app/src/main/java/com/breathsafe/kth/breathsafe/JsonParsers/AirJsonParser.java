package com.breathsafe.kth.breathsafe.JsonParsers;

import android.content.res.Resources;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Model.AirPollution;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses json documents from the luftdaten api.
 */
public class AirJsonParser {

    /**
     * Parses data retrieved from the luftdaten api. The data retrieved contains many different kind of sensors.
     * Only the sensor with data containing PM10 (P1) and PM2.5 (P2) is kept.
     * @param res The resource object used.
     * @param s The string containing the json object retrieved.
     * @return A List<AirPollution> containing only the desired information.
     * @throws JSONException If the data cannot be parsed.
     */
    public static List<AirPollution> parseAirLuftdaten(Resources res, String s) throws JSONException {
        JSONArray jsonArray = new JSONArray(s);
        List<AirPollution> list = new ArrayList<>();
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
            // Filter sensors with faulty values.
            if (p1 > 500 || p2 > 500)
                continue;
            double lat = location.getDouble("latitude");
            double lng = location.getDouble("longitude");
            String country = location.getString("country");
            int id = sensor.getInt("id");
            AirPollution airPollution = new AirPollution(id, name, country, lat, lng, p1, p2, System.currentTimeMillis());

            list.add(airPollution);
        }
        return list;
    }

}
