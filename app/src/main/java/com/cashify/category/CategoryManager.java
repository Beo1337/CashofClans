package com.cashify.category;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


// Category manager is intended to be the central point to get category entries from
// TODO: revisit 15 APR 2017 at the latest
public class CategoryManager {

    private static List<Category> categoryList;

    // Mock method to generate category elements while there is no infrastructure in place to load
    // from a data source
    // TODO: review mechanism when database available
    private static void populateList() {
        if (categoryList == null) {
            categoryList = new LinkedList<>();
            String test = "See this repo for the source code. First, we often need to define a model to represent the data within each list item.";
            for(String i : test.split("\\s+")) categoryList.add(new Category(i));
        }
    }

    // CategoryManager, this should probably be a singleton.
    // TODO: review once other functionality is in place
    public CategoryManager() {
        populateList();
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
        categoryList.add(new Category(name));
        return true;
    }
}
