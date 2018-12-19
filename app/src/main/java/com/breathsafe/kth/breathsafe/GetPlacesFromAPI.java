package com.breathsafe.kth.breathsafe;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GetPlacesFromAPI extends AsyncTask<Void,Void,Void> {
    private Context context;

    public GetPlacesFromAPI(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String json = null;
        try {
            InputStream is =  context.getAssets().open("test.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            //JSONObject mainObject = new JSONObject(json);
            JSONArray jsonArray = new JSONArray(json);
            //JSONArray parameterArray = obj.getJSONArray("parameters");


            Log.i("JSON", "JSON STREAM SIZE " + json.length() + " " + jsonArray.length());

            //||\\
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject tmpJsonObject = (JSONObject) jsonArray.get(0);

            }

          //  System.out.println("OBTAINED " +json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
