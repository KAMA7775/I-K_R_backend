package org.example.bookingservice.dto;

public class HotelBookingDto extends BookingDto{
    private Long hotelId;

    public HotelBookingDto(String userId, int quantity, Long hotelId) {
        super(userId,quantity);
      this.hotelId=hotelId;
    }

    public Long getHotelId() {
        return hotelId;
    }
}
