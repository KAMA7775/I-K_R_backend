package org.example.tourservice.controller;

import com.example.saga.TourBookingStartedEvent;
import org.example.tourservice.dto.AdminTourDto;
import org.example.tourservice.dto.BookingDto;
import org.example.tourservice.dto.TourBookingRequest;
import org.example.tourservice.dto.TourDto;
import org.example.tourservice.entity.Tour;
import org.example.tourservice.repository.TourRepository;
import org.example.tourservice.service.TourSagaStarter;
import org.example.tourservice.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private static final Logger log = LoggerFactory.getLogger(TourController.class);

    @GetMapping
    public ResponseEntity<List<TourDto>> getAllTour() {
        return ResponseEntity.ok(service.getAllToursForUsers());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<AdminTourDto>> getAllToursForAdmin() {
        return ResponseEntity.ok(service.getAllToursForAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable Long id) {
        return service.getTourById(id)
                .filter(t -> !t.isDeleted())
                .map(tour -> new TourDto(
                        tour.getId(),
                        tour.getDestination(),
                        tour.getRegion(),
                        tour.getDescription(),
                        tour.getDateTime(),
                        tour.getDuration(),
                        tour.getPrice(),
                        tour.getQuantity(),
                        tour.getImageUrl()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/destination/{destination}")
    public ResponseEntity<?> getTourByDestination(@PathVariable String destination) {
        return service.getTourByDestination(destination)
                .filter(t -> !t.isDeleted())
                .map(tour -> new TourDto(
                        tour.getId(),
                        tour.getDestination(),
                        tour.getRegion(),
                        tour.getDescription(),
                        tour.getDateTime(),
                        tour.getDuration(),
                        tour.getPrice(),
                        tour.getQuantity(),
                        tour.getImageUrl()
                ))
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Tour updated) {
        try {
            return ResponseEntity.ok(service.update(id, updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> softDeleteTour(@PathVariable Long id) {
        try {
            service.softDelete(id);
            return ResponseEntity.ok("Tour marked as deleted");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping("/{tourId}/check-availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long tourId) {
        return tourRepository.findById(tourId)
                .filter(t -> !t.isDeleted())
                .map(tour -> ResponseEntity.ok(tour.getQuantity() > 0))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }

//    public ResponseEntity<List<TourDto>> filterTours(
//            @RequestParam(required = false) String destination,
//            @RequestParam(required = false) String region,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
//            @RequestParam(required = false) String duration
//    ) {
//        Duration parsedDuration = duration != null ? Duration.parse(duration) : null;
//        List<Tour> tours = service.filterTours(destination, region, dateTime, parsedDuration);
//
//        List<TourDto> dtos = tours.stream()
//                .filter(t -> !t.isDeleted())
//                .map(t -> new TourDto(
//                        t.getId(), t.getDestination(), t.getRegion(), t.getDescription(),
//                        t.getDateTime(), t.getDuration(), t.getPrice(), t.getQuantity(), t.getImageUrl()))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(dtos);
//    }
@GetMapping("/filter")
public ResponseEntity<List<Tour>> filterTours(
        @RequestParam(required = false) String destination,
        @RequestParam(required = false) String region,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
        @RequestParam(required = false) String duration
) {
    Duration parsedDuration = null;
    if (duration != null) {
        parsedDuration = Duration.parse(duration);
    }

    List<Tour> tours = service.filterTours(destination, region, dateTime, parsedDuration);
    return ResponseEntity.ok(tours);
}

    @GetMapping("/destination")
    public ResponseEntity<List<String>> getAllDirections() {
        List<String> directions = service.getAllUniqueDestinations();
        return ResponseEntity.ok(directions);
    }
    @GetMapping("/region")
    public ResponseEntity<List<String>> getAllRegions() {
        List<String> regions = service.getAllUniqueRegions();
        return ResponseEntity.ok(regions);
    }
}