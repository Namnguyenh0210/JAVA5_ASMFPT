package com.example.projectend.service;

import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.repository.LoaiSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service quản lý danh mục sản phẩm
 */
@Service
public class LoaiSanPhamService {

    @Autowired
    private LoaiSanPhamRepository repo;

    /**
     * Lấy tất cả danh mục sản phẩm
     */
    public List<LoaiSanPham> findAll() {
        return repo.findAll();
    }

    /**
     * Tìm danh mục theo ID
     */
    public Optional<LoaiSanPham> findById(Integer id) {
        return repo.findById(id);
    }

    /**
     * Lưu hoặc cập nhật danh mục
     */
    public LoaiSanPham save(LoaiSanPham loai) {
        return repo.save(loai);
    }
}
