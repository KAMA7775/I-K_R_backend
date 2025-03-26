package org.example.bookingservice.controller;

import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.TourBookingDto;
import org.example.bookingservice.repository.BookingHandler;
import org.example.bookingservice.service.TourBookingHandler;
import org.example.bookingservice.entity.BookingType;
import org.example.bookingservice.service.BookingRouter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingHandler<TourBookingDto> tourBookingHandler;

    public BookingController(TourBookingHandler tourBookingHandler) {
        this.tourBookingHandler = tourBookingHandler;
    }

    @PostMapping("/tour")
    public ResponseEntity<BookingResponse> bookTour(@RequestBody TourBookingDto dto) {
        BookingResponse response = tourBookingHandler.handleBooking(dto);
        return ResponseEntity.ok(response);
    }

}
