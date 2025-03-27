package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.AttendanceResponseDto;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.models.Attendance;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.repository.AppointmentRepository;
import com.mechanical.workshops.repository.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ModelMapper modelMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    public ResponseEntity<PageResponseDto> getAllAttendanceByTechnician(UUID technicianId, StatusAttendance status, int page, int size) {

       Page<Attendance> attendances = attendanceRepository.findByTechnicalAllAndStatus(status, Person.builder().id(technicianId).build(), PageRequest.of(page, size));

       List<AttendanceResponseDto> attendanceResponseDtoList = attendances
                .stream().map(attendance -> modelMapper.map(attendance, AttendanceResponseDto.class)).toList();

       return ResponseEntity.ok(PageResponseDto.builder()
                .content(attendanceResponseDtoList)
                .pageNumber(attendances.getNumber())
                .pageSize(attendances.getSize())
                .totalElements(attendances.getTotalElements())
                .totalPages(attendances.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<PageResponseDto> getAllAttendanceByClient(UUID clientId, StatusAttendance status, int page, int size) {

        Page<Attendance> attendances = attendanceRepository.findByClientAllAndStatus(status, Person.builder().id(clientId).build(), PageRequest.of(page, size));

        List<AttendanceResponseDto> attendanceResponseDtoList = attendances
                .stream().map(attendance -> modelMapper.map(attendance, AttendanceResponseDto.class)).toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                .content(attendanceResponseDtoList)
                .pageNumber(attendances.getNumber())
                .pageSize(attendances.getSize())
                .totalElements(attendances.getTotalElements())
                .totalPages(attendances.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> updateStatus(UUID attendanceId, StatusAttendance status) {
        attendanceRepository.findById(attendanceId).ifPresent(attendance -> {
            attendance.setStatus(status);
            attendanceRepository.save(attendance);

            appointmentRepository.findById(attendance.getAppointment().getId()).ifPresent(appointment -> {
                if(status.equals(StatusAttendance.FINISH)) {
                    appointment.setStatus(StatusAppointment.FINISH);
                }else {
                    appointment.setStatus(StatusAppointment.PROGRESS);
                }
                appointmentRepository.save(appointment);
            });

        });

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.ATTENDANCE, attendanceId))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> cancel(UUID attendanceId) {
        attendanceRepository.findById(attendanceId).ifPresent(attendance -> {
            attendance.setStatus(StatusAttendance.CANCELED);
            attendanceRepository.save(attendance);

            appointmentRepository.findById(attendance.getAppointment().getId()).ifPresent(appointment -> {
                appointment.setStatus(StatusAppointment.CANCELED);
                appointmentRepository.save(appointment);
            });

        });

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.ATTENDANCE, attendanceId))
                .build());
    }
}
