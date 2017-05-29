package com.example.seps.cashofclans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


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
        this.waehrung = sharedPref.getString("waehrung","");

        final Preference prefer1 = findPreference("zeit");
        final Preference prefer2 = findPreference("waehrung");

        prefer1.setSummary(this.time);
        prefer2.setSummary(this.waehrung);


        this.changeText = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                final String value = (String) newVal;
                preference.setSummary(value);
                return true;
            }

        };

        prefer1.setOnPreferenceChangeListener(this.changeText);
        prefer2.setOnPreferenceChangeListener(this.changeText);
    }




}
