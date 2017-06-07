package com.cashify.monthly_entries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;

/**
 * Diese Klasse stelt die monatlichen Einträge dar, und bietet eine Möglichkeit neue Einträge hinzuzufügen.
 */
public class MonthlyEntryActivity extends AppCompatActivity {

    private RecyclerView catRecycleView;
    /**Toolbar für den hinzufügen-Button*/
    private Toolbar toolbar;
    /**Dieser Adapter listed die einzelnen Einträge aus der Datenbank auf.*/
    private MonthlyEntryAdapter adapter;
    /**Über den Manager werden die Daten aus der Datenbank ausgelesen.*/
    private MonthlyEntryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_recycler);

        catRecycleView = (RecyclerView) findViewById(R.id.overview_list);
        toolbar = (Toolbar) findViewById(R.id.overview_toolbar);

        manager = new MonthlyEntryManager(new DatabaseHelper(this));
        adapter = new MonthlyEntryAdapter(manager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        catRecycleView.setHasFixedSize(true);
        catRecycleView.setLayoutManager(layoutManager);
        //Die RecyclerView mit den Werten aus dem Adater befüllen.
        catRecycleView.setAdapter(adapter);

        catRecycleView.addItemDecoration(
                new DividerItemDecoration(
                        catRecycleView.getContext(),
                        layoutManager.getOrientation()
                )
        );

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
    }

    /**Diese  Methode initialisert das Contextmenu das durch drücken des + Buttons in der Titelleiste aufgerufen wird.*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.monthly_entry_menu, menu);
        return true;
    }

    /**Wenn eine der Optionen des Contexmenüs ausgewählt wurde, wird die entsprechende Aktion ausgeführt.*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_monthly_entry_add: {
                Intent i = new Intent(this, AddMonthlyEntryActivity.class);
                startActivityForResult(i, 0);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**Wird die Aktivity aus dem Hintergrund wieder in den Vordergrund gebarcht, soll der Adapter aktualisert werden da Änderungen an den Datensätzen vorgenommen werden konnten.*/
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("MonthlyEntryAdapter", "DataSetChanged");
        adapter.notifyDataSetChanged();
    }


}
