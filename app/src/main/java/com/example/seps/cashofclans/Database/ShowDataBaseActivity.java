package com.example.seps.cashofclans.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.R;

/**
 *
 * Diese Klasse iste eine Hilfsklasse und soll die in der Datenbank gespeicherten Werte für Testzwecke ausgeben.
 *
 * */
public class ShowDataBaseActivity extends AppCompatActivity {

    /**Klasse für den Datenbankzugriff*/
    private DatabaseHelper myDb;
    /**Datenbankobjekt*/
    private SQLiteDatabase db;
    /**Textfeld welches den Inhalt der Datenbank anzeigt. */
    private EditText ausg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_base);

        showDB();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        showDB();
    }

    /**Diese Methode gibt den Inhalt aller Datenbanktabellen für Testzwecke aus.*/
    public void showDB(){
        myDb = new DatabaseHelper(this);
        db = myDb.getReadableDatabase();
        //Alle Einträge aus der Kategorietabelle holen.
        Cursor cursor = db.rawQuery("SELECT * FROM category", null);
        String entry = "";
        while(cursor.moveToNext())//Solange Einträge vorhanden sind, in den Ausgabestring speichern.
        {
            entry = entry +" "+ cursor.getInt(0);//ID
            entry = entry +" "+ cursor.getString(1);//Name
            entry = entry +" "+ cursor.getString(2);//Iconstring
            entry = entry +"\n";
        }
        entry = entry +"\n--------------------------\n";

        //Alle Einträge aus der Übersichtstabelle holen.
        cursor = db.rawQuery("SELECT * FROM uebersicht", null);
        while(cursor.moveToNext())//Solange Einträge vorhanden sind, in den Ausgabestring speichern.
        {
            entry = entry +" "+ cursor.getInt(0);//ID
            entry = entry +" "+ cursor.getDouble(1);//Betrag
            entry = entry +" "+ cursor.getString(2);//Bezeichnung
            entry = entry +" "+ cursor.getInt(3);//Kategorie (Fremdschlüssel)
            entry = entry +" "+ cursor.getString(4);//Datum
            //entry = entry +" "+ cursor.getString(5);//GPS
            entry = entry +" "+ cursor.getString(6);//Foto
            entry = entry +"\n";
        }
        cursor.close();

        //Datenbankausgabe in die View schreiben.
        ausg = (EditText) findViewById(R.id.ausg);
        ausg.setEnabled(false);
        ausg.setText(entry);
    }
}
