package com.cashify.monthly_entries;

import android.widget.Toast;

import com.cashify.database.DatabaseHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Diese Klasse befüllt eine Liste mit den monatlichen Einträgen aus der Datenbank und bietet Mehtoden für das Hinzufügen und Löschen von Einträgen an.
 */
public class MonthlyEntryManager {

    /**Diese Liste repräsentiert alle monatlichen Einträge in der Datenbank.*/
    private static List<MonthlyEntry> entryList;
    /**Über den DatabaseHelper können sämtliche Datenbankfunktionen abgerufen werden.*/
    private DatabaseHelper dbHelper;

    public MonthlyEntryManager(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        reloadFromDb();
    }

    /**Diese Klasse lädt die monatlichen Einträge aus der Datenbank in die Liste.*/
    private void reloadFromDb() {
        if (entryList == null) entryList = new LinkedList<>();
        entryList.clear();
        entryList.addAll(dbHelper.getMonthlyEntries());
        Collections.sort(entryList, new Comparator<MonthlyEntry>() {
            @Override
            public int compare(MonthlyEntry o1, MonthlyEntry o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }

    /**Diese Methode liefert den monatlichen Eintrag der Liste mit dem übergebenen Index.*/
    public MonthlyEntry getMonthlyEntryByIndex(int index) {
        return entryList.get(index);
    }

    /**Diese Methode liefert den monatlichen Eintrag der Liste mit der übergebenen ID.*/
    public MonthlyEntry getMonthlyEntryById(int id) throws Exception {
        for(MonthlyEntry c : entryList) if (c.getId() == id) return c;
        throw new Exception("Monthly Entry id not found");
    }

    /**Diese Methode liefert alle monatlichen Einträge als Liste zurück.*/
    public List<MonthlyEntry> getMonthlyEntries() {
        return Collections.unmodifiableList(entryList);
    }

    /**Diese Methode liefert die Anzahl der Elemente in der Liste.*/
    public int getCount() {
        return entryList.size();
    }

    /**Diese Methode löscht den monatlichen Eintrag mit der übergebenen ID aus der Datenbank.*/
    public boolean removeMonthlyEntry(int id) {
        boolean success = this.dbHelper.deleteMonthlyEntry(id);
        if (success) this.reloadFromDb();
        return success;
    }

    /**Diese Methode fügt einen neuen monatlichen Eintrag in die Datenbank ein.*/
    public boolean addMonthlyEntry(double betrag,String titel,String kategorie,int tag){
        boolean success = this.dbHelper.addMonthlyEntry(betrag,titel,kategorie,tag);
        if (success) this.reloadFromDb();
        return success;

    }

    public boolean changeMonthlyEntry(int id, double betrag, String titel, String kategorie, int tag){
        boolean success = this.dbHelper.changeMonthlyEntry(id,betrag,titel,kategorie,tag);
        if (success) {
            this.reloadFromDb();
            return success;
        }
        return false;
    }
}
