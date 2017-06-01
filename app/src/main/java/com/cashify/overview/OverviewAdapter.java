package com.cashify.overview;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cashify.R;

// Category adapter interfaces between presentation layer and model
// - Generates singular view elements for each item that is currently visible on screen
// - Takes data from a Manager, which for all practical purposes acts as a singleton

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    private OverviewManager manager;

    // ViewHolder wraps the view that we want to pass to the RecyclerView,
    // we only needs this because RecyclerView.ViewHolder is abstract
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public OverviewAdapter(OverviewManager manager) {this.manager = manager;}


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
        //Felder des Eintrags holen
        TextView entryText = (TextView) holder.view.findViewById(R.id.entry_text);
        TextView entryAmount = (TextView) holder.view.findViewById(R.id.entry_amount);
        TextView entryCategory = (TextView) holder.view.findViewById(R.id.entry_category);
        TextView entryDate = (TextView) holder.view.findViewById(R.id.entry_date);

        //Felder befüllen
        Entry ent = manager.getEntryByIndex(position);

        entryText.setText(ent.getTitle());
        if(ent.getBetrag()<0)
            entryAmount.setTextColor(Color.RED);
        else
            entryAmount.setTextColor(Color.GREEN);
        entryAmount.setText(""+ent.getBetrag()+"");
        entryCategory.setText(ent.getKategorie());
        entryDate.setText(ent.getDatum());

        if(ent.getFoto()!=null)//Wenn ein Foto mitgespeichert wurde, dann das durch ein kleines Icon anzeigen.
        {
            ImageView cam = (ImageView) holder.view.findViewById(R.id.entry_cam_icon);
            cam.setVisibility(View.VISIBLE);
        }

        //Wenn lange auf einen Eintrag gedrückt wird
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                AlertDialog.Builder optionsDialog = new AlertDialog.Builder(holder.view.getContext());


                if(ent.getFoto()!=null)//Optionsmenü mit Foto öffen
                    optionsDialog.setTitle("Bitte Option auswählen").setItems(
                            R.array.options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {

                                    item++;

                                    if (item == 1) {//Foto ansehen
                                        Intent pic = new Intent(holder.view.getContext(),PictureActivity.class);
                                        pic.putExtra("picture",ent.getFoto());
                                        holder.view.getContext().startActivity(pic);


                                    } else if (item == 2) {//Bearbeiten
                                        //TODO Bearbeiten


                                    } else if (item == 3) {//Löschen
                                        manager.removeEntry(ent.getId());
                                        notifyDataSetChanged();
                                    }

                                }
                            });
                else//Optionsmenü ohne Foto öffen
                    optionsDialog.setTitle("Bitte Option auswählen").setItems(
                            R.array.options_without_pic, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {

                                    item++;

                                    if (item == 1) {//Bearbeiten
                                        //TODO Bearbeiten


                                    } else if (item == 2) {//Löschen
                                        manager.removeEntry(ent.getId());
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
