package com.ekyc.backend.dto.ai;

import lombok.Data;

@Data
public class FaceVerifyAiResponse {
    private String status;
    private String message;
    private Boolean verified;
    private Double distance;
    private Double threshold;
    private String model;
    private Double similarity_percent;
}