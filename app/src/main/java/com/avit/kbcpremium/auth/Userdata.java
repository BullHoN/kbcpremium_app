package com.avit.kbcpremium.auth;

public class Userdata {

    private String name;
    private String address;
    private String nearByAddress;

    public Userdata(String name, String address, String nearByAddress) {
        this.name = name;
        this.address = address;
        this.nearByAddress = nearByAddress;
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
}
