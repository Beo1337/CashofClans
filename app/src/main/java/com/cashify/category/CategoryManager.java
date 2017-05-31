package com.cashify.category;

import com.example.seps.cashofclans.Database.DatabaseHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
        boolean success = this.dbHelper.addCategory(name);
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean removeCategory(String name) {
        boolean success = this.dbHelper.deleteCategory(name);
        if (success) this.reloadFromDb();
        return success;
    }
}
