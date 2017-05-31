package com.example.seps.cashofclans;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.seps.cashofclans.Database.DatabaseHelper;

import java.util.Calendar;

/**
 * Created by Beo on 31.05.2017.
 */

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"Name des Intents: "+intent.getAction());
        //setAlarm(context);
        if(!isMyServiceRunning(DauerauftraegeService.class,context)) {
            Log.d(TAG,"Service gestartet!");
            Intent myIntent = new Intent(context, DauerauftraegeService.class);
            //context.startService(myIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /*public void setAlarm(Context context){
        Log.d(TAG,"Alarm gesetzt.");
        RegisterAlarmBroadcast(context);
        // Set the alarm to start at 8:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 7);
        calendar.set(Calendar.SECOND,0);
        Log.d(TAG,"Zeit: "+calendar.getTimeInMillis());

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, 1000*20, pendingIntent);
    }


    private void RegisterAlarmBroadcast(Context context) {
        Log.d(TAG,"BroadcastReceiver registriert.");
        mReceiver = new BroadcastReceiver() {
            // private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Log.d("mReceiver","Name des Intents:   "+intent.getAction());
                Log.d("mReceiver","ALARM Zeit zum Eintragen "+calendar.getTime());
                Toast.makeText(context, "Alarm time has been reached", Toast.LENGTH_LONG).show();

            }
        };

        context.getApplicationContext().registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
    }*/
}