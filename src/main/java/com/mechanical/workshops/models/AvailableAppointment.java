package com.mechanical.workshops.models;

import com.mechanical.workshops.enums.DayOfWeek;
import com.mechanical.workshops.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "available_appointments")
public class AvailableAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "date_available", nullable = false)
    private LocalDate dateAvailable;  // Fecha de disponibilidad (solo el día, sin la hora)

    @Column(name = "time_available", nullable = false)
    private LocalTime timeAvailable;  // Hora de disponibilidad (inicio del turno)

    @Column(name = "duration", nullable = false)
    private LocalTime duration = LocalTime.of(1, 0);  // Duración del turno (1 hora por cita)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;  // Estado de la disponibilidad

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;  // Día de la semana
}

