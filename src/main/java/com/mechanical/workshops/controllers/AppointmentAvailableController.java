package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.service.AppointmentAvailableService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment-available")
@AllArgsConstructor
@Tag(name = "Citas Disponibles", description = "Controller gestiona las citas disponibles (Citas Disponibles CRUD)")
public class AppointmentAvailableController {

    private final AppointmentAvailableService appointmentAvailableService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllAppointmentAvailable(@RequestParam(defaultValue = "") String text,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "5") int size) {
        return appointmentAvailableService.getAllAppointmentAvailable(text, page, size);
    }

}
