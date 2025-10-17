package com.example.projectend.service;

import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.repository.LoaiSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * LOAI SAN PHAM SERVICE - Quản lý danh mục sản phẩm
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (2 METHODS):
 *  1. findAll() - Lấy tất cả danh mục
 *     → Dùng cho menu, filter sản phẩm
 *     → return repo.findAll();
 * <p>
 * 2. findById(Integer id) - Lấy danh mục theo ID
 * → return repo.findById(id);
 * <p>
 * THỜI GIAN: 30 phút (rất đơn giản)
 * NOTE: TV3 sẽ dùng methods này cho Admin CRUD danh mục
 * =============================
 */
@Service
public class LoaiSanPhamService {

    @Autowired
    private LoaiSanPhamRepository repo;

    // =============================
    // TODO TV2: Method 1 - Lấy tất cả danh mục
    // HƯỚNG DẪN: return repo.findAll();
    public List<LoaiSanPham> findAll() {
        return repo.findAll();
    }

    // =============================
    // TODO TV2: Method 2 - Tìm theo ID (đã có sẵn)
    public Optional<LoaiSanPham> findById(Integer id) {
        return repo.findById(id);
    }

    // =============================
    // NOTE: TV3 Admin sẽ dùng save() để CRUD danh mục
    public LoaiSanPham save(LoaiSanPham loai) {
        return repo.save(loai);
    }
}
