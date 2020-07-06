package com.avit.kbcpremium.ui.home;

import java.util.ArrayList;

public class BookingItem {
    private String title;
    private int resourceId;
    private String items[];

    public BookingItem(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public BookingItem(String title, int resourceId,String items[]){
        this.title = title;
        this.resourceId = resourceId;
        this.items = items;
    }

    public String[] getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }
}
