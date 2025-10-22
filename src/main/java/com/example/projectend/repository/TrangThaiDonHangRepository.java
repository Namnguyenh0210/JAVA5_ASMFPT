// Bổ sung vào file TrangThaiDonHangRepository.java
package com.example.projectend.repository;

import com.example.projectend.entity.TrangThaiDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * TRANG THAI DON HANG REPOSITORY
 * Người 1 - Database Core (Bổ sung 03/10/2025)
 */
@Repository
public interface TrangThaiDonHangRepository extends JpaRepository<TrangThaiDonHang, Integer> {

    // 🔸 Lấy trạng thái đơn hàng theo tên (vd: "Chờ xác nhận", "Đã hủy", "Đã giao")
    Optional<TrangThaiDonHang> findByTenTTDH(String tenTTDH);
}
