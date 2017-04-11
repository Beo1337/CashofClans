package com.cashify.category;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.cashify.category.CategoryAdapter;
import com.cashify.category.CategoryManager;
import com.example.seps.cashofclans.R;

// Category view consists rn of a RecycleView and a non-functional floating add button.
// Provided functionality:
// - add new categories
// - rename categories
// - delete categories
// See specification
// TODO: revisit 15 APR 2017 at the latest
public class CategoryActivity extends AppCompatActivity {

    private RecyclerView catRecycleView;
    private LinearLayoutManager layoutManager;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        layoutManager = new LinearLayoutManager(this);
        adapter = new CategoryAdapter(new CategoryManager());

        catRecycleView = (RecyclerView) findViewById(R.id.category_list);
        catRecycleView.setHasFixedSize(true);
        catRecycleView.setLayoutManager(layoutManager);
        catRecycleView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                catRecycleView.getContext(),
                layoutManager.getOrientation());

        catRecycleView.addItemDecoration(dividerItemDecoration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.title_activity_category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }
}
