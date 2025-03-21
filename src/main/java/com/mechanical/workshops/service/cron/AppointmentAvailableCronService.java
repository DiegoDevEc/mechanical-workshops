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
    private static final int MAX_PARALLEL = 3;
    private static final int APPOINTMENT_DURATION_MINUTES = 60;

    private AvailableAppointmentRepository availableAppointmentRepository; // Repository to save appointments


    @Scheduled(cron = "0 0 6 ? * MON")
    public void generateAppointmentsFor7Days() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(6);

        List<AvailableAppointment> availableAppointments = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isSaturday = date.getDayOfWeek() == DayOfWeek.SATURDAY;
            boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;

            if (!isSunday) { // No appointments on Sundays
                LocalTime start = isSaturday ? SATURDAY_START : MONDAY_FRIDAY_START;
                LocalTime end = isSaturday ? SATURDAY_END : MONDAY_FRIDAY_END;

                LocalTime appointmentStart = start;

                while (start.isBefore(end)) {

                    for (int i = 0; i < MAX_PARALLEL; i++) {

                        availableAppointments.add(AvailableAppointment.builder()
                                .dateAvailable(date)
                                .timeAvailable(appointmentStart)
                                .duration(LocalTime.of(1, 0))
                                .status(Status.ACT)
                                .dayOfWeek(com.mechanical.workshops.enums.DayOfWeek.valueOf(date.getDayOfWeek().toString().substring(0, 3)))
                                .build());
                    }

                    appointmentStart = appointmentStart.plusMinutes(APPOINTMENT_DURATION_MINUTES);
                    start = start.plusMinutes(APPOINTMENT_DURATION_MINUTES);
                }
            }
        }

        saveAppointments(availableAppointments);
        log.info("Appointments generated for the week from {} to {}", startDate, endDate);
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
}
