package org.example.bookingservice.repository;

import org.example.bookingservice.entity.HotelBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBookingEntity, Long> {
    List<HotelBookingEntity> findByUserId(String userId);
}

