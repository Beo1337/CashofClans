package com.cashify.category;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

            delButton = (Button) findViewById(R.id.category_detail_delete);
            delButton.setOnClickListener(l -> {
                catManager.removeCategory(category.getName());
                this.finish();
            });


        } catch (Exception e) {

        }
    }
}
