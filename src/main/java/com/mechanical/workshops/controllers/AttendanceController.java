package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.AttendanceRequestDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.service.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all-by-technical-assigned")
    public ResponseEntity<PageResponseDto> getAttendanceAllByTechnicalAssigned(@RequestParam(defaultValue = "") UUID technicianId,
                                                                       @RequestParam(required = false) StatusAttendance status,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "5") int size
    ) {
        return attendanceService.getAllAttendanceByTechnicianAssigned(technicianId, status, page, size);
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

    @PutMapping("/update-ingress/{attendanceId}")
    public ResponseEntity<ResponseDto> update(@PathVariable String attendanceId) {
        return attendanceService.updateIngress(attendanceId);
    }

    @PutMapping("/update-finalize-service/{attendanceId}")
    public ResponseEntity<ResponseDto> updateFinalizeService(@PathVariable String attendanceId,
                                                             @RequestBody AttendanceRequestDto attendanceRequestDto) {
        return attendanceService.updateFinalizeService(attendanceId, attendanceRequestDto);
    }

    @DeleteMapping("/cancel/{attendanceId}")
    public ResponseEntity<ResponseDto> cancel(@PathVariable String attendanceId) {
        return attendanceService.cancel(attendanceId);
    }

}
