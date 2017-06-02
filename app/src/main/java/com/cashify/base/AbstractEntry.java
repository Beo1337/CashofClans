package com.cashify.base;

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
