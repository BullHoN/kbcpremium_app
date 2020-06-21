package com.avit.kbcpremium.auth;

public class LoginPostData {

    private String phoneNo;

    public LoginPostData(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
