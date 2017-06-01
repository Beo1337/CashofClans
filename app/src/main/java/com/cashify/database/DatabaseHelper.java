package com.cashify.database;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cashify.category.Category;
import com.cashify.MainActivity;
import com.cashify.monthly_entries.MonthlyEntry;
import com.cashify.overview.Entry;
import com.cashify.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.content.Context.NOTIFICATION_SERVICE;

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
        db.execSQL("CREATE TABLE "+TABLE_NAME_REPEAT_ENTRY+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, TAG INTEGER, FOREIGN KEY(KATEGORIE) REFERENCES category(ID) ON DELETE RESTRICT)");

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
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_REPEAT_ENTRY);
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

    public boolean addEntry(double betrag, String title, String foto, String kategorie, String date){
        SQLiteDatabase db;
        SQLiteDatabase dbr = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("BETRAG", betrag);
        values.put("TITEL",title);
        values.put("FOTO",foto);

        Cursor c = dbr.rawQuery("SELECT ID FROM category WHERE NAME = '"+kategorie+"'", null);
        c.moveToNext();
        values.put("KATEGORIE",c.getInt(0));
        c.close();
        dbr.close();
        values.put("DATUM", date);
        db = this.getWritableDatabase();
        //In die Datenbank speichern
        long newRowId = db.insert("uebersicht", null, values);
        Log.i(TAG, "Eingefügt " + newRowId);

        db.close();

        if(newRowId > 0)
            return true;
        else
            return false;
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

    /**Diese Methode checkt ob Einträge aus den monatlichen Einträgen heute eingetragen werden müssen.*/
    public void checkMonthlyEntries(Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate;
        String entries = "";
        int aktuellerTag;
        int count = 0;



        aktuellerTag =Integer.valueOf(sdf.format(new Date()));
        Log.d(TAG,"aktueller Tag: "+aktuellerTag);

        List<MonthlyEntry> entryList = new LinkedList<>();
        entryList.addAll(getMonthlyEntries());

        Iterator<MonthlyEntry> i = entryList.iterator();
        Log.d(TAG,"Anzahl monatlicher Einträge: "+entryList.size());
        while(i.hasNext()) {//Für jeden Eintrag
            MonthlyEntry e = i.next();
            Log.d(TAG,"Tag des Eintrags: "+e.getTag());
            if(e.getTag()==aktuellerTag)
            {
                //Eintragen
                Log.d(TAG,"Monatlicher Eintrag eingetragen: "+e.getTitel());
                strDate = sdft.format(new Date());
                addEntry(e.getBetrag(),e.getTitel(),null,e.getKategorie(),strDate);
                count++;
                entries = entries+e.getTitel()+"\n ";

            }
        }

        if(count>0){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notificationicon)
                            .setContentTitle("Neue Einträge gebucht!!")
                            .setContentText(entries);

            Intent resultIntent = new Intent(context, MainActivity.class);

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);

            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

        }


    }

    /**Diese Methode liefert alle monatlichen Entries als HashSet zurück*/
    public Set<MonthlyEntry> getMonthlyEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        Set<MonthlyEntry> allEntries = new HashSet<MonthlyEntry>();

        Cursor cursor = db.rawQuery("SELECT u.ID,u.BETRAG,u.TITEL,u.TAG,c.NAME FROM "+TABLE_NAME_REPEAT_ENTRY+" u JOIN "+TABLE_NAME_CATEGORY+" c ON u.KATEGORIE = c.ID", null);
        while(cursor.moveToNext())
        {
            //Log.d("DatabaseHelper"," "+cursor.getInt(0));
            MonthlyEntry e = new MonthlyEntry(cursor.getInt(0),cursor.getDouble(1),cursor.getString(2),cursor.getString(4),cursor.getInt(3));
            allEntries.add(e);
        }
        Log.i(TAG,"The list is:");
        Iterator<MonthlyEntry> i = allEntries.iterator();
        while(i.hasNext())
            Log.i(TAG,i.next().toString());
        cursor.close();
        db.close();

        return allEntries;
    }

    /**Diese Methode fügt einen neuen Monatlichen Eintrag in die Datenbank ein.*/
    public boolean addMonthlyEntry (double betrag, String titel, String kategorie, int tag){
        SQLiteDatabase db;
        SQLiteDatabase dbr = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        long newRowId;
        values.put("BETRAG", betrag);
        values.put("TITEL",titel);

        Cursor c = dbr.rawQuery("SELECT ID FROM category WHERE NAME = '"+kategorie+"'", null);
        c.moveToNext();
        values.put("KATEGORIE",c.getInt(0));
        c.close();
        dbr.close();

        values.put("TAG",tag);
        db = this.getWritableDatabase();
        newRowId = db.insert(TABLE_NAME_REPEAT_ENTRY, null, values);
        db.close();
        if(newRowId > 0) {
            Log.d(TAG,"Monatlicher Eintrag hinzugefügt");
            return true;
        }
        else
            return false;
    }

    /**Diese Methode löscht einen monatlichen Eintrag.*/
    public boolean deleteMonthlyEntry (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(TABLE_NAME_REPEAT_ENTRY,"ID ="+id,null);
        db.close();
        if(del > 0) {
            Log.i(TAG,"montatlicher Eintrag gelöscht!!!");
            return true;
        }
        else
            return false;
    }
}