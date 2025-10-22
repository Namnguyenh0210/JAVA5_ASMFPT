package com.example.projectend.controller.admin;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.VaiTro;
import com.example.projectend.repository.VaiTroRepository;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/taikhoan")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Danh sách tài khoản
    @GetMapping
    public String danhSachTaiKhoan(Model model) {
        List<TaiKhoan> accounts = taiKhoanService.findAll();
        List<VaiTro> vaiTros = vaiTroRepository.findAll();

        model.addAttribute("accounts", accounts);
        model.addAttribute("vaiTros", vaiTros);
        model.addAttribute("account", new TaiKhoan());
        model.addAttribute("isEdit", false);
        model.addAttribute("currentPage", "taikhoan");
        model.addAttribute("pageTitle", "Quản lý Tài khoản");

        return "admin/taikhoan";
    }

    // Form sửa tài khoản
    @GetMapping("/edit/{id}")
    public String editTaiKhoan(@PathVariable Integer id, Model model) {
        TaiKhoan account = taiKhoanService.findById(id).orElse(new TaiKhoan());
        List<TaiKhoan> accounts = taiKhoanService.findAll();
        List<VaiTro> vaiTros = vaiTroRepository.findAll();

        model.addAttribute("account", account);
        model.addAttribute("accounts", accounts);
        model.addAttribute("vaiTros", vaiTros);
        model.addAttribute("isEdit", true);
        model.addAttribute("currentPage", "taikhoan");
        model.addAttribute("pageTitle", "Sửa Tài khoản");

        return "admin/taikhoan";
    }

    // Lưu tài khoản (thêm mới hoặc cập nhật)
    @PostMapping("/save")
    public String saveTaiKhoan(@ModelAttribute TaiKhoan account,
                               @RequestParam(required = false) String matKhauMoi,
                               RedirectAttributes ra) {
        try {
            // Nếu là tài khoản mới
            if (account.getMaTK() == null) {
                // Mật khẩu mặc định là "123456" nếu không nhập
                String password = (matKhauMoi != null && !matKhauMoi.isEmpty())
                        ? matKhauMoi : "123456";
                account.setMatKhau(passwordEncoder.encode(password));
                account.setTrangThai(true); // Mặc định kích hoạt
            } else {
                // Nếu đang sửa, giữ nguyên mật khẩu cũ nếu không nhập mật khẩu mới
                TaiKhoan existingAccount = taiKhoanService.findById(account.getMaTK()).orElse(null);
                if (existingAccount != null) {
                    if (matKhauMoi != null && !matKhauMoi.isEmpty()) {
                        account.setMatKhau(passwordEncoder.encode(matKhauMoi));
                    } else {
                        account.setMatKhau(existingAccount.getMatKhau());
                    }
                    account.setNgayTao(existingAccount.getNgayTao());
                    account.setTrangThai(existingAccount.getTrangThai());
                }
            }

            taiKhoanService.save(account);
            ra.addFlashAttribute("success",
                    account.getMaTK() == null ? "Thêm tài khoản thành công!" : "Cập nhật tài khoản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/taikhoan";
    }

    // Khóa/Mở khóa tài khoản
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            TaiKhoan account = taiKhoanService.findById(id).orElse(null);
            if (account != null) {
                account.setTrangThai(!account.getTrangThai());
                taiKhoanService.save(account);
                ra.addFlashAttribute("success",
                        account.getTrangThai() ? "Đã mở khóa tài khoản!" : "Đã khóa tài khoản!");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể thay đổi trạng thái: " + e.getMessage());
        }
        return "redirect:/admin/taikhoan";
    }

    // Xóa tài khoản
    @PostMapping("/delete/{id}")
    public String deleteTaiKhoan(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            taiKhoanService.deleteById(id);
            ra.addFlashAttribute("success", "Xóa tài khoản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa tài khoản: " + e.getMessage());
        }
        return "redirect:/admin/taikhoan";
    }

    // Thay đổi vai trò
    @PostMapping("/{id}/change-role")
    public String changeRole(@PathVariable Integer id,
                             @RequestParam Integer vaiTroId,
                             RedirectAttributes ra) {
        try {
            TaiKhoan account = taiKhoanService.findById(id).orElse(null);
            VaiTro vaiTro = vaiTroRepository.findById(vaiTroId).orElse(null);

            if (account != null && vaiTro != null) {
                account.setVaiTro(vaiTro);
                taiKhoanService.save(account);
                ra.addFlashAttribute("success", "Thay đổi vai trò thành công!");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể thay đổi vai trò: " + e.getMessage());
        }
        return "redirect:/admin/taikhoan";
    }
}
