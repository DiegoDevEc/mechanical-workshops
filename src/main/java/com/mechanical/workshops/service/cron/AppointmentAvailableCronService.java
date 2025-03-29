package com.mechanical.workshops.service.cron;


import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.AvailableAppointment;
import com.mechanical.workshops.repository.AvailableAppointmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentAvailableCronService {

    private static final LocalTime MONDAY_FRIDAY_START = LocalTime.of(8, 0);
    private static final LocalTime MONDAY_FRIDAY_END = LocalTime.of(17, 0);
    private static final LocalTime SATURDAY_START = LocalTime.of(8, 0);
    private static final LocalTime SATURDAY_END = LocalTime.of(12, 0);
    private static final int MAX_PARALLEL = 3;
    private static final int APPOINTMENT_DURATION_MINUTES = 20;

    private final AvailableAppointmentRepository availableAppointmentRepository;
    private final Random random = new Random();

    @Scheduled(cron = "0 0 22 * * ?")
    public void generateAppointmentsFor7Days() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.plusDays(1);
        LocalDate endDate = startDate.plusDays(6);

        // Eliminar turnos antiguos
        cleanOldAppointments(today);

        List<AvailableAppointment> availableAppointments = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isSaturday = date.getDayOfWeek() == DayOfWeek.SATURDAY;
            boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;

            if (!isSunday) { // No hay turnos los domingos

                if (availableAppointmentRepository.existsByDateAvailable(date)) {
                    log.info("Appointments already exist for date {}", date);
                    continue; // Omitir la generación de turnos para esta fecha
                }

                LocalTime start = isSaturday ? SATURDAY_START : MONDAY_FRIDAY_START;
                LocalTime end = isSaturday ? SATURDAY_END : MONDAY_FRIDAY_END;

                LocalTime appointmentStart = start;
                while (appointmentStart.isBefore(end)) {
                    for (int i = 0; i < MAX_PARALLEL; i++) {
                        availableAppointments.add(AvailableAppointment.builder()
                                .dateAvailable(date)
                                .timeAvailable(appointmentStart)
                                .duration(LocalTime.of(0, APPOINTMENT_DURATION_MINUTES))
                                .status(Status.ACT)
                                .dayOfWeek(getDayInSpanish(date.getDayOfWeek()))
                                .code(generateAppointmentCode(date))
                                .build());
                    }
                    appointmentStart = appointmentStart.plusMinutes(APPOINTMENT_DURATION_MINUTES);
                }
            }
        }
        saveAppointments(availableAppointments);
        log.info("Appointments generated from {} to {}", startDate, endDate);
    }

    @PostConstruct
    public void checkFirstRun() {
        if (availableAppointmentRepository.count() == 0) {
            generateAppointmentsFor7Days();
        }
    }

    private void saveAppointments(List<AvailableAppointment> availableAppointments) {
        availableAppointmentRepository.saveAll(availableAppointments);
    }

    private void cleanOldAppointments(LocalDate today) {
        int deletedCount = availableAppointmentRepository.deleteByDateAvailableBeforeAndStatus(today, Status.ACT);
        log.info("Deleted {} old appointments.", deletedCount);
    }

    private String getDayInSpanish(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            default -> "Domingo";
        };
    }

    private String generateAppointmentCode(LocalDate date) {
        String dayNumber = String.format("%02d", date.getDayOfMonth());
        String dayAbbreviation = getDayInSpanish(date.getDayOfWeek()).substring(0, 3);
        int randomFourDigit = random.nextInt(9000) + 1000; // Número aleatorio de 4 dígitos
        return dayNumber + dayAbbreviation + randomFourDigit;
    }
}
