package org.example.bookingservice.service;

import com.example.saga.CancelTourBookingEvent;
import com.example.saga.TourBookingFailedEvent;
import com.example.saga.TourBookingStartedEvent;
import com.example.saga.TourBookingSucceededEvent;
import org.example.bookingservice.dto.TourBookingDto;
import org.example.bookingservice.entity.SagaStatusEntity;
import org.example.bookingservice.repository.SagaStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

@Service
public class BookingSagaService {
    private final KafkaTemplate<String, Object> kafka;
    private final SagaStatusRepository sagaRepo;

    public BookingSagaService(KafkaTemplate<String, Object> kafka, SagaStatusRepository sagaRepo) {
        this.kafka = kafka;
        this.sagaRepo = sagaRepo;
    }

    @KafkaListener(topics = "booking.tour.success", groupId = "booking-group")
    public void handleTourSuccess(TourBookingSucceededEvent event) {
        sagaRepo.save(new SagaStatusEntity(
                event.getSagaId(),
                "TOUR_BOOKED",
                "TOUR",
                event.getTourId(),
                Instant.now()
        ));
    }

    @KafkaListener(topics = "booking.tour.failed", groupId = "booking-group")
    public void handleTourFailed(TourBookingFailedEvent event) {
        sagaRepo.save(new SagaStatusEntity(
                event.getSagaId(),
                "TOUR_FAILED",
                "TOUR",
                event.getTourId(),
                Instant.now()
        ));
        kafka.send("booking.tour.compensate", new CancelTourBookingEvent(event.getSagaId(), event.getTourId()));
    }
}