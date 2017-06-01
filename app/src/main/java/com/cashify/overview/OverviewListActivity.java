package com.cashify.overview;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cashify.category.CategoryAdapter;
import com.cashify.category.CategoryManager;
import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.R;

public class OverviewListActivity extends AppCompatActivity {

    private RecyclerView catRecycleView;
    private Toolbar toolbar;
    private OverviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_recycler);

        catRecycleView = (RecyclerView) findViewById(R.id.overview_list);
        toolbar = (Toolbar) findViewById(R.id.overview_toolbar);

        adapter = new OverviewAdapter();

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
}
