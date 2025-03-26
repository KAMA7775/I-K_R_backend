package org.example.bookingservice.controller;

import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.TourBookingDto;
import org.example.bookingservice.dto.TourBookingHandler;
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
    private final BookingRouter bookingRouter;
    private final TourBookingHandler tourBookingHandler;

    public BookingController(BookingRouter bookingRouter, TourBookingHandler tourBookingHandler) {
        this.bookingRouter = bookingRouter;
        this.tourBookingHandler = tourBookingHandler;
    }

    @PostMapping("/tour")
    public ResponseEntity<BookingResponse> bookTour(@RequestBody TourBookingDto dto) {
        BookingResponse response = bookingRouter.route(BookingType.TOUR, dto);
        return ResponseEntity.ok(response);
    }

}
