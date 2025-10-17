package com.example.projectend.repository;

import com.example.projectend.entity.SanPham;
import com.example.projectend.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;

/**
 * SAN PHAM REPOSITORY
 * CORE: Thành viên 1 đã hoàn thành mapping cơ bản.
 * MỞ RỘNG:
 * - THÀNH VIÊN 3: Thêm query phục vụ filter / search front-end
 * - THÀNH VIÊN 4: Thêm query cảnh báo tồn kho, thống kê hỗ trợ adminx
 */
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer>, JpaSpecificationExecutor<SanPham> {

    // =============================
    // TODO THÀNH VIÊN 3 - Queries FE
    // =============================
    // Lấy sản phẩm nổi bật (mới nhất)
    List<SanPham> findTop8ByOrderByNgayTaoDesc();

    // Tìm kiếm theo tên
    List<SanPham> findTop10ByTenSPContainingIgnoreCaseOrderByNgayTaoDesc(String keyword);

    // Lấy sản phẩm cùng loại (sản phẩm liên quan)
    List<SanPham> findTop6ByLoaiSanPham_MaLoaiAndMaSPNotOrderByNgayTaoDesc(Integer loaiId, Integer excludeId);

    // Tìm theo loại sản phẩm
    List<SanPham> findByLoaiSanPham_MaLoaiOrderByNgayTaoDesc(Integer maLoai);

    // Tìm kiếm theo tên
    List<SanPham> findByTenSPContainingIgnoreCaseOrderByNgayTaoDesc(String keyword);

    // Lấy sản phẩm theo khoảng giá
    List<SanPham> findByGiaBetweenOrderByNgayTaoDesc(BigDecimal minPrice, BigDecimal maxPrice);

    // =============================
    // TODO THÀNH VIÊN 4 - Queries Admin
    // =============================
    // Cảnh báo tồn kho thấp
    List<SanPham> findBySoLuongLessThanOrderBySoLuongAsc(Integer threshold);

    // Đếm số lượng sản phẩm
    long count();
}
