package com.example.projectend.repository;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TAI KHOAN REPOSITORY - ĐĂNG NHẬP BẰNG EMAIL
 * Người 1 - Database Design & Backend Core
 */
@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    // Tìm tài khoản bằng EMAIL (cho đăng nhập)
    Optional<TaiKhoan> findByEmail(String email);

    // Kiểm tra email đã tồn tại chưa (cho đăng ký)
    boolean existsByEmail(String email);

    // Tìm tài khoản theo vai trò
    @Query("SELECT t FROM TaiKhoan t WHERE t.vaiTro.tenVT = :tenVaiTro")
    List<TaiKhoan> findByVaiTro(@Param("tenVaiTro") String tenVaiTro);

    // Đếm số lượng tài khoản theo vai trò
    Long countByVaiTro(VaiTro vaiTro);

    // Lấy danh sách tài khoản active
    List<TaiKhoan> findByTrangThaiTrue();

    // Tìm kiếm tài khoản theo tên hoặc email
    @Query("SELECT t FROM TaiKhoan t WHERE t.hoTen LIKE %:keyword% OR t.email LIKE %:keyword%")
    List<TaiKhoan> searchByKeyword(@Param("keyword") String keyword);
}
