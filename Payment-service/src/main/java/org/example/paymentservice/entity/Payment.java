package org.example.paymentservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name="payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookingId;
    private String status;
    private double amount;
    private String paymentIntent;
    public Payment (){}
    public Payment(Long id, Long bookingId, String status, double  amount, String paymentIntent){
        this.id=id;
        this.bookingId=bookingId;
        this.amount=amount;
        this.status=status;
        this.paymentIntent=paymentIntent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentIntent() {
        return paymentIntent;
    }

    public void setPaymentIntent(String paymentIntent) {
        this.paymentIntent = paymentIntent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
