package com.example.seps.cashofclans.Overview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashify.category.Category;
import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.EinstellungenActivity;
import com.example.seps.cashofclans.MainActivity;
import com.example.seps.cashofclans.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StatistikActivity extends AppCompatActivity {

    private static final String TAG = "StatistikActivity";
    Button btn_list;
    Button btn_3_months;
    Button btn_6_months;
    Button btn_12_months;
    EditText start_editText;
    EditText end_editText;

    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieChart pieChart;
    PieDataSet pieDataSet ;
    PieData pieData ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);
        addChart();

        btn_list = (Button) findViewById(R.id.list_button);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i = new Intent(StatistikActivity.this, OverviewListActivity.class);
                i.putExtra("start",start_editText.getText());
                i.putExtra("end",end_editText.getText());
                StatistikActivity.this.startActivity(i);
                */

                Intent i = new Intent(StatistikActivity.this, com.cashify.overview.OverviewListActivity.class);
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

    public void stats(View v) {

    }

    public void  main(View v){
        Intent i = new Intent(v.getContext(),MainActivity.class);
        startActivityForResult(i,0);
    }

    public void einstellungen(View v) {
        Intent i = new Intent(v.getContext(), EinstellungenActivity.class);
        startActivityForResult(i, 0);
    }


    public void addChart(){

        DatabaseHelper myDb = new DatabaseHelper(this);

        pieChart = (PieChart) findViewById(R.id.chart);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        HashMap<String,Object> value = new HashMap<String,Object>();
        List<Category> categoryList = new LinkedList<>();
        categoryList.clear();
        categoryList.addAll(myDb.getCategories());
        Collections.sort(categoryList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        Iterator<Category> i = categoryList.iterator();
        while(i.hasNext()) {//Für jeden Eintrag
            Category c = i.next();
            value.put(c.getName(),0.0);
        }
        //Liste aller Einträge aus der Datenbank holen.
        List<com.example.seps.cashofclans.Overview.Entry> entryList = new LinkedList<>();
        entryList.addAll(myDb.getEntries());
        Collections.sort(entryList, new Comparator<com.example.seps.cashofclans.Overview.Entry>() {//Sortiert nach Datum
            @Override
            public int compare(com.example.seps.cashofclans.Overview.Entry o1, com.example.seps.cashofclans.Overview.Entry o2) {
                return o1.getDatum().compareTo(o2.getDatum());
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String time = sharedPref.getString("zeit","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Iterator<com.example.seps.cashofclans.Overview.Entry> i1 = entryList.iterator();
        while(i1.hasNext()) {//Für jeden Eintrag
            com.example.seps.cashofclans.Overview.Entry e = i1.next();
            if(e.getBetrag()<0)
            {

                if(time.equals("Alle"))
                {
                    double v = ((Double)value.get(e.getKategorie())).doubleValue();
                    v += (e.getBetrag()*-1);
                    value.put(e.getKategorie(),v);
                }
                else {

                    Date d = null;
                    Date a = null;
                    try {

                        d = sdf.parse(e.getDatum());
                        a = sdf.parse(sdf.format(new Date()));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    if (time.equals("Monat")) {
                        if (d.getMonth() == a.getMonth()) {
                            double v = ((Double) value.get(e.getKategorie())).doubleValue();
                            v += (e.getBetrag() * -1);
                            value.put(e.getKategorie(), v);
                        }
                    }
                    else if(time.equals("Jahr")){
                        if(d.getYear()==a.getYear()){
                            double v = ((Double)value.get(e.getKategorie())).doubleValue();
                            v += (e.getBetrag()*-1);
                            value.put(e.getKategorie(),v);
                        }
                    }
                }

            }
        }

        i = categoryList.iterator();
        int counter = 0;
        while(i.hasNext()) {//Für jeden Eintrag
            Category c = i.next();

            int wert = ((Double)value.get(c.getName())).intValue();
            if(wert>0) {
                Log.d(TAG,"Kategorie: "+c.getName()+" Wert: "+wert);
                entries.add(new BarEntry(wert, counter));
                PieEntryLabels.add(c.getName());
            }
            else
                value.remove(c.getName());
            counter++;
        }

        pieDataSet = new PieDataSet(entries, "");

        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(new int[] { Color.parseColor("#5c759a"), Color.parseColor("#695c9a"), Color.parseColor("#8e5c9a"), Color.parseColor("#9a5c82"),Color.parseColor("#9a5c5c"),Color.parseColor("#9a825c"),Color.parseColor("#8e9a5c"), Color.parseColor("#699a5c")});
        pieDataSet.setValueTextSize(16f);

        pieChart.setData(pieData);

        pieChart.animateY(3000);
        pieChart.setHoleColor(Color.parseColor("#85a8e5"));
        pieChart.setDescription("Ausgaben in €");


    }

    @Override
    public void onRestart() {
        super.onRestart();
        addChart();
    }


}