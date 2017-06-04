package com.cashify.category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cashify.database.DatabaseHelper;
import com.cashify.R;

import static android.content.ContentValues.TAG;

public class CategoryDetailActivity extends AppCompatActivity {

    private Category category;
    private EditText catNameField;
    private Button delButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        int catId = getIntent().getExtras().getInt("categoryId");
        Log.i(TAG, "onCreate: " + catId);

        CategoryManager catManager = new CategoryManager(new DatabaseHelper(this));
        try {
            category = catManager.getCategoryById(catId);
            catNameField = (EditText) findViewById(R.id.category_detail_name);
            catNameField.setText(category.getName());

            CategoryDetailActivity parent = this;

            delButton = (Button) findViewById(R.id.category_detail_delete);

            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle("Kategorie löschen")
                            .setIcon(R.drawable.delete_x)
                            .setMessage(category.getName() + " wirklich löschen?")
                            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    catManager.removeCategory(category);
                                    parent.finish();
                                }
                            })
                            .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .show();
                }
            });
        } catch (Exception e) {

        }
    }
}
