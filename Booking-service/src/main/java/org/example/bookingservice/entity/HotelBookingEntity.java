package org.example.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="hotel_bookings")
public class HotelBookingEntity extends BookingEntity{
    private Long hotelId;
    public HotelBookingEntity(){}
    public HotelBookingEntity(String userId, Long hotelId, int quantity){
        super.userId=userId;
        super.quantity=quantity;
        this.hotelId=hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
