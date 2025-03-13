package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryId(UUID categoryId);
    Optional<Product> findBySku(String sku);
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.status = :status " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findByStatusAndTextAndCategory(@Param("status") Status status,
                                                 @Param("text") String text,
                                                 @Param("categoryId") UUID categoryId,
                                                 Pageable pageable);

}
