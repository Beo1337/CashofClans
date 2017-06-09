package com.cashify.category;

import android.util.Log;

import com.cashify.database.DatabaseHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

// Category manager is intended to be the central point to get category entries from

public class CategoryManager {

    private static List<Category> categoryList;
    private DatabaseHelper dbHelper;

    public CategoryManager(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        reloadFromDb();
    }

    private void reloadFromDb() {
        if (categoryList == null) categoryList = new LinkedList<>();
        categoryList.clear();
        categoryList.addAll(dbHelper.getCategories());
        Collections.sort(categoryList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    // Return single category entry by list position
    public Category getCategoryByIndex(int index) {
        return categoryList.get(index);
    }

    public Category getCategoryById(int id) {
        for (Category c : categoryList) if (c.getId() == id) return c;
        return Category.DefaultError();
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
        boolean success = this.dbHelper.addCategory(name);
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean updateCategory(Category c) {
        boolean success = this.dbHelper.changeCategoryName(c.getId(),c.getName());
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean removeCategory(Category c) {
        boolean success = this.dbHelper.deleteCategory(c.getId());
        if (success) this.reloadFromDb();
        Log.e(TAG, "removeCategory: " + success);
        return success;
    }
}
