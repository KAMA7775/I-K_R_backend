package org.example.bookingservice.dto;

public class TourBookingResponse extends BookingResponse{

    private Long tourId;
    public TourBookingResponse(String status, String message,String userId,Long tourId){
        super(status,message,userId);
        this.tourId=tourId;
    }


    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }
}
