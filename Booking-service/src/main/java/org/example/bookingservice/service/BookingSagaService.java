package org.example.bookingservice.service;


import com.example.saga.*;
import org.example.bookingservice.entity.HotelBookingEntity;
import org.example.bookingservice.entity.SagaStatusEntity;
import org.example.bookingservice.entity.TourBookingEntity;
import org.example.bookingservice.repository.HotelBookingRepository;
import org.example.bookingservice.repository.SagaStatusRepository;
import org.example.bookingservice.repository.TourBookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class BookingSagaService {
    private final KafkaTemplate<String, Object> kafka;
    private final SagaStatusRepository sagaRepo;
    private final TourBookingRepository tourBookingRepo;
    private final HotelBookingRepository hotelBookingRepo;

    public BookingSagaService(
            KafkaTemplate<String, Object> kafka,
            SagaStatusRepository sagaRepo,
            TourBookingRepository tourBookingRepo,
            HotelBookingRepository hotelBookingRepo
    ) {
        this.kafka = kafka;
        this.sagaRepo = sagaRepo;
        this.tourBookingRepo = tourBookingRepo;
        this.hotelBookingRepo = hotelBookingRepo;
    }

    @KafkaListener(topics = "booking.tour.success")
    public void handleTourSuccess(TourBookingSucceededEvent event) {

        SagaStatusEntity sagaStatus = new SagaStatusEntity(
                event.getSagaId(),
                "TOUR_BOOKED",
                "TOUR",
                event.getTourId(),
                Instant.now()
        );
        sagaRepo.save(sagaStatus);

        TourBookingEntity booking = tourBookingRepo.findById(event.getBookingId()).orElse(null);
        if (booking != null) {
            booking.setStatus("TOUR_BOOKED");
            booking.setBookingTime(LocalDateTime.now());
            tourBookingRepo.save(booking);
        }

        PaymentStartedEvent paymentEvent = new PaymentStartedEvent(
                event.getSagaId(),
                event.getUserId(),
                event.getBookingId(),
                event.getTourId(),
                "TOUR",
                event.getTotalAmount(),
                "USD"
        );

        kafka.send("payment.started", paymentEvent);
    }

    @KafkaListener(topics = "payment.success")
    public void handlePaymentSuccess(PaymentSuccessEvent event) {
        SagaStatusEntity saga = sagaRepo.findById(event.getSagaId()).orElse(null);
        if (saga == null) return;

        saga.setStatus("PAID");
        sagaRepo.save(saga);

        switch (saga.getResourceType()) {
            case "TOUR" -> {
                TourBookingEntity booking = tourBookingRepo.findById(event.getBookingId()).orElse(null);
                if (booking != null) {
                    booking.setStatus("PAID");
                    tourBookingRepo.save(booking);
                }
            }
            case "HOTEL" -> {
                HotelBookingEntity booking = hotelBookingRepo.findById(event.getBookingId()).orElse(null);
                if (booking != null) {
                    booking.setStatus("PAID");
                    hotelBookingRepo.save(booking);
                }
            }
        }
    }

    @KafkaListener(topics = "payment.failed")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        SagaStatusEntity saga = sagaRepo.findById(event.getSagaId()).orElse(null);
        if (saga != null) {
            saga.setStatus("PAYMENT_FAILED");
            sagaRepo.save(saga);

            switch (saga.getResourceType()) {
                case "TOUR" -> {
                    TourBookingEntity booking = tourBookingRepo.findById(event.getBookingId()).orElse(null);
                    if (booking != null) {
                        booking.setStatus("FAILED");
                        tourBookingRepo.save(booking);
                    }
                }
                case "HOTEL" -> {
                    HotelBookingEntity booking = hotelBookingRepo.findById(event.getBookingId()).orElse(null);
                    if (booking != null) {
                        booking.setStatus("FAILED");
                        hotelBookingRepo.save(booking);
                    }
                }
            }
        }
    }
    private static final Logger log = LoggerFactory.getLogger(BookingSagaService.class);
    @KafkaListener(topics = "booking.tour.failed")
    public void handleTourFailed(TourBookingFailedEvent event) {
        log.warn(" Сага провалилась: sagaId={}, reason={}", event.getSagaId(), event.getReason());

        SagaStatusEntity saga = sagaRepo.findById(event.getSagaId()).orElse(null);
        if (saga == null) {
            saga = new SagaStatusEntity(
                    event.getSagaId(),
                    "TOUR_FAILED",
                    "TOUR",
                    event.getTourId(),
                    Instant.now()
            );
        } else {
            saga.setStatus("TOUR_FAILED");
        }
        sagaRepo.save(saga);

        if (event.getBookingId() != null) {
            TourBookingEntity booking = tourBookingRepo.findById(event.getBookingId()).orElse(null);
            if (booking != null) {
                booking.setStatus("FAILED");
                tourBookingRepo.save(booking);
            } else {
                log.warn("Не найдено бронирование для bookingId={}", event.getBookingId());
            }
        } else {
            log.warn("bookingId = null в TourBookingFailedEvent, sagaId={}", event.getSagaId());
        }

        kafka.send("booking.tour.compensate", new CancelTourBookingEvent(
                event.getSagaId(),
                event.getTourId(),
                event.getBookingId()
        ));
    }

}
