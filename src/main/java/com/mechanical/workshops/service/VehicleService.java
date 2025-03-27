package com.mechanical.workshops.service;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.VehicleSaveRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface VehicleService {
    ResponseEntity<PageResponseDto> getAllVehiclesByClient(UUID clientId, int page, int size);
    ResponseEntity<ResponseDto> register(VehicleSaveRequestDto vehicleSaveRequestDto);
    ResponseEntity<ResponseDto> update(UUID vehicleId, VehicleSaveRequestDto vehicleSaveRequestDto);
    ResponseEntity<ResponseDto> delete(UUID vehicleId);
}
