package org.example.tourservice.dto;

public abstract class BookingDto {
    protected String userId;
    protected int quantity;

    public BookingDto(String userId, int quantity) {
        this.userId = userId;
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public int getQuantity() {
        return quantity;
    }
}