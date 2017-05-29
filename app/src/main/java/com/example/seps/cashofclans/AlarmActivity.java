package com.example.seps.cashofclans;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmActivity extends Activity {
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
        RegisterAlarmBroadcast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void onClickSetAlarm(View v) {
        String shr = ethr.getText().toString();
        String smin = etmin.getText().toString();
        String ssec = etsec.getText().toString();

        if(shr.equals(""))
            hr = 0;
        else {
            hr = Integer.parseInt(ethr.getText().toString());
            hr=hr*60*60*1000;
        }

        if(smin.equals(""))
            min = 0;
        else {
            min = Integer.parseInt(etmin.getText().toString());
            min = min*60*1000;
        }

        if(ssec.equals(""))
            sec = 0;
        else {
            sec = Integer.parseInt(etsec.getText().toString());
            sec = sec * 1000;
        }
        result = hr+min+sec;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), result , pendingIntent);
    }

    private void RegisterAlarmBroadcast() {
        mReceiver = new BroadcastReceiver() {
            // private static final String TAG = "Alarm Example Receiver";
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Alarm time has been reached", Toast.LENGTH_LONG).show();
            }
        };

        registerReceiver(mReceiver, new IntentFilter("sample"));
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("sample"), 0);
        alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
    }

    private void UnregisterAlarmBroadcast() {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(mReceiver);
    }
}
