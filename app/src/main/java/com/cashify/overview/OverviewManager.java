package com.cashify.overview;

import com.cashify.database.DatabaseHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse befüllt eine Liste mit den Einträgen aus der Datenbank und bietet Mehtoden für das Hinzufügen und Löschen von Einträgen an.
 */
public class OverviewManager {

    /**Liste mit den Einträgen die aus der Datenbank befüllt wird. */
    private static List<Entry> entryList;
    /**Über den Databasehelper kann die Datenbank angesprochen werden.*/
    private DatabaseHelper dbHelper;

    public OverviewManager(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        reloadFromDb();
    }

    private void reloadFromDb() {
        if (entryList == null) entryList = new LinkedList<>();
        entryList.clear();
        entryList.addAll(dbHelper.getEntries());
        Collections.sort(entryList, new Comparator<Entry>() {//Sortiert nach Datum
            @Override
            public int compare(Entry o1, Entry o2) {
                return o1.getDatum().compareTo(o2.getDatum());
            }
        });
    }
    
    // Return single entry by list position
    public Entry getEntryByIndex(int index) {
        return entryList.get(index);
    }

    public Entry getEntryById(int id) throws Exception {
        for (Entry c : entryList) if (c.getId() == id) return c;
        return Entry.DefaultError();
    }

    // Return list of all elements (unmodifiable)
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entryList);
    }

    // Return collection size
    public int getCount() {
        return entryList.size();
    }


    public boolean removeEntry(int id) {
        boolean success = this.dbHelper.removeEntry(id);
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean changeEntry(int id, double betrag, String title, String foto, String kategorie, String date) {
        boolean success = this.dbHelper.changeEntry(id, betrag, title, foto, kategorie, date);
        if (success) this.reloadFromDb();
        return success;
    }
}
