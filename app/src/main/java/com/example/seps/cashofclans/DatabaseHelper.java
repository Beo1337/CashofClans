package com.example.seps.cashofclans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * Diese Klasse wird benötigt um die Datenbank nach Installation der App zu erstelle und mit den vordefinierten Daten zu füllen.
 * Es werden Datenbankfunktionen angeboten, welche in der ganzen App verwendet werden können.
 *
 * */
public class DatabaseHelper extends SQLiteOpenHelper {

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
        Log.d("Databasehelper","onCreate!");
        db.execSQL("CREATE TABLE "+TABLE_NAME_MAIN+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, DATUM DATE, GPS TEXT, FOTO TEXT, FOREIGN KEY(KATEGORIE) REFERENCES category(ID))");
        db.execSQL("CREATE TABLE "+TABLE_NAME_CATEGORY+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT , ICON TEXT, CONSTRAINT name_unique UNIQUE (NAME))");

        //Vordefinierte Kategorien in die Datenbank speichern.
        addCategory("Lebensmittel",db);
        addCategory("Bar", db);
        addCategory("Sport", db);
        addCategory("Kleidung", db);
        addCategory("Bücher", db);
        addCategory("Kino", db);
        addCategory("Gehalt",db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Databasehelper","onUpgrade!");
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

    /**Diese Methode ändert den Namen einer Kategorie.*/
    public void changeCategoryName(String oldname, String newname, SQLiteDatabase db) {
        //TODO funktioniert noch nicht
        db.rawQuery("UPDATE "+TABLE_NAME_CATEGORY+" SET name = '"+newname+"' WHERE id = '"+oldname+"'",null);
        Log.i("DataBaseHelper","Kategoriename alt "+oldname);
        Log.i("DataBaseHelper","Kategoriename neu "+newname);
        Log.i("DataBaseHelper","Kategoriename geändert!!!");

        Cursor cursor = db.rawQuery("SELECT * FROM category", null);
        String entry = "";
        while(cursor.moveToNext())//Solange Einträge vorhanden sind, in den Ausgabestring speichern.
        {
            entry = entry +" "+ cursor.getInt(0);//ID
            entry = entry +" "+ cursor.getString(1);//Name
            entry = entry +" "+ cursor.getString(2);//Iconstring
            entry = entry +"\n";
        }
        Log.i("DataBaseHelper",entry);
    }

    /**Diese Methode ändert das Icon einer Kategorie.*/
    public void changeCategoryIcon(String name, String icon, SQLiteDatabase db) {
        //TODO funktioniert noch nicht
        db.rawQuery("UPDATE category SET icon = '"+name+"' WHERE name = '"+icon+"'",null);

    }

    /**Diese Methode löscht eine Kategorie aus der Datenbank.*/
    public void deleteCategory (String name, SQLiteDatabase db){
        //TODO funktioniert noch nicht
        db.rawQuery("DELETE FROM category WHERE name = '"+name,null);
    }
}
