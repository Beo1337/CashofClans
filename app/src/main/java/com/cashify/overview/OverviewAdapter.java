package com.cashify.overview;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashify.category.Category;
import com.cashify.category.CategoryManager;
import com.example.seps.cashofclans.R;

import static android.content.ContentValues.TAG;

// Category adapter interfaces between presentation layer and model
// - Generates singular view elements for each item that is currently visible on screen
// - Takes data from a Manager, which for all practical purposes acts as a singleton

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    // ViewHolder wraps the view that we want to pass to the RecyclerView,
    // we only needs this because RecyclerView.ViewHolder is abstract
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public OverviewAdapter() {}


    // Generates a new ViewHolder and preloads a layout
    @Override
    public OverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_overview_elem, parent, false);
        return new ViewHolder(v);
    }

    // Once the ViewHolder is bound, populate the view it contains with data and event action code
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView entryText = (TextView) holder.view.findViewById(R.id.entry_text);
        TextView entryAmount = (TextView) holder.view.findViewById(R.id.entry_amount);
        TextView entryCategory = (TextView) holder.view.findViewById(R.id.entry_category);
        TextView entryDate = (TextView) holder.view.findViewById(R.id.entry_date);
    }

    public int getItemCount() {
        return 20;
    }
}
