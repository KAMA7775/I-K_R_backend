package org.example.moderatorservice;



import com.example.saga.tourReview.TourReviewModeratedEvent;
import org.example.moderatorservice.entity.ReviewStatus;
import org.example.moderatorservice.entity.TourReviewModeration;
import org.example.moderatorservice.repository.TourReviewModerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/moderation")
public class ReviewModerationController {

    @Autowired
    private TourReviewModerationRepository repo;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/pending")
    public List<TourReviewModeration> getPendingReviews() {
        return repo.findByStatus(ReviewStatus.PENDING);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveReview(@PathVariable Long id) {
        return moderateReview(id, ReviewStatus.APPROVED);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectReview(@PathVariable Long id) {
        return moderateReview(id, ReviewStatus.REJECTED);
    }

    private ResponseEntity<Void> moderateReview(Long id, ReviewStatus decision) {
        TourReviewModeration review = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден"));

        review.setStatus(decision);
        repo.save(review);

        kafkaTemplate.send("review.moderated",
                new TourReviewModeratedEvent(review.getReviewId(), decision.name()));

        return ResponseEntity.ok().build();
    }}


