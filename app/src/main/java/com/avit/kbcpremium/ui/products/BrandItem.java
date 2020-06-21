package com.avit.kbcpremium.ui.products;

import java.util.ArrayList;

public class BrandItem {

    String brandName;
    ArrayList<ProductItem> items;

    public BrandItem(String brandName, ArrayList<ProductItem> items) {
        this.brandName = brandName;
        this.items = items;
    }

    public String getBrandName() {
        return brandName;
    }

    public ArrayList<ProductItem> getItems() {
        return items;
    }
}
