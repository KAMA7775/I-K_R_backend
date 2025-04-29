package org.example.moderatorservice.service;

import com.example.saga.tourReview.TourReviewCreatedEvent;
import com.example.saga.tourReview.TourReviewModeratedEvent;
import org.example.moderatorservice.entity.ReviewStatus;
import org.example.moderatorservice.entity.TourReviewModeration;
import org.example.moderatorservice.repository.TourReviewModerationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ReviewModeration {

    @Autowired
    private TourReviewModerationRepository repository;

    @KafkaListener(topics = "review.new", groupId = "moderator-group")
    public void onReviewCreated(TourReviewCreatedEvent event) {
        System.out.println("получен новый отзыв для модерации (reviewId = " + event.getReviewId() + ")");

        TourReviewModeration moderation = new TourReviewModeration();
        moderation.setReviewId(event.getReviewId());
        moderation.setUserId(event.getUserId());
        moderation.setTourId(event.getTourId());
        moderation.setComment(event.getComment());
        moderation.setRating(event.getRating());
        moderation.setStatus(ReviewStatus.PENDING);

        repository.save(moderation);
        System.out.println("отзыв сохранён со статусом PENDING");
    }
}
