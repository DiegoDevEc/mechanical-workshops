package com.mechanical.workshops.repository;

import com.mechanical.workshops.enums.Role;
import com.mechanical.workshops.enums.Status;
import com.mechanical.workshops.models.Person;
import com.mechanical.workshops.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.status = :status " +
            "AND u.role = :role " +
            "AND (LOWER(u.username) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(u.identification) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :text, '%')))")
    Page<User> findByStatusAndTextAndRole(@Param("status") Status status,
                                   @Param("text") String text,
                                   @Param("role") Role role,
                                   Pageable pageable);
    Page<User> findAllByStatus(Status status, Pageable pageable);


    @Query("SELECT u FROM User u WHERE (u.username = :text OR u.phone = :text OR u.email = :text OR u.identification = :text) AND u.status = :status")
    Optional<User> findByUsernameOrPhoneOrEmailOrIdentificationAndStatus(@Param("text") String text, @Param("status") Status status);

    //<>
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByIdentification(String identification);
    Optional<User> findByPerson(Person person);
}
