package com.example.projectend.entity;

import jakarta.persistence.*;

/**
 * ENTITY TRANG THAI DON HANG
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 4: Sử dụng khi cập nhật trạng thái đơn (DonHangService)
 * - THÀNH VIÊN 3: Chỉ đọc để hiển thị lịch sử
 */
@Entity
@Table(name = "TrangThaiDonHang")
public class TrangThaiDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTTDH")
    private Integer maTTDH;

    @Column(name = "TenTTDH", nullable = false, length = 50)
    private String tenTTDH;

    public TrangThaiDonHang() {
    }

    public TrangThaiDonHang(String tenTTDH) {
        this.tenTTDH = tenTTDH;
    }

    public Integer getMaTTDH() {
        return maTTDH;
    }

    public void setMaTTDH(Integer maTTDH) {
        this.maTTDH = maTTDH;
    }

    public String getTenTTDH() {
        return tenTTDH;
    }

    public void setTenTTDH(String tenTTDH) {
        this.tenTTDH = tenTTDH;
    }
}
