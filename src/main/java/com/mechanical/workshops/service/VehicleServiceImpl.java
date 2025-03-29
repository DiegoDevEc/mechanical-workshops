package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.VehicleResponseDto;
import com.mechanical.workshops.dto.VehicleSaveRequestDto;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.models.Vehicle;
import com.mechanical.workshops.repository.PersonRepository;
import com.mechanical.workshops.repository.UserRepository;
import com.mechanical.workshops.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<PageResponseDto> getAllVehiclesByClient(UUID clientId, int page, int size) {

        Person client = userRepository.findByPerson(Person.builder().id(clientId).build())
                .orElseThrow(() -> new NotFoundException("Client not found")).getPerson();

        Pageable pageable = PageRequest.of(page, size);

      Page<Vehicle> vehicles = vehicleRepository.findByClientAndStatus(client,Status.ACT ,pageable);

        List<VehicleResponseDto> vehiculesDtoList = vehicles.getContent()
                .stream().map(vehicle -> modelMapper.map(vehicle, VehicleResponseDto.class)).toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                .content(vehiculesDtoList)
                .pageNumber(vehicles.getNumber())
                .pageSize(vehicles.getSize())
                .totalElements(vehicles.getTotalElements())
                .totalPages(vehicles.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> register(VehicleSaveRequestDto vehicleSaveRequestDto) {

        Vehicle vehicle = Vehicle.builder()
                .brand(vehicleSaveRequestDto.getBrand())
                .model(vehicleSaveRequestDto.getModel())
                .year(vehicleSaveRequestDto.getYear())
                .plate(vehicleSaveRequestDto.getPlate())
                .client(Person.builder().id(vehicleSaveRequestDto.getClientId()).build())
                .status(Status.ACT)
                .build();

        vehicleRepository.save(vehicle);

        return ResponseEntity.ok(ResponseDto.builder()
                        .message(String.format(Constants.ENTITY_CREATED, Constants.VEHICLE, vehicle.getPlate()))
                        .status(HttpStatus.CREATED)
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> update(UUID vehicleId, VehicleSaveRequestDto vehicleSaveRequestDto) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        vehicle.setBrand(vehicleSaveRequestDto.getBrand());
        vehicle.setModel(vehicleSaveRequestDto.getModel());
        vehicle.setYear(vehicleSaveRequestDto.getYear());
        vehicle.setPlate(vehicleSaveRequestDto.getPlate());

        vehicleRepository.save(vehicle);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_CREATED, Constants.VEHICLE, vehicle.getPlate()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> delete(UUID vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        vehicle.setStatus(Status.INA);

        vehicleRepository.save(vehicle);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_DELETED, Constants.VEHICLE, vehicle.getPlate()))
                .build());
    }
}
