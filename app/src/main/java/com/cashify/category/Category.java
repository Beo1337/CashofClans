package com.cashify.category;

public class Category {
    private int id;
    private String categoryName;

    public Category(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return categoryName;
    }

    public void setName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    // Provide a default object so that we do not need to except
    public static Category DefaultError() {
        return new Category(Integer.MIN_VALUE, "Fehler");
    }
}
