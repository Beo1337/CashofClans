package com.cashify.category;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.seps.cashofclans.EinstellungenActivity;
import com.example.seps.cashofclans.R;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

// Category adapter interfaces between presentation layer and model
// TODO: revisit 15 APR 2017 at the latest
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private CategoryManager manager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public CategoryAdapter(CategoryManager manager) {
        this.manager = manager;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: this generates a shitty view, read up on this and fix
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_category_elem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category cat = manager.getCategoryByIndex(position);
        TextView textView = (TextView) holder.view.findViewById(R.id.category_label);
        textView.setText(cat.getCategoryName() + cat.getId());
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
