package com.example.projectend.repository;

import com.example.projectend.entity.GioHang;
import com.example.projectend.entity.GioHangId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * GIO HANG REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Các truy vấn phục vụ thao tác giỏ hàng người dùng
 * - THÀNH VIÊN 4: (Optional) Số liệu tổng quan (đếm record) cho dashboard
 * <p>
 * TODO THÀNH VIÊN 3: Thêm các method được comment bên dưới khi cần
 * TODO THÀNH VIÊN 4 (Optional): Dùng count() trực tiếp để thống kê
 */
@Repository
public interface GioHangRepository extends JpaRepository<GioHang, GioHangId> {

    // List<GioHang> findByTaiKhoanOrderBySanPham_MaSPAsc(TaiKhoan taiKhoan); // TODO THÀNH VIÊN 3: Lấy danh sách items
    // Optional<GioHang> findByTaiKhoanAndSanPham(TaiKhoan taiKhoan, SanPham sanPham); // TODO THÀNH VIÊN 3: Tìm 1 item
    // void deleteByTaiKhoan(TaiKhoan taiKhoan); // TODO THÀNH VIÊN 3: Clear cart sau checkout
    // long countByTaiKhoan(TaiKhoan taiKhoan); // TODO THÀNH VIÊN 3: Badge số items
}
