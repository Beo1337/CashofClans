package com.cashify.monthly_entries;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Dieser Service wird vom BootReceiver gestartet und startet den Timer für die Überprüfung der monatlichen Einträge.
 */
public class ReoccurringJobService extends Service {

    /**In der diser Klasse wird die tägliche Überprüfung der monatlichen Einträge durchgeführt.*/
    ReoccurringJob alarm = new ReoccurringJob();

    public void onCreate() {
        Log.d("Dauerauftragservice", "Service gestartet");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
