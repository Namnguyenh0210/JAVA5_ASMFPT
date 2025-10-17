package com.example.projectend.repository;

import com.example.projectend.entity.ThongKe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * THONG KE REPOSITORY
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 4: Truy vấn thống kê nếu sử dụng bảng ThongKe thay vì query runtime
 */
@Repository
public interface ThongKeRepository extends JpaRepository<ThongKe, Integer> {

    // List<ThongKe> findByNgayBaoCaoBetween(LocalDate start, LocalDate end); // TODO THÀNH VIÊN 4
    // Optional<ThongKe> findByNgayBaoCao(LocalDate day); // TODO THÀNH VIÊN 4
}
