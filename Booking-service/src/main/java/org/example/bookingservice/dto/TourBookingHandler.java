package org.example.bookingservice.dto;

import org.example.bookingservice.entity.BookingType;
import org.example.bookingservice.repository.BookingHandler;
import org.springframework.stereotype.Service;

@Service
public class TourBookingHandler implements BookingHandler<TourBookingDto> {
    @Override
    public BookingResponse handleBooking(TourBookingDto request) {
        return new BookingResponse("SUCCESS", "Tour booked: Tour ID = " + request.getTourId());
    }

    @Override
    public BookingType getSupportedType() {
        return BookingType.TOUR;
    }
}
