package org.example.tourservice.repository;

import org.example.tourservice.dto.TourDto;
import org.example.tourservice.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour,Long> {

    Optional<Tour> findById(Long id);
    Optional<Tour> findByDestination(String destination);
}
