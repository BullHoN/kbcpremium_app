package com.avit.kbcpremium.ui.cart;

public class AvailabilityResponse {

    private boolean isAvailable;
    private int deliveryPrice;

    public AvailabilityResponse(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public AvailabilityResponse(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
