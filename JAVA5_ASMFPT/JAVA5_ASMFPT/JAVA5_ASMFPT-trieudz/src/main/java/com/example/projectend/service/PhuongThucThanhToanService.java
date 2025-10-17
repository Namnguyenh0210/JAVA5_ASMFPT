package com.example.projectend.service;

import com.example.projectend.entity.PhuongThucThanhToan;
import com.example.projectend.repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * PHUONG THUC THANH TOAN SERVICE - Quản lý phương thức thanh toán
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (2 METHODS):
 *  1. findAll() - Lấy tất cả phương thức thanh toán
 *     → Dùng cho trang checkout
 *     → return repo.findAll();
 * <p>
 * 2. findById(Integer id) - Lấy PTTT theo ID
 * → return repo.findById(id);
 * <p>
 * THỜI GIAN: 30 phút (rất đơn giản)
 * =============================
 */
@Service
public class PhuongThucThanhToanService {

    @Autowired
    private PhuongThucThanhToanRepository repo;

    // =============================
    // TODO TV2: Method 1 - Lấy tất cả PTTT
    // HƯỚNG DẪN: return repo.findAll();
    public List<PhuongThucThanhToan> findAll() {
        return Collections.emptyList(); // TODO TV2: Implement
    }

    // =============================
    // TODO TV2: Method 2 - Tìm theo ID (đã có sẵn)
    public Optional<PhuongThucThanhToan> findById(Integer id) {
        return repo.findById(id);
    }
}
