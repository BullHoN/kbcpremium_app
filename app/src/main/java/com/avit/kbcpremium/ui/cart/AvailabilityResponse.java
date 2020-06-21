package com.avit.kbcpremium.ui.cart;

public class AvailabilityResponse {

    private boolean isAvailable;

    public AvailabilityResponse(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
