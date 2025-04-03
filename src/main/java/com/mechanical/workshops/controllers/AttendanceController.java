package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.service.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/attendances")
@AllArgsConstructor
@Tag(name = "Atenciones a clientes", description = "Controller gestiona las atenciones (Atenciones CRUD)")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/all-by-technical")
    public ResponseEntity<PageResponseDto> getAttendanceAllByTechnical(@RequestParam(defaultValue = "") UUID technicianId,
                                                                       @RequestParam(required = false) StatusAttendance status,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "5") int size
    ) {
        return attendanceService.getAllAttendanceByTechnician(technicianId, status, page, size);
    }

    @GetMapping("/all-by-client")
    public ResponseEntity<PageResponseDto> getAttendanceAllByClient(@RequestParam(defaultValue = "") UUID clientId,
                                                                       @RequestParam(required = false) StatusAttendance status,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "5") int size
    ) {
        return attendanceService.getAllAttendanceByClient(clientId, status, page, size);
    }

    @PutMapping("/update-status/{attendanceId}")
    public ResponseEntity<ResponseDto> updateStatus(@PathVariable UUID attendanceId, @RequestParam(required = false) StatusAttendance status) {
        return attendanceService.updateStatus(attendanceId, status);
    }

    @DeleteMapping("/cancel/{attendanceId}")
    public ResponseEntity<ResponseDto> cancel(@PathVariable UUID attendanceId) {
        return attendanceService.cancel(attendanceId);
    }

}
