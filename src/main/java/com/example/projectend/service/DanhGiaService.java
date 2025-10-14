package com.example.projectend.service;

import com.example.projectend.entity.DanhGia;
import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.SanPham;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * DANH GIA SERVICE - Xử lý đánh giá sản phẩm
 * PHÂN CÔNG:
 * - THÀNH VIÊN 3: Hiển thị đánh giá trong chi tiết sản phẩm, tính điểm trung bình
 * - THÀNH VIÊN 4: Quản trị (xóa, duyệt nếu bổ sung moderation sau này)
 * <p>
 * =============================
 * TODO THÀNH VIÊN 3: Thêm method createReview(TaiKhoan kh, SanPham sp, DonHang dh, int soSao, String binhLuan)
 * TODO THÀNH VIÊN 3: Thêm method getRatingSummary(SanPham sp) trả về map {avg, countEachStar}
 * TODO THÀNH VIÊN 4: (Optional) Thêm cơ chế duyệt -> field TrangThai trong DB (nếu mở rộng schema sau này)
 */
@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    // Lưu đánh giá mới
    public DanhGia save(DanhGia danhGia) {
        return danhGiaRepository.save(danhGia);
    }

    // Tìm đánh giá theo ID
    public Optional<DanhGia> findById(Integer id) {
        return danhGiaRepository.findById(id);
    }

    // Lấy đánh giá theo sản phẩm
    public Page<DanhGia> getDanhGiaBySanPham(SanPham sanPham, Pageable pageable) {
        return danhGiaRepository.findBySanPhamOrderByNgayDGDesc(sanPham, pageable);
    }

    // Lấy đánh giá theo khách hàng
    public Page<DanhGia> getDanhGiaByKhachHang(TaiKhoan khachHang, Pageable pageable) {
        return danhGiaRepository.findByKhachHangOrderByNgayDGDesc(khachHang, pageable);
    }

    // Kiểm tra khách hàng đã đánh giá sản phẩm trong đơn hàng chưa
    public boolean hasReviewed(TaiKhoan khachHang, SanPham sanPham, DonHang donHang) {
        return danhGiaRepository.findByKhachHangAndSanPhamAndDonHang(khachHang, sanPham, donHang).isPresent();
    }

    // Lấy đánh giá của khách hàng cho sản phẩm trong đơn hàng
    public Optional<DanhGia> getDanhGiaByKhachHangAndSanPhamAndDonHang(TaiKhoan khachHang, SanPham sanPham, DonHang donHang) {
        return danhGiaRepository.findByKhachHangAndSanPhamAndDonHang(khachHang, sanPham, donHang);
    }

    // Tính điểm trung bình của sản phẩm
    public Double getAverageRating(SanPham sanPham) {
        Double avg = danhGiaRepository.findAverageRatingBySanPham(sanPham);
        return avg != null ? avg : 0.0;
    }

    // Đếm số lượng đánh giá của sản phẩm
    public long countReviews(SanPham sanPham) {
        return danhGiaRepository.countBySanPham(sanPham);
    }

    // Lấy đánh giá theo số sao
    public List<DanhGia> getDanhGiaBySoSao(SanPham sanPham, Integer soSao) {
        return danhGiaRepository.findBySanPhamAndSoSaoOrderByNgayDGDesc(sanPham, soSao);
    }

    // Xóa đánh giá
    public void deleteById(Integer id) {
        danhGiaRepository.deleteById(id);
    }

    // =============================
    // TODO THÀNH VIÊN 3: Implement createReview(...)
    // TODO THÀNH VIÊN 3: Implement getRatingSummary(...)
    // TODO THÀNH VIÊN 4: (Optional) Moderation queue
}
