package com.avit.kbcpremium.ui.booking;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class BookingItem {

    private String name;
    private ArrayList<String> prices;

    public BookingItem(String name, ArrayList<String> prices) {
        this.name = name;
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getPrices() {
        return prices;
    }
}
