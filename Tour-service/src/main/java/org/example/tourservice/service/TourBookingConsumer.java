package org.example.tourservice.service;

import org.example.tourservice.dto.TourBookingDto;
import org.example.tourservice.dto.TourBookingResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TourBookingConsumer {
    private final TourService tourService;
    private final KafkaTemplate<String, TourBookingResponse> kafkaTemplate;

    public TourBookingConsumer(TourService tourService, KafkaTemplate<String, TourBookingResponse> kafkaTemplate) {
        this.tourService = tourService;
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(topics = "booking.tour.request", groupId = "tour-group")
    public void handleBookingRequest(TourBookingDto request) {
        boolean reserved = tourService.reserveTour(request.getTourId(), request.getQuantity());

        TourBookingResponse response = new TourBookingResponse(
                reserved ? "SUCCESS" : "ERROR",
                reserved ? "Booking confirmed" : "Not enough spots or tour not found",
                request.getUserId(),
                request.getTourId()
        );
        kafkaTemplate.send("booking.tour.response", response);
    }
}
