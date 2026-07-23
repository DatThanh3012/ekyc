package com.ekyc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerificationResponse {
    private Boolean verified;
    private Double similarityPercent;
    private String status;
}