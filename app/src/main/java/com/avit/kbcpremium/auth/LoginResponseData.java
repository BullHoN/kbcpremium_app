package com.avit.kbcpremium.auth;

public class LoginResponseData {

    private Boolean accountExists;
    private Userdata accountData;

    public LoginResponseData(Boolean accountExists, Userdata accountData) {
        this.accountExists = accountExists;
        this.accountData = accountData;
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
                "accountExists=" + accountExists +
                '}';
    }
}
