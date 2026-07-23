package com.ekyc.backend.service;

import com.ekyc.backend.dto.AuthResponse;
import com.ekyc.backend.dto.LoginRequest;
import com.ekyc.backend.dto.RegisterRequest;
import com.ekyc.backend.entity.User;
import com.ekyc.backend.repository.UserRepository;
import com.ekyc.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username da ton tai");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email da duoc su dung");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getId(), saved.getUsername());

        return new AuthResponse("success", "Dang ky thanh cong", saved.getId(), saved.getUsername(), token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Sai username hoac password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Sai username hoac password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        return new AuthResponse("success", "Dang nhap thanh cong", user.getId(), user.getUsername(), token);
    }
}