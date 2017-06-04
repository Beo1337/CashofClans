package com.cashify.overview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cashify.R;
import com.cashify.category.Category;
import com.cashify.database.DatabaseHelper;
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

public class OverviewFragment extends Fragment {

    private static final String TAG = "StatistikActivity";
    private TextView totalAmountView;       // View that display combined sum of income and expenses
    private DatabaseHelper dbHelper;        // Database helper object to retrieve database entries
    private SharedPreferences sharedPref;   // Shared preferences, do we really need these here?
    private PieChart pieChart;              // Fancy pie chart!!

    // Context for Database helper (and others) provided through onAttach event
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbHelper = new DatabaseHelper(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // On View instantiation, load and return fragment layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    // Once we are ready to go, set up event logic here and display stuff
    @Override
    public void onStart() {
        super.onStart();

        Button listButton;

        listButton = (Button) getActivity().findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OverviewListActivity.class);
                OverviewFragment.this.startActivity(i);
            }
        });

        pieChart = (PieChart) getActivity().findViewById(R.id.chart);
        addChart();
    }

    public void addChart(){
        List<Entry> entries = new ArrayList<>();
        List<String> PieEntryLabels = new ArrayList<String>();
        List<Category> categoryList = new LinkedList<>();
        List<com.cashify.overview.Entry> entryList = new LinkedList<>();
        HashMap<String,Object> sumMap = new HashMap<String,Object>();
        int counter = 0;

        categoryList.addAll(dbHelper.getCategories());
        entryList.addAll(dbHelper.getEntries());

        Collections.sort(categoryList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        String time = sharedPref.getString("zeit","");

        for(com.cashify.overview.Entry e : entryList) {
            if(e.getAmount()<0)
            {
                String catName = e.getCategory().getName();
                if(!sumMap.containsKey(catName)) sumMap.put(catName, 0.0);
                double v = (Double) sumMap.get(e.getCategory().getName());
                v += (e.getAmount()*-1);
                sumMap.put(e.getCategory().getName(),v);
            }
        }

        for(String catName : sumMap.keySet()) {
            double val = (double) sumMap.get(catName);
            if(val>0.0) {
                Log.d(TAG,"Kategorie: "+catName+" Wert: "+val);
                entries.add(new BarEntry(
                        (int) val,
                        counter++
                ));
                PieEntryLabels.add(catName);
            }
            else
                sumMap.remove(catName);
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(PieEntryLabels, pieDataSet);
        pieDataSet.setColors(new int[] {
                Color.parseColor("#5c759a"),
                Color.parseColor("#695c9a"),
                Color.parseColor("#8e5c9a"),
                Color.parseColor("#9a5c82"),
                Color.parseColor("#9a5c5c"),
                Color.parseColor("#9a825c"),
                Color.parseColor("#8e9a5c"),
                Color.parseColor("#699a5c")
        });
        pieDataSet.setValueTextSize(16f);
        pieChart.setData(pieData);
        pieChart.animateY(1000);
        pieChart.setHoleColor(Color.parseColor("#85a8e5"));
        pieChart.setDescription("Ausgaben in €");
    }
}