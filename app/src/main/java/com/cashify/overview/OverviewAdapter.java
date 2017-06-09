package com.cashify.overview;

import android.content.Context;
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
import com.cashify.base.ViewHolder;

// Adapter interfaces between presentation layer and model
// - Generates singular view elements for each item that is currently visible on screen
// - Takes data from a manager

public class OverviewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private OverviewManager manager;

    public OverviewAdapter(OverviewManager manager) {
        this.manager = manager;
    }


    // Generates a new ViewHolder and preloads a layout
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.content_overview_elem, parent, false);
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
        ImageView cam = (ImageView) view.findViewById(R.id.entry_cam_icon);
        cam.setVisibility(View.INVISIBLE);

        //Felder befüllen
        final Entry ent = manager.getEntryByIndex(position);

        entryText.setText(ent.getTitle());
        if (ent.getAmount() < 0)
            entryAmount.setTextColor(Color.RED);
        else
            entryAmount.setTextColor(Color.GREEN);
        entryAmount.setText("" + Math.round(ent.getAmount() * 100) / 100.0 + "");
        entryCategory.setText(ent.getCategory().getName());
        entryDate.setText(ent.getDatum());

        if (ent.getFoto() != null)//Wenn ein Foto mitgespeichert wurde, dann das durch ein kleines Icon anzeigen.
            cam.setVisibility(View.VISIBLE);


        //Wenn lange auf einen Eintrag gedrückt wird
        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                AlertDialog.Builder optionsDialog = new AlertDialog.Builder(view.getContext());
                optionsDialog.setTitle("Bitte Option auswählen")
                        .setItems(
                                ent.getFoto() != null ? R.array.options : R.array.options_without_pic,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

                                        if (ent.getFoto() == null) item += 1;

                                        switch (item) {
                                            case 0:
                                                Intent pic = new Intent(view.getContext(), PictureActivity.class);
                                                pic.putExtra("picture", ent.getFoto());
                                                view.getContext().startActivity(pic);
                                                break;
                                            case 1:
                                                Intent i = new Intent(view.getContext(), ChangeEntryActivity.class);
                                                i.putExtra("id", "" + ent.getId());
                                                i.putExtra("titel", ent.getTitle());
                                                i.putExtra("betrag", "" + ent.getAmount());
                                                i.putExtra("datum", "" + ent.getDatum());
                                                i.putExtra("kategorie", ent.getCategory().getName());
                                                i.putExtra("foto", ent.getFoto());
                                                view.getContext().startActivity(i);
                                                break;
                                            case 2:
                                                AlertDialog diaBox = AskOption(view.getContext(), ent);
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

    /**Diese Methode liefert die Anzahl an Items in dem Viewholder.*/
    public int getItemCount() {
        return manager.getCount();
    }

    /**Diese Methode liefert einen Abfragedialog bevor das Löschen durchgeführt wird.*/
    private AlertDialog AskOption(Context context, final Entry ent) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.diag_title_entry_delete)
                .setMessage(R.string.diag_text_entry_delete)
                .setIcon(R.drawable.delete_x)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        manager.removeEntry(ent.getId());
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
