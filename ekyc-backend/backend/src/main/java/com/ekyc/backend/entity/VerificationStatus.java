package com.ekyc.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "face_match_verified")
    private Boolean faceMatchVerified;

    @Column(name = "similarity_percent")
    private Double similarityPercent;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status = Status.PENDING;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    public enum Status {
        PENDING, APPROVED, REJECTED
    }
}