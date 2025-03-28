package org.example.tourservice.service;

import com.example.saga.CancelTourBookingEvent;
import com.example.saga.TourBookingFailedEvent;
import com.example.saga.TourBookingSucceededEvent;
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

    public String startTourBooking(Long tourId, String userId, int quantity) {
        boolean available = tourService.reserveTour(tourId, quantity);
        String sagaId = UUID.randomUUID().toString();

        if (!available) {
            kafka.send("booking.tour.failed", sagaId, new TourBookingFailedEvent(sagaId, tourId, "Not enough spots"));
        } else {
            kafka.send("booking.tour.success", sagaId, new TourBookingSucceededEvent(sagaId, tourId));
        }

        return sagaId;
    }

    @KafkaListener(topics = "booking.tour.compensate")
    public void handleCompensation(CancelTourBookingEvent event) {
        tourService.rollbackReservation(event.getTourId());
    }
}
