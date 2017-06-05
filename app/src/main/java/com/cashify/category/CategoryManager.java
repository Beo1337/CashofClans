package com.cashify.category;

import com.cashify.database.DatabaseHelper;

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

    // Ändert den Namen einer Kategorie
    public boolean changeCategory(int id, String name) {
        boolean success = this.dbHelper.changeCategoryName(id,name);
        if (success) this.reloadFromDb();
        return success;
    }

    public boolean removeCategory(int id) {
        boolean success = this.dbHelper.deleteCategory(id);
        if (success) this.reloadFromDb();
        return success;
    }
}
