package com.cashify.monthly_entries;

import com.cashify.base.AbstractEntry;
import com.cashify.category.Category;

/**
 * Diese Klasse repräsentiert einen monatlichen Eintrag.
 */
public class MonthlyEntry extends AbstractEntry {

    int tag;

    public MonthlyEntry(int id, double betrag, String titel, Category cat, int tag) {
        super(id, betrag, titel, cat);
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
