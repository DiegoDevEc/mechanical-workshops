package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.AvailableAppointmentDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.AvailableAppointment;
import com.mechanical.workshops.repository.AvailableAppointmentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentAvailableServiceImpl implements AppointmentAvailableService {

    private final AvailableAppointmentRepository availableAppointmentRepository;
    private final ModelMapper modelMapper;


    @Override
    public ResponseEntity<PageResponseDto> getAllAppointmentAvailable() {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDate localDate = dateTime.toLocalDate();
        LocalTime localTime = dateTime.toLocalTime();
        List<AvailableAppointmentDto> availableAppointments = availableAppointmentRepository.findAvailableAppointmentsFrom(Status.ACT, localDate, localTime)
                .stream()
                .map(availableAppointment -> modelMapper.map(availableAppointment, AvailableAppointmentDto.class)).toList();
        return ResponseEntity.ok(PageResponseDto.builder()
                .content(availableAppointments)
                        .totalElements(Long.valueOf(availableAppointments.size()))
                .build());
    }
}
