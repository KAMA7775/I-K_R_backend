package org.example.tourservice.tourREVIEWservice;

import com.example.saga.tourReview.TourReviewCreatedEvent;
import org.example.tourservice.tourREVIEWdto.TourReviewDto;
import org.example.tourservice.tourREVIEWentity.TourReview;
import org.example.tourservice.tourREVIEWrepository.TourReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourReviewService {
    @Autowired
    private TourReviewRepository reviewRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void createReview(TourReviewDto dto, Long userId) {
        TourReview review = new TourReview();
        review.setTourId(dto.getTourId());
        review.setUserId(userId);
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        kafkaTemplate.send("review.new", new TourReviewCreatedEvent(
                review.getId(),
                userId,
                dto.getTourId(),
                dto.getComment(),
                dto.getRating()
        ));
    }


    public List<TourReviewDto> getReviewsForTour(Long tourId) {
        return reviewRepository.findByTourId(tourId).stream().map(r -> {
            TourReviewDto dto = new TourReviewDto();
            dto.setTourId(r.getTourId());
            dto.setComment(r.getComment());
            dto.setRating(r.getRating());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
