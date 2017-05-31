package com.example.seps.cashofclans.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cashify.category.Category;
import com.example.seps.cashofclans.Entry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * Diese Klasse wird benötigt um die Datenbank nach Installation der App zu erstelle und mit den vordefinierten Daten zu füllen.
 * Es werden Datenbankfunktionen angeboten, welche in der ganzen App verwendet werden können.
 *
 * */

public class DatabaseHelper extends SQLiteOpenHelper {

    /**Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.*/
    private static final String TAG = "DatabaseHelper";
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
        db.execSQL("CREATE TABLE "+TABLE_NAME_MAIN+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, DATUM DATE, FOTO TEXT, FOREIGN KEY(KATEGORIE) REFERENCES category(ID) ON DELETE RESTRICT)");
        db.execSQL("CREATE TABLE "+TABLE_NAME_CATEGORY+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT , ICON TEXT, CONSTRAINT name_unique UNIQUE (NAME))");

        //Vordefinierte Kategorien in die Datenbank speichern.
        addCat("Lebensmittel",db);
        addCat("Bar",db);
        addCat("Sport",db);
        addCat("Kleidung",db);
        addCat("Bücher",db);
        addCat("Kino",db);
        addCat("Gehalt",db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"onUpgrade!");
        //Tabellen löschen und neu erstellen.
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_MAIN);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_CATEGORY);
        onCreate(db);
    }


    /**Diese Methode fügt eine neue Kategorie in die Kategorie-Tabelle ein.*/
    private boolean addCat (String name,SQLiteDatabase db){
        long newRowId = 0;

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);

        //In die Datenbank speichern
        try {
            newRowId = db.insert(TABLE_NAME_CATEGORY, null, values);
        } catch(SQLiteException e){
            Log.i(TAG, "Fehler beim Einfügen der Kategorie" + e.getMessage());
            return false;
        }


        if(newRowId > 0) {
            Log.i(TAG, "Eingefügt " + newRowId);
            return true;
        } else {
            Log.i(TAG, "Eingefügt NOPE " + newRowId);
            return false;
        }
    }


    /**Diese Methode fügt eine neue Kategorie in die Kategorie-Tabelle ein.*/
    public boolean addCategory (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = 0;

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);

        //In die Datenbank speichern
        try {
            newRowId = db.insert(TABLE_NAME_CATEGORY, null, values);
        } catch(SQLiteException e){
            Log.i(TAG, "Fehler beim Einfügen der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if(newRowId > 0) {
            Log.i(TAG, "Eingefügt " + newRowId);
            return true;
        } else {
            Log.i(TAG, "Eingefügt NOPE " + newRowId);
            return false;
        }
    }

    /**Diese Methode fügt eine neue Kategorie in die Kategorie-Tabelle ein. Optional ist es möglich String für ein Icon mitzugeben.*/
    public boolean addCategory (String name, String icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        long newRowId;
        values.put("NAME", name);
        values.put("ICON",icon);

        //In die Datenbank speichern
        try {
            newRowId = db.insert(TABLE_NAME_CATEGORY, null, values);
            Log.i(TAG, "Eingefügt " + newRowId);
        }catch(SQLiteException e){
            Log.i(TAG, "Fehler beim Einfügen der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if(newRowId > 0)
            return true;
        else
            return false;
    }

    /**Diese Methode ändert den Namen einer Kategorie.*/
    public boolean changeCategoryName(int id, String newname) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Wert für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        long rowId;
        values.put("NAME", newname);

        //In die Datenbank speichern
        try {
            rowId = db.update(TABLE_NAME_CATEGORY,values,"ID = "+id,null);
            Log.i(TAG,"Kategoriename geändert!!!");
        }catch(SQLiteException e){
            Log.i(TAG, "Fehler beim Ändern des Namens der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if(rowId > 0)
            return true;
        else
            return false;
    }

    /**Diese Methode ändert das Icon einer Kategorie.*/
    public boolean changeCategoryIcon(int id, String icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Wert für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        long rowId;
        values.put("ICON", icon);

        //In die Datenbank speichern
        try {
            rowId = db.update(TABLE_NAME_CATEGORY,values,"ID = "+id,null);
            Log.i(TAG,"Kategoriename geändert!!!");
        }catch(SQLiteException e){
            Log.i(TAG, "Fehler beim Ändern des Icons der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if(rowId > 0)
            return true;
        else
            return false;
    }

    /**Diese Methode löscht eine Kategorie aus der Datenbank.*/
    public boolean deleteCategory (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        int del;
        try {

            del = db.delete(TABLE_NAME_CATEGORY,"name LIKE '"+name+"' AND NOT EXISTS (SELECT KATEGORIE FROM "+TABLE_NAME_MAIN+" WHERE KATEGORIE = (SELECT ID FROM "+TABLE_NAME_CATEGORY+" WHERE name LIKE '"+name+"'))",null);
            Log.i("DataBaseHelper","Kategorie gelöscht!!!  "+ del);
        }
        catch(SQLiteException e){
            Log.i(TAG, "Fehler beim Löschen einer Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();
        if(del > 0)
            return true;
        else
            return false;
    }

    /**Diese Methode liefert alle Kategorien als HashSet zurück.*/
    public Set<Category> getCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        Set<Category> allCategories = new HashSet<Category>();

        Cursor cursor = db.rawQuery("SELECT * FROM category", null);
        while(cursor.moveToNext())
        {
            Category c = new Category(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            allCategories.add(c);
        }
        Log.i(TAG,"The list is:");
        Iterator<Category> i = allCategories.iterator();
        while(i.hasNext())
            Log.i(TAG,i.next().toString());
        cursor.close();
        db.close();
        return allCategories;
    }

    /**Diese Methode liefert alle Entries als HashSet zurück*/
    public Set<Entry> getEntries(){
        SQLiteDatabase db = this.getReadableDatabase();
        Set<Entry> allEntries = new HashSet<Entry>();

        Cursor cursor = db.rawQuery("SELECT u.ID,u.BETRAG,u.TITEL,u.DATUM,U.FOTO,c.NAME FROM "+TABLE_NAME_MAIN+" u JOIN "+TABLE_NAME_CATEGORY+" c ON u.KATEGORIE = c.ID", null);
        while(cursor.moveToNext())
        {
            //Log.d("DatabaseHelper"," "+cursor.getInt(0));
            Entry e = new Entry(cursor.getInt(0),cursor.getDouble(1),cursor.getString(2),cursor.getString(5),cursor.getString(3),cursor.getString(4));
            allEntries.add(e);
        }
        Log.i(TAG,"The list is:");
        Iterator<Entry> i = allEntries.iterator();
        while(i.hasNext())
            Log.i(TAG,i.next().toString());
        cursor.close();

        return allEntries;
    }

    /**Diese Methode löscht den Eintrag mit der übergegenen ID aus der Datenbank.*/
    public boolean removeEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(TABLE_NAME_MAIN,"ID ="+id,null);
        db.close();
        Log.i(TAG,"Eintrag gelöscht!!!");
        if(del > 0)
            return true;
        else
            return false;
    }
}