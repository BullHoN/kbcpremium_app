package com.avit.kbcpremium.auth;

public class LoginResponseData {

    private Boolean accountExists;
    private Userdata accountData;
    private String otp;

    public LoginResponseData(Boolean accountExists, Userdata accountData,String otp) {
        this.accountExists = accountExists;
        this.accountData = accountData;
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public Boolean getAccountExists() {
        return accountExists;
    }

    public Userdata getAccountData() {
        return accountData;
    }

    @Override
    public String toString() {
        return "LoginResponseData{" +
                "otp='" + otp + '\'' +
                '}';
    }
}
