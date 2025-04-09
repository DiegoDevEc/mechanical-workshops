package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.models.Appointment;
import com.mechanical.workshops.models.Attendance;
import com.mechanical.workshops.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    @Query("SELECT a FROM Attendance a " +
            "WHERE (:status IS NULL OR a.status = :status) " +
            "AND a.technician = :person ")
    Page<Attendance> findByTechnicalAllAndStatus(
            @Param("status") StatusAttendance status,
            @Param("person") Person person,
            Pageable pageable);

    @Query("SELECT a FROM Attendance a " +
            "WHERE (:status IS NULL OR a.status = :status) " +
            "AND a.appointment.client = :person ")
    Page<Attendance> findByClientAllAndStatus(
            @Param("status") StatusAttendance status,
            @Param("person") Person person,
            Pageable pageable);

    @Query("SELECT a FROM Attendance a " +
            "WHERE (:statuses IS NULL OR a.status IN :statuses) " +
            "AND a.technician = :person")
    Page<Attendance> findByTechnicianAndStatuses(
            @Param("statuses") List<StatusAttendance> statuses,
            @Param("person") Person person,
            Pageable pageable);

    Optional<Attendance> findByAppointment(Appointment appointment);
    Optional<Attendance> findByCode(String code);
}
