package com.example.projectend.repository;

import com.example.projectend.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DIA CHI REPOSITORY
 * PHÂN CÔNG:
 *  - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 *  - THÀNH VIÊN 3: CRUD địa chỉ người dùng + lấy địa chỉ mặc định (Checkout/Profile)
 */
@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {

    @Query("SELECT d FROM DiaChi d WHERE d.taiKhoan.maTK = :maTK")
    List<DiaChi> findAllByMaTK(@Param("maTK") Integer maTK);

    // List<DiaChi> findByTaiKhoanOrderByMacDinhDesc(TaiKhoan taiKhoan); // TODO THÀNH VIÊN 3
    // Optional<DiaChi> findByTaiKhoanAndMacDinhTrue(TaiKhoan taiKhoan); // TODO THÀNH VIÊN 3
    // long countByTaiKhoan(TaiKhoan taiKhoan); // TODO THÀNH VIÊN 3 (Optional limit)
}
