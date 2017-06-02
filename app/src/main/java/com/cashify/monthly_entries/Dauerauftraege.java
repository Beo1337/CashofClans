package com.cashify.monthly_entries;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.cashify.database.DatabaseHelper;

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

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND,0);
        Log.d(TAG,"Zeit zwischen gerade und Auslösen:"+getTimeFromMilli(calendar.getTimeInMillis()+180000-System.currentTimeMillis()));
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+180000, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**Diese Methode rechnet Millisekunden in Stunden, Minuten und Sekunden um*/
    private String getTimeFromMilli(long millies){

        int stunde =(int) (millies / 3600000);
        int minute =(int) ((millies % 3600000) / 60000);
        int sekunde =(int) ((millies % 3600000) % 60000)/10000;

        return ""+stunde+":"+minute+":"+sekunde;
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

            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Log.d("mReceiver","Name des Intents:   "+intent.getAction());
                Log.d("mReceiver","ALARM Zeit zum Eintragen "+calendar.getTime());
                DatabaseHelper db = new DatabaseHelper(context);
                db.checkMonthlyEntries(context);
            }
        };

        context.getApplicationContext().registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
    }
}
