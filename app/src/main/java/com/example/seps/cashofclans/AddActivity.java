package com.example.seps.cashofclans;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Diese Klasse wird verwendet um neue Einträge in die Überischtstabelle einzufügen.
 *
 * */
public class AddActivity extends AppCompatActivity {

    /**Name der Klasse für Logcat*/
    private String TAG = this.getClass().getName();
    /**In diesem Textfeld wird der aktuelle Kontostand angezeigt.*/
    private EditText betrag;
    /**Wird benötigt um die gewählte Zahl in das Textfeld zu schreiben.*/
    private String s;
    /**Datenbank*/
    DatabaseHelper myDb;
    /**Wird benötigt um negative Einträge zu verbuchen*/
    private int vorzeichen = 1;
    /**Speichert eine Auswahlliste der verfügbaren Kategorien.*/
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        betrag= (EditText) findViewById(R.id.Betrag);
        //nicht auswählbar machen, damit die standardmäßige Tastatur nicht erscheint.
        betrag.setEnabled(false);

        myDb = new DatabaseHelper(this);

        //Über bundle können zusätzliche Infos aus dem Intent ausgelesen werden.
        Bundle bundle = getIntent().getExtras();

        Button commit = (Button)findViewById(R.id.ButtonCommit);

        //Cursor von der Kategorietabelle holen.
        Cursor c = myDb.getReadableDatabase().rawQuery("SELECT ID AS _id, NAME FROM category", null);
        Log.i(TAG, "CURSOR COUNT: "+c.getCount());
        //Adapter aus dem Cursor erstellen.
        String[] from = new String[] {"NAME"};
        int[] to = new int[] {android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to);

        //Vordefiniertes Layout dem Spinner zuweisen.
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin = (Spinner) this.findViewById(R.id.category);
        spin.setAdapter(sca);


        //Schauen ob hinzugefügt oder abgezogen wird
        if(bundle.getString("mode")!= null)
        {
            if(bundle.getString("mode").equals("sub"))//Wenn abgezogen wird, Vorzeichen ändern.
            {
                vorzeichen = -1;//Wenn abgezogen wird, Vorzeichen ändern
                commit.setBackgroundColor(Color.RED);
            }
            else
                commit.setBackgroundColor(Color.GREEN);

            if(bundle.getString("cat")!=null)//Wenn Shortcut gewählt wurde, ist Kategorie schon vorausgewählt.
            {
                Spinner cat = (Spinner) findViewById(R.id.category);
                selectValue(cat,bundle.getString("cat"));
                cat.setEnabled(false);
            }
        }
    }

    /** Diese Methode setzt den Spinner auf eine Kategorie.*/
    private void selectValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((Cursor)spinner.getItemAtPosition(i)).getString(1).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    /**Nummern in das Betragfeld schreiben.*/
    public void add1(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"1");
    }

    public void add2(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"2");
    }

    public void add3(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"3");
    }

    public void add4(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"4");
    }

    public void add5(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"5");
    }

    public void add6(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"6");
    }

    public void add7(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"7");
    }

    public void add8(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"8");
    }

    public void add9(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"9");
    }

    public void add0(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+"0");
    }

    public void addDot(View v) {
        s = betrag.getText().toString();
        betrag.setText(s+".");
    }

    /**Diese Methode speichert die eingegebenen Werte in die Datenbank.*/
    public void eintragen(View v){

        if(!betrag.getText().toString().equals("")) {//Wenn ein Betrag eingeben wurde, wird kein Eintrag in der Datenbank erstellt.

            SQLiteDatabase db = myDb.getWritableDatabase();
            EditText title = (EditText) findViewById(R.id.title);

            //Werte für Datenbank vorbereiten
            ContentValues values = new ContentValues();
            values.put("BETRAG", Double.valueOf(betrag.getText().toString()) * vorzeichen);
            values.put("TITEL",title.getText().toString());


            String s = ((Cursor)spin.getSelectedItem()).getString(1);
            Cursor c = myDb.getReadableDatabase().rawQuery("SELECT ID FROM category WHERE NAME = '"+s+"'", null);
            c.moveToNext();
            values.put("KATEGORIE",c.getInt(0));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(new Date());

            values.put("DATUM", strDate);

            //In die Datenbank speichern
            long newRowId = db.insert("uebersicht", null, values);
            Log.i("AddActivity", "Eingefügt " + newRowId);

            finish();
        }
    }

    /**Diese Methode löscht die letzte Stelle des Betragfelds.*/
    public void del(View v){
        s = betrag.getText().toString();
        if(s.length()>0)
            s = s.substring(0,s.length()-1);
        betrag.setText(s);
    }
}