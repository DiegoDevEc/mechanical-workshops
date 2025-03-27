package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.AppointmentAssignedRequestDto;
import com.mechanical.workshops.dto.AppointmentRequestDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@AllArgsConstructor
@Tag(name = "Citas Ingresadas", description = "Controller gestiona las citas ingresadas (Citas Ingresadas CRUD)")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllAppointments(@RequestParam(defaultValue = "") LocalDate startDate,
                                                              @RequestParam(required = false) LocalDate endDate,
                                                              @RequestParam(required = false) StatusAppointment status,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size) {
        return appointmentService.getAllAppointments(startDate, endDate,status, page, size);
    }

    @PutMapping("/update-assigned/{appointmentId}")
    public ResponseEntity<ResponseDto> updateAssigned(@RequestBody AppointmentAssignedRequestDto appointmentAssignedRequestDto, @PathVariable UUID appointmentId) {
        return appointmentService.updateAssigned(appointmentId, appointmentAssignedRequestDto);
    }

    @DeleteMapping("/delete/{appointmentId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable UUID appointmentId) {
        return appointmentService.delete(appointmentId);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        return appointmentService.register(appointmentRequestDto);
    }

}
