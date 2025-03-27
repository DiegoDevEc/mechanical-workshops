package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.models.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Page<Vehicle> findByClientAndStatus(Person client, Status status, Pageable pageable);
    Optional<Vehicle> findByClient(Person client);
}
