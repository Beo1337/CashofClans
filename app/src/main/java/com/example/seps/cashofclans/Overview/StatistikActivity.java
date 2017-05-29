package com.example.seps.cashofclans.Overview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.seps.cashofclans.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class StatistikActivity extends AppCompatActivity {

    Button btn_list;
    Button btn_3_months;
    Button btn_6_months;
    Button btn_12_months;
    EditText start_editText;
    EditText end_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);

        btn_list = (Button) findViewById(R.id.list_button);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatistikActivity.this, OverviewListActivity.class);
                i.putExtra("start",start_editText.getText());
                i.putExtra("end",end_editText.getText());
                StatistikActivity.this.startActivity(i);
            }
        });

        start_editText = (EditText) findViewById(R.id.start_editText);
        end_editText = (EditText) findViewById(R.id.end_editText);



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /*------------------3 MONTHS------------------*/
        btn_3_months = (Button) findViewById(R.id.months_3_button);
        btn_3_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = new GregorianCalendar();
                c.setTimeInMillis(System.currentTimeMillis());

                String dateUntil = sdf.format(c.getTime());
                c.add(Calendar.MONTH,-3);
                String dateFrom = sdf.format(c.getTime());

                start_editText.setText(dateFrom);
                end_editText.setText(dateUntil);
            }
        });

        /*------------------6 MONTHS------------------*/
        btn_6_months = (Button) findViewById(R.id.months_6_button);
        btn_6_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = new GregorianCalendar();
                c.setTimeInMillis(System.currentTimeMillis());

                String dateUntil = sdf.format(c.getTime());
                c.add(Calendar.MONTH,-6);
                String dateFrom = sdf.format(c.getTime());

                start_editText.setText(dateFrom);
                end_editText.setText(dateUntil);
            }
        });

        /*------------------12 MONTHS------------------*/
        btn_12_months = (Button) findViewById(R.id.months_12_button);
        btn_12_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = new GregorianCalendar();
                c.setTimeInMillis(System.currentTimeMillis());

                String dateUntil = sdf.format(c.getTime());
                c.add(Calendar.MONTH,-12);
                String dateFrom = sdf.format(c.getTime());

                start_editText.setText(dateFrom);
                end_editText.setText(dateUntil);
            }
        });
    }
}