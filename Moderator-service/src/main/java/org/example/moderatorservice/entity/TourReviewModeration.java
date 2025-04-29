package org.example.moderatorservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderation_reviews")
public class TourReviewModeration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewId;
    private Long userId;
    private Long tourId;

    @Column(length = 1000)
    private String comment;

    private int rating;

    private LocalDateTime receivedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
    public TourReviewModeration(){}
    public TourReviewModeration(Long id, Long reviewId, Long userId, Long tourId, String comment, int rating, LocalDateTime receivedAt, ReviewStatus status){
        this.id=id;
        this.reviewId=reviewId;
        this.userId=userId;
        this.tourId=tourId;
        this.comment=comment;
        this.rating=rating;
        this.receivedAt=receivedAt;
        this.status=status;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

}
