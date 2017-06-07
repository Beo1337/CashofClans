package com.cashify.monthly_entries;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;

/**
 * Diese Klasse stellt die alten Einträge eines monatlichen Eintrags dar und schreibt die geänderten Werte zurück in die Datenbank.
 */
public class ChangeMonthlyEntryActivity extends AppCompatActivity {

    /**In diesem Textfeld wird der aktuelle Kontostand angezeigt.*/
    private EditText betrag;
    /**In diesem Textfeld wird der aktuelle Title angezeigt.*/
    private EditText title;
    /**Wird benötigt um die gewählte Zahl in das Textfeld zu schreiben.*/
    private String s;
    /**Datenbank*/
    private DatabaseHelper myDb;
    /**Speichert eine Auswahlliste der verfügbaren Kategorien.*/
    private Spinner spin;
    /**Diser Numberpicker repräsentiert den Tag an dem der Eintrag gebucht werden soll (1-31)*/
    private NumberPicker np;
    /**Wird benötigt um Werte die der Aktivity mitgegeben wurden auszulesen.*/
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_monthly_entry);
        betrag = (EditText) findViewById(R.id.Betrag);
        title = (EditText) findViewById(R.id.title);
        np = (NumberPicker) findViewById(R.id.tag);
        np.setMaxValue(31);
        np.setMinValue(1);

        myDb = new DatabaseHelper(this);

        Button commit = (Button) findViewById(R.id.ButtonCommit);

        //Cursor von der Kategorietabelle holen.
        Cursor c = myDb.getReadableDatabase().rawQuery("SELECT ID AS _id, NAME FROM category", null);
        Log.i("AddActivity", "CURSOR COUNT: " + c.getCount());
        //Adapter aus dem Cursor erstellen.
        String[] from = new String[]{"NAME"};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, from, to);

        //Vordefiniertes Layout dem Spinner zuweisen.
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin = (Spinner) this.findViewById(R.id.category);
        spin.setAdapter(sca);

        bundle = getIntent().getExtras();

        betrag.setText(bundle.getString("betrag"));
        np.setValue(Integer.valueOf(bundle.getString("tag")));
        selectValue(spin, bundle.getString("kategorie"));
        title.setText(bundle.getString("titel"));
    }

    /**Diese Methode setzt den Spinner auf eine Kategorie.*/
    private void selectValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (((Cursor) spinner.getItemAtPosition(i)).getString(1).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    /**Diese Methode speichert die eingegebenen Werte in die Datenbank.*/
    public void eintragen(View v) {
        boolean success;

        //Wenn kein Betrag eingeben wurde, wird kein Eintrag in der Datenbank erstellt.
        if (!betrag.getText().toString().equals("")) {

            MonthlyEntryManager manager = new MonthlyEntryManager(myDb);

            title = (EditText) findViewById(R.id.title);

            //Kategorie holen
            String catStr = ((Cursor) spin.getSelectedItem()).getString(1);

            success = manager.changeMonthlyEntry(
                    Integer.valueOf(bundle.getString("id")),
                    Double.valueOf(betrag.getText().toString()),
                    title.getText().toString(),
                    catStr,
                    np.getValue()
            );

            if (success)
                Toast.makeText(this, "Eintrag geändert!!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Ändern nicht möglich!!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
