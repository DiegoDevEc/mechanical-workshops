package com.mechanical.workshops.repository;

import com.mechanical.workshops.models.Appointment;
import com.mechanical.workshops.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByAppointment(Appointment appointment);
}
