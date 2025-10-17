package com.example.projectend.entity;

import jakarta.persistence.*;

/**
 * ENTITY PHUONG THUC THANH TOAN
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Hiển thị danh sách ở checkout
 * - THÀNH VIÊN 4: (Optional) Quản trị bật/tắt phương thức
 */
@Entity
@Table(name = "PhuongThucThanhToan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPTTT")
    private Integer maPTTT;

    @Column(name = "TenPTTT", nullable = false, unique = true, length = 100)
    private String tenPTTT;

    public PhuongThucThanhToan() {
    }

    public PhuongThucThanhToan(String tenPTTT) {
        this.tenPTTT = tenPTTT;
    }

    public Integer getMaPTTT() {
        return maPTTT;
    }

    public void setMaPTTT(Integer maPTTT) {
        this.maPTTT = maPTTT;
    }

    public String getTenPTTT() {
        return tenPTTT;
    }

    public void setTenPTTT(String tenPTTT) {
        this.tenPTTT = tenPTTT;
    }
}
