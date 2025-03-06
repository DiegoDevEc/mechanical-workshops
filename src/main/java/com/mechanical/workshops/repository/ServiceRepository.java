package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.Service;
import com.mechanical.workshops.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    @Query("SELECT u FROM Service u WHERE u.status = :status " +
            "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(u.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    Page<Service> findByStatusAndText(@Param("status") Status status,
                                   @Param("text") String text,
                                   Pageable pageable);
}
