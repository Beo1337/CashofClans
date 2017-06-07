package com.cashify.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;
import com.cashify.overview.Entry;
import com.cashify.overview.MarshMallowPermission;
import com.cashify.overview.OverviewManager;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsActivity";
    private DatabaseHelper myDb;        // Database helper object to retrieve database entries
    private SharedPreferences sharedPref;   // Shared preferences
    /**Über den Manager werden die Einträge aus der Datenbank ausgelesen.*/
    private OverviewManager manager;
    /**Wert für die Datenbankabfrage*/
    String category;

    // Context for Database helper (and others) provided through onAttach event
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myDb = new DatabaseHelper(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }

    // Once we are ready to go, set up event logic here and display stuff
    @Override
    public void onStart() {
        super.onStart();

        final EditText start_editText = (EditText) getActivity().findViewById(R.id.start_editText);
        final EditText end_editText = (EditText) getActivity().findViewById(R.id.end_editText);

        Button btn_1_month;
        Button btn_3_months;
        Button btn_12_months;

        final Spinner spin;

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Cursor von der Kategorietabelle holen.
        Cursor c = myDb.getReadableDatabase().rawQuery("SELECT ID AS _id, NAME FROM category", null);
        Log.i("AddActivity", "CURSOR COUNT: " + c.getCount());
        //Adapter aus dem Cursor erstellen.
        String[] from = new String[]{"NAME"};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, c, from, to);

        //Vordefiniertes Layout dem Spinner zuweisen.
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin = (Spinner) getActivity().findViewById(R.id.category_spinner);
        spin.setAdapter(sca);

        /*Workaround für die Speicherung des gewählten Kategorienamens als String*/
        final EditText editText_category = (EditText) getActivity().findViewById(R.id.editText3);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editText_category.setText(((Cursor) spin.getItemAtPosition(position)).getString(1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category = editText_category.getText().toString();
        editText_category.setText(this.category);

        Button button_export = (Button) getActivity().findViewById(R.id.button_export);
        button_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData(null, null, category);
            }
        });


        /*------------------3 MONTHS------------------*/
        btn_1_month = (Button) getActivity().findViewById(R.id.months_1_btn);
        btn_1_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = new GregorianCalendar();
                c.setTimeInMillis(System.currentTimeMillis());

                String dateUntil = sdf.format(c.getTime());
                c.add(Calendar.MONTH, -1);
                String dateFrom = sdf.format(c.getTime());

                start_editText.setText(dateFrom);
                end_editText.setText(dateUntil);
            }
        });


        /*------------------6 MONTHS------------------*/
        btn_3_months = (Button) getActivity().findViewById(R.id.months_3_btn);
        btn_3_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = new GregorianCalendar();
                c.setTimeInMillis(System.currentTimeMillis());

                String dateUntil = sdf.format(c.getTime());
                c.add(Calendar.MONTH, -3);
                String dateFrom = sdf.format(c.getTime());

                start_editText.setText(dateFrom);
                end_editText.setText(dateUntil);
            }
        });

        /*------------------12 MONTHS------------------*/
        btn_12_months = (Button) getActivity().findViewById(R.id.months_12_btn);
        btn_12_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = new GregorianCalendar();
                c.setTimeInMillis(System.currentTimeMillis());

                String dateUntil = sdf.format(c.getTime());
                c.add(Calendar.MONTH, -12);
                String dateFrom = sdf.format(c.getTime());

                start_editText.setText(dateFrom);
                end_editText.setText(dateUntil);
            }
        });
    }


    /**Diese Methode schreibt die Einträge der Tabell in ein CSV-File*/
    public void exportData(String from, String to, String category) {
        DatabaseHelper myDb = new DatabaseHelper(getContext());
        manager = new OverviewManager(myDb);

        MarshMallowPermission mmp = new MarshMallowPermission(DetailsFragment.this.getActivity());
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
                List<Entry> ent = manager.getEntriesWithinDateAndCategory(from, to, category);


                String header[] = {"ID", "Titel", "Betrag", "Kategorie", "Datum"};//Überschriften des CSV-Files

                csvWrite.writeNext(header);

                //CSV-File mit Einträgen befüllen
                Set<Entry> set = myDb.getEntriesWithinDateAndCategory(null,null,category);
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
