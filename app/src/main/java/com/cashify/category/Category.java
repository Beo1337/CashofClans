package com.cashify.category;


import com.example.seps.cashofclans.Database.DatabaseHelper;

// Category class keeps category data round
// Right now this consists only of the category name
// Do we need anything more, or can this be reduced to a simple string?
// TODO: revisit 15 APR 2017 at the latest
public class Category {
    private int id;
    private String categoryName;
    private String icon;

    public Category(int id, String categoryName, String icon) {
        this.id = id;
        this.categoryName = categoryName;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getIcon() {
        return icon;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
