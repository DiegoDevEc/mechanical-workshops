package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.*;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.AvailableAppointment;
import com.mechanical.workshops.repository.AvailableAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AppointmentAvailableServiceImpl implements AppointmentAvailableService {

    private final AvailableAppointmentRepository availableAppointmentRepository;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<PageResponseDto> getAllAppointmentAvailable( String text, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();
        List<AvailableAppointmentDto> availableAppointments = availableAppointmentRepository.findAvailableAppointmentsFrom(Status.ACT, localDate, localTime, pageable)
                .stream()
                .map(availableAppointment -> modelMapper.map(availableAppointment, AvailableAppointmentDto.class)).toList();
        return ResponseEntity.ok(PageResponseDto.builder()
                .content(availableAppointments)
                        .pageNumber(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .totalPages(availableAppointments.size() / pageable.getPageSize())
                        .totalElements(Long.valueOf(availableAppointments.size()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> getAllAppointmentAvailableDays() {
        List<AvailableAppointment> availableAppointments = availableAppointmentRepository.findByDateAvailableAfterAndStatus(LocalDate.now(), Status.ACT);

        List<AvailableAppointmentDaysDto> availableAppointmentDaysDtos = availableAppointments.stream().map(availableAppointment -> {
            AvailableAppointmentDaysDto availableAppointmentDaysDto = new AvailableAppointmentDaysDto();
            availableAppointmentDaysDto.setDate(convertDateToString(availableAppointment.getDateAvailable()));
            availableAppointmentDaysDto.setDayName(availableAppointment.getDayOfWeek());
            availableAppointmentDaysDto.setDay(availableAppointment.getDateAvailable().getDayOfMonth());
            availableAppointmentDaysDto.setMonth(getMonthInSpanish(availableAppointment.getDateAvailable()));
            return availableAppointmentDaysDto;
        }).distinct()
                .sorted(Comparator.comparing(dto -> LocalDate.parse(dto.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
        .toList();

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .data(availableAppointmentDaysDtos)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResponseDto> getAllAppointmentAvailableTimes(String dateSelected) {

        LocalDate dateAvailable = LocalDate.parse(dateSelected, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        List<AvailableAppointment> availableAppointments = availableAppointmentRepository.findByDateAvailableAndStatus(dateAvailable, Status.ACT);

      List<AvailableAppointmentTimesDto> availableAppointmentTimesDtos = availableAppointments.stream().map(availableAppointment -> {
            AvailableAppointmentTimesDto availableAppointmentTimesDto = new AvailableAppointmentTimesDto();
            availableAppointmentTimesDto.setId(availableAppointment.getId());
            availableAppointmentTimesDto.setStart(availableAppointment.getTimeAvailable().toString());
            availableAppointmentTimesDto.setCode(availableAppointment.getCode());
            return availableAppointmentTimesDto;
        })
              .sorted(Comparator.comparing(dto -> LocalTime.parse(dto.getStart())))
              .toList();

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .data(availableAppointmentTimesDtos)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    private String convertDateToString(LocalDate dateAvailable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateAvailable.format(formatter);
    }

    private String getMonthInSpanish(LocalDate dateAvailable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.forLanguageTag("es"));
        return dateAvailable.format(formatter);  // Devuelve el mes en español
    }
}
