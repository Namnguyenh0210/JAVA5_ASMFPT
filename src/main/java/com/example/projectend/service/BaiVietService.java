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

    // Lấy bài viết nổi bật cho trang chủ
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
    // TODO TV4: Method 3 - Bật/tắt hiển thị bài viết
    public void toggleStatus(Integer id) {
        BaiViet baiViet = baiVietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

        // Đổi trạng thái
        if ("Hiển thị".equals(baiViet.getTrangThai())) {
            baiViet.setTrangThai("Ẩn");
        } else {
            baiViet.setTrangThai("Hiển thị");
        }

        baiVietRepository.save(baiViet);
    }

    // =============================
    // TODO TV4: Method 4 - Xóa bài viết
    public void deleteById(Integer id) {
        if (!baiVietRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bài viết");
        }
        baiVietRepository.deleteById(id);
    }

    // Đếm tổng số bài viết
    public long countAll() {
        return baiVietRepository.count();
    }

    // =============================
    // METHODS CHO STAFF (MỚI)
    // =============================

    // Lấy bài viết của một tác giả (cho staff)
    public Page<BaiViet> findByTacGia(TaiKhoan tacGia, Pageable pageable) {
        return baiVietRepository.findByTaiKhoan(tacGia, pageable);
    }

    // Tìm kiếm bài viết của tác giả theo keyword
    public Page<BaiViet> searchByTacGiaAndKeyword(TaiKhoan tacGia, String keyword, Pageable pageable) {
        return baiVietRepository.findByTaiKhoanAndTieuDeContainingIgnoreCase(tacGia, keyword, pageable);
    }

    // Tạo bài viết mới (với upload ảnh)
    public BaiViet createBaiViet(BaiViet baiViet, org.springframework.web.multipart.MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Xử lý upload ảnh (giả sử lưu URL)
            String imageUrl = saveImage(imageFile);
            baiViet.setHinhAnh(imageUrl);
        }

        if (baiViet.getTrangThai() == null) {
            baiViet.setTrangThai("Hiển thị");
        }

        return baiVietRepository.save(baiViet);
    }

    // Cập nhật bài viết (với upload ảnh)
    public BaiViet updateBaiViet(BaiViet baiViet, org.springframework.web.multipart.MultipartFile imageFile) throws Exception {
        BaiViet existing = baiVietRepository.findById(baiViet.getMaBV())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));

        existing.setTieuDe(baiViet.getTieuDe());
        existing.setNoiDung(baiViet.getNoiDung());
        existing.setTrangThai(baiViet.getTrangThai());

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            existing.setHinhAnh(imageUrl);
        }

        return baiVietRepository.save(existing);
    }

    // Xóa bài viết
    public void deleteBaiViet(Integer id) {
        if (!baiVietRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bài viết");
        }
        baiVietRepository.deleteById(id);
    }

    // Helper method để lưu ảnh (đơn giản hóa)
    private String saveImage(org.springframework.web.multipart.MultipartFile file) throws Exception {
        // TODO: Implement proper image upload to server/cloud
        // Tạm thời trả về placeholder
        return "https://picsum.photos/seed/" + System.currentTimeMillis() + "/800/400";
    }
}
