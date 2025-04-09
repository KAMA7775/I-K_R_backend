package org.example.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="tour_bookings")
public class TourBookingEntity extends BookingEntity{
    private Long tourId;
    private String paymentStatus;

public TourBookingEntity(){}

    public TourBookingEntity(String userId, Long tourId, int quantity) {
        super.userId = userId;
        super.quantity = quantity;
        this.tourId = tourId;
        this.status=status;
        this.paymentStatus=paymentStatus;
    }


    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
