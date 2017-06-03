package com.cashify;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.cashify.add.AddActivity;
import com.cashify.monthly_entries.DauerauftraegeService;
import com.cashify.overview.Entry;
import com.cashify.settings.EinstellungenActivity;
import com.cashify.tabmain.TabMainActivity;
import com.cashify.database.DatabaseHelper;
import com.cashify.database.ShowDataBaseActivity;
import com.example.seps.cashofclans.Overview.OverviewListActivity;
import com.cashify.overview.StatistikActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Die MainActivity wird aufgerufen sobald die App gestartet wird.
 * Von ihr aus kann dann zu den anderen Funktionalitäten der App navigiert werden.
 *
 * */
public class MainActivity extends AppCompatActivity {

    //TODO ändern von Kategorien
    //TODO Bilder der Kategorie anpassen (Nur Bierglas überall)
    //TODO Dialogabfrage vor löschen bei Kategorie

    //TODO Diagramme Ausgaben vs. Einnahemn usw.
    //TODO CSV Export
    //TODO Anzeige je nach eingegebener Zeit
    //TODO Methoden mit ID nicht Strings
    //TODO Strings.xml
    //TODO Prepared Statements



    /**Über den DatabaseHelper können sämtliche Datenbankfunktionen abgerufen werden.*/
    private DatabaseHelper myDb;
    /**Stellt den Saldo der Einnahmen und Ausgaen auf der Startseite dar.*/
    private TextView betrag;
    /**Diese Liste enthält alle Einträge aus der Datenbank.*/
    private static List<Entry> entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("START MainActivity","Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        //Nach dem Erstellen soll der Saldo berechnet und ausgegeben werden.
        refresh_money();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //Wird die App wieder in den Vordergrund gebracht, soll der Saldo neu berechnet werden, da Änderungen an der Datenbank durchgeführt worden sein konnten.
        refresh_money();
    }

    /**Diese Methode aktualisiert den Kontostand auf der Startseite*/
    public void refresh_money() {

        /**Die SharedPrefence wird benötigt um die Zeit für die relevanten Buchungen zu bekommen.*/
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        /**Zeitintervall aus der SharedPreference.*/
        String time = sharedPref.getString("zeit","");
        /**Zeitformat des Datums der Einträge.*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        double summe = 0;

        Log.d("MainActivity",time);

        //Liste aus der Datenbank befüllen.
        if (entryList == null)
            entryList = new LinkedList<>();
        entryList.clear();
        entryList.addAll(myDb.getEntries());

        Iterator<Entry> i = entryList.iterator();
        while(i.hasNext()) {//Für jeden Eintrag
            Entry e = i.next();

            if(time.equals("Alle"))
            {
                summe += e.getAmount();
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
                    if (d.getMonth() == a.getMonth())
                        summe += e.getAmount();
                }
                else if(time.equals("Jahr")){
                    if(d.getYear()==a.getYear())
                        summe += e.getAmount();
                }
                else
                {
                    summe += e.getAmount();
                }
            }
        }

        betrag= (TextView) findViewById(R.id.Money);
        Log.i("MainActivity","Betrag: "+summe);
        summe = Math.round(summe*100)/100.0;
        betrag.setText(String.valueOf(summe)+ "€");
        if(summe<0)
            betrag.setTextColor(Color.RED);
        else
            betrag.setTextColor(Color.GREEN);
    }

    /***************OnClick-Funktionen***************/

    public void einstellungen(View v) {
        Intent i = new Intent(v.getContext(), EinstellungenActivity.class);
        startActivityForResult(i, 0);
    }

    public void add(View v) {
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "add");
        startActivityForResult(i, 0);
    }

    public void sub(View v){
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        startActivityForResult(i, 0);
    }

    public void shortcut(View v){
        ImageButton b = (ImageButton) findViewById(v.getId());
        Log.i("MainActivity","Shortcut: "+b.getTag().toString()+" gewählt");
        Intent i = new Intent(v.getContext(), AddActivity.class);
        i.putExtra("mode", "sub");
        i.putExtra("cat",b.getTag().toString());
        startActivityForResult(i, 0);
    }

    public void stats(View v) {
        Intent i = new Intent(v.getContext(), StatistikActivity.class);
        startActivityForResult(i, 0);
    }

    public void  main(View v){

    }

    public void db(View v){
        Intent i = new Intent(v.getContext(), ShowDataBaseActivity.class);
        startActivityForResult(i, 0);
    }

    public void list(View v){
        Intent i = new Intent(v.getContext(),OverviewListActivity.class);
        startActivityForResult(i,0);
    }

    public void tabmain(View v) {
        Intent i = new Intent(v.getContext(), TabMainActivity.class);
        startActivityForResult(i, 0);
    }
    public void checkService(View v){
        if(isMyServiceRunning(DauerauftraegeService.class))
            Toast.makeText(this, "Service da!!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Service nicht da!!", Toast.LENGTH_LONG).show();
    }

    /*********************************************************/

    /**Diese Methode checkt ob der Service für die monatlichen Einträge gestartet ist.*/
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
