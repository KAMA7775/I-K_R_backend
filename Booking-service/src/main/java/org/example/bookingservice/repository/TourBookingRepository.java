package org.example.bookingservice.repository;

import org.example.bookingservice.entity.TourBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBookingEntity, Long> {
}

