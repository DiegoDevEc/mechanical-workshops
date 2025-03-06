package com.mechanical.workshops.repository;

import com.mechanical.workshops.models.AvailableAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AvailableAppointmentRepository extends JpaRepository<AvailableAppointment, UUID> {

}
