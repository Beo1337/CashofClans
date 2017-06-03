package com.cashify.monthly_entries;

import com.cashify.base.AbstractEntry;

/**
 * Diese Klasse repräsentiert einen monatlichen Eintrag.
 */
public class MonthlyEntry extends AbstractEntry {

    int day;

    public MonthlyEntry(int id, double betrag, String titel, String kategorie, int day) {
        super(id, betrag, titel, kategorie);
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "MonthlyEntry{" +
                "id=" + super.getId() +
                ", betrag=" + super.getAmount() +
                ", titel='" + super.getTitle() + '\'' +
                ", kategorie='" + super.getCategory() + '\'' +
                ", day=" + day +
                '}';
    }
}
