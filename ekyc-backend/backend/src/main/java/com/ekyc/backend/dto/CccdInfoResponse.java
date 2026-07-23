package com.ekyc.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CccdInfoResponse {
    private String soCccd;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String quocTich;
    private String queQuan;
    private String noiThuongTru;
}