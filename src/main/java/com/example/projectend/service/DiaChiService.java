package com.example.projectend.service;

import com.example.projectend.entity.DiaChi;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.DiaChiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * DIA CHI SERVICE - Quản lý địa chỉ giao hàng
 */
@Service
public class DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    // Lấy danh sách địa chỉ của user
    public List<DiaChi> getDiaChiByTaiKhoan(TaiKhoan tk) {
        return diaChiRepository.findByTaiKhoanOrderByMacDinhDesc(tk);
    }

    // Lấy địa chỉ mặc định
    public Optional<DiaChi> getDiaChiMacDinh(TaiKhoan tk) {
        return diaChiRepository.findByTaiKhoanAndMacDinhTrue(tk);
    }

    // Lưu địa chỉ
    public DiaChi save(DiaChi dc) {
        return diaChiRepository.save(dc);
    }

    // Đặt địa chỉ làm mặc định
    public void setMacDinh(Integer id, TaiKhoan tk) {
        List<DiaChi> allAddresses = getDiaChiByTaiKhoan(tk);
        for (DiaChi dc : allAddresses) {
            dc.setMacDinh(dc.getMaDC().equals(id));
            diaChiRepository.save(dc);
        }
    }

    public Optional<DiaChi> findById(Integer id) {
        return diaChiRepository.findById(id);
    }

    // Xóa địa chỉ
    public void delete(Integer id, TaiKhoan tk) {
        Optional<DiaChi> diaChi = findById(id);
        if (diaChi.isPresent() && !diaChi.get().getMacDinh()) {
            diaChiRepository.deleteById(id);
        }
    }
}
