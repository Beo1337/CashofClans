package com.cashify.category;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;


public class ChangeCategoryActivity extends AppCompatActivity {


    /**
     * Der TAG wird für das Log verwendet um anzuzeigen von welcher Klasse der Logeintrag stammt.
     */
    private static final String TAG = "AddActivity";
    /**
     * In diesem Textfeld wird die aktuelle Kategorie angezeigt.
     */
    private EditText kategorie;
    /**
     * In diesem Textfeld wird die aktuelle ID angezeigt.
     */
    private TextView id;
    /**
     * Datenbank
     */
    private DatabaseHelper myDb;
    /**
     * Wird benötigt um Werte die der Activity mitgegeben wurden auszulesen.
     */
    private Bundle bundle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_category);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            kategorie = (EditText) findViewById(R.id.editText_kategorie);
            id = (TextView) findViewById(R.id.textView_id);

            myDb = new DatabaseHelper(this);

            //Über bundle können zusätzliche Infos aus dem Intent ausgelesen werden.
            bundle = getIntent().getExtras();

            Button commit = (Button) findViewById(R.id.ButtonCommit);

            kategorie.setText(bundle.getString("name"));
            id.setText(bundle.getString("id"));
        }

        /**
         * Diese Methode speichert die eingegebenen Werte in die Datenbank.
         */
        public void eintragen(View v) {

            if (!kategorie.getText().toString().equals("")) {//Wenn keine Kategorie eingegeben wurde, wird kein Eintrag in der Datenbank erstellt.

                EditText kategorie = (EditText) findViewById(R.id.editText_kategorie);
                CategoryManager manager = new CategoryManager(myDb);

                if(!manager.changeCategory(Integer.valueOf(bundle.getString("id")), kategorie.getText().toString()))
                    Toast.makeText(this, "Fehler beim Ändern!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
}
