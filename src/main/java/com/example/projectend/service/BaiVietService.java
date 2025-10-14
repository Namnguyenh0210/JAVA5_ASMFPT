package com.example.projectend.service;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.BaiVietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * BAI VIET SERVICE - Xử lý logic bài viết tin tức
 * <p>
 * =============================
 * PHÂN CÔNG: TV4 - THỐNG KÊ & BÁO CÁO
 * =============================
 * TODO TV4 - CẦN LÀM (4 METHODS ADMIN):
 * <p>
 * 1. getAllBaiVietAdmin(Pageable) - Lấy tất cả bài viết (kể cả ẩn)
 * → Dùng cho trang quản trị
 * <p>
 * 2. save(BaiViet) - Tạo/cập nhật bài viết
 * → Validate tiêu đề không trùng
 * → Set NgayDang = now() nếu tạo mới
 * <p>
 * 3. toggleStatus(Integer id) - Bật/tắt hiển thị
 * → Đổi TrangThai: "Hiển thị" <-> "Ẩn"
 * <p>
 * 4. deleteById(Integer id) - Xóa bài viết
 * → Hard delete hoặc soft delete (optional)
 * <p>
 * THỜI GIAN: 1 ngày
 * LƯU Ý: Methods hiển thị khách (getFeaturedPosts, searchByTitle) đã có sẵn
 * =============================
 * <p>
 * NOTE CHO TV2: Dùng getFeaturedPosts(3) ở HomeController
 * =============================
 */
@Service
public class BaiVietService {

    @Autowired
    private BaiVietRepository baiVietRepository;

    // =============================
    // METHODS KHÁCH HÀNG (ĐÃ CÓ SẴN)

    // Lấy tất cả bài viết với phân trang (status = "Hiển thị")
    public Page<BaiViet> getAllBaiViet(Pageable pageable) {
        return baiVietRepository.findByTrangThaiOrderByNgayDangDesc("Hiển thị", pageable);
    }

    public Optional<BaiViet> findById(Integer id) {
        return baiVietRepository.findById(id);
    }

    public Page<BaiViet> searchByTitle(String keyword, Pageable pageable) {
        return baiVietRepository.findByTieuDeContainingIgnoreCaseAndTrangThaiOrderByNgayDangDesc(keyword, "Hiển thị", pageable);
    }

    public List<BaiViet> getFeaturedPosts(int limit) {
        return baiVietRepository.findTop3ByTrangThaiOrderByNgayDangDesc("Hiển thị");
    }

    public Page<BaiViet> getBaiVietByAuthor(TaiKhoan taiKhoan, Pageable pageable) {
        return baiVietRepository.findByTaiKhoanAndTrangThaiOrderByNgayDangDesc(taiKhoan, "Hiển thị", pageable);
    }

    public long countActivePosts() {
        return baiVietRepository.countByTrangThai("Hiển thị");
    }

    // =============================
    // TODO TV4: Method 1 - Lấy tất cả bài viết cho admin (kể cả ẩn)
    // HƯỚNG DẪN: return baiVietRepository.findAll(pageable);
    public Page<BaiViet> getAllBaiVietAdmin(Pageable pageable) {
        return baiVietRepository.findAll(pageable);
    }

    // =============================
    // TODO TV4: Method 2 - Lưu bài viết (tạo mới hoặc cập nhật)
    // HƯỚNG DẪN:
    // 1. Validate tiêu đề không trùng (optional):
    //    if (baiViet.getMaBV() == null) { // Tạo mới
    //        List<BaiViet> existing = baiVietRepository.findByTieuDe(baiViet.getTieuDe());
    //        if (!existing.isEmpty()) throw new RuntimeException("Tiêu đề đã tồn tại");
    //    }
    // 2. if (baiViet.getNgayDang() == null) baiViet.setNgayDang(LocalDateTime.now());
    // 3. if (baiViet.getTrangThai() == null) baiViet.setTrangThai("Hiển thị");
    // 4. return baiVietRepository.save(baiViet);
    public BaiViet save(BaiViet baiViet) {
        return baiVietRepository.save(baiViet); // TODO TV4: Thêm validation
    }

    // =============================
    // TODO TV4: Method 3 - Bật/tắt hiển thị bài viết (đã có sẵn)
    public void toggleStatus(Integer id) {
        Optional<BaiViet> baiVietOpt = baiVietRepository.findById(id);
        if (baiVietOpt.isPresent()) {
            BaiViet bv = baiVietOpt.get();
            String newStatus = "Hiển thị".equals(bv.getTrangThai()) ? "Ẩn" : "Hiển thị";
            bv.setTrangThai(newStatus);
            baiVietRepository.save(bv);
        }
    }

    // =============================
    // TODO TV4: Method 4 - Xóa bài viết (đã có sẵn)
    // HƯỚNG DẪN:
    // Option 1 (Hard delete): baiVietRepository.deleteById(id);
    // Option 2 (Soft delete): toggleStatus(id) để set TrangThai = "Đã xóa"
    public void deleteById(Integer id) {
        baiVietRepository.deleteById(id); // TODO TV4: Có thể đổi sang soft delete
    }
}
