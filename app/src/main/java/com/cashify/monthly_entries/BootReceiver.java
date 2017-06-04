package com.cashify.monthly_entries;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Diese Klasse wird benötigt um einen Service zu starten, welcher ein mal am Tag überprüft, ob monatliche Einträge zu buchen sind.
 * Der Service wird gestartet sobald der Intent USER_PRESENT vom System geschickt wird. (Entsperren des Bildschirms)
 */
public class BootReceiver extends BroadcastReceiver {
    /**
     * Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.
     */
    private static final String TAG = "BootReceiver";

    //Diese Methode wird aufgerufen wenn vom System der Intent USER_PRESENT empfangen wird.
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "Name des Intents: " + intent.getAction());
        //Wenn unser Service nicht schon gestartet ist.
        if (!isMyServiceRunning(DauerauftraegeService.class, context)) {
            //Den unseren Service für die monatlichen Einträge starten.
            Log.d(TAG, "Service gestartet!");
            Intent myIntent = new Intent(context, DauerauftraegeService.class);
            context.startService(myIntent);
        }
    }

    /**
     * Diese Methode schaut nach ob der Übergebene Service bereits gestart ist.
     */
    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}