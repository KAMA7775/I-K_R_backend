package org.example.tourservice.tourREVIEWdto;

public class TourReviewDto {
        private Long tourId;
        private String comment;
        private int rating;
        public TourReviewDto(){
        }
        public TourReviewDto(Long tourId, String comment, int rating){
            this.tourId=tourId;
            this.comment=comment;
            this.rating=rating;
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
    }


