package com.example.seps.cashofclans;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SON GOHAN on 13.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cash.db";
    public static final String TABLE_NAME_MAIN = "UEBERSICHT";
    /*public static final String COL_ID = "ID";
    public static final String COL_BETRAG = "BETRAG";
    public static final String COL_TITEL = "TITEL";
    public static final String COL_KATEGORIE = "KATEGORIE";
    public static final String COL_DATE = "DATUM";
    public static final String COL_GPS = "GPS";
    public static final String COL_FOTO = "FOTO";

    public static final String TABLE_CATEGORY = "KATEGORIE";
    public static final String CAT_ID = "ID";
    public static final String CAT_NAME ="NAME";
    */


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Typen der Spalten stimmen noch nicht
        db.execSQL("CREATE TABLE "+TABLE_NAME_MAIN+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG TEXT, TITEL TEXT, KATEGORIE TEXT, DATUM TEXT, GPS TEXT, FOTO TEXT)");
        //db.execSQL("CREATE TABLE "+TABLE_CATEGORY+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ICON TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MAIN);
        onCreate(db);
    }
}
