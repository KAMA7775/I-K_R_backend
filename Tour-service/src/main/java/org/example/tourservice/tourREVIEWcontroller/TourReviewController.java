package org.example.tourservice.tourREVIEWcontroller;
import org.example.tourservice.tourREVIEWdto.TourReviewDto;
import org.example.tourservice.tourREVIEWservice.TourReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class TourReviewController {
    @Autowired
    private TourReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> addReview(@RequestBody TourReviewDto dto,
                                          Authentication authentication) {
        String userIdStr = (String) authentication.getDetails();
        Long userId = Long.parseLong(userIdStr);

        reviewService.createReview(dto, userId);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/tour/{tourId}")
    public List<TourReviewDto> getReviewsForTour(@PathVariable Long tourId) {
        return reviewService.getReviewsForTour(tourId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
