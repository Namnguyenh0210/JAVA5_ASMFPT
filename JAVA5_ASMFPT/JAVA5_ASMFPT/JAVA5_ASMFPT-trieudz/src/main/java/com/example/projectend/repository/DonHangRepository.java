package com.example.projectend.repository;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.TrangThaiDonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <- thêm
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer>,
                                            JpaSpecificationExecutor<DonHang> { // <- thêm

    Optional<DonHang> findByMaDHAndKhachHang(Integer maDH, TaiKhoan khachHang);

    Page<DonHang> findByKhachHangOrderByNgayDatDesc(TaiKhoan khachHang, Pageable pageable);

    Page<DonHang> findByTrangThaiDonHangOrderByNgayDatDesc(TrangThaiDonHang ttdh, Pageable pageable);

    List<DonHang> findByTrangThaiDonHang_TenTTDH(String tenTTDH);
}
