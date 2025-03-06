package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.dto.ServiceSaveRequestDto;
import com.mechanical.workshops.dto.ServiceUpdateRequestDto;
import com.mechanical.workshops.service.ServicesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/services")
@AllArgsConstructor
public class ServicesController {

    private final ServicesService servicesService;

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllServices(@RequestParam(defaultValue = "") String text,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "5") int size){
        return servicesService.getAllServices(text, page, size);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerService(@Valid @RequestBody ServiceSaveRequestDto serviceSaveRequestDto){
        return servicesService.register(serviceSaveRequestDto);
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<ResponseDto> updateService(@PathVariable UUID serviceId,
                                                     @RequestBody ServiceUpdateRequestDto serviceUpdateRequestDto){
        return servicesService.update(serviceId, serviceUpdateRequestDto);
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<ResponseDto> deleteService(@PathVariable UUID serviceId){
        return servicesService.delete(serviceId);
    }

}
