package com.cashify.category;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.seps.cashofclans.Database.DatabaseHelper;
import com.example.seps.cashofclans.R;

import static android.content.ContentValues.TAG;

public class CategoryDetailActivity extends AppCompatActivity {

    private Category category;
    private EditText catNameField;

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
            catNameField.setText(category.getCategoryName());
        } catch (Exception e) {
            
        }
    }
}
