package com.example.projectend.repository;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.DonHangChiTiet;
import com.example.projectend.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangChiTietRepository extends JpaRepository<DonHangChiTiet, Integer> {

    // ✅ Lấy danh sách chi tiết theo đơn hàng
    List<DonHangChiTiet> findByDonHang(DonHang donHang);

    // ✅ Tìm 1 dòng chi tiết đơn hàng cụ thể theo sản phẩm
    DonHangChiTiet findByDonHangAndSanPham(DonHang donHang, SanPham sanPham);

    // ✅ Truy vấn top sản phẩm bán chạy
    @Query("""
            SELECT c.sanPham, SUM(c.soLuong) AS tongSoLuong
            FROM DonHangChiTiet c
            GROUP BY c.sanPham
            ORDER BY tongSoLuong DESC
            """)
    List<Object[]> getTopSellingProducts();
}
