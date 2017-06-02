package com.cashify.base;

/**
 * Created by mhackl on 02.06.2017.
 */

// Entry and MonthlyEntry have a huge intersecting surface, gotta refactor this out

public class AbstractEntry {

    int id;
    double betrag;
    String titel;
    String kategorie;

    public AbstractEntry(int id, double betrag, String titel, String kategorie) {
        this.id = id;
        this.betrag = betrag;
        this.titel = titel;
        this.kategorie = kategorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return betrag;
    }

    public void setAmount(double betrag) {
        this.betrag = betrag;
    }

    public String getTitle() {
        return titel;
    }

    public void setTitle(String titel) {
        this.titel = titel;
    }

    public String getCategory() {
        return kategorie;
    }

    public void setCategory(String kategorie) {
        this.kategorie = kategorie;
    }

    @Override
    public String toString() {
        return "MonthlyEntry{" +
                "id=" + id +
                ", betrag=" + betrag +
                ", titel='" + titel + '\'' +
                ", kategorie='" + kategorie + '\'' +
                '}';
    }
}
