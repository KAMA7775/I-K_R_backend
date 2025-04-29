package org.example.moderatorservice.repository;

import org.example.moderatorservice.entity.ReviewStatus;
import org.example.moderatorservice.entity.TourReviewModeration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourReviewModerationRepository extends JpaRepository<TourReviewModeration, Long> {
    List<TourReviewModeration> findByStatus(ReviewStatus status);

}
