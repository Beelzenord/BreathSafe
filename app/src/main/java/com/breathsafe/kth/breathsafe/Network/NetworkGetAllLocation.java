package com.breathsafe.kth.breathsafe.Network;

import android.content.Context;
import android.os.AsyncTask;

import com.breathsafe.kth.breathsafe.Database.Repository;
import com.breathsafe.kth.breathsafe.JsonParsers.LocationJsonParser;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.OnTaskCompleteHelper;
import com.breathsafe.kth.breathsafe.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NetworkGetAllLocation extends AsyncTask<Void,Void,List<Location>> {
    private Context context;
    private HttpURLConnection httpURLConnection;
    public NetworkGetAllLocation(Context context) {
        this.context = context;
    }

    @Override
    protected List<Location> doInBackground(Void... voids) {
        String urlString = this.context.getResources().getString(R.string.api_stockholm_all_locations) + this.context.getResources().getString(R.string.stockholm_api_key);
        URL url = null;
        try {
            url = new URL(urlString);
            openConnection(url);

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            StringBuffer buffer = new StringBuffer();
            String line = "";


            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                System.out.println("Response: "+ "> " + line);   //here u ll get whole response...... :-)
            }

            List<Location> locationList = LocationJsonParser.parseLocationStockholmApi(buffer.toString());
            System.out.println("location 0 " + locationList.get(0).toString());
            Repository repository = Repository.getInstance(this.context);


            System.out.println("size of the list " + locationList.size() + " " + repository.locationDoa().countNumberOfEntities());


            return repository.locationDoa().getAllLocations();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Location> locations) {
        super.onPostExecute(locations);
    }

    private void openConnection(URL url) throws IOException {
        this.httpURLConnection = (HttpURLConnection) url.openConnection();
        this.httpURLConnection.setReadTimeout(10000 /* milliseconds */);
        this.httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
        this.httpURLConnection.setRequestMethod("GET");
        this.httpURLConnection.setDoInput(true);
        this.httpURLConnection.connect();

    }
}
