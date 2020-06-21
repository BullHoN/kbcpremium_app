package com.avit.kbcpremium.notification;

import java.util.ArrayList;

public class NotificationPostData {

    private String name;
    private String phNumber;
    private String fcm_id;
    private int total;
    private ArrayList<String> orderItems;
    private String address;
    private String nearBy;
    private String orderId;
    private boolean isPaid;
    private int deliveryCharge;

    public NotificationPostData(String name, String phNumber, String fcm_id, int total, ArrayList<String> orderItems
            , String address, String nearBy, String orderId, boolean isPaid,int deliveryCharge) {
        this.name = name;
        this.phNumber = phNumber;
        this.fcm_id = fcm_id;
        this.total = total;
        this.orderItems = orderItems;
        this.address = address;
        this.nearBy = nearBy;
        this.orderId = orderId;
        this.isPaid = isPaid;
        this.deliveryCharge = deliveryCharge;
    }

    public int getDeliveryCharge() {
        return deliveryCharge;
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

    public ArrayList<String> getOrderItems() {
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

    public boolean isPaid() {
        return isPaid;
    }
}
