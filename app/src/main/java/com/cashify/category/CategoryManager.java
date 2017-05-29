package com.cashify.category;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.seps.cashofclans.Database.DatabaseHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static android.content.ContentValues.TAG;

// Category manager is intended to be the central point to get category entries from
// TODO: revisit 15 APR 2017 at the latest
public class CategoryManager {

    private static List<Category> categoryList;
    private DatabaseHelper dbHelper;

    // CategoryManager, this should probably be a singleton.
    // TODO: review once other functionality is in place
    public CategoryManager(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        categoryList = new LinkedList<>();
        categoryList.addAll(dbHelper.getCategories(dbHelper.getReadableDatabase()));
    }

    private void reloadFromDb() {
        categoryList.clear();
        categoryList.addAll(dbHelper.getCategories(dbHelper.getReadableDatabase()));
    }

    // Return single category entry by list position
    public Category getCategoryByIndex(int index) {
        return categoryList.get(index);
    }

    public Category getCategoryById(int id) throws Exception {
        for(Category c : categoryList) if (c.getId() == id) return c;
        throw new Exception("Cat id not found");
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
        boolean success = this.dbHelper.addCategory(name, dbHelper.getWritableDatabase());
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean removeCategory(String name) {
        boolean success = this.dbHelper.deleteCategory(name, dbHelper.getWritableDatabase());
        if (success) this.reloadFromDb();
        return success;
    }
}
