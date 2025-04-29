package org.example.tourservice.tourREVIEWrepository;

import org.example.tourservice.tourREVIEWentity.TourReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TourReviewRepository extends JpaRepository<TourReview, Long> {
    List<TourReview> findByTourId(Long tourId);
}
