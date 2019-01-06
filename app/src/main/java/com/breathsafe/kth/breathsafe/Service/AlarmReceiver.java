package com.breathsafe.kth.breathsafe.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.breathsafe.kth.breathsafe.R;

public class AlarmReceiver  extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
    public static final String TAG = "ALARM";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CheckForChanges.class);
        i.putExtra("foo", "bar");
        context.startService(i);
       /// ContextCompat.startForegroundService(this,i);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Intent repeatingIntent = new Intent(context)
    }
    private void generateNotification(Context context, String message) {






    }

}
