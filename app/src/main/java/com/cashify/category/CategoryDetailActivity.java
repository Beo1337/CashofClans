package com.cashify.category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashify.R;
import com.cashify.database.DatabaseHelper;

import static android.content.ContentValues.TAG;

public class CategoryDetailActivity extends AppCompatActivity {

    private Category category;
    private EditText catNameField;
    private final CategoryDetailActivity parent = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        int catId = getIntent().getExtras().getInt("categoryId");
        Log.i(TAG, "onCreate: " + catId);

        final CategoryManager catManager = new CategoryManager(new DatabaseHelper(this));
        try {
            category = catManager.getCategoryById(catId);
            catNameField = (EditText) findViewById(R.id.category_detail_name);
            catNameField.setText(category.getName());

            Button delButton = (Button) findViewById(R.id.category_detail_delete);
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle(R.string.diag_title_category_delete)
                            .setIcon(R.drawable.delete_x)
                            .setMessage(category.getName() + " - " + R.string.diag_text_category_delete)
                            .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    catManager.removeCategory(category);
                                    dialog.dismiss();
                                    parent.finish();
                                }
                            })
                            .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });

            Button chgButton = (Button) findViewById(R.id.category_detail_save);
            chgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle(R.string.diag_title_category_change)
                            .setMessage(category.getName() + " - " + R.string.diag_text_category_change)
                            .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    category.setName(catNameField.getText().toString());
                                    catManager.updateCategory(category);
                                    dialog.dismiss();
                                    parent.finish();
                                }
                            })
                            .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        } catch (Exception e) {

        }
    }
}
