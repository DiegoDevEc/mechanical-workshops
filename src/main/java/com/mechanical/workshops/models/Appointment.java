package com.mechanical.workshops.models;

import com.mechanical.workshops.enums.StatusAppointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Person client;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate dateAppointment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAppointment status;

    @ManyToOne
    @JoinColumn(name = "available_appointment_id")
    private AvailableAppointment availableAppointment;
}

