package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.VehicleSaveRequestDto;
import com.mechanical.workshops.service.VehicleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@AllArgsConstructor
@Tag(name = "Vehículos de los clientes Controller", description = "Controller para gestionar los vehículos de los clientes (Operaciones CRUD)")
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllVehicles(@RequestParam UUID clientId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size){
        return vehicleService.getAllVehiclesByClient(clientId, page, size);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@Valid @RequestBody VehicleSaveRequestDto vehicleSaveRequestDto){
        return vehicleService.register(vehicleSaveRequestDto);
    }

    @PutMapping("/update/{vehicleId}")
    public ResponseEntity<ResponseDto> update(@PathVariable UUID vehicleId, @Valid @RequestBody VehicleSaveRequestDto vehicleSaveRequestDto){
        return vehicleService.update(vehicleId, vehicleSaveRequestDto);
    }

    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable UUID vehicleId){
        return vehicleService.delete(vehicleId);
    }
}
