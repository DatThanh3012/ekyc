package com.ekyc.backend.repository;

import com.ekyc.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA tu sinh cau truy van tu ten ham (khong can viet SQL)
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}