package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.models.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("SELECT a FROM Appointment a " +
            "WHERE (:status IS NULL OR a.status = :status) " +
            "AND a.dateAppointment >= :startDate " +
            "AND (COALESCE(:endDate, a.dateAppointment) IS NULL OR a.dateAppointment <= COALESCE(:endDate, a.dateAppointment))")
    Page<Appointment> findAppointmentsInRangeAndStatus(
            @Param("status") StatusAppointment status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

}
