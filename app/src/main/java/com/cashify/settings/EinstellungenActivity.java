package com.cashify.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.cashify.R;

/**
 * Diese Klasse stellt die in den Einstelungen gemachten Werte dar und kümmert sich um Änderungen dieser.
 */
public class EinstellungenActivity extends PreferenceActivity {

    /**
     * Über die sharedPreference können Einstellungen überall in der App gesetzt und ausglesen werden.
     */
    private SharedPreferences sharedPref;
    /**
     * Dieser Listener hört auf Änderungen der Einstellungen.
     */
    private Preference.OnPreferenceChangeListener changeText;
    /**
     * Dieser String repräsentiert eine Zeitperiode.
     */
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        //Die SharedPreferences der App auslesen
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Die Einstellung zeit aus den SharedPrefernces holen und in die View setzten.
        this.time = sharedPref.getString("zeit", "");

        final Preference prefer1 = findPreference("zeit");

        prefer1.setSummary(this.time);

        //Wenn die Einstellung vom User geändert wird, wird diese zurück in de SharedPreference geschrieben.
        this.changeText = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newVal) {
                final String value = (String) newVal;
                preference.setSummary(value);
                return true;
            }

        };

        //Listener auf die Einstellungsview der Zeiteinheit registrieren.
        prefer1.setOnPreferenceChangeListener(this.changeText);
    }


}
