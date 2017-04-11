package com.example.seps.cashofclans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cashify.category.CategoryAdapter;
import com.cashify.category.CategoryManager;

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
        setContentView(R.layout.content_category);

        catRecycleView = (RecyclerView) findViewById(R.id.category_list);
        catRecycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        catRecycleView.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(new CategoryManager());
        catRecycleView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                catRecycleView.getContext(),
                layoutManager.getOrientation());
        catRecycleView.addItemDecoration(dividerItemDecoration);
    }
}
