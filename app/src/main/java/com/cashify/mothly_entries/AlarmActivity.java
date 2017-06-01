package com.cashify.mothly_entries;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.cashify.R;

import java.util.Calendar;

public class AlarmActivity extends Activity {

    private static final String TAG = "AlarmActivity";
    int hr = 0;
    int min = 0;
    int sec = 0;
    int result = 1;

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    BroadcastReceiver mReceiver;


    EditText ethr;
    EditText etmin;
    EditText etsec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ethr = (EditText) findViewById(R.id.ethr);
        etmin = (EditText) findViewById(R.id.etmin);
        etsec = (EditText) findViewById(R.id.etsec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void onClickSetAlarm(View v) {


        Log.d(TAG,"Alarm gesetzt.");
        RegisterAlarmBroadcast();
        // Set the alarm to start at 8:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND,0);
        Log.d(TAG,"Zeit: "+calendar.getTimeInMillis());

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*20, pendingIntent);

    }

    private void RegisterAlarmBroadcast() {
        Log.d(TAG,"BroadcastReceiver registriert.");
        mReceiver = new BroadcastReceiver() {
            // private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                Log.d(TAG,"ALARM Zeit zum Eintragen "+calendar.getTime());
                Toast.makeText(context, "Alarm time has been reached", Toast.LENGTH_LONG).show();
            }
        };

        LocalBroadcastManager.getInstance(AlarmActivity.this).registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
    }

    private void UnregisterAlarmBroadcast() {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(mReceiver);
    }
}
