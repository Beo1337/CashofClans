package com.example.seps.cashofclans;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText betrag;
    String s;
    DatabaseHelper myDb;
    int vorzeichen = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        betrag= (EditText) findViewById(R.id.Betrag);
        betrag.setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        Button commit = (Button)findViewById(R.id.ButtonCommit);


        //Schauen ob hinzugefügt oder abgezogen wird
        if(bundle.getString("mode")!= null)
        {
            if(bundle.getString("mode").equals("sub"))
            {
                vorzeichen = -1;//Wenn abgezogen wird, Vorzeichen ändern
                commit.setBackgroundColor(Color.RED);
            }
            else
                commit.setBackgroundColor(Color.GREEN);
        }
    }


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

    public void eintragen(View v){

        if(!betrag.getText().toString().equals("")) {
            myDb = new DatabaseHelper(this);
            SQLiteDatabase db = myDb.getWritableDatabase();

            //Werte für Datenbank vorbereiten
            ContentValues values = new ContentValues();
            values.put("BETRAG", Double.valueOf(betrag.getText().toString()) * vorzeichen);

            //In die Datenbank speichern
            long newRowId = db.insert("uebersicht", null, values);
            Log.i("AddActivity", "Eingefügt " + newRowId);

            Intent i = new Intent(v.getContext(), MainActivity.class);
            startActivityForResult(i, 0);
            finish();
        }
    }

    public void del(View v){
        s = betrag.getText().toString();
        if(s.length()>0)
            s = s.substring(0,s.length()-1);
        betrag.setText(s);
    }


}
