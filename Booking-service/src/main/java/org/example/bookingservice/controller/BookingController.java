package org.example.bookingservice.controller;

import com.example.saga.TourBookingRequest;
import com.example.saga.TourBookingStartedEvent;
import org.example.bookingservice.entity.TourBookingEntity;
import org.example.bookingservice.repository.TourBookingRepository;

import org.example.bookingservice.service.TourBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    private final TourBookingRepository bookingRepo;
    private final KafkaTemplate<String, Object> kafka;
    private final TourBookingService tourBookingService;

    public BookingController(TourBookingRepository bookingRepo, KafkaTemplate<String, Object> kafka, TourBookingService tourBookingService) {
        this.bookingRepo = bookingRepo;
        this.kafka = kafka;
        this.tourBookingService=tourBookingService;
    }

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @PostMapping("/{tourId}/reserve")
    public ResponseEntity<Long> reserveTour(
            @PathVariable Long tourId,
            @RequestBody TourBookingRequest request
    ) {
        TourBookingEntity booking = new TourBookingEntity(
                request.getUserId(), tourId, request.getQuantity()
        );
        booking.setStatus("PENDING");
        TourBookingEntity saved = bookingRepo.save(booking);

        log.info("✅ Тур зарезервирован в БД: bookingId={}, userId={}, tourId={}",
                saved.getId(), request.getUserId(), tourId);

        return ResponseEntity.ok(saved.getId());
    }

    @PostMapping("/{tourId}/start-payment")
    public ResponseEntity<String> startPayment(
            @PathVariable Long tourId,
            @RequestParam Long bookingId,
            @RequestBody com.example.saga.TourBookingRequest request
    ) {
        String sagaId = UUID.randomUUID().toString();

        kafka.send("booking.tour.started", new TourBookingStartedEvent(
                sagaId,
                tourId,
                Long.valueOf(request.getUserId()),
                bookingId,
                request.getQuantity()
        ));

        log.info(" Сага запущена: sagaId={}, tourId={}, userId={}, bookingId={}, quantity={}",
                sagaId, tourId, request.getUserId(), bookingId, request.getQuantity());

        return ResponseEntity.ok("Payment process started. Saga ID: " + sagaId);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TourBookingEntity>> getBookingsByUser(@PathVariable String userId) {
        List<TourBookingEntity> bookings = bookingRepo.findByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/alltourbookings")
    public ResponseEntity<List<TourBookingEntity>> getAllTourBookings() {
        return ResponseEntity.ok(tourBookingService.getAllTourBookings());
    }

}
