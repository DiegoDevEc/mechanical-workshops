package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.AvailableAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvailableAppointmentRepository extends JpaRepository<AvailableAppointment, UUID> {

    @Query("SELECT a FROM AvailableAppointment a " +
            "WHERE a.status = :status " +
            "AND (a.dateAvailable > :date OR (a.dateAvailable = :date AND a.timeAvailable >= :time))")
    Page<AvailableAppointment> findAvailableAppointmentsFrom(
            @Param("status") Status status,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            Pageable pageable);

    int deleteByDateAvailableBeforeAndStatus(LocalDate date, Status status);

    boolean existsByDateAvailable(LocalDate date);
    List<AvailableAppointment> findByDateAvailableAfterAndStatus(LocalDate date, Status status);
    List<AvailableAppointment> findByDateAvailableAndStatus(LocalDate date, Status status);
}
