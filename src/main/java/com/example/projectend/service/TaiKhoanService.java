package com.example.projectend.service;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.VaiTro;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TAI KHOAN SERVICE - Xử lý logic tài khoản
 * <p>
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND
 * =============================
 * TODO TV3 - CẦN LÀM (5 METHODS):
 * <p>
 * 1. updateProfile(TaiKhoan tk, String hoTen, String sdt) - Cập nhật thông tin
 * → tk.setHoTen(hoTen);
 * → tk.setSoDienThoai(sdt);
 * → return save(tk);
 * <p>
 * 2. changePassword(TaiKhoan tk, String newPassword) - Đổi mật khẩu
 * → tk.setMatKhau(newPassword); // Plain text cho ASM
 * → return save(tk);
 * <p>
 * 3. lockAccount(Integer id) - Khóa tài khoản (admin)
 * → Set TrangThai = "Vô hiệu hóa"
 * <p>
 * 4. unlockAccount(Integer id) - Mở khóa tài khoản (admin)
 * → Set TrangThai = "Hoạt động"
 * <p>
 * 5. changeRole(Integer id, Integer vaiTroId) - Đổi vai trò (admin)
 * → Tìm VaiTro mới
 * → Set vaiTro cho tài khoản
 * <p>
 * THỜI GIAN: 2 ngày
 * LƯU Ý: Methods cơ bản (findByEmail, save, findById) đã có sẵn
 * =============================
 */
@Service
public class TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    // =============================
    // METHODS CƠ BẢN (ĐÃ CÓ SẴN)

    public TaiKhoan findByEmail(String email) {
        return taiKhoanRepository.findByEmail(email).orElse(null);
    }

    public TaiKhoan save(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    public Optional<TaiKhoan> findById(Integer id) {
        return taiKhoanRepository.findById(id);
    }

    public void deleteById(Integer id) {
        taiKhoanRepository.deleteById(id);
    }

    public Page<TaiKhoan> getAllTaiKhoan(Pageable pageable) {
        return taiKhoanRepository.findAll(pageable);
    }

    public Long countByVaiTro(String tenVaiTro) {
        VaiTro vaiTro = vaiTroRepository.findByTenVT(tenVaiTro);
        if (vaiTro != null) {
            return taiKhoanRepository.countByVaiTro(vaiTro);
        }
        return 0L;
    }

    public boolean existsByEmail(String email) {
        return taiKhoanRepository.findByEmail(email).isPresent();
    }

    // =============================
    // TODO TV3: Method 1 - Cập nhật thông tin cá nhân
    // HƯỚNG DẪN:
    // tk.setHoTen(hoTen);
    // tk.setSoDienThoai(soDienThoai);
    // return save(tk);
    public TaiKhoan updateProfile(TaiKhoan tk, String hoTen, String soDienThoai) {
        return null; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 2 - Đổi mật khẩu
    // HƯỚNG DẪN:
    // tk.setMatKhau(newPassword); // Plain text cho ASM
    // return save(tk);
    public TaiKhoan changePassword(TaiKhoan tk, String newPassword) {
        return null; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 3 - Khóa tài khoản (admin)
    // HƯỚNG DẪN:
    // Optional<TaiKhoan> tkOpt = findById(id);
    // if (tkOpt.isPresent()) {
    //     tkOpt.get().setTrangThai("Vô hiệu hóa");
    //     save(tkOpt.get());
    //     return true;
    // }
    // return false;
    public boolean lockAccount(Integer id) {
        return false; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 4 - Mở khóa tài khoản (admin)
    // HƯỚNG DẪN: Tương tự lockAccount, set TrangThai = "Hoạt động"
    public boolean unlockAccount(Integer id) {
        return false; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 5 - Đổi vai trò (admin)
    // HƯỚNG DẪN:
    // Optional<TaiKhoan> tkOpt = findById(id);
    // Optional<VaiTro> vtOpt = vaiTroRepository.findById(vaiTroId);
    // if (tkOpt.isPresent() && vtOpt.isPresent()) {
    //     tkOpt.get().setVaiTro(vtOpt.get());
    //     save(tkOpt.get());
    //     return true;
    // }
    // return false;
    public boolean changeRole(Integer id, Integer vaiTroId) {
        return false; // TODO TV3: Implement
    }
}
