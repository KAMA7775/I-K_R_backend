package org.example.tourservice.service;

import com.example.saga.CancelTourBookingEvent;
import com.example.saga.TourBookingFailedEvent;
import com.example.saga.TourBookingStartedEvent;
import com.example.saga.TourBookingSucceededEvent;
import com.example.saga.tourInfo.TourInfoRequestEvent;
import com.example.saga.tourInfo.TourInfoResponseEvent;
import org.example.tourservice.entity.Tour;
import org.example.tourservice.repository.TourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
@Service
public class TourSagaStarter {
    private final KafkaTemplate<String, Object> kafka;
    private final TourService tourService;
    private TourRepository tourRepository;

    public TourSagaStarter(KafkaTemplate<String, Object> kafka, TourService tourService, TourRepository tourRepository) {
        this.kafka = kafka;
        this.tourService = tourService;
        this.tourRepository=tourRepository;
    }
    private static final Logger log = LoggerFactory.getLogger(TourSagaStarter.class);
    @KafkaListener(topics = "booking.tour.started", groupId = "tour-group")
    public void handleTourBookingStarted(TourBookingStartedEvent event) {
        Long tourId = event.getTourId();
        int quantity = event.getQuantity();
        log.info("🎫 Получено событие booking.tour.started: sagaId={}, tourId={}, bookingId={}",
                event.getSagaId(), event.getTourId(), event.getBookingId());

        Optional<Tour> optionalTour = tourRepository.findById(tourId);
        if (optionalTour.isEmpty()) {
            kafka.send("booking.tour.failed", event.getSagaId(), new TourBookingFailedEvent());
            return;
        }

        Tour tour = optionalTour.get();

        boolean reserved = tourService.reserveTour(tourId, quantity);
        if (reserved) {
            double totalAmount = tour.getPrice() * quantity;

            kafka.send("booking.tour.success", event.getSagaId(), new TourBookingSucceededEvent(
                    event.getSagaId(),
                    event.getUserId(),
                    event.getBookingId(),
                    event.getTourId(),
                    event.getQuantity(),
                    totalAmount
            ));
        } else {
            kafka.send("booking.tour.failed", event.getSagaId(),
                    new TourBookingFailedEvent(
                            event.getSagaId(),
                            event.getTourId(),
                            event.getUserId(),
                            event.getBookingId(),
                            "Tour reservation failed"
                    )
            );
        }
    }

    @KafkaListener(topics = "booking.tour.compensate", groupId = "tour-group")
    public void handleCompensate(CancelTourBookingEvent event) {
        tourService.rollbackReservation(event.getTourId());
    }

    @KafkaListener(topics = "tour.info.request", groupId = "tour-group")
    public void handleTourInfoRequest(TourInfoRequestEvent event) {
        Tour tour = tourRepository.findById(event.getTourId()).orElseThrow();
        kafka.send("tour.info.response", new TourInfoResponseEvent(
        ));
    }
}
