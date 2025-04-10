package com.mechanical.workshops.models;

import com.mechanical.workshops.auditable.AuditListener;
import com.mechanical.workshops.enums.StatusAttendance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attendances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditListener.class)
public class Attendance {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "received_by", nullable = false)
    private Person receivedBy; // Persona que recibió el vehículo

    @ManyToOne
    @JoinColumn(name = "technician_id", nullable = false)
    private Person technician; // Técnico encargado

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String comments;

    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAttendance status;
}
