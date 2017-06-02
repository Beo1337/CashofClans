package com.cashify.monthly_entries;

import com.cashify.base.AbstractEntry;

/**
 * Created by Beo on 01.06.2017.
 */

public class MonthlyEntry extends AbstractEntry {

    int tag;

    public MonthlyEntry(int id, double betrag, String titel, String kategorie, int tag) {
        super(id, betrag, titel, kategorie);
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "MonthlyEntry{" +
                "id=" + super.getId() +
                ", betrag=" + super.getAmount() +
                ", titel='" + super.getTitle() + '\'' +
                ", kategorie='" + super.getCategory() + '\'' +
                ", tag=" + tag +
                '}';
    }
}
