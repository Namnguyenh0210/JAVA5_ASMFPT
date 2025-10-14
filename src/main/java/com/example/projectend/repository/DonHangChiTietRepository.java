package com.example.projectend.repository;

import com.example.projectend.entity.DonHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DON HANG CHI TIET REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Lấy chi tiết đơn để hiển thị lịch sử & trang cảm ơn
 * - THÀNH VIÊN 4: Thống kê top bán chạy, tổng số lượng bán
 */
@Repository
public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer> {

    // List<DonHangChiTiet> findByDonHang(DonHang donHang); // TODO THÀNH VIÊN 3

    // ================= Thống kê (THÀNH VIÊN 4) =================
    // @Query("SELECT c.sanPham AS sp, SUM(c.soLuong) AS total FROM DonHangChiTiet c GROUP BY c.sanPham ORDER BY total DESC")
    // List<Object[]> getTopSellingProducts(); // TODO THÀNH VIÊN 4 (Optional custom limit thực hiện ở service)

    // long countBySanPham(SanPham sanPham); // TODO THÀNH VIÊN 4: Tổng số lượng dòng chi tiết (hoặc SUM soLuong với query riêng)
}
