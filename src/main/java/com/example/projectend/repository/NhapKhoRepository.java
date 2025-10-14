package com.example.projectend.repository;

import com.example.projectend.entity.NhapKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * NHAP KHO REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 4: Lịch sử nhập kho, thống kê bổ sung tồn kho (optional)
 * <p>
 * TODO THÀNH VIÊN 4 (Optional): Thêm các query lịch sử theo sản phẩm / khoảng thời gian
 */
@Repository
public interface NhapKhoRepository extends JpaRepository<NhapKho, Integer> {
    // List<NhapKho> findBySanPhamOrderByNgayNhapDesc(SanPham sanPham); // TODO THÀNH VIÊN 4
    // List<NhapKho> findByNgayNhapBetween(LocalDateTime start, LocalDateTime end); // TODO THÀNH VIÊN 4
}
