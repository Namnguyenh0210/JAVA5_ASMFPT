package com.example.projectend.repository;

import com.example.projectend.entity.DiaChi;
import com.example.projectend.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DIA CHI REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: CRUD địa chỉ người dùng + lấy địa chỉ mặc định (Checkout/Profile)
 */
@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {

    @Query("SELECT d FROM DiaChi d WHERE d.taiKhoan.maTK = :maTK")
    List<DiaChi> findAllByMaTK(@Param("maTK") Integer maTK);

    // Tìm tất cả địa chỉ của tài khoản
    List<DiaChi> findByTaiKhoanOrderByMacDinhDesc(TaiKhoan taiKhoan);

    // Tìm địa chỉ mặc định của tài khoản
    Optional<DiaChi> findByTaiKhoanAndMacDinhTrue(TaiKhoan taiKhoan);

    // Đếm số địa chỉ của tài khoản
    long countByTaiKhoan(TaiKhoan taiKhoan);
}
