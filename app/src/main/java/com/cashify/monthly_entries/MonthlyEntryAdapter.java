package com.cashify.monthly_entries;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashify.R;

/**
 * Diese Klasse befüllt den Viewholder mit den einzelnen Einträgen aus der Datenbank.
 */
public class MonthlyEntryAdapter extends RecyclerView.Adapter<MonthlyEntryAdapter.ViewHolder>{

    /**Über den Manager können die monatlichen Einträge aus der Datenbank geholt werden. */
    private MonthlyEntryManager manager;

    /**Diese Methode liefert den ViewHolder welcher mit den Daten aus der Datenbank befüllt wurde*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MonthlyEntryAdapter(MonthlyEntryManager manager) {this.manager = manager;}


    // Generates a new ViewHolder and preloads a layout
    @Override
    public MonthlyEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_monthly_entry_elem, parent, false);
        return new ViewHolder(v);
    }

    // Once the ViewHolder is bound, populate the view it contains with data and event action code
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        //Felder des Eintrags holen
        TextView entryText = (TextView) holder.view.findViewById(R.id.entry_text);
        TextView entryAmount = (TextView) holder.view.findViewById(R.id.entry_amount);
        TextView entryCategory = (TextView) holder.view.findViewById(R.id.entry_category);
        TextView entryDate = (TextView) holder.view.findViewById(R.id.entry_date);

        //Felder befüllen
        MonthlyEntry ent = manager.getMonthlyEntryByIndex(position);

        entryText.setText(ent.getTitle());
        if(ent.getAmount()<0)
            entryAmount.setTextColor(Color.RED);
        else
            entryAmount.setTextColor(Color.GREEN);
        entryAmount.setText(""+ent.getAmount()+"");
        entryCategory.setText(ent.getCategory());
        entryDate.setText("Monatstag: "+ent.getTag());

        //Wenn lange auf einen Eintrag gedrückt wird
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                //Es wird ein Dialog mit den
                AlertDialog.Builder optionsDialog = new AlertDialog.Builder(holder.view.getContext());
                optionsDialog.setTitle("Bitte Option auswählen").setItems(
                    R.array.options_without_pic, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            item++;

                            if (item == 1) {//Bearbeiten
                                //TODO Bearbeiten


                            } else if (item == 2) {//Löschen
                                manager.removeMonthlyEntry(ent.getId());
                                notifyDataSetChanged();
                            }
                        }
                    });

                optionsDialog.create();
                optionsDialog.show();

                return false;
            }
        });
    }

    /**Diese Methode liefert die Anzahl an Items in dem Viewholder.*/
    public int getItemCount() {
        return manager.getCount();
    }

}
