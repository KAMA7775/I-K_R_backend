package org.example.tourservice.repository;

import org.example.tourservice.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour,Long> {

    Optional<Tour> findById(Long id);
    Optional<Tour> findByDestination(String destination);
    @Query("SELECT DISTINCT t.destination FROM Tour t WHERE t.deleted = false")
    List<String> findAllUniqueDestinations();
    @Query("SELECT DISTINCT t.region FROM Tour t WHERE t.deleted = false")
    List<String> findAllUniqueRegions();

    @Query("SELECT t FROM Tour t WHERE " +
            "(:destination IS NULL OR t.destination = :destination) AND " +
            "(:region IS NULL OR t.region = :region) AND " +
            "(:startDate IS NULL OR t.dateTime >= :startDate) AND " +
            "(:duration IS NULL OR t.duration >= :duration)")
    List<Tour> filterTours(
            @Param("destination") String destination,
            @Param("region") String region,
            @Param("startDate") LocalDateTime startDate,
            @Param("duration") Duration duration
    );

}
