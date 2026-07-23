package com.ekyc.backend.dto.ai;

import lombok.Data;
import java.util.Map;

@Data
public class CccdExtractAiResponse {
    private String status;
    private String message;
    private String so_cccd;
    private String ho_ten;
    private String ngay_sinh;
    private String gioi_tinh;
    private String quoc_tich;
    private String que_quan;
    private String noi_thuong_tru;
    private Map<String, String> raw_texts;
}