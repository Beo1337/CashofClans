package com.cashify.overview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cashify.R;
import com.cashify.base.MoneyHelper;
import com.cashify.base.Refreshable;
import com.cashify.category.Category;
import com.cashify.database.DatabaseHelper;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class OverviewFragment extends Fragment implements Refreshable {

    private static final String TAG = "StatistikActivity";
    private TextView totalAmountView;       // View that display combined sum of income and expenses
    private DatabaseHelper dbHelper;        // Database helper object to retrieve database entries
    private SharedPreferences sharedPref;   // Shared preferences
    private PieChart pieChart;              // Fancy pie chart!!
    /**Über den Manager werden die Einträge aus der Datenbank ausgelesen.*/
    private OverviewManager manager;

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
        Button csvButton;

        listButton = (Button) getActivity().findViewById(R.id.list_button);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OverviewListActivity.class);
                OverviewFragment.this.startActivity(i);
            }
        });

        csvButton = (Button) getActivity().findViewById(R.id.csv_button);
        csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData();
            }
        });

        pieChart = (PieChart) getActivity().findViewById(R.id.chart);
        addChart();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void refresh() {
        addChart();
    }

    public void addChart() {
        List<Entry> entries = new ArrayList<>();
        List<String> PieEntryLabels = new ArrayList<String>();
        List<com.cashify.overview.Entry> entryList = new LinkedList<>();
        HashMap<String, Object> sumMap = new HashMap<String, Object>();
        int counter = 0;

        entryList.addAll(MoneyHelper.filter(dbHelper.getEntries()));

        String time = sharedPref.getString("zeit", "");

        for (com.cashify.overview.Entry e : entryList) {
            if (e.getAmount() < 0) {
                String catName = e.getCategory().getName();
                if (!sumMap.containsKey(catName)) sumMap.put(catName, 0.0);
                double v = (Double) sumMap.get(e.getCategory().getName());
                v += (e.getAmount() * -1);
                sumMap.put(e.getCategory().getName(), v);
            }
        }

        for (String catName : sumMap.keySet()) {
            double val = (double) sumMap.get(catName);
            if (val > 0.0) {
                Log.d(TAG, "Kategorie: " + catName + " Wert: " + val);
                entries.add(new BarEntry(
                        (int) val,
                        counter++
                ));
                PieEntryLabels.add(catName);
            } else
                sumMap.remove(catName);
        }

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        PieData pieData = new PieData(PieEntryLabels, pieDataSet);
        pieDataSet.setColors(new int[]{
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

    /**Diese Methode schreibt die Einträge der Tabell in ein CSV-File*/
    public void exportData() {
        DatabaseHelper myDb = new DatabaseHelper(getContext());
        manager = new OverviewManager(myDb);

        MarshMallowPermission mmp = new MarshMallowPermission(OverviewFragment.this.getActivity());
        if (!mmp.checkPermissionForExternalStorage())//Wenn die Berechtigung noch nicht vorhanden ist
            mmp.requestPermissionForExternalStorage();
        else {//Wenn die Berechtigung schon vorhanden ist
            File exportDir = new File(Environment.getExternalStorageDirectory(), "Cashify");
            if (!exportDir.exists()) {
                exportDir.mkdirs();//Ordner Cashify anlegen wenn er noch nicht existiert.
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH mm ss");
            String strDate = sdf.format(new Date());
            File file = new File(exportDir, "Chashify_Export_" + strDate + ".csv");

            try {
                file.createNewFile();//neues CSV-File erstellen

                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                List<com.cashify.overview.Entry> ent = manager.getEntries();


                String header[] = {"ID", "Titel", "Betrag", "Kategorie", "Datum"};//Überschriften des CSV-Files

                csvWrite.writeNext(header);

                //CSV-File mit Einträgen befüllen
                Set<com.cashify.overview.Entry> set = myDb.getEntries();
                for (com.cashify.overview.Entry e : set) {
                    String[] text = {String.valueOf(e.getId()), e.getTitle(), String.valueOf(e.getAmount()), e.getCategory().getName(), e.getDatum()};
                    csvWrite.writeNext(text);
                }
                Toast.makeText(getContext(), "File erstellt /Cashify/Chasify_Export.csv", Toast.LENGTH_SHORT).show();
                csvWrite.close();
                Log.d("OverviewFragment", "Fertig mit Export!!");
            } catch (SQLException sqlEx) {
                Log.e("OverviewFragment", sqlEx.getMessage(), sqlEx);
            } catch (IOException e) {
                Log.e("OverviewFragment", e.getMessage(), e);
            }
        }
    }

}