package org.example.bookingservice.service;

import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.TourBookingDto;
import org.example.bookingservice.dto.TourBookingResponse;
import org.example.bookingservice.entity.BookingType;
import org.example.bookingservice.repository.BookingHandler;
import org.springframework.stereotype.Service;

@Service
public class TourBookingHandler implements BookingHandler<TourBookingDto> {
    @Override
    public TourBookingResponse handleBooking(TourBookingDto request) {
        return new TourBookingResponse(
                "SUCCESS",
                "Tour booked: Tour ID = " + request.getTourId(),
                request.getUserId(),
                request.getTourId()
        );
    }


    @Override
    public BookingType getSupportedType() {
        return BookingType.TOUR;
    }
}
