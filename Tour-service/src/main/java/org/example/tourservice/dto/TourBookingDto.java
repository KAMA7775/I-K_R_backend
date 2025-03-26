package org.example.tourservice.dto;

public class TourBookingDto extends BookingDto {
    private Long tourId;
    public TourBookingDto(String userId, int quantity, Long tourId) {
        super(userId,quantity);
        this.tourId= tourId;
    }
    public Long getTourId() {
        return tourId;
    }
}
