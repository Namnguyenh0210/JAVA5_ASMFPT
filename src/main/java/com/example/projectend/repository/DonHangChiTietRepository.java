// Bổ sung vào file DonHangChiTietRepository.java
package com.example.projectend.repository;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.DonHangChiTiet;
import com.example.projectend.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DON HANG CHI TIET REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Lấy chi tiết đơn để hiển thị lịch sử & trang cảm ơn
 * - THÀNH VIÊN 4: Thống kê top bán chạy, tổng số lượng bán
 */
@Repository
public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer> {

    // 🟡 Lấy toàn bộ chi tiết sản phẩm trong 1 đơn hàng
    List<DonHangChiTiet> findByDonHang(DonHang donHang);

    // ================= 📊 Thống kê (THÀNH VIÊN 4) =================

    // 🥇 Top sản phẩm bán chạy — nhóm theo sản phẩm, sắp giảm dần theo tổng số lượng bán
    @Query("SELECT c.sanPham AS sp, SUM(c.soLuong) AS total " +
            "FROM DonHangChiTiet c " +
            "GROUP BY c.sanPham " +
            "ORDER BY total DESC")
    List<Object[]> getTopSellingProducts();

    // 🧮 Tổng số lượng dòng chi tiết (hoặc có thể dùng SUM soLuong bằng query riêng)
    long countBySanPham(SanPham sanPham);
}
