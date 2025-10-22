package com.example.projectend.repository;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DON HANG REPOSITORY
 * Người 1 - Database Core (Bổ sung 03/10/2025)
 */
@Repository
// Bổ sung JpaSpecificationExecutor để hỗ trợ DonHangService.searchAdmin
public interface DonHangRepository extends JpaRepository<DonHang, Integer>, JpaSpecificationExecutor<DonHang> {

    // ========================================
    // 🧑 NGƯỜI 3 - FRONTEND (LỊCH SỬ ĐƠN HÀNG KHÁCH)
    // ========================================

    // Lấy lịch sử đơn hàng của 1 khách (mới nhất trước)
    List<DonHang> findByKhachHangOrderByNgayDatDesc(TaiKhoan khachHang);

    // Lấy lịch sử theo trạng thái cụ thể (vd: Hoàn tất, Đang giao)
    // List<DonHang> findByKhachHangAndTrangThaiDonHangOrderByNgayDatDesc(TaiKhoan khachHang, TrangThaiDonHang tt);

    // ========================================
    // 👷 NGƯỜI 4 - STAFF / ADMIN (QUY TRÌNH CLAIM ĐƠN)
    // ========================================

    // Đơn chưa có nhân viên xử lý & đang ở trạng thái chờ xác nhận
    // List<DonHang> findByNhanVienIsNullAndTrangThaiDonHang(TrangThaiDonHang tt);

    // Đơn đang được xử lý bởi 1 nhân viên với trạng thái cụ thể
    // List<DonHang> findByNhanVienAndTrangThaiDonHangOrderByNgayDatAsc(TaiKhoan nhanVien, TrangThaiDonHang tt);

    // Tất cả đơn của 1 nhân viên
    // List<DonHang> findByNhanVienOrderByNgayDatDesc(TaiKhoan nhanVien);

    // Lọc theo khoảng thời gian (dashboard / báo cáo)
    // List<DonHang> findByNgayDatBetween(LocalDateTime start, LocalDateTime end);

    // ========================================
    // 📈 NGƯỜI 5 - REPORTS & ANALYTICS
    // ========================================

    // Tổng doanh thu theo khoảng thời gian
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.ngayDat BETWEEN :start AND :end")
    BigDecimal sumRevenueByDateRange(LocalDateTime start, LocalDateTime end);

    // ========================================
    // ⚡ BỔ SUNG CHO DonHangService
    // ========================================

    // 1️⃣ Lấy đơn theo MaDH và MaKH (dùng khi kiểm tra quyền truy cập lịch sử đơn hàng)
    Optional<DonHang> findByMaDHAndKhachHang_MaTK(Integer maDH, Integer maKH);

    // 2️⃣ Lịch sử đơn hàng có phân trang (theo MaKH)
    Page<DonHang> findByKhachHang_MaTKOrderByNgayDatDesc(Integer maKH, Pageable pageable);

    // 3️⃣ Top đơn hàng chờ xác nhận (dashboard admin)
    Page<DonHang> findByTrangThaiDonHang_MaTTDHOrderByNgayDatDesc(Integer maTTDH, Pageable pageable);

    // 4️⃣ Các phương thức hỗ trợ Staff claim đơn
    List<DonHang> findByNhanVienIsNullAndTrangThaiDonHang_MaTTDH(Integer maTTDH);

    List<DonHang> findByNhanVien_MaTKAndTrangThaiDonHang_MaTTDHOrderByNgayDatAsc(Integer maNV, Integer maTTDH);

    List<DonHang> findByNhanVien_MaTKOrderByNgayDatDesc(Integer maNV);

    List<DonHang> findByNgayDatBetween(LocalDateTime start, LocalDateTime end);

    // 5️⃣ Reports: đếm số đơn trong khoảng
    long countByNgayDatBetween(LocalDateTime start, LocalDateTime end);

    // 6️⃣ Lấy danh sách đơn hoàn tất trong khoảng thời gian (theo mã trạng thái)
    List<DonHang> findByTrangThaiDonHang_MaTTDHAndNgayDatBetween(Integer maTTDH, LocalDateTime start, LocalDateTime end);

    // 7️⃣ Tổng sản phẩm bán ra trong khoảng (MaTTDH = 3: Hoàn tất)
    @Query("SELECT SUM(ct.soLuong) " +
            "FROM DonHang d JOIN d.chiTietList ct " +
            "WHERE d.trangThaiDonHang.maTTDH = 3 AND d.ngayDat BETWEEN :start AND :end")
    Long sumTotalProductsSoldByDateRange(LocalDateTime start, LocalDateTime end);
}
