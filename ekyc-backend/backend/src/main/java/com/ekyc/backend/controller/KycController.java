package com.ekyc.backend.controller;

import com.ekyc.backend.dto.CccdInfoResponse;
import com.ekyc.backend.dto.ProfileResponse;
import com.ekyc.backend.dto.VerificationResponse;
import com.ekyc.backend.service.KycService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
public class KycController {

    private final KycService kycService;

    @PostMapping(value = "/cccd", consumes = "multipart/form-data")
    public CccdInfoResponse uploadCccd(Authentication authentication, @RequestParam("file") MultipartFile file) {
        return kycService.uploadCccd(authentication.getName(), file);
    }

    @PostMapping(value = "/verify-face", consumes = "multipart/form-data")
    public VerificationResponse verifyFace(Authentication authentication, @RequestParam("selfie") MultipartFile selfieFile) {
        return kycService.verifyFace(authentication.getName(), selfieFile);
    }
    @GetMapping("/profile")
    public ProfileResponse getProfile(Authentication authentication) {
        return kycService.getProfile(authentication.getName());
}
}