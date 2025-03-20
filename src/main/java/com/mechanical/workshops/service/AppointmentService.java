package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.AppointmentAssignedRequestDto;
import com.mechanical.workshops.dto.AppointmentRequestDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.StatusAppointment;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface AppointmentService {
    ResponseEntity<PageResponseDto> getAllAppointments(LocalDate startDate, LocalDate endDate, StatusAppointment status, int page, int size);
    ResponseEntity<ResponseDto> update(UUID appointmentId, AppointmentRequestDto appointmentRequestDto);
    ResponseEntity<ResponseDto> updateAssigned(UUID appointmentId, AppointmentAssignedRequestDto appointmentAssignedRequestDto);
    ResponseEntity<ResponseDto> delete(UUID appointmentId);
}
