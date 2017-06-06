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

import com.cashify.R;
import com.cashify.category.Category;
import com.cashify.main.MainActivity;
import com.cashify.monthly_entries.MonthlyEntry;
import com.cashify.overview.Entry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Diese Klasse wird benötigt um die Datenbank nach Installation der App zu erstelle und mit den vordefinierten Daten zu füllen.
 * Es werden Datenbankfunktionen angeboten, welche in der ganzen App verwendet werden können.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.
     */
    private static final String TAG = "DatabaseHelper";
    /**
     * Name der Datenbank
     */
    public static final String DATABASE_NAME = "cash.db";
    /**
     * Name der Tabelle welche die Einträge speichert
     */
    public static final String TABLE_NAME_MAIN = "uebersicht";
    /**
     * Name der Tabelle welche die Kategorien speichert
     */
    public static final String TABLE_NAME_CATEGORY = "category";
    /**
     * Name der Tabelle welche die monatlich wiederholten Aufträge speichert.
     */
    public static final String TABLE_NAME_REPEAT_ENTRY = "REPEAT_ENTRY";

    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd");
    public static final SimpleDateFormat sdfm = new SimpleDateFormat("MM");
    public static final SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat sdft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate!");
        db.execSQL("CREATE TABLE " + TABLE_NAME_MAIN + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, DATUM DATE, FOTO TEXT, FOREIGN KEY(KATEGORIE) REFERENCES category(ID) ON DELETE RESTRICT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_CATEGORY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT , ICON TEXT, CONSTRAINT name_unique UNIQUE (NAME))");
        db.execSQL("CREATE TABLE " + TABLE_NAME_REPEAT_ENTRY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, BETRAG NUMBER(10,2), TITEL TEXT, KATEGORIE INTEGER, TAG INTEGER, FOREIGN KEY(KATEGORIE) REFERENCES category(ID) ON DELETE RESTRICT)");

        //Vordefinierte Kategorien in die Datenbank speichern.
        addCat("Lebensmittel", db);
        addCat("Bar", db);
        addCat("Sport", db);
        addCat("Kleidung", db);
        addCat("Bücher", db);
        addCat("Kino", db);
        addCat("Gehalt", db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade!");
        //Tabellen löschen und neu erstellen.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REPEAT_ENTRY);
        onCreate(db);
    }


    /**
     * Diese Methode fügt eine neue Kategorie in die Kategorie-Tabelle ein.
     */
    private boolean addCat(String name, SQLiteDatabase db) {
        long newRowId = 0;

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);

        //In die Datenbank speichern
        try {
            newRowId = db.insert(TABLE_NAME_CATEGORY, null, values);
        } catch (SQLiteException e) {
            Log.i(TAG, "Fehler beim Einfügen der Kategorie" + e.getMessage());
            return false;
        }


        if (newRowId > 0) {
            Log.i(TAG, "Eingefügt " + newRowId);
            return true;
        } else {
            Log.i(TAG, "Eingefügt NOPE " + newRowId);
            return false;
        }
    }


    /**
     * Diese Methode fügt eine neue Kategorie in die Kategorie-Tabelle ein.
     */
    public boolean addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = 0;

        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        values.put("NAME", name);

        //In die Datenbank speichern
        try {
            newRowId = db.insert(TABLE_NAME_CATEGORY, null, values);
        } catch (SQLiteException e) {
            Log.i(TAG, "Fehler beim Einfügen der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if (newRowId > 0) {
            Log.i(TAG, "Eingefügt " + newRowId);
            return true;
        } else {
            Log.i(TAG, "Eingefügt NOPE " + newRowId);
            return false;
        }
    }

    /**
     * Diese Methode fügt eine neue Kategorie in die Kategorie-Tabelle ein. Optional ist es möglich String für ein Icon mitzugeben.
     */
    public boolean addCategory(String name, String icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Werte für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        long newRowId;
        values.put("NAME", name);
        values.put("ICON", icon);

        //In die Datenbank speichern
        try {
            newRowId = db.insert(TABLE_NAME_CATEGORY, null, values);
            Log.i(TAG, "Eingefügt " + newRowId);
        } catch (SQLiteException e) {
            Log.i(TAG, "Fehler beim Einfügen der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if (newRowId > 0)
            return true;
        else
            return false;
    }

    /**
     * Diese Methode ändert den Namen einer Kategorie.
     */
    public boolean changeCategoryName(int id, String newname) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Wert für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        long rowId;
        values.put("NAME", newname);

        //In die Datenbank speichern
        try {
            rowId = db.update(TABLE_NAME_CATEGORY, values, "ID = " + id, null);
            Log.i(TAG, "Kategoriename geändert!!!");
        } catch (SQLiteException e) {
            Log.i(TAG, "Fehler beim Ändern des Namens der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if (rowId > 0)
            return true;
        else
            return false;
    }

    /**
     * Diese Methode ändert das Icon einer Kategorie.
     */
    public boolean changeCategoryIcon(int id, String icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Wert für Datenbank vorbereiten
        ContentValues values = new ContentValues();
        long rowId;
        values.put("ICON", icon);

        //In die Datenbank speichern
        try {
            rowId = db.update(TABLE_NAME_CATEGORY, values, "ID = " + id, null);
            Log.i(TAG, "Kategoriename geändert!!!");
        } catch (SQLiteException e) {
            Log.i(TAG, "Fehler beim Ändern des Icons der Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();

        if (rowId > 0)
            return true;
        else
            return false;
    }

    /**
     * Diese Methode löscht eine Kategorie aus der Datenbank.
     */
    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del;
        try {

            del = db.delete(TABLE_NAME_CATEGORY, "id = " + id + " AND NOT EXISTS (SELECT KATEGORIE FROM " + TABLE_NAME_MAIN + " WHERE KATEGORIE = (SELECT ID FROM " + TABLE_NAME_CATEGORY + " WHERE id = " + id + ") AND NOT EXISTS (SELECT KATEGORIE FROM " + TABLE_NAME_REPEAT_ENTRY + " WHERE KATEGORIE = (SELECT ID FROM " + TABLE_NAME_CATEGORY + " WHERE id = " + id + "))", null);
            Log.i("DataBaseHelper", "Kategorie gelöscht!!!  " + del);
        } catch (SQLiteException e) {
            Log.i(TAG, "Fehler beim Löschen einer Kategorie" + e.getMessage());
            db.close();
            return false;
        }
        db.close();
        if (del > 0)
            return true;
        else
            return false;
    }

    /**
     * Diese Methode liefert alle Kategorien als HashSet zurück.
     */
    public Set<Category> getCategories() {
        return this.getCategoriesReuse(false);
    }

    /**
     * Diese Methode liefert alle Kategorien als HashSet zurück.
     */
    private Set<Category> getCategoriesReuse(boolean keepOpen) {
        SQLiteDatabase db = this.getReadableDatabase();
        Set<Category> allCategories = new HashSet<Category>();

        Cursor c = db.query(
                TABLE_NAME_CATEGORY, // Category table
                null, // All columns
                null, // no selection
                null, // no selection args
                null, // no grouping
                null, // no having
                null // no order
        );

        while (c.moveToNext()) allCategories.add(
                new Category(c.getInt(0), c.getString(1), c.getString(2))
        );

        Log.i(TAG, "The list is:");
        for (Category cat : allCategories) Log.i(TAG, cat.toString());

        c.close();
        if (!keepOpen) db.close();
        return allCategories;
    }

    /**
     * Diese Methode liefert alle Entries als HashSet zurück
     */
    public Set<Entry> getEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        Set<Entry> allEntries = new HashSet<Entry>();
        Map<Integer, Category> catMap = new TreeMap<>();

        for (Category c : getCategoriesReuse(true)) catMap.put(c.getId(), c);

        Cursor cursor = db.rawQuery("SELECT u.ID,u.BETRAG,u.TITEL,u.DATUM,U.FOTO,c.ID FROM " + TABLE_NAME_MAIN + " u JOIN " + TABLE_NAME_CATEGORY + " c ON u.KATEGORIE = c.ID", null);
        while (cursor.moveToNext()) {
            //Log.d("DatabaseHelper"," "+cursor.getInt(0));
            Entry e = new Entry(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), catMap.get(cursor.getInt(5)), cursor.getString(3), cursor.getString(4));
            allEntries.add(e);
        }
        Log.i(TAG, "The list is:");
        Iterator<Entry> i = allEntries.iterator();
        while (i.hasNext())
            Log.i(TAG, i.next().toString());
        cursor.close();

        return allEntries;
    }

    /**
     * Diese Methode fügt einen Eintrag in die Übersichtstabelle ein.
     */
    public boolean addEntry(double betrag, String title, String foto, String kategorie, String date) {
        SQLiteDatabase db;
        SQLiteDatabase dbr = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("DATUM", date);
        values.put("BETRAG", betrag);
        values.put("TITEL", title);
        values.put("FOTO", foto);

        Cursor c = dbr.query(
                TABLE_NAME_CATEGORY, // Category table
                new String[]{"ID"}, // column ID
                "NAME = ?", // where name = kategorie
                new String[]{kategorie}, // see above
                null, // no grouping
                null, // no having
                null // no order
        );
        c.moveToNext();

        values.put("KATEGORIE", c.getInt(0));
        c.close();
        dbr.close();
        db = this.getWritableDatabase();

        //In die Datenbank speichern
        long newRowId = db.insert("uebersicht", null, values);
        Log.i(TAG, "Eingefügt " + newRowId);

        db.close();

        return newRowId > 0;
    }

    /**
     * Diese Methode löscht den Eintrag mit der übergegenen ID aus der Datenbank.
     */
    public boolean removeEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(TABLE_NAME_MAIN, "ID =" + id, null);
        db.close();
        Log.i(TAG, "Eintrag gelöscht!!!");
        if (del > 0)
            return true;
        else
            return false;
    }

    /**
     * Diese Methode ändert den Eintrag der übergebenen ID.
     */
    public boolean changeEntry(int id, double betrag, String title, String foto, String kategorie, String date) {

        SQLiteDatabase db;
        SQLiteDatabase dbr = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("BETRAG", betrag);
        values.put("TITEL", title);
        values.put("FOTO", foto);

        Cursor c = dbr.rawQuery("SELECT ID FROM category WHERE NAME = '" + kategorie + "'", null);
        c.moveToNext();
        values.put("KATEGORIE", c.getInt(0));
        c.close();
        dbr.close();
        values.put("DATUM", date);
        db = this.getWritableDatabase();
        //In die Datenbank speichern
        long changedRowId = db.update(TABLE_NAME_MAIN, values, "ID =" + id, null);

        db.close();

        if (changedRowId > 0) {
            Log.d(TAG, "Eintrag geändert!!");
            return true;
        } else
            return false;

    }

    /**
     * Diese Methode checkt ob Einträge aus den monatlichen Einträgen heute eingetragen werden müssen.
     */
    public void checkMonthlyEntries(Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        String strDate;
        String entries = "";
        int aktuellerTag;
        int aktuellerMonat;
        int aktuellesJahr;
        int count = 0;

        //aktuellen Tag festellen.
        aktuellerTag = Integer.valueOf(sdf.format(new Date()));
        //aktuelles Monat feststellen.
        aktuellerMonat = Integer.valueOf(sdfm.format(new Date()));
        //aktuelles Jahr feststellen.
        aktuellesJahr = Integer.valueOf(sdfy.format(new Date()));

        List<MonthlyEntry> entryList = new LinkedList<>();
        entryList.addAll(getMonthlyEntries());

        Iterator<MonthlyEntry> i = entryList.iterator();//Die Liste der monatlichen Einträge durchgehen.
        Log.d(TAG, "Anzahl monatlicher Einträge: " + entryList.size());
        while (i.hasNext()) {//Für jeden Eintrag
            MonthlyEntry e = i.next();
            Log.d(TAG, "Tag des Eintrags: " + e.getTag());
            if (e.getTag() == aktuellerTag)//Wenn Tag genau der Tag des Monats ist.
            {
                //Eintragen
                Log.d(TAG, "Monatlicher Eintrag eingetragen: " + e.getTitle());
                strDate = sdft.format(new Date());
                addEntry(e.getAmount(), e.getTitle(), null, e.getCategory().getName(), strDate);
                count++;
                entries = entries + e.getTitle() + "\n ";

            }
            //Wenn der Monat nur 30 Tage hat, werden die Einträge die am 31 gebucht werden schon am 30 gebucht.
            if (aktuellerTag == 30 && (e.getTag() == 31 && aktuellerMonat == 4) || (e.getTag() == 31 && aktuellerMonat == 6) || (e.getTag() == 31 && aktuellerMonat == 9) || (e.getTag() == 31 && aktuellerMonat == 11)) {
                //Eintragen
                Log.d(TAG, "Monatlicher Eintrag eingetragen(Monat hat nur 30Tage): " + e.getTitle());
                strDate = sdft.format(new Date());
                addEntry(e.getAmount(), e.getTitle(), null, e.getCategory().getName(), strDate);
                count++;
                entries = entries + e.getTitle() + "\n ";
            }

            //Wenn Februar ist, kein Schaltjahr ist, Buchungen vom 31,30 und 29 schon am 28 durchführen.
            if (aktuellerMonat == 2 && aktuellesJahr % 4 != 0 && aktuellerTag == 28 && (e.getTag() == 31 || e.getTag() == 30 || e.getTag() == 29)) {
                //Eintragen
                Log.d(TAG, "Monatlicher Eintrag eingetragen (Februar hat nur 28 Tage, kein Schaltjahr): " + e.getTitle());
                strDate = sdft.format(new Date());
                addEntry(e.getAmount(), e.getTitle(), null, e.getCategory().getName(), strDate);
                count++;
                entries = entries + e.getTitle() + "\n ";
            }

            //Wenn Februar ist, Schaltjahr ist, Buchungen vom 31,30 schon am 29 durchführen.
            if (aktuellerMonat == 2 && aktuellesJahr % 4 == 0 && aktuellerTag == 29 && (e.getTag() == 31 || e.getTag() == 30)) {
                //Eintragen
                Log.d(TAG, "Monatlicher Eintrag eingetragen (Februar hat nur 29 Tage, Schaltjahr): " + e.getTitle());
                strDate = sdft.format(new Date());
                addEntry(e.getAmount(), e.getTitle(), null, e.getCategory().getName(), strDate);
                count++;
                entries = entries + e.getTitle() + "\n ";
            }
        }

        if (count > 0) {//Wenn ein Eintrag gemacht wurde soll eine Benachrichtgung an den Benutzer erstellt werden.
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

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }

    /**
     * Diese Methode liefert alle monatlichen Entries als HashSet zurück
     */
    public Set<MonthlyEntry> getMonthlyEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        Set<MonthlyEntry> allEntries = new HashSet<MonthlyEntry>();
        Map<Integer, Category> catMap = new TreeMap<>();

        for (Category c : getCategoriesReuse(true)) catMap.put(c.getId(), c);

        Cursor cursor = db.rawQuery("SELECT u.ID,u.BETRAG,u.TITEL,u.TAG,c.ID FROM " + TABLE_NAME_REPEAT_ENTRY + " u JOIN " + TABLE_NAME_CATEGORY + " c ON u.KATEGORIE = c.ID", null);
        while (cursor.moveToNext()) {
            //Log.d("DatabaseHelper"," "+cursor.getInt(0));
            MonthlyEntry e = new MonthlyEntry(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), catMap.get(cursor.getInt(4)), cursor.getInt(3));
            allEntries.add(e);
        }
        Log.i(TAG, "The list is:");
        Iterator<MonthlyEntry> i = allEntries.iterator();
        while (i.hasNext())
            Log.i(TAG, i.next().toString());
        cursor.close();
        db.close();

        return allEntries;
    }

    /**
     * Diese Methode fügt einen neuen Monatlichen Eintrag in die Datenbank ein.
     */
    public boolean addMonthlyEntry(double betrag, String titel, String kategorie, int tag) {
        SQLiteDatabase db;
        SQLiteDatabase dbr = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        long newRowId;
        values.put("BETRAG", betrag);
        values.put("TITEL", titel);

        Cursor c = dbr.rawQuery("SELECT ID FROM category WHERE NAME = '" + kategorie + "'", null);
        c.moveToNext();
        values.put("KATEGORIE", c.getInt(0));
        c.close();
        dbr.close();

        values.put("TAG", tag);
        db = this.getWritableDatabase();
        newRowId = db.insert(TABLE_NAME_REPEAT_ENTRY, null, values);
        db.close();
        if (newRowId > 0) {
            Log.d(TAG, "Monatlicher Eintrag hinzugefügt");
            return true;
        } else
            return false;
    }

    /**
     * Diese Methode löscht einen monatlichen Eintrag.
     */
    public boolean deleteMonthlyEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(TABLE_NAME_REPEAT_ENTRY, "ID =" + id, null);
        db.close();
        if (del > 0) {
            Log.i(TAG, "montatlicher Eintrag gelöscht!!!");
            return true;
        } else
            return false;
    }

    public boolean changeMonthlyEntry(int id, double betrag, String titel, String kategorie, int tag) {
        SQLiteDatabase db;
        SQLiteDatabase dbr = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        long changedRowId;

        values.put("BETRAG", betrag);
        values.put("TITEL", titel);

        Cursor c = dbr.rawQuery("SELECT ID FROM category WHERE NAME = '" + kategorie + "'", null);
        c.moveToNext();
        values.put("KATEGORIE", c.getInt(0));
        dbr.close();

        values.put("TAG", tag);

        db = this.getWritableDatabase();

        changedRowId = db.update(TABLE_NAME_REPEAT_ENTRY, values, "ID =" + id, null);
        db.close();

        if (changedRowId > 0) {
            Log.i(TAG, "montatlicher Eintrag geändert!!!");
            return true;
        } else
            return false;

    }

    //TODO: Simon bitte Einträge innerhalb eines Datum und einer bestimmten Kategorie sollen zurückgegeben werden
    public Set<Entry> getEntriesWithinDateAndCategory(String from, String to, String category) {
        if (from.equals(null) && to.equals(null)) return getEntriesWithCategory(category); //Falls Startdatum ODER Enddatum leer sind geben wir alle Einträge einer bestimmten Kategorie zurück
        return null;
    }

    private Set<Entry> getEntriesWithCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Set<Entry> allEntries = new HashSet<Entry>();
        Map<Integer, Category> catMap = new TreeMap<>();

        for (Category c : getCategoriesReuse(true)) catMap.put(c.getId(), c);

        Cursor cursor = db.rawQuery("SELECT u.ID,u.BETRAG,u.TITEL,u.DATUM,U.FOTO,c.ID FROM " + TABLE_NAME_MAIN + " u JOIN " + TABLE_NAME_CATEGORY + " c ON u.KATEGORIE = c.ID WHERE c.NAME = " + category, null);
        while (cursor.moveToNext()) {
            //Log.d("DatabaseHelper"," "+cursor.getInt(0));
            Entry e = new Entry(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), catMap.get(cursor.getInt(5)), cursor.getString(3), cursor.getString(4));
            allEntries.add(e);
        }
        Log.i(TAG, "The list is:");
        Iterator<Entry> i = allEntries.iterator();
        while (i.hasNext())
            Log.i(TAG, i.next().toString());
        cursor.close();

        return allEntries;
    }
}