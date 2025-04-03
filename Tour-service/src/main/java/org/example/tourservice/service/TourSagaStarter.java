package org.example.tourservice.service;

import com.example.saga.CancelTourBookingEvent;
import com.example.saga.TourBookingFailedEvent;
import com.example.saga.TourBookingStartedEvent;
import com.example.saga.TourBookingSucceededEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class TourSagaStarter {
    private final KafkaTemplate<String, Object> kafka;
    private final TourService tourService;

    public TourSagaStarter(KafkaTemplate<String, Object> kafka, TourService tourService) {
        this.kafka = kafka;
        this.tourService = tourService;
    }
    private static final Logger log = LoggerFactory.getLogger(TourSagaStarter.class);
    @KafkaListener(topics = "booking.tour.started", groupId = "tour-group")
    public void handleTourBookingStarted(TourBookingStartedEvent event) {
        log.info("📥 Получено событие: {}", event);

        boolean available = tourService.reserveTour(event.getTourId(), 1); // например

        if (available) {
            kafka.send("booking.tour.success", event.getSagaId(),
                    new TourBookingSucceededEvent(event.getSagaId(), event.getTourId()));
        } else {
            kafka.send("booking.tour.failed", event.getSagaId(),
                    new TourBookingFailedEvent(event.getSagaId(), event.getTourId(), "No spots"));
        }
    }


    @KafkaListener(topics = "booking.tour.compensate")
    public void handleCompensation(CancelTourBookingEvent event) {
        tourService.rollbackReservation(event.getTourId());
    }
}
