package com.example.projectend.repository;

import com.example.projectend.entity.DanhGia;
import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.SanPham;
import com.example.projectend.entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY ĐÁNH GIÁ - ASM Web Bán Hàng Tết
 * Quản lý đánh giá sản phẩm
 */
@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {

    // Tìm đánh giá theo sản phẩm
    Page<DanhGia> findBySanPhamOrderByNgayDGDesc(SanPham sanPham, Pageable pageable);

    // Tìm đánh giá theo khách hàng
    Page<DanhGia> findByKhachHangOrderByNgayDGDesc(TaiKhoan khachHang, Pageable pageable);

    // Kiểm tra khách hàng đã đánh giá sản phẩm trong đơn hàng chưa
    Optional<DanhGia> findByKhachHangAndSanPhamAndDonHang(TaiKhoan khachHang, SanPham sanPham, DonHang donHang);

    // Tính điểm trung bình của sản phẩm
    @Query("SELECT AVG(d.soSao) FROM DanhGia d WHERE d.sanPham = :sanPham")
    Double findAverageRatingBySanPham(@Param("sanPham") SanPham sanPham);

    // Đếm số lượng đánh giá của sản phẩm
    long countBySanPham(SanPham sanPham);

    // Lấy đánh giá theo số sao
    List<DanhGia> findBySanPhamAndSoSaoOrderByNgayDGDesc(SanPham sanPham, Integer soSao);
}
