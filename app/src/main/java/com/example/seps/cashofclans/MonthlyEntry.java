package com.example.seps.cashofclans;

/**
 * Created by Beo on 01.06.2017.
 */

public class MonthlyEntry {

    int id;
    double betrag;
    String titel;
    String kategorie;
    int tag;

    public MonthlyEntry(int id, double betrag, String titel, String kategorie, int tag) {
        this.id = id;
        this.betrag = betrag;
        this.titel = titel;
        this.kategorie = kategorie;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBetrag() {
        return betrag;
    }

    public void setBetrag(double betrag) {
        this.betrag = betrag;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
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
                "id=" + id +
                ", betrag=" + betrag +
                ", titel='" + titel + '\'' +
                ", kategorie='" + kategorie + '\'' +
                ", tag=" + tag +
                '}';
    }
}
