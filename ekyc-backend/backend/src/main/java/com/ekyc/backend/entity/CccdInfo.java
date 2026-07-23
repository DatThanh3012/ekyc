package com.ekyc.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cccd_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CccdInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "so_cccd", length = 20)
    private String soCccd;

    @Column(name = "ho_ten", length = 150)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh", length = 10)
    private String gioiTinh;

    @Column(name = "quoc_tich", length = 50)
    private String quocTich;

    @Column(name = "que_quan", length = 255)
    private String queQuan;

    @Column(name = "noi_thuong_tru", length = 255)
    private String noiThuongTru;

    // Duong dan file anh CCCD luu tren server, khong luu anh truc tiep trong DB
    @Column(name = "cccd_image_path", length = 500)
    private String cccdImagePath;

    @Column(name = "extracted_at")
    private LocalDateTime extractedAt = LocalDateTime.now();
}