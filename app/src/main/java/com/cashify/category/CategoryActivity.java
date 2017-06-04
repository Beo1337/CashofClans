package com.cashify.category;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;

// CategoryActivity
// primarily provides a scrolling list of all categories.
// * Functionality:
// - add new categories
// (- rename categories through CategoryDetailActivity - to be added)
// - delete categories through CategoryDetailActivity

public class CategoryActivity extends AppCompatActivity implements CategoryAddFragment.Listener {

    private RecyclerView catRecycleView;
    private Toolbar toolbar;
    private CategoryAdapter adapter;
    private CategoryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        catRecycleView = (RecyclerView) findViewById(R.id.category_list);
        toolbar = (Toolbar) findViewById(R.id.category_toolbar);

        manager = new CategoryManager(new DatabaseHelper(this));
        adapter = new CategoryAdapter(manager);

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
        actionbar.setTitle(R.string.title_activity_category);
    }


    // Resume behaviour

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
        String toastMsg = getString(success ? R.string.msg_category_added : R.string.msg_category_add_failed);
        if (success) adapter.notifyDataSetChanged();
        Toast.makeText(CategoryActivity.this, toastMsg, Toast.LENGTH_LONG).show();
    }
}
