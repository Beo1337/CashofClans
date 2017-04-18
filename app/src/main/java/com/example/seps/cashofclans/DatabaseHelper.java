package com.example.seps.cashofclans;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SON GOHAN on 13.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


        // Database Version
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "COC_DB";
        // Contacts table name
        private static final String TABLE_SHOPS = "UEBERSICHT";
        // Shops Table Columns names
        private static final String KEY_ID = "ID";
        private static final String KEY_NAME = "name";
        private static final String KEY_BETRAG = "betrag";


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SHOPS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_BETRAG + "NUMBER" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
// Creating tables again
            onCreate(db);
        }
    }