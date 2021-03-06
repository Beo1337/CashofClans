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
 * Diese Klasse startet die tägliche Überprüfung der monatlichen Einträge welche immer um 00:02 durchgeführt wird.
 * Der Broadcastreceiver führt nach Empfangen des Intents die Methode zum Überprüfen der Einträge aus.
 */
public class ReoccurringJob {

    /**Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.*/
    private static final String TAG = "ReoccurringJob";
    /**Der Alarmmanger führt zu einer bestimmten Zeit eine Aktion durch.*/
    private AlarmManager alarmManager;
    /**Intent der an den Receivier geschickt wird, wenn die monatlichen Einträge gecheckt werden sollen.*/
    private PendingIntent pendingIntent;
    /**Dieser Receiver hört auf die vom Alarmmanger ausgelöste Aktion.*/
    private BroadcastReceiver receiver;

    /**Diese Methode setzt die Zeit für die Überprüfung der monatlichen Einträge.*/
    public void setAlarm(Context context) {
        Log.d(TAG, "Alarm gesetzt.");
        RegisterAlarmBroadcast(context);
        //Zeit auf 23:59 setzen
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);

        Log.d(TAG, "Zeit zwischen gerade und Auslösen:" + getTimeFromMilli(calendar.getTimeInMillis() + 180000 - System.currentTimeMillis()));
        //Die Alarmzeit auf 23:59 + 3Minuten als 00:2 des nächsten Tages setzetn. Dieser Alarm wird dann täglich wiederholt.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 180000, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**Diese Methode rechnet Millisekunden in Stunden, Minuten und Sekunden um*/
    private String getTimeFromMilli(long millies) {

        int stunde = (int) (millies / 3600000);
        int minute = (int) ((millies % 3600000) / 60000);
        int sekunde = (int) ((millies % 3600000) % 60000) / 10000;

        return "" + stunde + ":" + minute + ":" + sekunde;
    }

    /**Diese Methode registriert einen Receiver beim Alarmmanger um nach dem Auslösen dessen, die monatlichen Einträge zu checken.*/
    private void RegisterAlarmBroadcast(Context context) {
        Log.d(TAG, "BroadcastReceiver registriert.");
        receiver = new BroadcastReceiver() {


            //Wenn der Alarmmanger den PendingIntent auslöst wird die Überprüfung der monatlichen Einträge angestoßen.
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Log.d("receiver", "Name des Intents:   " + intent.getAction());
                Log.d("receiver", "ALARM Zeit zum Eintragen " + calendar.getTime());
                //Überprüfen der monatlichen Einträge.
                DatabaseHelper db = new DatabaseHelper(context);
                db.checkMonthlyEntries(context);
            }
        };

        context.getApplicationContext().registerReceiver(receiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
    }
}
