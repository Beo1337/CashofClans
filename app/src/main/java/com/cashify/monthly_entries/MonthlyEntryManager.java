package com.cashify.monthly_entries;

import com.cashify.database.DatabaseHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Beo on 01.06.2017.
 */

public class MonthlyEntryManager {

    private static List<MonthlyEntry> entryList;
    private DatabaseHelper dbHelper;

    public MonthlyEntryManager(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        reloadFromDb();
    }

    private void reloadFromDb() {
        if (entryList == null) entryList = new LinkedList<>();
        entryList.clear();
        entryList.addAll(dbHelper.getMonthlyEntries());
        /*Collections.sort(entryList, new Comparator<Entry>() {//Sortiert nach Datum
            @Override
            public int compare(Entry o1, Entry o2) {
                return o1.getDatum().compareTo(o2.getDatum());
            }
        });*/
    }

    // Return single entry by list position
    public MonthlyEntry getMonthlyEntryByIndex(int index) {
        return entryList.get(index);
    }

    public MonthlyEntry getMonthlyEntryById(int id) throws Exception {
        for(MonthlyEntry c : entryList) if (c.getId() == id) return c;
        throw new Exception("Monthly Entry id not found");
    }

    // Return list of all elements (unmodifiable)
    public List<MonthlyEntry> getMonthlyEntries() {
        return Collections.unmodifiableList(entryList);
    }

    // Return collection size
    public int getCount() {
        return entryList.size();
    }


    public boolean removeMonthlyEntry(int id) {
        boolean success = this.dbHelper.deleteMonthlyEntry(id);
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean addMonthlyEntry(double betrag,String titel,String kategorie,int tag){
        boolean success = this.dbHelper.addMonthlyEntry(betrag,titel,kategorie,tag);
        if (success) this.reloadFromDb();
        return success;

    }
}
