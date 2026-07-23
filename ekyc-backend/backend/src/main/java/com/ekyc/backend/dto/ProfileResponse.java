package com.ekyc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String username;
    private String email;
    private CccdInfoResponse cccdInfo;       // null neu chua upload CCCD
    private VerificationResponse verification; // null neu chua xac thuc khuon mat
}