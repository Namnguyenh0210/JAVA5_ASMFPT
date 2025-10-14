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

    // Lấy các bài viết nổi bật (mới nhất)
    List<BaiViet> findTop3ByTrangThaiOrderByNgayDangDesc(String trangThai);

    // Tìm bài viết theo tác giả
    Page<BaiViet> findByTaiKhoanAndTrangThaiOrderByNgayDangDesc(TaiKhoan taiKhoan, String trangThai, Pageable pageable);

    // Đếm số bài viết theo trạng thái
    long countByTrangThai(String trangThai);
}
