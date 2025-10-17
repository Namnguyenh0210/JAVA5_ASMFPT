package com.example.projectend.repository;

import com.example.projectend.entity.GioHang;
import com.example.projectend.entity.GioHangId;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT gh FROM GioHang gh " +
           "JOIN FETCH gh.sanPham sp " +
           "JOIN FETCH sp.loaiSanPham " +
           "WHERE gh.taiKhoan = :taiKhoan")
    List<GioHang> findByTaiKhoan(@Param("taiKhoan") TaiKhoan taiKhoan); // Lấy danh sách items

    Optional<GioHang> findByTaiKhoanAndSanPham(TaiKhoan taiKhoan, SanPham sanPham); // Tìm 1 item

    void deleteByTaiKhoan(TaiKhoan taiKhoan); // Clear cart sau checkout

    long countByTaiKhoan(TaiKhoan taiKhoan); // Badge số items
}
