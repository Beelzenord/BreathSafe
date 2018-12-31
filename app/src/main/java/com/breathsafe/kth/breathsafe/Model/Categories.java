package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class Categories {
    private List<String> categories;

    private String singleCategory;

    public Categories(List<String> categories) {
        this.categories = categories;
    }

    public Categories(String singleCategory) {
        this.singleCategory = singleCategory;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getSingleCategory() {
        return singleCategory;
    }

    public void setSingleCategory(String singleCategory) {
        this.singleCategory = singleCategory;
    }
}
