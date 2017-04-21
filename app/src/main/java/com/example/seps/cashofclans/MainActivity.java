package com.example.seps.cashofclans;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    TextView betrag;
    double summe = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("START MainActivity","Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM uebersicht", null);
        Log.i("MainActivity:","Anzahl Daten: "+cursor.getCount());

        while(cursor.moveToNext())
        {
            summe += cursor.getDouble(1);
        }
        betrag= (TextView) findViewById(R.id.Money);
        Log.i("MainActivity","Betrag: "+summe);
        betrag.setText(String.valueOf(summe)+ " €");
        if(summe<0)
            betrag.setTextColor(Color.RED);
        else
            betrag.setTextColor(Color.GREEN);

    }

    public void einstellungen(View v) {
        Intent i = new Intent(v.getContext(), EinstellungenActivity.class);
        startActivityForResult(i, 0);
    }

    public void add(View v) {
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "add");
        startActivityForResult(i, 0);
        finish();
    }

    public void sub(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        startActivityForResult(i, 0);
        finish();
    }

    public void stats(View v) {
        Intent i = new Intent(v.getContext(), StatistikActivity.class);
        startActivityForResult(i, 0);
    }

}
