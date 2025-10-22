package com.example.projectend.service;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.VaiTro;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service xử lý logic nghiệp vụ cho tài khoản
 */
@Service
public class TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    /**
     * Tìm tài khoản theo email
     */
    public TaiKhoan findByEmail(String email) {
        return taiKhoanRepository.findByEmail(email).orElse(null);
    }

    /**
     * Lưu hoặc cập nhật tài khoản
     */
    public TaiKhoan save(TaiKhoan taiKhoan) {
        return taiKhoanRepository.save(taiKhoan);
    }

    /**
     * Tìm tài khoản theo ID
     */
    public Optional<TaiKhoan> findById(Integer id) {
        return taiKhoanRepository.findById(id);
    }

    /**
     * Xóa tài khoản theo ID
     */
    public void deleteById(Integer id) {
        taiKhoanRepository.deleteById(id);
    }

    /**
     * Lấy tất cả tài khoản
     */
    public List<TaiKhoan> findAll() {
        return taiKhoanRepository.findAll();
    }

    /**
     * Lấy tất cả tài khoản với phân trang
     */
    public Page<TaiKhoan> getAllTaiKhoan(Pageable pageable) {
        return taiKhoanRepository.findAll(pageable);
    }

    /**
     * Đếm số tài khoản theo vai trò
     */
    public Long countByVaiTro(String tenVaiTro) {
        VaiTro vaiTro = vaiTroRepository.findByTenVT(tenVaiTro);
        if (vaiTro != null) {
            return taiKhoanRepository.countByVaiTro(vaiTro);
        }
        return 0L;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean existsByEmail(String email) {
        return taiKhoanRepository.findByEmail(email).isPresent();
    }

    /**
     * Cập nhật thông tin cá nhân
     */
    public TaiKhoan updateProfile(TaiKhoan tk, String hoTen, String soDienThoai) {
        tk.setHoTen(hoTen);
        tk.setSoDienThoai(soDienThoai);
        return save(tk);
    }

    /**
     * Đổi mật khẩu
     */
    public TaiKhoan changePassword(TaiKhoan tk, String newPassword) {
        tk.setMatKhau(newPassword);
        return save(tk);
    }

    /**
     * Khóa tài khoản (admin)
     */
    public boolean lockAccount(Integer id) {
        Optional<TaiKhoan> tkOpt = findById(id);
        if (tkOpt.isPresent()) {
            TaiKhoan tk = tkOpt.get();
            tk.setTrangThai(false);
            save(tk);
            return true;
        }
        return false;
    }

    /**
     * Mở khóa tài khoản (admin)
     */
    public boolean unlockAccount(Integer id) {
        Optional<TaiKhoan> tkOpt = findById(id);
        if (tkOpt.isPresent()) {
            TaiKhoan tk = tkOpt.get();
            tk.setTrangThai(true);
            save(tk);
            return true;
        }
        return false;
    }

    /**
     * Đổi vai trò tài khoản (admin)
     */
    public boolean changeRole(Integer id, Integer vaiTroId) {
        Optional<TaiKhoan> tkOpt = findById(id);
        VaiTro vaiTro = vaiTroRepository.findById(vaiTroId).orElse(null);

        if (tkOpt.isPresent() && vaiTro != null) {
            TaiKhoan tk = tkOpt.get();
            tk.setVaiTro(vaiTro);
            save(tk);
            return true;
        }
        return false;
    }
}
