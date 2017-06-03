package com.cashify.base;

import com.cashify.category.Category;

/**
 * Created by mhackl on 02.06.2017.
 */

// Entry and MonthlyEntry have a huge intersecting surface, gotta refactor this out


public class AbstractEntry {

    private final int id;             // Database id for entry
    private double amount;          // Amount of money spent
    private String title;           // Description
    private Category category;      // Category

    public AbstractEntry(int id, double betrag, String title, Category cat) {
        this.id = id;
        this.amount = betrag;
        this.title = title == null? "(null value?!)" : title;
        this.category = cat == null ? new Category(Integer.MIN_VALUE, "Error", "") : cat;
    }

    // Retrieve entry id
    public int getId() {
        return id;
    }

    // Retrieve amount of money spent
    public double getAmount() {
        return amount;
    }

    // Set amount of money spent
    public void setAmount(double betrag) {
        this.amount = betrag;
    }

    // Retrieve description
    public String getTitle() {
        return title;
    }

    // Set description
    public void setTitle(String titel) {
        this.title = titel;
    }

    // Get category object
    public Category getCategory() {

        return category;
    }

    // Set category
    public void setCategory(Category kategorie) {
        this.category = kategorie;
    }

    @Override
    public String toString() {
        return "MonthlyEntry{" +
                "id=" + id +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", category='" + category.toString() + '\'' +
                '}';
    }
}
