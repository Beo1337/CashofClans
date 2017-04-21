package com.example.seps.cashofclans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SON GOHAN on 13.04.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cash.db";
    public static final String TABLE_NAME_MAIN = "uebersicht";
    public static final String TABLE_NAME_CATEGORY = "category";
    public static final String TABLE_NAME_REPEAT_ENTRY = "REPEAT_ENTRY";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("DatabaseHelper","onCreate!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        db.execSQL("CREATE TABLE "+TABLE_NAME_MAIN+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, DATUM DATE, GPS TEXT, FOTO TEXT, FOREIGN KEY(KATEGORIE) REFERENCES category(ID))");
        db.execSQL("CREATE TABLE "+TABLE_NAME_CATEGORY+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT , ICON TEXT, CONSTRAINT name_unique UNIQUE (NAME))");
        addCategory("Lebensmittel", db);
        addCategory("Bar", db);
        addCategory("Sport", db);
        addCategory("Kleidung", db);
        addCategory("Bücher", db);
        addCategory("Kino", db);
        addCategory("Gehalt",db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DatabaseHelper","onUpgrade!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MAIN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CATEGORY);
        onCreate(db);

    }

    public void addCategory (String name, SQLiteDatabase db){

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);

        //In die Datenbank speichern
        long newRowId = db.insert("category", null, values);
        Log.i("DatabaseHelper", "Eingefügt " + newRowId);
    }

    public void addCategory (String name, String icon, SQLiteDatabase db) {

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("ICON",icon);

        //In die Datenbank speichern
        long newRowId = db.insert("category", null, values);
        Log.i("DatabaseHelper", "Eingefügt " + newRowId);


    }

}
