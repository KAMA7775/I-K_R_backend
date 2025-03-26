package org.example.bookingservice.service;

import org.example.bookingservice.dto.TourBookingDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingProducer {
    private final KafkaTemplate<String, TourBookingDto> kafkaTemplate;

    public BookingProducer(KafkaTemplate<String, TourBookingDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTourBookingRequest(TourBookingDto request) {
        kafkaTemplate.send("booking.tour.request", request);
    }
}
