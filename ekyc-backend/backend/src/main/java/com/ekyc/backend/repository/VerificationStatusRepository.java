package com.ekyc.backend.repository;

import com.ekyc.backend.entity.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationStatusRepository extends JpaRepository<VerificationStatus, Long> {

    Optional<VerificationStatus> findByUserId(Long userId);
}