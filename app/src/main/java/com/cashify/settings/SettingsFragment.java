package com.cashify.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.cashify.R;


// Lightweight fragment version of Main activity
// To get views, we must work with getActivity() here, otherwise explodes with null pointer exceptions.

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
    }
}
