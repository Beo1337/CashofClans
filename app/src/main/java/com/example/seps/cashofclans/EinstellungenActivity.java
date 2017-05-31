package com.example.seps.cashofclans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;


import com.example.seps.cashofclans.Overview.StatistikActivity;

import static android.provider.SyncStateContract.Helpers.update;

public class EinstellungenActivity extends PreferenceActivity {

    private SharedPreferences sharedPref;

    private Preference.OnPreferenceChangeListener changeText;

    private String time;
    private String waehrung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);


        this.time = sharedPref.getString("zeit","");

        final Preference prefer1 = findPreference("zeit");

        prefer1.setSummary(this.time);


        this.changeText = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                final String value = (String) newVal;
                preference.setSummary(value);
                return true;
            }

        };

        prefer1.setOnPreferenceChangeListener(this.changeText);
    }

    public void stats(View v) {
        Intent i = new Intent(v.getContext(), StatistikActivity.class);
        startActivityForResult(i, 0);
    }

    public void  main(View v){
        Intent i = new Intent(v.getContext(),MainActivity.class);
        startActivityForResult(i,0);
    }

    public void einstellungen(View v) {
        Intent i = new Intent(v.getContext(), EinstellungenActivity.class);
        startActivityForResult(i, 0);
    }



}
