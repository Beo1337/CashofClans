package com.cashify.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.seps.cashofclans.R;

// Category adapter interfaces between presentation layer and model
// TODO: revisit 15 APR 2017 at the latest
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private CategoryManager manager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    public CategoryAdapter(CategoryManager manager) {
        this.manager = manager;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: this generates a shitty view, read up on this and fix
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_category_elem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category cat = manager.getCategoryByIndex(position);
        //TODO hab die vorhandene Categoryklasse genommen, muss hier eventuel angepasst werden. Lg Simon
        //holder.textView.setText(cat.getName());
    }

    public int getItemCount() {
        return manager.getCount();
    }
}
