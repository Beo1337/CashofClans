package com.example.seps.cashofclans;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ShowDataBaseActivity extends AppCompatActivity {

    DatabaseHelper myDb;
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

    public void showDB(){
        myDb = new DatabaseHelper(this);
        SQLiteDatabase db = myDb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM category", null);
        String entry = "";
        while(cursor.moveToNext())
        {

            entry = entry +" "+ cursor.getInt(0);
            entry = entry +" "+ cursor.getString(1);
            entry = entry +" "+ cursor.getString(2);
            entry = entry +"\n";
        }
        entry = entry +"\n--------------------------\n";

        cursor = db.rawQuery("SELECT * FROM uebersicht", null);
        while(cursor.moveToNext())
        {

            entry = entry +" "+ cursor.getInt(0);
            entry = entry +" "+ cursor.getDouble(1);
            entry = entry +" "+ cursor.getString(2);
            entry = entry +" "+ cursor.getInt(3);
            entry = entry +" "+ cursor.getString(4);
            //GPS entry = entry +" "+ cursor.getString(5);
            //Foto entry = entry +" "+ cursor.getString(6);
            entry = entry +"\n";
        }

        EditText ausg = (EditText) findViewById(R.id.ausg);
        ausg.setEnabled(false);
        ausg.setText(entry);
    }
}
