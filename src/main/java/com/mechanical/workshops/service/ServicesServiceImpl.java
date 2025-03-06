package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.*;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.repository.ServiceRepository;
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
public class ServicesServiceImpl implements ServicesService {

    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<PageResponseDto> getAllServices(String text, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<com.mechanical.workshops.models.Service> servicesActives =
                serviceRepository.findByStatusAndText(Status.ACT, text, pageable);

        List<ServiceResponseDto> servicesDtoList = servicesActives.getContent().stream()
                .map(service -> {
                    ServiceResponseDto dto = modelMapper.map(service, ServiceResponseDto.class);
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                .content(servicesDtoList)
                .pageNumber(servicesActives.getNumber())
                .pageSize(servicesActives.getSize())
                .totalElements(servicesActives.getTotalElements())
                .totalPages(servicesActives.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> register(ServiceSaveRequestDto serviceSaveRequestDto) {

        com.mechanical.workshops.models.Service service  = modelMapper
                .map(serviceSaveRequestDto, com.mechanical.workshops.models.Service.class);
        service.setStatus(Status.ACT);

        serviceRepository.save(service);
        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_CREATED, Constants.SERVICE, service.getName()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> update(UUID serviceId, ServiceUpdateRequestDto serviceUpdateRequestDto) {

        com.mechanical.workshops.models.Service serviceData =  serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.SERVICE , serviceId)));

        if(serviceUpdateRequestDto.getName() != null)
            serviceData.setName(serviceUpdateRequestDto.getName());

        if(serviceUpdateRequestDto.getDescription() != null)
            serviceData.setDescription(serviceUpdateRequestDto.getDescription());

        if(serviceUpdateRequestDto.getCost() != null)
            serviceData.setCost(serviceUpdateRequestDto.getCost());

        serviceRepository.save(serviceData);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_UPDATED, Constants.SERVICE , serviceData.getName()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> delete(UUID serviceId) {

        com.mechanical.workshops.models.Service serviceData =  serviceRepository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.SERVICE , serviceId)));

        serviceData.setStatus(Status.INA);
        serviceRepository.save(serviceData);
        return ResponseEntity.ok(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .message(String.format(Constants.ENTITY_DELETED, Constants.SERVICE , serviceData.getName()))
                .build());
    }
}
