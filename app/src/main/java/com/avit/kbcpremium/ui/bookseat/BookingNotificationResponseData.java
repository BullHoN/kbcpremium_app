package com.avit.kbcpremium.ui.bookseat;

public class BookingNotificationResponseData {

    private boolean status;
    private String reason;

    public BookingNotificationResponseData(boolean status) {
        this.status = status;
    }

    public BookingNotificationResponseData(boolean status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public String getMessage() {
        return reason;
    }

    public boolean isStatus() {
        return status;
    }
}
