package com.example.seps.cashofclans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_category);

        String test = "See this repo for the source code. First, we often need to define a model to represent the data within each list item.";

        ListView catListView = (ListView) findViewById(R.id.category_listview);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, test.split("\\s+"));
        catListView.setAdapter(itemsAdapter);
    }
}
