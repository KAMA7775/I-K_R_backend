package org.example.tourservice.dto;

public class TourBookingRequest {
    private String userId;
    private int quantity;

    public TourBookingRequest() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
