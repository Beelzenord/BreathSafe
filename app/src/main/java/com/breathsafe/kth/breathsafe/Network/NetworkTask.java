package com.breathsafe.kth.breathsafe.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.breathsafe.kth.breathsafe.Exceptions.CancelTaskException;
import com.breathsafe.kth.breathsafe.JsonParsers.AirJsonParser;
import com.breathsafe.kth.breathsafe.MainActivity;


import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Asynchronous task to collect Locations from a specific string. Returns CancelTaskException
 * if the task i canceled.
 */
public class NetworkTask extends AsyncTask<String, Void, NetworkTask.Result> {
    private static final String logStr = "NetworkAirTask";
    private MainActivity callbackActivity;
    private String urlString;
    private int tag;

    /**
     * Asynchronous task to collect Locations from a specific string.
     * @param callbackActivity The activity to send the result to.
     * @param urlString The urlString to use.
     * @param tag The tag of the task.
     */
    public NetworkTask(MainActivity callbackActivity, String urlString, int tag) {
        this.callbackActivity = callbackActivity;
        this.urlString = urlString;
        this.tag = tag;
    }

    /**
     * When the task is done call onLocationDownloadComplete.
     * @param result The result of the download.
     */
    @Override
    protected void onPostExecute(NetworkTask.Result result) {
        callbackActivity.onDownloadComplete(result);
    }

    /**
     * Downloads information about a specific location. Throws CancelTaskException
     * if the task is canceled.
     * @param urlStrings The url string to use.
     * @return The result of the download.
     */
    @Override
    protected NetworkTask.Result doInBackground(String... urlStrings) {
        NetworkTask.Result result = null;
        InputStream in = null;
        HttpURLConnection http = null;
        BufferedReader br = null;

        StringBuilder urlStr = new StringBuilder();
        URL url = null;
        try {
            urlStr.append(urlString);
            if (isCancelled())
                throw new CancelTaskException();

            Log.i(logStr, urlStr.toString());
            url = new URL(urlStr.toString());
            System.out.println("CALLING LUFTDATEN API");
            http = (HttpURLConnection) url.openConnection();
            if (isCancelled())
                throw new CancelTaskException();

            in = http.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            if (isCancelled())
                throw new CancelTaskException();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line);
            if (isCancelled())
                throw new CancelTaskException();

            String msg = sb.toString();
          //  List<AirPollusion> parsedJsonObject = AirJsonParser.parseAirLuftdaten(callbackActivity.getResources(), sb.toString());
            if (msg != null)
                result = new NetworkTask.Result(msg, tag);
            else
                result = new NetworkTask.Result(new JSONException("Could not parse SMHI data"), tag);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result = new NetworkTask.Result(e, tag);
        } catch (IOException e) {
            e.printStackTrace();
            result = new NetworkTask.Result(e, tag);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            result = new NetworkTask.Result(e, tag);
        } catch (CancelTaskException e) {
            result = new NetworkTask.Result(e, tag);
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (in != null)
                    in.close();
                if (http != null)
                    http.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                return new NetworkTask.Result(e, tag);
            }
        }
        return result;
    }

    public static class Result {
        public Object msg;
        public Exception ex;
        public int tag;
        public Result(Object msg, int tag) {
            this.msg = msg;
            this.tag = tag;
            ex = null;
        }
        public Result(Exception ex, int tag) {
            this.ex = ex;
            this.tag = tag;
            msg = null;
        }
    }

}
