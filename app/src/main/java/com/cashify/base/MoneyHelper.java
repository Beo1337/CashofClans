package com.cashify.base;

import android.util.Log;
import com.cashify.overview.Entry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static android.content.ContentValues.TAG;


public class MoneyHelper {

    private static int YEAR = 1900;
    private static int MONTH = 0;
    private static int DAY = 0;
    private static boolean FILTER_SET = false;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static double count(Set<Entry> in) {
        double summe = 0;
        for (Entry entry : in) {
            summe += entry.getAmount();
        }
        return summe;
    }

    public static int getYear() {
        return YEAR;
    }

    public static int getMonth() {
        return MONTH;
    }

    public static int getDay() {
        return DAY;
    }

    public static boolean isFilterSet() {
        return FILTER_SET;
    }

    public static void unsetFilter(){
        FILTER_SET = false;
    }

    public static void setFilter() { FILTER_SET = true;}

    public static void setStartDate(int y, int m, int d) {
        FILTER_SET = true;
        YEAR = y;
        MONTH = m;
        DAY = d;
        Log.e(TAG, "setStartDate: " + YEAR + " " + MONTH + " " + DAY);
    }

    // Date is just plain evil and there is a very good reason why everything is deprecated!
    // This very, very hacky and shame on me for even considering leaving it this way.
    public static Set<Entry> filter(Set<Entry> in) {
        if (!FILTER_SET) return in;
        Set<Entry> out = new HashSet<>();
        Date baseDate = new Date(YEAR-1900, MONTH, DAY);
        try {
            for (Entry e : in) {
                Date entryDate = sdf.parse(e.getDatum());
                Log.e(TAG, "filter: " + baseDate + " " + entryDate);
                if (entryDate.after(baseDate)) {
                    out.add(e);
                }
            }
        } catch (ParseException ex) {
            return Collections.emptySet();
        }
        return out;
    }
}
