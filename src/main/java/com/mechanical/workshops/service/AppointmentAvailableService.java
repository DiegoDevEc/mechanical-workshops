package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import org.springframework.http.ResponseEntity;

public interface AppointmentAvailableService {
    ResponseEntity<PageResponseDto> getAllAppointmentAvailable();
}
