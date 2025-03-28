package org.example.bookingservice.repository;

import org.example.bookingservice.entity.SagaStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaStatusRepository extends JpaRepository<SagaStatusEntity, String> {
}

