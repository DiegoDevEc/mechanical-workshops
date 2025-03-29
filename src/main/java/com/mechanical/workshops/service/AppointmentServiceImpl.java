package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.*;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.enums.StatusAppointment;
import com.mechanical.workshops.enums.StatusAttendance;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.models.*;
import com.mechanical.workshops.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final AvailableAppointmentRepository availableAppointmentRepository;
    private final PersonRepository personRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public ResponseEntity<ResponseDto> register(AppointmentRequestDto appointmentRequestDto) {

        AvailableAppointment availableAppointment = availableAppointmentRepository
               .findById(appointmentRequestDto.getAvailableAppointmentId()).orElseThrow(() -> new NotFoundException("Availability Appointment not found"));

       Person client = personRepository.findById(appointmentRequestDto.getClientId())
               .orElseThrow(() -> new NotFoundException("Client not found"));

       Vehicle vehicle = vehicleRepository.findById(appointmentRequestDto.getVehicleId())
               .orElseThrow(() -> new NotFoundException("Vehicle not found"));

       com.mechanical.workshops.models.Service service = serviceRepository.findById(appointmentRequestDto.getServiceId())
               .orElseThrow(() -> new NotFoundException("Service not found"));

       Appointment appointment = Appointment.builder()
               .client(client)
               .availableAppointment(availableAppointment)
               .vehicle(vehicle)
               .status(StatusAppointment.INGRESS)
               .dateAppointment(availableAppointment.getDateAvailable())
               .build();

       appointmentRepository.save(appointment);

        Attendance attendance = Attendance.builder()
               .appointment(appointment)
               .service(service)
                .code(availableAppointment.getCode())
               .status(StatusAttendance.INGRESS)
               .build();

       attendanceRepository.save(attendance);

        availableAppointment.setStatus(Status.INA);

        availableAppointmentRepository.save(availableAppointment);

       return ResponseEntity.ok(ResponseDto.builder()
               .status(HttpStatus.OK)
               .message(String.format(Constants.ENTITY_CREATED, Constants.ATTENDANCE, availableAppointment.getId()))
               .build());
    }

    @Override
    public ResponseEntity<PageResponseDto> getAllAppointments(LocalDate startDate, LocalDate endDate, StatusAppointment status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentRepository.findAppointmentsInRangeAndStatus(status, startDate, endDate, pageable);

        List<AppointmentResponseDto> appointmentResponseDtos = appointments.stream()
                .map(appointment -> {
                    AppointmentResponseDto appointmentResponseDto =   modelMapper.map(appointment, AppointmentResponseDto.class);

                    UUID userId = appointmentResponseDto.getClient().getId();

                    userRepository.findByPerson(Person.builder().id(userId).build()).ifPresent(person -> {
                        appointmentResponseDto.getClient().setUsername(person.getUsername());
                        appointmentResponseDto.getClient().setEmail(person.getEmail());
                        appointmentResponseDto.getClient().setPhone(person.getPhone());
                        appointmentResponseDto.getClient().setIdentification(person.getIdentification());
                    });

                    attendanceRepository.findByAppointment(appointment).ifPresent(attendance -> {
                        appointmentResponseDto.setStatusAttendance(attendance.getStatus().name());
                        if(attendance.getTechnician() != null){
                            appointmentResponseDto.setTechnician(modelMapper.map(attendance.getTechnician(), UserResponseDto.class));
                        }
                        appointmentResponseDto.setService(modelMapper.map(attendance.getService(), ServiceResponseDto.class));
                    });
                    appointmentResponseDto.setCode(appointment.getAvailableAppointment().getCode());

                    return appointmentResponseDto;

                }).toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                .content(appointmentResponseDtos)
                .pageNumber(appointments.getNumber())
                .pageSize(appointments.getSize())
                .totalElements(appointments.getTotalElements())
                .totalPages(appointments.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> update(UUID appointmentId, AppointmentRequestDto appointmentRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> updateAssigned(UUID appointmentId, AppointmentAssignedRequestDto appointmentAssignedRequestDto) {
        // Buscar la cita
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        // Actualizar el estado de la cita
        appointment.setStatus(StatusAppointment.PROGRESS);
        appointmentRepository.save(appointment);

        Person technical = Person.builder().id(appointmentAssignedRequestDto.getTechnicalId()).build();
        com.mechanical.workshops.models.Service service = com.mechanical.workshops.models.Service.builder().id(appointmentAssignedRequestDto.getServiceId()).build();

        // Crear la asistencia
        Attendance attendance = attendanceRepository.findByAppointment(appointment)
                .map(existingAttendance -> {
                    // Actualizar asistencia existente
                    existingAttendance.setStatus(StatusAttendance.ASSIGN);
                    existingAttendance.setService(service);
                    existingAttendance.setTechnician(technical);
                    return existingAttendance;
                })
                .orElseGet(() -> {
                    // Crear nueva asistencia si no existe
                    return Attendance.builder()
                            .status(StatusAttendance.ASSIGN)
                            .appointment(appointment)
                            .service(service)
                            .technician(technical)
                            .build();
                });

        attendanceRepository.save(attendance);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.ATTENDANCE, appointmentId))
                .build());
    }


    @Override
    public ResponseEntity<ResponseDto> delete(UUID appointmentId) {

        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            appointment.setStatus(StatusAppointment.CANCELED);
            appointmentRepository.save(appointment);

            attendanceRepository.findByAppointment(appointment).ifPresent(attendance -> {
                attendance.setStatus(StatusAttendance.CANCELED);
                attendanceRepository.save(attendance);
            });

        });

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_CANCELED, Constants.APPOINTMENT, appointmentId))
                .build());
    }
}
