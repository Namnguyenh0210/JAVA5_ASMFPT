package com.example.projectend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * COMPOSITE KEY GIO HANG (MaTK, MaSP)
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Tạo cấu trúc (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Không sửa – chỉ sử dụng trong GioHangService
 * - THÀNH VIÊN 4: Đọc khi tạo đơn
 */
public class GioHangId implements Serializable {

    private Integer taiKhoan;  // Phải khớp tên với thuộc tính trong GioHang
    private Integer sanPham;   // Phải khớp tên với thuộc tính trong GioHang

    public GioHangId() {
    }

    public GioHangId(Integer taiKhoan, Integer sanPham) {
        this.taiKhoan = taiKhoan;
        this.sanPham = sanPham;
    }

    public Integer getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(Integer taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public Integer getSanPham() {
        return sanPham;
    }

    public void setSanPham(Integer sanPham) {
        this.sanPham = sanPham;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GioHangId that = (GioHangId) o;
        return Objects.equals(taiKhoan, that.taiKhoan) && Objects.equals(sanPham, that.sanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taiKhoan, sanPham);
    }
}
