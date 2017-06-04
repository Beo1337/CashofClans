package com.cashify.category;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;

// CategoryAddFragment is a glorified input dialog, kinda like back in Windows 95

public class CategoryAddFragment extends DialogFragment {

    // Necessary to pass the dialog input back to the activity that created the dialog
    public interface Listener {
        public void onCategoryAdd(String catName);
    }

    private Listener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        //TODO: Fix magic strings
        builder.setTitle("Kategorie hinzufügen")
                .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: Needs a bunch of safety checks
                        listener.onCategoryAdd(input.getText().toString());
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    // This is deprecated, but it's small and it works...
    // Underlying activity needs to implement the listener interface to receive the dialog input
    // The fragment hooks into the activity through this method and passes the result back
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
