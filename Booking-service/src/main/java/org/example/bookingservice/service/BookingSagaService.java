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
    @Autowired
    private RestTemplate restV;

    @KafkaListener(topics = "booking.tour.started", groupId = "booking-group")
    public void handleBookingStarted(TourBookingStartedEvent event) {
        System.out.println("📥 Получено событие booking.tour.started: " + event);

        String url = "http://tour-service/tours/" + event.getTourId() + "/check-availability";

        try {
            ResponseEntity<Boolean> response = restV.getForEntity(url, Boolean.class);

            if (Boolean.TRUE.equals(response.getBody())) {
                kafka.send("booking.tour.success", event.getSagaId(),
                        new TourBookingSucceededEvent(event.getSagaId(), event.getTourId()));
                System.out.println("✅ Тур доступен — отправлен booking.tour.success");
            } else {
                kafka.send("booking.tour.failed", event.getSagaId(),
                        new TourBookingFailedEvent(event.getSagaId(), event.getTourId(), "No spots available"));
                System.out.println("❌ Мест нет — отправлен booking.tour.failed");
            }

        } catch (Exception ex) {
            kafka.send("booking.tour.failed", event.getSagaId(),
                    new TourBookingFailedEvent(event.getSagaId(), event.getTourId(), "Tour not found or error"));
            System.out.println("❌ Ошибка при обращении к tour-service — booking.tour.failed");
        }
    }
}