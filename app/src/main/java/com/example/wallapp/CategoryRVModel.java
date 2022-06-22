package com.example.wallapp;

public class CategoryRVModel {

    private String category;
    private String categoryIVURL;


    public CategoryRVModel(String category, String categoryIVURL) {
        this.category = category;
        this.categoryIVURL = categoryIVURL;
    }

    public  String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryIVURL() {
        return categoryIVURL;
    }

    public void setCategoryIVURL(String categoryIVURL) {
        this.categoryIVURL = categoryIVURL;
    }
}

