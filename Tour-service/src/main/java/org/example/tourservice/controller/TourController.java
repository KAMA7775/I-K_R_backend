package org.example.tourservice.controller;

import com.example.saga.TourBookingStartedEvent;
import org.example.tourservice.dto.BookingDto;
import org.example.tourservice.dto.TourBookingRequest;
import org.example.tourservice.dto.TourDto;
import org.example.tourservice.entity.Tour;
import org.example.tourservice.repository.TourRepository;
import org.example.tourservice.service.TourSagaStarter;
import org.example.tourservice.service.TourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tours")
@CrossOrigin(origins = "http://localhost:3000")
public class TourController {
    private TourService service;
    private final TourSagaStarter sagaStarter;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private TourRepository tourRepository;
    public TourController(TourService service, TourSagaStarter sagaStarter, TourRepository tourRepository, KafkaTemplate<String, Object> kafkaTemplate){
        this.service = service;
        this.sagaStarter = sagaStarter;
        this.kafkaTemplate=kafkaTemplate;
        this.tourRepository=tourRepository;
    }
    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTour(){
        return ResponseEntity.ok(service.getAllTour());
    }
    @GetMapping("/destination/{destination}")
    public ResponseEntity<?> getTourByDestination(@PathVariable String destination){
        return service.getTourByDestination(destination)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable Long id){
        return service.getTourById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Tour> create(@Validated @RequestBody Tour tour) {
        return ResponseEntity.ok(service.create(tour));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<Tour> update(@PathVariable Long id, @RequestBody Tour updated) {
        return ResponseEntity.ok(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> deleteTour(@PathVariable Long id) {
        service.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tourId}/book")
    public ResponseEntity<String> bookTour(
            @PathVariable Long tourId,
            @RequestBody TourBookingRequest request
    ) {
        Optional<Tour> tourOptional = tourRepository.findById(tourId);
        if (tourOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tour with ID " + tourId + " not found.");
        }

        String sagaId = UUID.randomUUID().toString();
        kafkaTemplate.send("booking.tour.started", sagaId,
                new TourBookingStartedEvent(sagaId, tourId, request.getUserId()));

        return ResponseEntity.ok("Tour booked, saga ID: " + sagaId);
    }



}
