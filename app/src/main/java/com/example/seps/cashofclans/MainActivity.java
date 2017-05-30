package com.example.seps.cashofclans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.Database.ShowDataBaseActivity;
import com.example.seps.cashofclans.Overview.OverviewListActivity;
import com.example.seps.cashofclans.Overview.StatistikActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**Diese Methode aktualisiert den Kontostand auf der Startseite*/
    public void refresh_money() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String time = sharedPref.getString("zeit","");
        Log.d("MainActivity",time);

        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM uebersicht", null);
        Log.i("MainActivity:","Anzahl Daten: "+cursor.getCount());
        double summe = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while(cursor.moveToNext())
        {

            if(time.equals("Alle"))
            {
                summe += cursor.getDouble(1);
            }
            else {

                Date d = null;
                Date a = null;
                try {

                    d = sdf.parse(cursor.getString(4));
                    a = sdf.parse(sdf.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (time.equals("Monat")) {
                    if (d.getMonth() == a.getMonth())
                        summe += cursor.getDouble(1);
                }
                else if(time.equals("Jahr")){
                    if(d.getYear()==a.getYear())
                        summe += cursor.getDouble(1);
                }
            }
        }
        betrag= (TextView) findViewById(R.id.Money);
        Log.i("MainActivity","Betrag: "+summe);
        summe = Math.round(summe*100)/100.0;
        betrag.setText(String.valueOf(summe)+ "€");
        if(summe<0)
            betrag.setTextColor(Color.RED);
        else
            betrag.setTextColor(Color.GREEN);
        cursor.close();
        db.close();
    }

    /***************OnClick-Funktionen***************/
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

    public void shortcut(View v){
        ImageButton b = (ImageButton) findViewById(v.getId());
        Log.i("MainActivity","Shortcut: "+b.getTag().toString()+" gewählt");
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat",b.getTag().toString());
        startActivityForResult(i, 0);
    }

    public void stats(View v) {
        Intent i = new Intent(v.getContext(), StatistikActivity.class);
        startActivityForResult(i, 0);
    }

    public void  main(View v){

    }

    public void db(View v){
        Intent i = new Intent(v.getContext(), ShowDataBaseActivity.class);
        startActivityForResult(i, 0);
    }

    public void list(View v){
        Intent i = new Intent(v.getContext(),OverviewListActivity.class);
        startActivityForResult(i,0);
    }

}
