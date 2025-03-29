package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface AppointmentAvailableService {
    ResponseEntity<PageResponseDto> getAllAppointmentAvailable(String text, Integer page, Integer size);
    ResponseEntity<ResponseDto> getAllAppointmentAvailableDays();
    ResponseEntity<ResponseDto> getAllAppointmentAvailableTimes(String dateSelected);
}
