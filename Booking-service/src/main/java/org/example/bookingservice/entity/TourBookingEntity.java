package org.example.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="tour_bookings")
public class TourBookingEntity extends BookingEntity{
    private Long tourId;

    public TourBookingEntity(String userId, Long tourId, int quantity) {
        super.userId = userId;
        super.quantity = quantity;
        this.tourId = tourId;
    }

    public TourBookingEntity() {

    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}
