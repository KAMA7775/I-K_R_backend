package org.example.tourservice.service;

import org.example.tourservice.entity.Tour;
import org.example.tourservice.repository.TourRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {
    private TourRepository repo;
    public TourService(TourRepository repo){
        this.repo=repo;
    }
    public List<Tour>getAllTour(){
        return repo.findAll();
    }
    public Optional<Tour> getTourById(Long id){
        return repo.findAllById(id);
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
                    return repo.save(tour);
                }).orElseThrow(() -> new RuntimeException("tour not found"));
    }
}
