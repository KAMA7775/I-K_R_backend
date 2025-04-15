package org.example.bookingservice.service;

import org.example.bookingservice.entity.TourBookingEntity;
import org.example.bookingservice.repository.TourBookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TourBookingService {
    private final TourBookingRepository tourBookingRepository;


    public TourBookingService(TourBookingRepository tourBookingRepository) {
        this.tourBookingRepository = tourBookingRepository;
    }

    public List<TourBookingEntity> getAllTourBookings() {
        return tourBookingRepository.findAll();
    }

}
