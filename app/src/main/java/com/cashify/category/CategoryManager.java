package com.cashify.category;

import com.example.seps.cashofclans.Database.DatabaseHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// Category manager is intended to be the central point to get category entries from
// TODO: revisit 15 APR 2017 at the latest
public class CategoryManager {

    private static List<Category> categoryList;

    // CategoryManager, this should probably be a singleton.
    // TODO: review once other functionality is in place
    public CategoryManager(DatabaseHelper dbHelper) {
        categoryList.addAll(dbHelper.getCategories(dbHelper.getReadableDatabase()));
    }

    // Return single category entry by list position
    public Category getCategoryByIndex(int index) {
        return categoryList.get(index);
    }

    // Return list of all elements (unmodifiable)
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categoryList);
    }

    // Return collection size
    public int getCount() {
        return categoryList.size();
    }

    public boolean addCategory(String name) {
        //TODO hab die vorhandene Categoryklasse genommen, muss hier eventuel angepasst werden. Lg Simon
        return true;
    }
}
