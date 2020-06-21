package com.avit.kbcpremium.ui.booking;

import java.util.ArrayList;

public class BookingItems {

    private ArrayList<BookingItem> items;
    private ArrayList<String> options;

    public BookingItems(ArrayList<BookingItem> items, ArrayList<String> options) {
        this.items = items;
        this.options = options;
    }

    public ArrayList<BookingItem> getItems() {
        return items;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
}
