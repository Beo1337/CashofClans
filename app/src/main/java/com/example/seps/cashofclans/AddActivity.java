package com.example.seps.cashofclans;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText betrag;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        betrag= (EditText) findViewById(R.id.Betrag);
        betrag.setEnabled(false);
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
        Intent i = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(i, 0);
    }

    public void del(View v){
        s = betrag.getText().toString();
        if(s.length()>0)
            s = s.substring(0,s.length()-1);
        betrag.setText(s);
    }


}
