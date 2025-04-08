package org.example.paymentservice.dto;

public class PaymentDto {
    private Long bookingId;
    private double amount;
    private String currency;
    public PaymentDto(){}
    public PaymentDto(Long bookingId, double amount, String currency){
        this.bookingId=bookingId;
        this.amount=amount;
        this.currency=currency;
    }

    public Long getBookingId() {
        return  bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
