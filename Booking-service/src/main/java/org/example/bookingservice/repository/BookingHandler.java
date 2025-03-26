package org.example.bookingservice.repository;

import org.example.bookingservice.dto.BookingDto;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.entity.BookingType;

public interface BookingHandler <T extends BookingDto>{
    BookingResponse handleBooking(T request);
    BookingType getSupportedType();
}
