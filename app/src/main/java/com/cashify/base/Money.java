package com.cashify.base;

import android.content.SharedPreferences;
import android.util.Log;
import com.cashify.database.DatabaseHelper;
import com.cashify.overview.Entry;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


public class Money {
    public static double count(SharedPreferences sharedPref, DatabaseHelper dbHelper) {

        String time = sharedPref.getString("zeit","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double summe = 0;
        Log.d("MainFragment",time);

        for(Entry entry : dbHelper.getEntries()){
            summe += entry.getAmount();
        }
        return summe;
    }
}
