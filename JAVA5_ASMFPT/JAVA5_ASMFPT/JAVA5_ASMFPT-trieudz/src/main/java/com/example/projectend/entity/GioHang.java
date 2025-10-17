package com.example.projectend.entity;

import jakarta.persistence.*;

/**
 * ENTITY GIO HANG
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH) – PK kép (MaTK, MaSP) dùng @IdClass
 * - THÀNH VIÊN 3: CRUD giỏ hàng (GioHangService) – đồng bộ với UI
 * - THÀNH VIÊN 4: Đọc dữ liệu để tạo DonHang
 * <p>
 * TODO THÀNH VIÊN 3: Đảm bảo unique 1 dòng / sản phẩm / user (dùng find trước khi save)
 */
@Entity
@Table(name = "GioHang")
@IdClass(GioHangId.class)
public class GioHang {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK")
    private TaiKhoan taiKhoan;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSP")
    private SanPham sanPham;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    public GioHang() {
    }

    public GioHang(TaiKhoan taiKhoan, SanPham sanPham, Integer soLuong) {
        this.taiKhoan = taiKhoan;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
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
}
