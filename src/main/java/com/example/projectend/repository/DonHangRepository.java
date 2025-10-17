package com.example.projectend.repository;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DON HANG REPOSITORY
 * Người 1 - Database Core (Bổ sung 03/10/2025)
 */
@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    // ========================================
    // TODO: NGƯỜI 3 - FRONTEND (LỊCH SỬ ĐƠN HÀNG KHÁCH)
    // ========================================
    // Lịch sử đơn khách hàng (mới nhất trước)
    List<DonHang> findByKhachHangOrderByNgayDatDesc(TaiKhoan khachHang);
    // Lịch sử theo trạng thái (ví dụ: chỉ đơn Hoàn tất / Đang giao)
    // List<DonHang> findByKhachHangAndTrangThaiDonHangOrderByNgayDatDesc(TaiKhoan khachHang, TrangThaiDonHang tt);

    // ========================================
    // TODO: NGƯỜI 4 - STAFF / ADMIN (QUY TRÌNH CLAIM ĐƠN)
    // ========================================
    // Danh sách đơn CHƯA được nhân viên nào nhận xử lý (nhanVien IS NULL) và ở trạng thái chờ xác nhận
    // List<DonHang> findByNhanVienIsNullAndTrangThaiDonHang(TrangThaiDonHang tt);
    // Đơn đang do 1 nhân viên phụ trách với 1 trạng thái cụ thể (đang xử lý / đang giao)
    // List<DonHang> findByNhanVienAndTrangThaiDonHangOrderByNgayDatAsc(TaiKhoan nhanVien, TrangThaiDonHang tt);
    // Tất cả đơn của 1 nhân viên (để dashboard nhân viên)
    // List<DonHang> findByNhanVienOrderByNgayDatDesc(TaiKhoan nhanVien);
    // Lọc theo khoảng thời gian (báo cáo / dashboard)
    // List<DonHang> findByNgayDatBetween(LocalDateTime start, LocalDateTime end);

    // ========================================
    // TODO: NGƯỜI 5 - REPORTS & ANALYTICS
    // ========================================
    // Tổng doanh thu khoảng thời gian (đã có) – sử dụng cho biểu đồ / thống kê
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.ngayDat BETWEEN :start AND :end")
    BigDecimal sumRevenueByDateRange(LocalDateTime start, LocalDateTime end);
    // Đếm số đơn trong khoảng (phục vụ tính conversion / trung bình đơn)
    // long countByNgayDatBetween(LocalDateTime start, LocalDateTime end);
    // Lấy danh sách đơn đã hoàn tất trong khoảng (để tính sản phẩm bán ra -> ThongKe)
    // List<DonHang> findByTrangThaiDonHangAndNgayDatBetween(TrangThaiDonHang tt, LocalDateTime start, LocalDateTime end);
}
