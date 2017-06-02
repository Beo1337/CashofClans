package com.cashify.overview;

import com.cashify.base.AbstractEntry;

import java.util.Date;

/**
 * Created by Beo on 30.05.2017.
 * Diese Klasse repräsentiert einen Geldeintrag in der Datenbank.
 */

public class Entry extends AbstractEntry{

    private String datum;
    private String foto;

    public Entry(int id, double betrag,String title, String kategorie, String datum, String foto) {
        super(id, betrag, title, kategorie);
        this.datum = datum;
        this.foto = foto;
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
                "id=" + super.getId() +
                ", betrag=" + super.getBetrag() +
                ", title='" + super.getTitel() + '\'' +
                ", kategorie='" + super.getKategorie() + '\'' +
                ", datum='" + datum + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
