package com.avit.kbcpremium.ui.products;

public class ProductItem {

    private int price;
    private String name;
    private int discount;
    private String imageUrl;

    public ProductItem(int price, String name, int discount, String imageUrl) {
        this.price = price;
        this.name = name;
        this.discount = discount;
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getDiscount() {
        return discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
