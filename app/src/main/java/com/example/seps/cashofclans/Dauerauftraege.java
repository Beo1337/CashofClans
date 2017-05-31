package com.example.seps.cashofclans;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.seps.cashofclans.Database.DatabaseHelper;

import java.util.Calendar;

/**
 * Created by Beo on 29.05.2017.
 */

public class Dauerauftraege{

    private static final String TAG = "Dauerauftraege";
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;

    public void setAlarm(Context context){
        Log.d(TAG,"Alarm gesetzt.");
        RegisterAlarmBroadcast(context);
        // Set the alarm to start at 8:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 7);
        calendar.set(Calendar.SECOND,0);
        Log.d(TAG,"Zeit: "+calendar.getTimeInMillis());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, 1000*20, pendingIntent);
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Dauerauftraege.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
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
                DatabaseHelper db = new DatabaseHelper(context);


            }
        };

        context.getApplicationContext().registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
    }
}
