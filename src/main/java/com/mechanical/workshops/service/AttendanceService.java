package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.StatusAttendance;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface AttendanceService {
    ResponseEntity<PageResponseDto> getAllAttendanceByTechnician(UUID technicianId, StatusAttendance status, int page, int size);
    ResponseEntity<PageResponseDto> getAllAttendanceByClient(UUID technicianId, StatusAttendance status, int page, int size);
    ResponseEntity<ResponseDto> updateStatus(UUID attendanceId, StatusAttendance status);
    ResponseEntity<ResponseDto> updateIngress(UUID attendanceId);
    ResponseEntity<ResponseDto> cancel(UUID attendanceId);
}
