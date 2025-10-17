package com.example.projectend.repository;

import com.example.projectend.entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PHUONG THUC THANH TOAN REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping entity (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Đọc danh sách PTTT ở checkout (findAll)
 * - THÀNH VIÊN 4: Quản trị thêm / kiểm tra trùng tên, thống kê tần suất sử dụng (optional)
 * <p>
 * TODO THÀNH VIÊN 4: existsByTenPTTT(String tenPTTT) để validate thêm mới (nếu cần)
 * TODO THÀNH VIÊN 4 (Optional): Đếm số đơn hàng sử dụng phương thức này (query ở DonHangRepository)
 */
@Repository
public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    // boolean existsByTenPTTT(String tenPTTT); // TODO THÀNH VIÊN 4: Gỡ comment nếu dùng
}
