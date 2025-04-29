package org.example.tourservice.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tour")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;
    private String region;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime dateTime;
    private Duration duration;
    private int price;
    private int quantity;

    @ElementCollection
    @Column(columnDefinition = "TEXT")
    private List<String> imageUrl;
    private boolean deleted = false;
    public Tour(){

    }
    public Tour(Long id, String destination, String region, String description, LocalDateTime dateTime, Duration duration, int price, int quantity, List<String> imageUrl , boolean deleted){
        this.id= id;
        this.destination = destination;
        this.region= region;
        this.description = description;
        this.dateTime=dateTime;
        this.duration= duration;
        this.price=price;
        this.quantity= quantity;
        this.imageUrl=imageUrl;
        this.deleted=deleted;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region){
        this.region = region;
    }
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
