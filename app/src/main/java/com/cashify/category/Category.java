package com.cashify.category;

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

    public String getName() {
        return categoryName;
    }

    public String getIcon() {
        return icon;
    }

    public void setName(String categoryName) {
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
