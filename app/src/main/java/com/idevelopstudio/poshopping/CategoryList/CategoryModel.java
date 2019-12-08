package com.idevelopstudio.poshopping.CategoryList;

import android.graphics.drawable.Drawable;

public class CategoryModel {

    private String categoryName;
    private Drawable categoryIcon;

    public String getCategoryName() {
        return categoryName;
    }

    public Drawable getCategoryIcon() {
        return categoryIcon;
    }

    public CategoryModel(String categoryName, Drawable categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }
}
