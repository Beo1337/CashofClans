package com.cashify.overview;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;

/**
 * Diese Klasse stelt die Einträge dar, und bietet eine Möglichkeit neue Einträge hinzuzufügen.
 */
public class OverviewListActivity extends AppCompatActivity {

    private RecyclerView catRecycleView;
    /**Toolbar für den hinzufügen-Button*/
    private Toolbar toolbar;
    /**Dieser Adapter listed die einzelnen Einträge aus der Datenbank auf.*/
    private OverviewAdapter adapter;
    /**Über den Manager werden die Daten aus der Datenbank ausgelesen.*/
    private OverviewManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_recycler);

        catRecycleView = (RecyclerView) findViewById(R.id.overview_list);
        toolbar = (Toolbar) findViewById(R.id.overview_toolbar);

        manager = new OverviewManager(new DatabaseHelper(this));
        adapter = new OverviewAdapter(manager);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        catRecycleView.setHasFixedSize(true);
        catRecycleView.setLayoutManager(layoutManager);
        catRecycleView.setAdapter(adapter);

        catRecycleView.addItemDecoration(
                new DividerItemDecoration(
                        catRecycleView.getContext(),
                        layoutManager.getOrientation()
                )
        );

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.title_activity_overview);
    }

    /**Wird die Aktivity aus dem Hintergrund wieder in den Vordergrund gebarcht, soll der Adapter aktualisert werden da Änderungen an den Datensätzen vorgenommen werden konnten.*/
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("OverviewListActivity", "DataSetChanged");
        adapter.notifyDataSetChanged();
    }

}
