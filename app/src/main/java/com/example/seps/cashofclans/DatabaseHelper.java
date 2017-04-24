package com.example.seps.cashofclans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

/**
 *
 * Diese Klasse wird benötigt um die Datenbank nach Installation der App zu erstelle und mit den vordefinierten Daten zu füllen.
 * Es werden Datenbankfunktionen angeboten, welche in der ganzen App verwendet werden können.
 *
 * */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**Name der Klasse für Logcat*/
    private String TAG = this.getClass().getName();
    /**Name der Datenbank*/
    public static final String DATABASE_NAME = "cash.db";
    /**Name der Tabelle welche die Einträge speichert*/
    public static final String TABLE_NAME_MAIN = "uebersicht";
    /**Name der Tabelle welche die Kategorien speichert*/
    public static final String TABLE_NAME_CATEGORY = "category";
    /**Name der Tabelle welche die monatlich wiederholten Aufträge speichert.*/
    public static final String TABLE_NAME_REPEAT_ENTRY = "REPEAT_ENTRY";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate!");
        db.execSQL("CREATE TABLE "+TABLE_NAME_MAIN+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, DATUM DATE, GPS TEXT, FOTO TEXT, FOREIGN KEY(KATEGORIE) REFERENCES category(ID))");
        db.execSQL("CREATE TABLE "+TABLE_NAME_CATEGORY+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT , ICON TEXT, CONSTRAINT name_unique UNIQUE (NAME))");

        //Vordefinierte Kategorien in die Datenbank speichern.
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
        Log.d(TAG,"onUpgrade!");
        //Tabellen löschen und neu erstellen.
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MAIN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CATEGORY);
        onCreate(db);
    }

    /**Diese Methoden fügt eine neue Kategorie in die Kategorie-Tabelle ein.*/
    public void addCategory (String name, SQLiteDatabase db){

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);

        //In die Datenbank speichern
        long newRowId = db.insert("category", null, values);
        Log.i("DatabaseHelper", "Eingefügt " + newRowId);
    }

    /**Diese Methoden fügt eine neue Kategorie in die Kategorie-Tabelle ein. Optional ist es möglich String für ein Icon mitzugeben.*/
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
