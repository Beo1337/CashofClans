package com.example.seps.cashofclans.Overview;

import java.util.Date;

/**
 * Created by Beo on 30.05.2017.
 * Diese Klasse repräsentiert einen Geldeintrag in der Datenbank.
 */

public class Entry {

    private int id;
    private double betrag;
    private String title;
    private String kategorie;
    private String datum;
    private String foto;

    public Entry(int id, double betrag,String title, String kategorie, String datum, String foto) {
        this.id = id;
        this.betrag = Math.round(betrag*100)/100.0;;
        this.title = title;
        this.kategorie = kategorie;
        this.datum = datum;
        this.foto = foto;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id=" + id +
                ", betrag=" + betrag +
                ", title='" + title + '\'' +
                ", kategorie='" + kategorie + '\'' +
                ", datum='" + datum + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
