package com.example.projectend.service;

import com.example.projectend.entity.PhuongThucThanhToan;
import com.example.projectend.repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service quản lý phương thức thanh toán
 */
@Service
public class PhuongThucThanhToanService {

    @Autowired
    private PhuongThucThanhToanRepository repo;

    /**
     * Lấy tất cả phương thức thanh toán
     */
    public List<PhuongThucThanhToan> findAll() {
        return repo.findAll();
    }

    /**
     * Tìm phương thức thanh toán theo ID
     */
    public Optional<PhuongThucThanhToan> findById(Integer id) {
        return repo.findById(id);
    }
}
