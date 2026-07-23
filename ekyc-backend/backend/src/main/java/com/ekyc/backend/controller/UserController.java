package com.ekyc.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public Map<String, String> getCurrentUser(Authentication authentication) {
        // "authentication" duoc Spring Security tu dong bom vao,
        // lay tu thong tin JwtAuthenticationFilter da "danh dau" truoc do
        return Map.of(
            "username", authentication.getName(),
            "message", "Ban da xac thuc thanh cong bang JWT"
        );
    }
}