package com.example.projectend.repository;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY BÀI VIẾT - ASM Web Bán Hàng Tết
 * Quản lý bài viết kiến thức
 */
@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {

    // Tìm tất cả bài viết đang hiển thị
    Page<BaiViet> findByTrangThaiOrderByNgayDangDesc(String trangThai, Pageable pageable);

    // Tìm bài viết theo tiêu đề (search)
    Page<BaiViet> findByTieuDeContainingIgnoreCaseAndTrangThaiOrderByNgayDangDesc(String tieuDe, String trangThai, Pageable pageable);

    // Tìm bài viết theo tiêu đề (admin - tất cả trạng thái)
    Page<BaiViet> findByTieuDeContainingIgnoreCaseOrderByNgayDangDesc(String tieuDe, Pageable pageable);

    // Lấy các bài viết nổi bật (mới nhất)
    List<BaiViet> findTop3ByTrangThaiOrderByNgayDangDesc(String trangThai);

    // Tìm bài viết theo tác giả và trạng thái
    Page<BaiViet> findByTaiKhoanAndTrangThaiOrderByNgayDangDesc(TaiKhoan taiKhoan, String trangThai, Pageable pageable);

    // Tìm tất cả bài viết của một tác giả (staff)
    Page<BaiViet> findByTaiKhoanOrderByNgayDangDesc(TaiKhoan taiKhoan, Pageable pageable);

    // Tìm bài viết của tác giả theo keyword (staff)
    Page<BaiViet> findByTaiKhoanAndTieuDeContainingIgnoreCaseOrderByNgayDangDesc(TaiKhoan taiKhoan, String tieuDe, Pageable pageable);

    // Đếm số bài viết theo trạng thái
    long countByTrangThai(String trangThai);
}
