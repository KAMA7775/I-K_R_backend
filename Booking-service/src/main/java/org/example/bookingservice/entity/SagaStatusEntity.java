package org.example.bookingservice.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "saga_status")
public class SagaStatusEntity {

    @Id
    private String sagaId ;

    private String status; // TOUR_BOOKED, HOTEL_BOOKED, FAILED, COMPENSATED

    private String resourceType; // "TOUR", "HOTEL", "EVENT"
    private Long resourceId;

    private Instant createdAt;



    public SagaStatusEntity(String sagaId, String status, String resourceType, Long resourceId, Instant createdAt) {
        this.sagaId = sagaId;
        this.status = status;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.createdAt = createdAt;
    }

    public SagaStatusEntity() {

    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getSagaId() {
        return sagaId;
    }

    public void setSagaId(String sagaId) {
        this.sagaId = sagaId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}