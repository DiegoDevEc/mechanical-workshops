package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.AttendanceDashboardFilter;
import com.mechanical.workshops.dto.AttendanceDashboardResponse;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.models.Attendance;
import com.mechanical.workshops.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements  DashboardService {

    private final AttendanceRepository attendanceRepository;

    @Override
    public ResponseEntity<ResponseDto> getDashboardAdministrator(AttendanceDashboardFilter filter) {
        return getDashboardByClientOrAdministrator(filter);
    }

    @Override
    public ResponseEntity<ResponseDto> getDashboardByClient(AttendanceDashboardFilter filter) {
        return getDashboardByClientOrAdministrator(filter);

    }

    @Override
    public ResponseEntity<ResponseDto> getDashboardByTechnician(AttendanceDashboardFilter filter) {

    List<Attendance> attendances  = attendanceRepository.findAttendancesInRangeAndTechnician(
            filter.getEntityId(),
            filter.getStartDate(),
            filter.getEndDate()
    );

        AttendanceDashboardResponse createResponse = createResponse(attendances);

        Map<String, BigDecimal> finishedServices = monthlyFinishedServicesTotal(filter, true);
        createResponse.setDataValueMount(finishedServices);

        Map<String, BigDecimal> finishedServicesByMonth = monthlyFinishedServices(filter, true);
        createResponse.setFinishValueMount(finishedServicesByMonth);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .data(createResponse)
                .build());
    }

    private ResponseEntity<ResponseDto> getDashboardByClientOrAdministrator(AttendanceDashboardFilter filter) {
        List<Attendance> attendances  = attendanceRepository.findAttendancesInRangeAndClient(
            filter.getEntityId(),
            filter.getStartDate(),
            filter.getEndDate()
    );
        AttendanceDashboardResponse createResponse = createResponse(attendances);

        Map<String, BigDecimal> finishedServices = monthlyFinishedServicesTotal(filter, false);
        createResponse.setDataValueMount(finishedServices);

        Map<String, BigDecimal> finishedServicesByMonth = monthlyFinishedServices(filter, false);
        createResponse.setFinishValueMount(finishedServicesByMonth);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .data(createResponse)
                .build());
    }

    private AttendanceDashboardResponse createResponse(List<Attendance> attendances) {

        Map<StatusAttendance, String> statusTranslations = Map.of(
                StatusAttendance.FINISH, "Finalizado",
                StatusAttendance.PROGRESS, "En progreso",
                StatusAttendance.INGRESS, "Pendiente",
                StatusAttendance.CANCELED, "Cancelado",
                StatusAttendance.ASSIGN, "Asignado"
        );

        Map<String, Long> statusCount = attendances.stream()
                .collect(Collectors.groupingBy(
                        attendance -> statusTranslations.get(attendance.getStatus()),
                        Collectors.counting()
                ));


        return AttendanceDashboardResponse.builder()
                .dataStatus(statusCount)
                .build();
    }

    private Map<String, BigDecimal> monthlyFinishedServicesTotal(AttendanceDashboardFilter filter, boolean isTechnician) {

        List<Attendance> finishedServices = getListByYear(filter, isTechnician);

        return finishedServices.stream()
                .filter(attendance -> StatusAttendance.FINISH.equals(attendance.getStatus()))
                .collect(Collectors.groupingBy(
                        attendance -> {
                            Month month = attendance.getStartDate().getMonth();
                            return month;
                        },
                        TreeMap::new,
                        Collectors.reducing(BigDecimal.ZERO, attendance -> attendance.getService().getCost(), BigDecimal::add)
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getDisplayName(TextStyle.FULL, Locale.getDefault()), // Convertimos Month a String
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Para mantener el orden final
                ));

    }

    private Map<String, BigDecimal> monthlyFinishedServices(AttendanceDashboardFilter filter, boolean isTechnician) {

        List<Attendance> finishedServices = getListByYear(filter, isTechnician);

        return finishedServices.stream()
                .filter(attendance -> StatusAttendance.FINISH.equals(attendance.getStatus()))
                .collect(Collectors.groupingBy(
                        attendance -> {
                            Month month = attendance.getStartDate().getMonth();
                            return month;
                        },
                        TreeMap::new,
                        Collectors.mapping(
                                attendance -> BigDecimal.ONE,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getDisplayName(TextStyle.FULL, Locale.getDefault()), // Convertimos Month a String
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Para mantener el orden final
                ));

    }

    private List<Attendance> getListByYear(AttendanceDashboardFilter filter, boolean isTechnician) {
        LocalDateTime startDate;
        LocalDateTime endDate;

        if (filter.getYear() == null) {
            int currentYear = LocalDateTime.now().getYear();
            startDate = LocalDateTime.of(currentYear, Month.JANUARY, 1, 0, 0);
            endDate = LocalDateTime.of(currentYear, Month.DECEMBER, 31, 23, 59);
        } else {
            int filterYear = Integer.parseInt(filter.getYear());
            startDate = LocalDateTime.of(filterYear, Month.JANUARY, 1, 0, 0);
            endDate = LocalDateTime.of(filterYear, Month.DECEMBER, 31, 23, 59);
        }

        if (isTechnician) {
            return attendanceRepository.findAttendancesInRangeAndTechnicianYear(filter.getEntityId(), startDate, endDate);
        }

        return attendanceRepository.findAttendancesInRangeYear(filter.getEntityId(), startDate, endDate);
    }
}
