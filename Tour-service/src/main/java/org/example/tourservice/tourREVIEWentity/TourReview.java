package org.example.tourservice.tourREVIEWentity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tour_reviews")
public class TourReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tourId;
    private Long userId;

    @Column(length = 1000)
    private String comment;

    private int rating;

    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private TourReviewStatus status = TourReviewStatus.PENDING;
    public TourReview(){}
    public TourReview(Long id, Long tourId, Long userId, String comment, int rating,LocalDateTime createdAt,  TourReviewStatus status ){
        this.id=id;
        this.tourId=tourId;
        this.userId=userId;
        this.comment=comment;
        this.rating=rating;
        this.createdAt=createdAt;
        this.status=status;

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTourId() {
        return tourId;
    }

    public void setTourId(Long tourId) {
        this.tourId = tourId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TourReviewStatus getStatus() {
        return status;
    }

    public void setStatus(TourReviewStatus status) {
        this.status = status;
    }
}
