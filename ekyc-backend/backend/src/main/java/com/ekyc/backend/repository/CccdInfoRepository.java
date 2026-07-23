package com.ekyc.backend.repository;

import com.ekyc.backend.entity.CccdInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CccdInfoRepository extends JpaRepository<CccdInfo, Long> {

    Optional<CccdInfo> findByUserId(Long userId);
}