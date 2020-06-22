package com.avit.kbcpremium.ui.bookseat;

import com.avit.kbcpremium.ui.booking.SelectedItem;

import java.util.ArrayList;

public class BookingNotificationPostData {

    private String name;
    private String phNumber;
    private String fcm_id;
    private int total;
    private ArrayList<SelectedItem> orderItems;
    private String address;
    private String nearBy;
    private String orderId;
    private String bookingCat;
    private String selectedDate,selectedTime;

    public BookingNotificationPostData(String name, String phNumber, String fcm_id, int total
            , ArrayList<SelectedItem> orderItems, String address
            , String nearBy, String orderId , String bookingCat,String selectedDate,String selectedTime) {
        this.name = name;
        this.phNumber = phNumber;
        this.fcm_id = fcm_id;
        this.total = total;
        this.orderItems = orderItems;
        this.address = address;
        this.nearBy = nearBy;
        this.orderId = orderId;
        this.bookingCat = bookingCat;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public String getBookingCat() {
        return bookingCat;
    }

    public String getName() {
        return name;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<SelectedItem> getOrderItems() {
        return orderItems;
    }

    public String getAddress() {
        return address;
    }

    public String getNearBy() {
        return nearBy;
    }

    public String getOrderId() {
        return orderId;
    }
}
