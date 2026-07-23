package com.ekyc.backend.service;

import com.ekyc.backend.client.AiServiceClient;
import com.ekyc.backend.dto.CccdInfoResponse;
import com.ekyc.backend.dto.ProfileResponse;
import com.ekyc.backend.dto.VerificationResponse;
import com.ekyc.backend.dto.ai.CccdExtractAiResponse;
import com.ekyc.backend.dto.ai.FaceVerifyAiResponse;
import com.ekyc.backend.entity.CccdInfo;
import com.ekyc.backend.entity.User;
import com.ekyc.backend.entity.VerificationStatus;
import com.ekyc.backend.repository.CccdInfoRepository;
import com.ekyc.backend.repository.UserRepository;
import com.ekyc.backend.repository.VerificationStatusRepository;
import com.ekyc.backend.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class KycService {

    private final FileStorageService fileStorageService;
    private final AiServiceClient aiServiceClient;
    private final UserRepository userRepository;
    private final CccdInfoRepository cccdInfoRepository;
    private final VerificationStatusRepository verificationStatusRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CccdInfoResponse uploadCccd(String username, MultipartFile file) {
        User user = getUserByUsername(username);

        String path = fileStorageService.store(file, "cccd");
        byte[] bytes = fileStorageService.readBytes(path);

        CccdExtractAiResponse aiResult = aiServiceClient.extractCccd(bytes, file.getOriginalFilename());

        if (!"success".equals(aiResult.getStatus())) {
            throw new IllegalArgumentException("Trich xuat CCCD that bai: " + aiResult.getMessage());
        }

        CccdInfo cccdInfo = cccdInfoRepository.findByUserId(user.getId()).orElse(new CccdInfo());
        cccdInfo.setUser(user);
        cccdInfo.setSoCccd(aiResult.getSo_cccd());
        cccdInfo.setHoTen(aiResult.getHo_ten());
        cccdInfo.setNgaySinh(parseDate(aiResult.getNgay_sinh()));
        cccdInfo.setGioiTinh(aiResult.getGioi_tinh());
        cccdInfo.setQuocTich(aiResult.getQuoc_tich());
        cccdInfo.setQueQuan(aiResult.getQue_quan());
        cccdInfo.setNoiThuongTru(aiResult.getNoi_thuong_tru());
        cccdInfo.setCccdImagePath(path);
        cccdInfo.setExtractedAt(LocalDateTime.now());

        CccdInfo saved = cccdInfoRepository.save(cccdInfo);

        return new CccdInfoResponse(
                saved.getSoCccd(), saved.getHoTen(),
                saved.getNgaySinh() != null ? saved.getNgaySinh().format(DATE_FORMAT) : null,
                saved.getGioiTinh(), saved.getQuocTich(), saved.getQueQuan(), saved.getNoiThuongTru()
        );
    }

    public VerificationResponse verifyFace(String username, MultipartFile selfieFile) {
        User user = getUserByUsername(username);

        CccdInfo cccdInfo = cccdInfoRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ban can upload CCCD truoc khi xac thuc khuon mat"));

        byte[] cccdBytes = fileStorageService.readBytes(cccdInfo.getCccdImagePath());
        String selfiePath = fileStorageService.store(selfieFile, "selfie");
        byte[] selfieBytes = fileStorageService.readBytes(selfiePath);

        FaceVerifyAiResponse aiResult = aiServiceClient.verifyFace(cccdBytes, selfieBytes);

        if (!"success".equals(aiResult.getStatus())) {
            throw new IllegalArgumentException("So khop khuon mat that bai: " + aiResult.getMessage());
        }

        VerificationStatus verificationStatus = verificationStatusRepository.findByUserId(user.getId())
                .orElse(new VerificationStatus());

        verificationStatus.setUser(user);
        verificationStatus.setFaceMatchVerified(aiResult.getVerified());
        verificationStatus.setSimilarityPercent(aiResult.getSimilarity_percent());
        verificationStatus.setStatus(
                Boolean.TRUE.equals(aiResult.getVerified())
                        ? VerificationStatus.Status.APPROVED
                        : VerificationStatus.Status.REJECTED
        );
        verificationStatus.setVerifiedAt(LocalDateTime.now());

        VerificationStatus saved = verificationStatusRepository.save(verificationStatus);

        return new VerificationResponse(
                saved.getFaceMatchVerified(), saved.getSimilarityPercent(), saved.getStatus().name()
        );
    }
    public ProfileResponse getProfile(String username) {
    User user = getUserByUsername(username);

    CccdInfoResponse cccdInfoResponse = cccdInfoRepository.findByUserId(user.getId())
            .map(info -> new CccdInfoResponse(
                    info.getSoCccd(), info.getHoTen(),
                    info.getNgaySinh() != null ? info.getNgaySinh().format(DATE_FORMAT) : null,
                    info.getGioiTinh(), info.getQuocTich(), info.getQueQuan(), info.getNoiThuongTru()
            ))
            .orElse(null);

    VerificationResponse verificationResponse = verificationStatusRepository.findByUserId(user.getId())
            .map(v -> new VerificationResponse(
                    v.getFaceMatchVerified(), v.getSimilarityPercent(), v.getStatus().name()
            ))
            .orElse(null);

    return new ProfileResponse(user.getUsername(), user.getEmail(), cccdInfoResponse, verificationResponse);
}

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay user"));
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (Exception e) {
            // Neu OCR doc sai dinh dang ngay, luu null thay vi lam crash toan bo request
            return null;
        }
    }
}