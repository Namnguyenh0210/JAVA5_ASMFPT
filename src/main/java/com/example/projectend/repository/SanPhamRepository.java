package com.example.projectend.repository;

import com.example.projectend.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SAN PHAM REPOSITORY
 * CORE: Thành viên 1 đã hoàn thành mapping cơ bản.
 * MỞ RỘNG:
 * - THÀNH VIÊN 3: Thêm query phục vụ filter / search front-end
 * - THÀNH VIÊN 4: Thêm query cảnh báo tồn kho, thống kê hỗ trợ admin
 */
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    // =============================
    // TODO THÀNH VIÊN 3 - Queries FE
    // =============================
    // List<SanPham> findByLoaiSanPham(LoaiSanPham loai);
    // List<SanPham> findByTenSPContainingIgnoreCase(String keyword);
    // List<SanPham> findTop6ByOrderByNgayTaoDesc();
    // List<SanPham> findByGiaBetween(BigDecimal min, BigDecimal max);

    // =============================
    // TODO THÀNH VIÊN 4 - Queries Admin
    // =============================
    // List<SanPham> findBySoLuongLessThan(Integer threshold);
    // List<SanPham> findByNgayTaoBetween(LocalDateTime start, LocalDateTime end);
}
