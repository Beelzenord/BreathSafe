package com.breathsafe.kth.breathsafe.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.breathsafe.kth.breathsafe.AsyncTaskCallback;
import com.breathsafe.kth.breathsafe.Database.RetrieveFavorites;
import com.breathsafe.kth.breathsafe.IO.Network.NetworkTask;
import com.breathsafe.kth.breathsafe.MainActivity;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.R;
import com.breathsafe.kth.breathsafe.Utilities.CoordinatesConverterRT90.CalculateAirPollutionData;
import com.breathsafe.kth.breathsafe.Utilities.OnTaskCompleteHelper;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckForChanges extends Service implements AsyncTaskCallback{
    private final String TAG = "CheckForChanges";
    private static final String CHANNEL_1_ID = "channel1";
    private static final int NETWORK_AIR = 5;
    private NotificationManagerCompat notificationManager;
  /*  public CheckForChanges() {
        super("CheckForChange");
    }

    public CheckForChanges(String name) {
        super(name);
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   return super.onStartCommand(intent, flags, startId);
        Log.i(TAG,"on start command");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setContentTitle("Example Service")
                .setContentText("Text")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    // @Override
   // protected void onHandleIntent(@Nullable Intent intent) {
    //    Log.i(SERVICE,"Another direction");
    //    createNotificationChannels();
       /* try {
            List<Location> list = new RetrieveFavorites(getApplicationContext()).execute().get();
            Log.i(SERVICE,"Another direction " + list.size());
          //  startNetworkTask();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    private void createNotificationChannels() {
    //    mNotificationManager = (NotificationManager)
    //            this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

            notificationManager = NotificationManagerCompat.from(getApplicationContext());
            Notification notification = new NotificationCompat.Builder(this,CHANNEL_1_ID).setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("title")
                    .setContentText("Text")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .build();
            // startForeground(1,notification);
            notificationManager.notify(1,notification);

        }


    }

    private void startNetworkTask() {
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.api_luftdaten_lat_lat_area));
        sb.append(getResources().getString(R.string.api_luftdaten_default_lat));
        sb.append(",");
        sb.append(getResources().getString(R.string.api_luftdaten_default_lng));
        sb.append(",");
        sb.append(getResources().getString(R.string.api_luftdaten_default_radius));
        NetworkTask networkTask = new NetworkTask(this, sb.toString(), NETWORK_AIR);
        networkTask.execute();
    }
    @Override
    public void onDownloadComplete(NetworkTask.Result result) {

        Log.i(TAG,"download complete");
        try {

            List<AirPollution> listAirPollution = OnTaskCompleteHelper.onAirTaskComplete((String)result.msg);
            Log.i(TAG, "onDownloadComplete: size: " + listAirPollution.size());
            List<Location> list = new RetrieveFavorites(getApplicationContext()).execute().get();
            for (Location l : list) {
                double res = CalculateAirPollutionData.weightedPM1andPM2(listAirPollution,l);
                Log.i(TAG, "onDownloadComplete: averageAQI: " + res);
                Log.i(TAG, "list data " + l.getName() +  " " + l.getCategoryNames().get(0).toString());
                l.setAverageAQI(res);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         *  if (result.msg != null) {
         List<AirPollution> list = OnTaskCompleteHelper.onAirTaskComplete(this, (String)result.msg);
         Log.i(TAG, "onDownloadComplete: size: " + list.size());

         for (Location l : DisplayOnMapList.getInstance().getList()) {
         double res = CalculateAirPollutionData.weightedPM1andPM2(list, l);
         Log.i(TAG, "onDownloadComplete: averageAQI: " + res);
         Log.i(TAG, "list data " + l.getName() +  " " + l.getCategoryNames().get(0).toString());
         l.setAverageAQI(res);
         }
         if(DisplayOnMapList.getInstance().getList()!=null){
         storeAirPollutionDataIntoDatabase(DisplayOnMapList.getInstance().getList().get(0));
         }

         fragment.refreshMap();
         AirPollutionData.getInstance().setList(list);
         StoreToDatabase.DeleteAllAndStoreAirPollution storeAirPollution = new StoreToDatabase.DeleteAllAndStoreAirPollution(this, list);
         storeAirPollution.execute();
         }
         else {
         Log.i(TAG, "onDownloadComplete: luftdaten exception");
         result.ex.printStackTrace();
         }
         */

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
