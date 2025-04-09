package org.example.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="hotel_bookings")
public class HotelBookingEntity extends BookingEntity{
    private Long hotelId;
    private String paymentStatus;
    public HotelBookingEntity(){}
    public HotelBookingEntity(String userId, Long hotelId, int quantity, String paymentStatus){
        super.userId=userId;
        super.quantity=quantity;
        this.hotelId=hotelId;
        this.paymentStatus=paymentStatus;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
