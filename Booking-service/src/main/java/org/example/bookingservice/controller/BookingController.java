package org.example.bookingservice.controller;

import com.example.saga.TourBookingStartedEvent;
import org.example.bookingservice.dto.BookingResponse;
import org.example.bookingservice.dto.TourBookingDto;
import org.example.bookingservice.repository.BookingHandler;
import org.example.bookingservice.service.TourBookingHandler;
import org.example.bookingservice.entity.BookingType;
import org.example.bookingservice.service.BookingRouter;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final KafkaTemplate<String, Object> kafka;

    public BookingController(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    @PostMapping("/start-tour")
    public ResponseEntity<String> startTour(@RequestParam Long tourId, @RequestParam String userId) {
        String sagaId = UUID.randomUUID().toString();
        kafka.send("booking.tour.started", sagaId, new TourBookingStartedEvent(sagaId, tourId, userId));
        return ResponseEntity.ok("Saga started: " + sagaId);
    }

}
