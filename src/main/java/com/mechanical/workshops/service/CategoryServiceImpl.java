package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.*;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.exception.EntityAlreadyExistsException;
import com.mechanical.workshops.models.Category;
import com.mechanical.workshops.repository.CategoryRepository;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<PageResponseDto> getAllCategories(String text, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoriesActives = categoryRepository.findByStatusAndText(Status.ACT, text, pageable);

        List<CategoryResponseDto> categoryDtoList = categoriesActives.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                .content(categoryDtoList)
                .pageNumber(categoriesActives.getNumber())
                .pageSize(categoriesActives.getSize())
                .totalElements(categoriesActives.getTotalElements())
                .totalPages(categoriesActives.getTotalPages())
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> register(CategorySaveRequestDto categorySaveRequestDto) {

        this.categoryRepository.findByName(categorySaveRequestDto.getName()).ifPresent(category -> {
            throw new EntityAlreadyExistsException(String.format(Constants.ENTITY_ALREADY_EXISTS, Constants.CATEGORY, category.getName()));
        });

        Category category = modelMapper.map(categorySaveRequestDto, Category.class);
        category.setStatus(Status.ACT);
        categoryRepository.save(category);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_CREATED, Constants.CATEGORY, category.getName()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> update(UUID categoryId, CategorySaveRequestDto categorySaveRequestDto) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    category.setName(categorySaveRequestDto.getName());
                    category.setDescription(categorySaveRequestDto.getDescription());
                    categoryRepository.save(category);
                    return ResponseEntity.ok(ResponseDto.builder()
                            .status(HttpStatus.OK)
                            .message(String.format(Constants.ENTITY_UPDATED, Constants.CATEGORY, category.getName()))
                            .build());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .message(String.format(Constants.ENTITY_NOT_FOUND, Constants.CATEGORY, categoryId))
                                .build()));
    }

    @Override
    public ResponseEntity<ResponseDto> delete(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    category.setStatus(Status.INA);
                    categoryRepository.save(category);
                    return ResponseEntity.ok(ResponseDto.builder()
                            .status(HttpStatus.OK)
                            .message(String.format(Constants.ENTITY_UPDATED, Constants.CATEGORY, category.getName()))
                            .build());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.NOT_FOUND)
                                .message(String.format(Constants.ENTITY_NOT_FOUND, Constants.CATEGORY, categoryId))
                                .build()));
    }
}
