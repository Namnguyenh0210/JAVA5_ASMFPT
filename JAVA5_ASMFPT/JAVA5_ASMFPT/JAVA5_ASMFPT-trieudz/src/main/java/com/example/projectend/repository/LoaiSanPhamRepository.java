package com.example.projectend.repository;

import com.example.projectend.entity.LoaiSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * LOAI SAN PHAM REPOSITORY
 * Người 1 - Database Design & Backend Core (ĐÃ HOÀN THÀNH)
 * Repository cho entity LoaiSanPham
 */
@Repository
public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Integer> {

    // ========================================
    // TODO: NGƯỜI 3 - Frontend & Customer Website
    // ========================================

    // TODO: NGƯỜI 3 - Thêm method lấy tất cả danh mục để hiển thị menu
    // List<LoaiSanPham> findAllByOrderByTenLoaiAsc();

    // TODO: NGƯỜI 3 - Thêm method tìm danh mục theo tên (nếu cần search)
    // Optional<LoaiSanPham> findByTenLoai(String tenLoai);

    // TODO: NGƯỜI 3 - Thêm method đếm số sản phẩm trong mỗi danh mục
    // @Query("SELECT lsp, COUNT(sp) FROM LoaiSanPham lsp LEFT JOIN SanPham sp ON sp.loaiSanPham = lsp GROUP BY lsp")
    // List<Object[]> countProductsByCategory();

    // ========================================
    // TODO: NGƯỜI 4 - Admin Panel & Product Management
    // ========================================

    // TODO: NGƯỜI 4 - Thêm method quản lý danh mục sản phẩm (đã có sẵn CRUD từ JpaRepository)
    // Có thể thêm custom validation methods nếu cần

}
