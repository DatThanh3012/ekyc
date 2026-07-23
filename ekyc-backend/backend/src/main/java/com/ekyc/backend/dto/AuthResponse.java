package com.ekyc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String status;
    private String message;
    private Long userId;
    private String username;
    private String token; // Them moi - JWT tra ve sau khi login/register thanh cong
}