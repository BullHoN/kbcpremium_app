package com.avit.kbcpremium.auth;

public class RegisterPostData {

    private String name;
    private String phoneNo;
    private String address;
    private String nearByAddress;

    public RegisterPostData(String name, String phoneNo, String address, String nearByAddress) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.address = address;
        this.nearByAddress = nearByAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public String getNearByAddress() {
        return nearByAddress;
    }
}
