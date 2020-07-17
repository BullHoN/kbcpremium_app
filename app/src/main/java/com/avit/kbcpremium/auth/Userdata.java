package com.avit.kbcpremium.auth;

import java.util.ArrayList;

public class Userdata {

    private String name;
    private String address;
    private String nearByAddress;
    private ArrayList<ResponseOrderItem> orderItems;

    public Userdata(String name, String address, String nearByAddress) {
        this.name = name;
        this.address = address;
        this.nearByAddress = nearByAddress;
    }

    public Userdata(String name, String address, String nearByAddress, ArrayList<ResponseOrderItem> orderItems) {
        this.name = name;
        this.address = address;
        this.nearByAddress = nearByAddress;
        this.orderItems = orderItems;
    }

    public ArrayList<ResponseOrderItem> getOrderItems() {
        return orderItems;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getNearByAddress() {
        return nearByAddress;
    }

    public ArrayList<String> getTheArray(){
        ArrayList<String> items = new ArrayList<>();
        items.add(name);
        items.add(address);
        items.add(nearByAddress);

        return items;
    }

    public static Userdata getFromArray(ArrayList<String> items){
        Userdata userdata = new Userdata(items.get(0),items.get(1),items.get(2));

        return userdata;
    }

}
