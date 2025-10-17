package com.example.projectend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * ENTITY NHAP KHO
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 4: Tạo phiếu nhập (trigger tăng số lượng) qua NhapKhoService (optional)
 * <p>
 * TODO THÀNH VIÊN 4: Khi nhập kho validate SoLuong > 0
 */
@Entity
@Table(name = "NhapKho")
public class NhapKho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNK")
    private Integer maNK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSP", nullable = false)
    private SanPham sanPham;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "NgayNhap")
    private LocalDateTime ngayNhap = LocalDateTime.now();

    public NhapKho() {
    }

    public NhapKho(SanPham sanPham, Integer soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public Integer getMaNK() {
        return maNK;
    }

    public void setMaNK(Integer maNK) {
        this.maNK = maNK;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }
}
