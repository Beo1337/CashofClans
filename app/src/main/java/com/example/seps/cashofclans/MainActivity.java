package com.example.seps.cashofclans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
    }

    public void einstellungen(View v) {
        Intent i = new Intent(v.getContext(), EinstellungenActivity.class);
        startActivityForResult(i, 0);
    }

    public void add(View v) {
        Intent i = new Intent(v.getContext(), AddActivity.class);
        startActivityForResult(i, 0);
    }

    public void stats(View v) {
        Intent i = new Intent(v.getContext(), StatistikActivity.class);
        startActivityForResult(i, 0);
    }

}
