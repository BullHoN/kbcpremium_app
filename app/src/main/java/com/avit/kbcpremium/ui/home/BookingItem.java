package com.avit.kbcpremium.ui.home;

public class BookingItem {
    private String title;
    private int resourceId;

    public BookingItem(String title, int resourceId) {
        this.title = title;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }
}
