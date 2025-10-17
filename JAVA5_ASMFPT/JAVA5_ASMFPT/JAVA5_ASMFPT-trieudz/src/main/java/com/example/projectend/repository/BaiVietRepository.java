package com.example.projectend.repository;

import com.example.projectend.entity.BaiViet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BaiVietRepository extends JpaRepository<BaiViet, Integer> {

    // 1. Phân trang tất cả bài viết
    Page<BaiViet> findAll(Pageable pageable);

    // 2. Tìm kiếm bài viết theo tiêu đề
    Page<BaiViet> findByTieuDeContainingIgnoreCase(String keyword, Pageable pageable);

    // 3. Lấy bài viết nổi bật (N bài mới nhất)
    @Query("SELECT b FROM BaiViet b ORDER BY b.ngayDang DESC")
    List<BaiViet> findTopByOrderByNgayDangDesc(Pageable pageable);
}
