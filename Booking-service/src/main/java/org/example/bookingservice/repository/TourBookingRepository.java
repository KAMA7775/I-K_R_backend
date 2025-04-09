package org.example.bookingservice.repository;

import org.example.bookingservice.entity.TourBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourBookingRepository extends JpaRepository<TourBookingEntity, Long> {
    Optional<TourBookingEntity> findByTourIdAndUserId(Long tourId, String userId);
    List<TourBookingEntity> findByUserId(String userId);

}

