package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.*;
import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.models.Attendance;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.models.User;
import com.mechanical.workshops.repository.AppointmentRepository;
import com.mechanical.workshops.repository.AttendanceRepository;
import com.mechanical.workshops.repository.UserRepository;
import com.mechanical.workshops.utils.DateUtil;
import com.mechanical.workshops.utils.EmailUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ModelMapper modelMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<PageResponseDto> getAllAttendanceByTechnician(UUID technicianId, StatusAttendance status, int page, int size) {

       Page<Attendance> attendances = attendanceRepository.findByTechnicalAllAndStatus(status, Person.builder().id(technicianId).build(), PageRequest.of(page, size));

       List<AttendanceResponseDto> attendanceResponseDtoList = attendances
                .stream().map(attendance -> {
                   User user = userRepository.findByPerson(attendance.getAppointment().getClient()).orElse(null);
                   AttendanceResponseDto attendanceResponseDto = modelMapper.map(attendance, AttendanceResponseDto.class);
                   attendanceResponseDto.setClient(modelMapper.map(attendance.getAppointment().getClient(), UserResponseDto.class));
                   attendanceResponseDto.getClient().setPhone(user.getPhone());
                   attendanceResponseDto.setVehicle(modelMapper.map(attendance.getAppointment().getVehicle(), VehicleResponseDto.class));
                   return attendanceResponseDto;
               }).toList();

       return ResponseEntity.ok(PageResponseDto.builder()
                .content(attendanceResponseDtoList)
                .pageNumber(attendances.getNumber())
                .pageSize(attendances.getSize())
                .totalElements(attendances.getTotalElements())
                .totalPages(attendances.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<PageResponseDto> getAllAttendanceByTechnicianAssigned(UUID technicianId, StatusAttendance status, int page, int size) {

        List<StatusAttendance> statuses;

        if(status == null) {
            statuses = List.of(StatusAttendance.ASSIGN, StatusAttendance.PROGRESS);
        }else {
            statuses = List.of(status);
        }

        Page<Attendance> attendances = attendanceRepository.findByTechnicianAndStatuses(statuses, Person.builder().id(technicianId).build(), PageRequest.of(page, size));

        List<AttendanceResponseDto> attendanceResponseDtoList = attendances
                .stream().map(attendance -> {
                    User user = userRepository.findByPerson(attendance.getAppointment().getClient()).orElse(null);
                    AttendanceResponseDto attendanceResponseDto = modelMapper.map(attendance, AttendanceResponseDto.class);
                    attendanceResponseDto.setClient(modelMapper.map(attendance.getAppointment().getClient(), UserResponseDto.class));
                    attendanceResponseDto.getClient().setPhone(user.getPhone());
                    attendanceResponseDto.setVehicle(modelMapper.map(attendance.getAppointment().getVehicle(), VehicleResponseDto.class));
                    return attendanceResponseDto;
                }).toList();

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
                .stream()
                .map(attendance -> modelMapper.map(attendance, AttendanceResponseDto.class)).toList();

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
    public ResponseEntity<ResponseDto> updateIngress(String attendanceId) {

      Attendance attendance =  attendanceRepository.findByCode(attendanceId)
              .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.ATTENDANCE, attendanceId)));

            attendance.setStatus(StatusAttendance.PROGRESS);
            attendance.setStartDate(LocalDateTime.now());
            attendance.setReceivedBy(attendance.getTechnician());
            attendanceRepository.save(attendance);

            appointmentRepository.findById(attendance.getAppointment().getId()).ifPresent(appointment -> {
                if(attendance.getStatus().equals(StatusAttendance.FINISH)) {
                    appointment.setStatus(StatusAppointment.FINISH);
                }else {
                    appointment.setStatus(StatusAppointment.PROGRESS);
                }
                appointmentRepository.save(appointment);
            });
            sendEmail(attendance, "Mantenimiento en proceso", "appointment_progress_template");
        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.ATTENDANCE, attendanceId))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> updateFinalizeService(String attendanceId, AttendanceRequestDto attendanceRequestDto) {

        Attendance attendance =  attendanceRepository.findByCode(attendanceId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.ATTENDANCE, attendanceId)));

        attendance.setStatus(StatusAttendance.FINISH);
        attendance.setEndDate(LocalDateTime.now());
        attendance.setComments(attendanceRequestDto.getComments());
        attendanceRepository.save(attendance);

        appointmentRepository.findById(attendance.getAppointment().getId()).ifPresent(appointment -> {
            appointment.setStatus(StatusAppointment.FINISH);
            appointmentRepository.save(appointment);
        });

        sendEmail(attendance, "Mantenimiento finalizado", "appointment_finish_template");

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.ATTENDANCE, attendanceId))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> cancel(String attendanceId) {

        Attendance attendance =  attendanceRepository.findByCode(attendanceId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.ATTENDANCE, attendanceId)));

            attendance.setStatus(StatusAttendance.CANCELED);
            attendanceRepository.save(attendance);

            appointmentRepository.findById(attendance.getAppointment().getId()).ifPresent(appointment -> {
                appointment.setStatus(StatusAppointment.CANCELED);
                appointmentRepository.save(appointment);
            });

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.ATTENDANCE, attendanceId))
                .build());
    }

    private void sendEmail(Attendance attendance, String subject ,String template) {
        User user = userRepository.findByPerson(attendance.getAppointment().getClient()).orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = attendance.getStartDate().format(formatter);

        Map<String, String> parameters = Map.of(
                "name", attendance.getAppointment().getClient().getFirstname() + " " + attendance.getAppointment().getClient().getLastname(),
                "date", formattedDate,
                "service", attendance.getService().getName(),
                "technician", attendance.getTechnician().getFirstname() + " " + attendance.getTechnician().getLastname()
        );

        EmailUtil.sendEmail(user.getEmail(), subject, template, parameters);
    }
}
