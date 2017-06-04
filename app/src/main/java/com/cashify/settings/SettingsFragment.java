package com.cashify.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;


// Lightweight fragment version of Main activity
// To get views, we must work with getActivity() here, otherwise explodes with null pointer exceptions.
public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference.OnPreferenceChangeListener changeText;

    private TextView totalAmountView;       // View that display combined sum of income and expenses
    private SharedPreferences sharedPref;   // Computation needs that for some reason
    private DatabaseHelper dbHelper;        // Database helper object to retrieve database entries

    private String timeStr;

    // Context for Database helper (and others) provided through onAttach event
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbHelper = new DatabaseHelper(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.timeStr = sharedPref.getString("zeit","");
        //addPreferencesFromResource(R.xml.preference);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    // On View instantiation, load and return fragment layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_einstellungen, container, false);

    }

    // Once we are ready to go, set up event logic here and display stuff
    @Override
    public void onStart() {
        super.onStart();
    }
}
