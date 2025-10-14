package com.example.projectend.service;

import com.example.projectend.entity.DiaChi;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.DiaChiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * DIA CHI SERVICE - Quản lý địa chỉ giao hàng
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (4 METHODS):
 *  1. getDiaChiByTaiKhoan(TaiKhoan tk) - Lấy danh sách địa chỉ của user
 *     → return diaChiRepository.findByTaiKhoan(tk);
 * <p>
 * 2. getDiaChiMacDinh(TaiKhoan tk) - Lấy địa chỉ mặc định
 * → return diaChiRepository.findByTaiKhoanAndMacDinh(tk, true);
 * <p>
 * 3. save(DiaChi dc) - Lưu địa chỉ mới hoặc cập nhật
 * → return diaChiRepository.save(dc);
 * <p>
 * 4. setMacDinh(Integer id, TaiKhoan tk) - Đặt địa chỉ làm mặc định
 * → Bỏ MacDinh của tất cả địa chỉ khác của user
 * → Set MacDinh=true cho địa chỉ id
 * <p>
 * THỜI GIAN: 1 ngày
 * =============================
 */
@Service
public class DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    // =============================
    // TODO TV2: Method 1 - Lấy danh sách địa chỉ của user
    // HƯỚNG DẪN: Tạo query trong Repository:
    // List<DiaChi> findByTaiKhoan(TaiKhoan tk);
    public List<DiaChi> getDiaChiByTaiKhoan(TaiKhoan tk) {
        return Collections.emptyList(); // TODO TV2: Implement
    }

    // =============================
    // TODO TV2: Method 2 - Lấy địa chỉ mặc định
    // HƯỚNG DẪN: Tạo query trong Repository:
    // Optional<DiaChi> findByTaiKhoanAndMacDinh(TaiKhoan tk, boolean macDinh);
    public Optional<DiaChi> getDiaChiMacDinh(TaiKhoan tk) {
        return Optional.empty(); // TODO TV2: Implement
    }

    // =============================
    // TODO TV2: Method 3 - Lưu địa chỉ (đã có sẵn)
    public DiaChi save(DiaChi dc) {
        return diaChiRepository.save(dc);
    }

    // =============================
    // TODO TV2: Method 4 - Đặt địa chỉ làm mặc định
    // HƯỚNG DẪN:
    // 1. List<DiaChi> allAddresses = getDiaChiByTaiKhoan(tk);
    // 2. for (DiaChi dc : allAddresses) {
    //      dc.setMacDinh(dc.getMaDC().equals(id));
    //      diaChiRepository.save(dc);
    //    }
    public void setMacDinh(Integer id, TaiKhoan tk) {
        // TODO TV2: Implement set mặc định
    }

    // =============================
    public Optional<DiaChi> findById(Integer id) {
        return diaChiRepository.findById(id);
    }

    // =============================
    // TODO TV2: Method bonus - Xóa địa chỉ (optional)
    // CHỈ xóa nếu không phải địa chỉ mặc định
    public void delete(Integer id, TaiKhoan tk) {
        // TODO TV2: Implement xóa (optional)
    }
}
