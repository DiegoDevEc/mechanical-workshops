package com.mechanical.workshops.service;

import com.mechanical.workshops.constants.Constants;
import com.mechanical.workshops.dto.*;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.exception.EntityAlreadyExistsException;
import com.mechanical.workshops.exception.NotFoundException;
import com.mechanical.workshops.models.Category;
import com.mechanical.workshops.models.Product;
import com.mechanical.workshops.repository.CategoryRepository;
import com.mechanical.workshops.repository.ProductRepository;
import jakarta.transaction.Transactional;
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

import static com.mechanical.workshops.utils.SKUGenerator.generateSKU;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<PageResponseDto> getAllProducts(String text, String categoryId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        UUID categoryUUID = (categoryId != null && !categoryId.isEmpty()) ? UUID.fromString(categoryId) : null;

        Page<Product> productsActives = productRepository.findByStatusAndTextAndCategory(Status.ACT, text, categoryUUID, pageable);

        List<ProductResponseDto> productsDtoList = productsActives.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();

        return ResponseEntity.ok(PageResponseDto.builder()
                .content(productsDtoList)
                .pageNumber(productsActives.getNumber())
                .pageSize(productsActives.getSize())
                .totalElements(productsActives.getTotalElements())
                .totalPages(productsActives.getTotalPages())
                .build());
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> register(ProductSaveRequestDto productSaveRequestDto) {

        Category category = getcategory(productSaveRequestDto.getCategoryId());
        productSaveRequestDto.setCategoryId(null);

        String sku = generateSKU(productSaveRequestDto.getName());
        Product product = modelMapper.map(productSaveRequestDto, Product.class);
        product.setSku(sku);

        validateProduct(sku, product.getName());

        product.setCategory(category);
        product.setStatus(Status.ACT);
        productRepository.save(product);

        return ResponseEntity.ok(ResponseDto.builder()
                .status(HttpStatus.OK)
                .message(String.format(Constants.ENTITY_CREATED, Constants.PRODUCT, product.getName()))
                .build());
    }

    @Override
    public ResponseEntity<ResponseDto> update(UUID productId, ProductSaveRequestDto productSaveRequestDto) {

        Category category = getcategory(productSaveRequestDto.getCategoryId());

        return this.productRepository.findById(productId)
                .map(product -> {
                    product.setName(productSaveRequestDto.getName());
                    product.setDescription(productSaveRequestDto.getDescription());
                    product.setPrice(productSaveRequestDto.getPrice());
                    product.setStock(productSaveRequestDto.getStock());
                    product.setStatus(productSaveRequestDto.getStatus());
                    product.setCategory(category);
                    productRepository.save(product);

                    return ResponseEntity.ok(ResponseDto.builder()
                            .status(HttpStatus.OK)
                            .message(String.format(Constants.ENTITY_UPDATED, Constants.PRODUCT, product.getName()))
                            .build());
                })
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.PRODUCT, productId)));
    }

    @Override
    public ResponseEntity<ResponseDto> delete(UUID productId) {
        return this.productRepository.findById(productId)
                .map(product -> {
                    product.setStatus(Status.INA);
                    productRepository.save(product);
                    return ResponseEntity.ok(ResponseDto.builder()
                            .status(HttpStatus.OK)
                            .message(String.format(Constants.ENTITY_UPDATED, Constants.PRODUCT, product.getName()))
                            .build());
                })
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.PRODUCT, productId)));
    }

    @Transactional
    private Category getcategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format(Constants.ENTITY_NOT_FOUND, Constants.CATEGORY, categoryId)));
    }

    @Transactional
    private void validateProduct(String sku, String name) {

        this.productRepository.findBySku(sku).ifPresent(productSku -> {
            throw new EntityAlreadyExistsException(String.format(Constants.ENTITY_ALREADY_EXISTS_BY_SKU, Constants.PRODUCT, sku));
        });

        this.productRepository.findByName(name).ifPresent(product -> {
            throw new EntityAlreadyExistsException(String.format(Constants.ENTITY_ALREADY_EXISTS, Constants.PRODUCT, product.getName()));
        });
    }
}
