package com.cashify.base;

import com.cashify.category.Category;

/**
 * Created by mhackl on 02.06.2017.
 */

// AbstractEntry
// Entry and MonthlyEntry have had a huge intersecting surface, we refactored it so that both
// share this in this abstract class

public class AbstractEntry {

    private final int id;             // Database id for entry
    private double amount;          // Amount of money "moved"
    private String title;           // Description
    private Category category;

    public AbstractEntry(int id, double betrag, String title, Category cat) {
        this.id = id;
        this.amount = betrag;
        this.title = title == null ? "(null value?!)" : title;
        this.category = cat == null ? Category.DefaultError() : cat;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double betrag) {
        this.amount = betrag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

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
