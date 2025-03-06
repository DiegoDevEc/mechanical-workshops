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

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentAvailableCronService {

    private static final LocalTime MONDAY_FRIDAY_START = LocalTime.of(8, 0);
    private static final LocalTime MONDAY_FRIDAY_END = LocalTime.of(17, 0);
    private static final LocalTime SATURDAY_START = LocalTime.of(8, 0);
    private static final LocalTime SATURDAY_END = LocalTime.of(12, 0);
    private static final int MAX_PARALLEL = 3;  // Three parallel appointments per hour
    private static final int APPOINTMENT_DURATION_MINUTES = 60;  // Duration of 1 hour per appointment

    private AvailableAppointmentRepository availableAppointmentRepository; // Repository to save appointments

    // Run every 7 days at 6:00 AM on Monday
    @Scheduled(cron = "0 0 6 ? * MON")
    public void generateAppointmentsFor7Days() {
        LocalDate startDate = LocalDate.now().plusDays(1); // Start from tomorrow
        LocalDate endDate = startDate.plusDays(6); // Seven days of appointments

        List<AvailableAppointment> availableAppointments = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isSaturday = date.getDayOfWeek() == DayOfWeek.SATURDAY;
            boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;

            if (!isSunday) { // No appointments on Sundays
                LocalTime start = isSaturday ? SATURDAY_START : MONDAY_FRIDAY_START;
                LocalTime end = isSaturday ? SATURDAY_END : MONDAY_FRIDAY_END;

                LocalTime appointmentStart = start;
                // Generate appointments progressively for each hour
                while (start.isBefore(end)) {
                    // Generate three parallel appointments per hour
                    for (int i = 0; i < MAX_PARALLEL; i++) { // Generate three appointments per hour

                        availableAppointments.add(AvailableAppointment.builder()
                                .dateAvailable(date)
                                .timeAvailable(appointmentStart)  // Appointment start time
                                .duration(LocalTime.of(1, 0))  // 1-hour duration for each appointment
                                .status(Status.ACT)  // "ACT" status
                                .dayOfWeek(com.mechanical.workshops.enums.DayOfWeek.valueOf(date.getDayOfWeek().toString().substring(0, 3)))  // Day of the week abbreviation
                                .build());
                    }

                    appointmentStart = appointmentStart.plusMinutes(APPOINTMENT_DURATION_MINUTES);

                    // After generating the three appointments, move to the next hour block
                    start = start.plusMinutes(APPOINTMENT_DURATION_MINUTES);
                }
            }
        }

        saveAppointments(availableAppointments);
        log.info("Appointments generated for the week from {} to {}", startDate, endDate);
    }

    // Method to check if it's the first time the app is run
    @PostConstruct
    public void checkFirstRun() {
        if (availableAppointmentRepository.count() == 0) {
            generateAppointmentsFor7Days(); // Generate appointments if it's the first run
        }
    }

    private void saveAppointments(List<AvailableAppointment> availableAppointments) {
        availableAppointmentRepository.saveAll(availableAppointments); // Save all generated appointments
    }
}
