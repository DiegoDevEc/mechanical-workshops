package com.mechanical.workshops.controllers;

import com.mechanical.workshops.dto.PageResponseDto;
import com.mechanical.workshops.dto.ProductSaveRequestDto;
import com.mechanical.workshops.dto.ResponseDto;
import com.mechanical.workshops.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Tag(name = "Productos", description = "Controller para Productos (Operaciones CRUD)")
public class ProductsController {

    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerProduct(@RequestBody ProductSaveRequestDto productSaveRequestDto) {
        return productService.register(productSaveRequestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<PageResponseDto> getAllProducts(@RequestParam(defaultValue = "") String text,
                                                           @RequestParam(defaultValue = "") String categoryId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size) {
        return productService.getAllProducts(text, categoryId, page, size);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody ProductSaveRequestDto productSaveRequestDto, @PathVariable UUID categoryId) {
        return productService.update(categoryId, productSaveRequestDto);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable UUID categoryId) {
        return productService.delete(categoryId);
    }
}
