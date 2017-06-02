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
    /**Der Alarmmanger führt zu einer bestimmten Zeit eine Aktion durch.*/
    private AlarmManager alarmManager;
    /**Intent der an den Receivier geschickt wird, wenn die monatlichen Einträge gecheckt werden sollen. */
    private PendingIntent pendingIntent;
    /**Dieser Receiver hört auf die vom Alarmmanger ausgelöste Aktion.*/
    private BroadcastReceiver mReceiver;

    /**Diese Methode setzt die Zeit für die Überprüfung der monatlichen Einträge.*/
    public void setAlarm(Context context){
        Log.d(TAG,"Alarm gesetzt.");
        RegisterAlarmBroadcast(context);
        //Zeit auf 23:59 setzen
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND,0);

        Log.d(TAG,"Zeit zwischen gerade und Auslösen:"+getTimeFromMilli(calendar.getTimeInMillis()+180000-System.currentTimeMillis()));
        //Die Alarmzeit auf 23:59 + 3Minuten als 00:2 des nächsten Tages setzetn. Dieser Alarm wird dann täglich wiederholt.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+180000, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**Diese Methode rechnet Millisekunden in Stunden, Minuten und Sekunden um*/
    private String getTimeFromMilli(long millies){

        int stunde =(int) (millies / 3600000);
        int minute =(int) ((millies % 3600000) / 60000);
        int sekunde =(int) ((millies % 3600000) % 60000)/10000;

        return ""+stunde+":"+minute+":"+sekunde;
    }

    /**Diese Methode registriert einen Receiver beim Alarmmanger um nach dem Auslösen dessen, die monatlichen Einträge zu checken. */
    private void RegisterAlarmBroadcast(Context context) {
        Log.d(TAG,"BroadcastReceiver registriert.");
        mReceiver = new BroadcastReceiver() {


            //Wenn der Alarmmanger den PendingIntent auslöst wird die Überprüfung der monatlichen Einträge angestoßen.
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Log.d("mReceiver","Name des Intents:   "+intent.getAction());
                Log.d("mReceiver","ALARM Zeit zum Eintragen "+calendar.getTime());
                //Überprüfen der monatlichen Einträge.
                DatabaseHelper db = new DatabaseHelper(context);
                db.checkMonthlyEntries(context);
            }
        };

        context.getApplicationContext().registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager)(context.getSystemService(Context.ALARM_SERVICE));
    }
}
