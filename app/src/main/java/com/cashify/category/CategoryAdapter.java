package com.cashify.category;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashify.R;
import com.cashify.base.ViewHolder;

import static android.content.ContentValues.TAG;

// Category adapter interfaces between presentation layer and model
// - Generates singular view elements for each item that is currently visible on screen
// - Takes data from a CategoryManager, which for all practical purposes acts as a singleton

public class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {

    private CategoryManager manager;

    public CategoryAdapter(CategoryManager manager) {
        this.manager = manager;
    }


    // Generates a new ViewHolder and preloads a layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_category_elem, parent, false);
        return new ViewHolder(v);
    }

    // Once the ViewHolder is bound, populate the view it contains with data and event action code
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category cat = manager.getCategoryByIndex(position);
        TextView textView = (TextView) holder.getView().findViewById(R.id.category_label);
        textView.setText(cat.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), com.cashify.category.CategoryDetailActivity.class);
                intent.putExtra("categoryId", cat.getId());
                Log.i(TAG, "onBindViewHolder: " + cat.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return manager.getCount();
    }

}
