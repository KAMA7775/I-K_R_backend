package org.example.tourservice.service;

import org.example.tourservice.dto.TourDto;
import org.example.tourservice.entity.Tour;
import org.example.tourservice.repository.TourRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourService {
    private TourRepository repo;
    public TourService(TourRepository repo){
        this.repo=repo;
    }
    public List<TourDto>getAllTour(){
        return repo.findAll().stream().map(tour -> new TourDto(
                        tour.getDestination(),
                        tour.getRegion(),
                        tour.getDescription(),
                        tour.getDateTime(),
                        tour.getDuration(),
                        tour.getPrice(),
                        tour.getQuantity(),
                        tour.getImageUrl()

                ))
                .collect(Collectors.toList());
    }
    public Optional<Tour> getTourById(Long id){
        return repo.findById(id);
    }
    public Optional<Tour> getTourByDestination(String destination){
        return repo.findByDestination(destination);
    }
    public Tour create(Tour tour){
        return repo.save(tour);
    }
    public void deleteTour(Long id){
        repo.deleteById(id);
    }
    public Tour update(Long id, Tour updated){
        return repo.findById(id)
                .map(tour -> {
                    tour.setDestination(updated.getDestination());
                    tour.setRegion(updated.getRegion());
                    tour.setDescription(updated.getDescription());
                    tour.setDateTime(updated.getDateTime());
                    tour.setDuration(updated.getDuration());
                    tour.setPrice(updated.getPrice());
                    tour.setImageUrl(updated.getImageUrl());
                    tour.setQuantity(updated.getQuantity());
                    return repo.save(tour);
                }).orElseThrow(() -> new RuntimeException("tour not found"));
    }
    public boolean reserveTour(Long tourId, int quantity) {
        Optional<Tour> optionalTour = repo.findById(tourId);
        if (optionalTour.isEmpty()) return false;

        Tour tour = optionalTour.get();
        if (tour.getQuantity() < quantity) return false;

        tour.setQuantity(tour.getQuantity() - quantity);
        repo.save(tour);
        return true;
    }
    public void rollbackReservation(Long tourId) {
        repo.findById(tourId).ifPresent(tour -> {
            tour.setQuantity(tour.getQuantity() + 1);
            repo.save(tour);
        });
    }
}
