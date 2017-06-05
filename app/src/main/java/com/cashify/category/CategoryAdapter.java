package com.cashify.category;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashify.R;

import static android.content.ContentValues.TAG;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private CategoryManager manager;

    // ViewHolder wraps the view that we want to pass to the RecyclerView,
    // we only needs this because RecyclerView.ViewHolder is abstract
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public CategoryAdapter(CategoryManager manager) {
        this.manager = manager;
    }

    // Generates a new ViewHolder and preloads a layout
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_category_elem, parent, false);
        return new ViewHolder(v);
    }

    // Once the ViewHolder is bound, populate the view it contains with data and event action code
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Category cat = manager.getCategoryByIndex(position);
        TextView textView = (TextView) holder.view.findViewById(R.id.category_label);
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
        //Wenn lange auf einen Eintrag gedrückt wird
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                AlertDialog.Builder optionsDialog = new AlertDialog.Builder(holder.view.getContext());
                optionsDialog.setTitle("Bitte Option auswählen")
                        .setItems(
                                R.array.options_without_pic,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

                                        switch (item) {
                                            case 0:
                                                Intent i = new Intent(holder.view.getContext(), ChangeCategoryActivity.class);
                                                i.putExtra("id", "" + String.valueOf(cat.getId()));
                                                i.putExtra("name", cat.getName());
                                                holder.view.getContext().startActivity(i);
                                                break;
                                            case 1:
                                                AlertDialog diaBox = AskOption(holder.view.getContext(), cat);
                                                diaBox.show();
                                                break;
                                        }
                                    }
                                })
                        .create()
                        .show();

                return false;
            }
        });
    }

    public int getItemCount() {
        return manager.getCount();
    }

    /**
     * Diese Methode liefert einen Abfragedialog bevor das Löschen durchgeführt wird.
     */
    private AlertDialog AskOption(Context context, final Category cat) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.diag_title_category_delete)
                .setMessage(R.string.diag_text_category_delete)
                .setIcon(R.drawable.delete_x)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        manager.removeCategory(cat.getId());
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

}
