package com.example.projectend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTITY SAN PHAM
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping chuẩn theo script SQL (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Sử dụng trong hiển thị, không thay đổi schema
 * - THÀNH VIÊN 4: Dùng cho thống kê tồn kho / top bán (qua service khác)
 * <p>
 * =============================
 * TODO THÀNH VIÊN 3 (Optional): Thêm phương thức tiện ích getGiaFormatted() trong DTO hoặc ViewModel (không thêm vào entity để giữ sạch)
 * TODO THÀNH VIÊN 4 (Optional): Kiểm tra trước khi xóa sản phẩm (ràng buộc DonHangChiTiet) ở Service, không chỉnh entity.
 */
@Entity
@Table(name = "SanPham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", nullable = false, length = 200)
    private String tenSP;

    @Column(name = "MoTa", columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "Gia", nullable = false, precision = 18, scale = 2)
    private BigDecimal gia;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong = 0;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLoai", nullable = false)
    private LoaiSanPham loaiSanPham;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    // Constructors
    public SanPham() {
    }

    public SanPham(String tenSP, BigDecimal gia, LoaiSanPham loaiSanPham) {
        this.tenSP = tenSP;
        this.gia = gia;
        this.loaiSanPham = loaiSanPham;
    }

    // Getters and Setters
    public Integer getMaSP() {
        return maSP;
    }

    public void setMaSP(Integer maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}
