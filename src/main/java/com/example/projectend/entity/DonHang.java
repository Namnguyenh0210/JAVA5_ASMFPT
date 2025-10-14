package com.example.projectend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTITY DON HANG
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH) – Không sửa schema
 * - THÀNH VIÊN 3: Đọc dữ liệu lịch sử đơn hàng cá nhân (Profile)
 * - THÀNH VIÊN 4: Tạo đơn, cập nhật trạng thái, thống kê
 * <p>
 * TODO THÀNH VIÊN 4: Khi tạo đơn set TrangThaiDonHang ban đầu = 'Chờ xác nhận'
 * TODO THÀNH VIÊN 4: Không thay đổi tongTien thủ công sau khi tạo (tính lại qua chi tiết nếu cần)
 */
@Entity
@Table(name = "DonHang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH")
    private Integer maDH;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKH", nullable = false)
    private TaiKhoan khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNV")
    private TaiKhoan nhanVien; // có thể null lúc mới tạo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDC", nullable = false)
    private DiaChi diaChiGiaoHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTTDH")
    private TrangThaiDonHang trangThaiDonHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPTTT", nullable = false)
    private PhuongThucThanhToan phuongThucThanhToan;

    @Column(name = "NgayDat")
    private LocalDateTime ngayDat = LocalDateTime.now();

    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonHangChiTiet> chiTietList = new ArrayList<>();

    public DonHang() {
    }

    public DonHang(TaiKhoan khachHang, DiaChi diaChiGiaoHang, BigDecimal tongTien) {
        this.khachHang = khachHang;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.tongTien = tongTien;
    }

    public Integer getMaDH() {
        return maDH;
    }

    public void setMaDH(Integer maDH) {
        this.maDH = maDH;
    }

    public TaiKhoan getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(TaiKhoan khachHang) {
        this.khachHang = khachHang;
    }

    public TaiKhoan getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(TaiKhoan nhanVien) {
        this.nhanVien = nhanVien;
    }

    public DiaChi getDiaChiGiaoHang() {
        return diaChiGiaoHang;
    }

    public void setDiaChiGiaoHang(DiaChi diaChiGiaoHang) {
        this.diaChiGiaoHang = diaChiGiaoHang;
    }

    public TrangThaiDonHang getTrangThaiDonHang() {
        return trangThaiDonHang;
    }

    public void setTrangThaiDonHang(TrangThaiDonHang trangThaiDonHang) {
        this.trangThaiDonHang = trangThaiDonHang;
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public List<DonHangChiTiet> getChiTietList() {
        return chiTietList;
    }

    public void setChiTietList(List<DonHangChiTiet> chiTietList) {
        this.chiTietList = chiTietList;
    }
}
