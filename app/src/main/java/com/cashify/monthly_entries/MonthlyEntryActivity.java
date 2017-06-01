package com.cashify.monthly_entries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

public class MonthlyEntryActivity extends AppCompatActivity {

    private RecyclerView catRecycleView;
    private Toolbar toolbar;
    private MonthlyEntryAdapter adapter;
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
        catRecycleView.setAdapter(adapter);

        catRecycleView.addItemDecoration(
                new DividerItemDecoration(
                        catRecycleView.getContext(),
                        layoutManager.getOrientation()
                )
        );

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("CHANGE THIS");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.monthly_entry_menu, menu);
        return true;
    }

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

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("MonthlyEntryAdapter","DataSetChanged");
        adapter.notifyDataSetChanged();
    }







}
