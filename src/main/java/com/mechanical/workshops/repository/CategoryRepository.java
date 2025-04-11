package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.Category;
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
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT u FROM Category u WHERE u.status = :status " +
            "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(u.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    Page<Category> findByStatusAndText(@Param("status") Status status,
                                      @Param("text") String text,
                                      Pageable pageable);
    Optional<Category> findByName(String name);

    List<Category> findByStatus(Status status);

}