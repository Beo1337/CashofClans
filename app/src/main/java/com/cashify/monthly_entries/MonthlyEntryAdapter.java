package com.cashify.monthly_entries;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cashify.R;
import com.cashify.base.ViewHolder;

/**
 * Diese Klasse befüllt den Viewholder mit den einzelnen Einträgen aus der Datenbank.
 */
public class MonthlyEntryAdapter extends RecyclerView.Adapter<ViewHolder> {

    /**Über den Manager können die monatlichen Einträge aus der Datenbank geholt werden.*/
    private MonthlyEntryManager manager;
    /** Context für die Farbe der Beträge*/
    private Context context;

    public MonthlyEntryAdapter(MonthlyEntryManager manager, Context context) {
        this.manager = manager;
        this.context = context;

    }


    // Generates a new ViewHolder and preloads a layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_monthly_entry_elem, parent, false);
        return new ViewHolder(v);
    }

    // Once the ViewHolder is bound, populate the view it contains with data and event action code
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final View view = ((ViewHolder) holder).getView();

        //Felder des Eintrags holen
        TextView entryText = (TextView) view.findViewById(R.id.entry_text);
        TextView entryAmount = (TextView) view.findViewById(R.id.entry_amount);
        TextView entryCategory = (TextView) view.findViewById(R.id.entry_category);
        TextView entryDate = (TextView) view.findViewById(R.id.entry_date);

        //Felder befüllen
        final MonthlyEntry ent = manager.getMonthlyEntryByIndex(position);

        entryText.setText(ent.getTitle());
        if (ent.getAmount() < 0)
            entryAmount.setTextColor(this.context.getResources().getColor(R.color.colorRed));
        else
            entryAmount.setTextColor(this.context.getResources().getColor(R.color.colorGreen));
        entryAmount.setText("" + Math.round(ent.getAmount() * 100) / 100.0 + "");
        entryCategory.setText(ent.getCategory().getName());
        entryDate.setText("Monatstag: " + ent.getTag());

        //Wenn lange auf einen Eintrag gedrückt wird
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                //Es wird ein Dialog mit den
                AlertDialog.Builder optionsDialog = new AlertDialog.Builder(view.getContext());
                optionsDialog.setTitle("Bitte Option auswählen").setItems(
                        R.array.options_without_pic, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {

                                item++;

                                if (item == 1) {//Bearbeiten
                                    Intent i = new Intent(view.getContext(), ChangeMonthlyEntryActivity.class);
                                    i.putExtra("id", "" + ent.getId());
                                    i.putExtra("titel", ent.getTitle());
                                    i.putExtra("betrag", "" + ent.getAmount());
                                    i.putExtra("tag", "" + ent.getTag());
                                    i.putExtra("kategorie", ent.getCategory().getId());
                                    view.getContext().startActivity(i);


                                } else if (item == 2) {//Löschen
                                    AlertDialog diaBox = AskOption(view.getContext(), ent);
                                    diaBox.show();
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


    /**Diese Methode liefert einen Abfragedialog bevor das Löschen durchgeführt wird.*/
    private AlertDialog AskOption(Context context, final MonthlyEntry ent) {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                .setTitle("Löschen")
                .setMessage("Wollen Sie wirklich löschen?")
                .setIcon(R.drawable.delete_x)

                .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        manager.removeMonthlyEntry(ent.getId());
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

}
