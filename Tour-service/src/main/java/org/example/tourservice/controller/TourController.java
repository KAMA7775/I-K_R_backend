package org.example.tourservice.controller;

import org.example.tourservice.entity.Tour;
import org.example.tourservice.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {
    private TourService service;
    public TourController(TourService service){
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<Tour>> getAllTour(){
        return ResponseEntity.ok(service.getAllTour());
    }
    @GetMapping("/{destination}")
    public ResponseEntity<?> getTourByDestination(@PathVariable String destination){
        return service.getTourByDestination(destination)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable Long id){
        return service.getTourById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Tour> create(@RequestBody Tour tour) {
        return ResponseEntity.ok(service.create(tour));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tour> update(@PathVariable Long id, @RequestBody Tour updated) {
        return ResponseEntity.ok(service.update(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTour(@PathVariable Long id) {
        service.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

}
