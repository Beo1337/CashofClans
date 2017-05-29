package com.cashify.category;

import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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

import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.R;

import static android.content.ContentValues.TAG;

// Category view consists rn of a RecycleView and a non-functional floating add button.
// Provided functionality:
// - add new categories
// - rename categories
// - delete categories
// See specification
// TODO: revisit 15 APR 2017 at the latest
public class CategoryActivity extends AppCompatActivity implements CategoryAddFragment.Listener {

    private RecyclerView catRecycleView;
    private Toolbar toolbar;
    private LinearLayoutManager layoutManager;
    private CategoryAdapter adapter;
    private CategoryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catRecycleView = (RecyclerView) findViewById(R.id.category_list);
        toolbar = (Toolbar) findViewById(R.id.category_toolbar);

        layoutManager = new LinearLayoutManager(this);
        manager = new CategoryManager(new DatabaseHelper(this));
        adapter = new CategoryAdapter(manager);

        catRecycleView.setHasFixedSize(true);
        catRecycleView.setLayoutManager(layoutManager);
        catRecycleView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                catRecycleView.getContext(),
                layoutManager.getOrientation()
        );
        catRecycleView.addItemDecoration(dividerItemDecoration);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.title_activity_category);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_category_add:
                DialogFragment addFragment = new CategoryAddFragment();
                addFragment.show(getSupportFragmentManager(), "category_add_diag");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCategoryAdd(String categoryName) {
        boolean success = manager.addCategory(categoryName);
        //TODO snackbaar
        adapter.notifyDataSetChanged();
    }
}
