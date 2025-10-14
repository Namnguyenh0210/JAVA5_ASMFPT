package com.example.projectend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * ENTITY DANH GIA
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Tạo / hiển thị đánh giá sản phẩm
 * - THÀNH VIÊN 4: Quản trị (xóa / duyệt nếu mở rộng moderation)
 * <p>
 * TODO THÀNH VIÊN 3 (Optional): Thêm DTO thống kê phân bố sao (không chỉnh entity)
 * TODO THÀNH VIÊN 4 (Optional): Thêm filter theo khoảng thời gian ở trang admin
 */
@Entity
@Table(name = "DanhGia")
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDG")
    private Integer maDG;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKH", nullable = false)
    private TaiKhoan khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSP", nullable = false)
    private SanPham sanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDH", nullable = false)
    private DonHang donHang;

    @Column(name = "SoSao", nullable = false)
    private Integer soSao; // 1-5 sao

    @Column(name = "BinhLuan", length = 500)
    private String binhLuan;

    @Column(name = "NgayDG")
    private LocalDateTime ngayDG = LocalDateTime.now();

    // Constructors
    public DanhGia() {
    }

    public DanhGia(TaiKhoan khachHang, SanPham sanPham, DonHang donHang, Integer soSao, String binhLuan) {
        this.khachHang = khachHang;
        this.sanPham = sanPham;
        this.donHang = donHang;
        this.soSao = soSao;
        this.binhLuan = binhLuan;
    }

    // Getters and Setters
    public Integer getMaDG() {
        return maDG;
    }

    public void setMaDG(Integer maDG) {
        this.maDG = maDG;
    }

    public TaiKhoan getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(TaiKhoan khachHang) {
        this.khachHang = khachHang;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public Integer getSoSao() {
        return soSao;
    }

    public void setSoSao(Integer soSao) {
        this.soSao = soSao;
    }

    public String getBinhLuan() {
        return binhLuan;
    }

    public void setBinhLuan(String binhLuan) {
        this.binhLuan = binhLuan;
    }

    public LocalDateTime getNgayDG() {
        return ngayDG;
    }

    public void setNgayDG(LocalDateTime ngayDG) {
        this.ngayDG = ngayDG;
    }

    @Override
    public String toString() {
        return "DanhGia{" +
                "maDG=" + maDG +
                ", soSao=" + soSao +
                ", binhLuan='" + binhLuan + '\'' +
                ", ngayDG=" + ngayDG +
                '}';
    }
}
