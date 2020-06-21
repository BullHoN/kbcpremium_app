package com.avit.kbcpremium.ui.products;

import java.util.ArrayList;

public class SubProducts {
    private String subCategory;
    private ArrayList<ProductItem> items;
    private String categoryOf;

    public SubProducts(String subCategory, ArrayList<ProductItem> items) {
        this.subCategory = subCategory;
        this.items = items;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public ArrayList<ProductItem> getItems() {
        return items;
    }

    public String getCategoryOf() {
        return categoryOf;
    }

}
