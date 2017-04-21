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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("START MainActivity","Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        refresh_money();


    }

    @Override
    public void onRestart() {
        super.onRestart();
        refresh_money();
    }

    public void refresh_money(){
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM uebersicht", null);
        Log.i("MainActivity:","Anzahl Daten: "+cursor.getCount());
        double summe = 0;

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
    }

    public void sub(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        startActivityForResult(i, 0);
    }

    public void grocery(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", "Lebensmittel");
        startActivityForResult(i, 0);
    }

    public void club(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", "Bar");
        startActivityForResult(i, 0);
    }

    public void sport(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", "Sport");
        startActivityForResult(i, 0);
    }

    public void clothes(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", "Kleidung");
        startActivityForResult(i, 0);
    }

    public void books(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", "Bücher");
        startActivityForResult(i, 0);
    }

    public void cinema(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat", "Kino");
        startActivityForResult(i, 0);
    }

    public void stats(View v) {
        Intent i = new Intent(v.getContext(), StatistikActivity.class);
        startActivityForResult(i, 0);
    }

    public void db(View v){
        Intent i = new Intent(v.getContext(), ShowDataBaseActivity.class);
        startActivityForResult(i, 0);
    }



}
