package com.example.projectend.controller;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.VaiTro;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/**
 * Controller xử lý đăng nhập và đăng ký tài khoản
 */
@Controller
public class AuthController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    /**
     * Hiển thị trang đăng nhập
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không đúng!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Đăng xuất thành công!");
        }
        return "login1";
    }

    /**
     * Hiển thị trang đăng ký
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("taiKhoan", new TaiKhoan());
        return "register";
    }

    /**
     * Xử lý đăng ký tài khoản mới
     */
    @PostMapping("/register")
    public String registerSubmit(@RequestParam String hoTen,
                                 @RequestParam String email,
                                 @RequestParam String matKhau,
                                 @RequestParam String confirmPassword,
                                 @RequestParam(required = false) String soDienThoai,
                                 RedirectAttributes redirectAttributes) {

        // Kiểm tra họ tên
        if (hoTen == null || hoTen.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Họ tên không được để trống!");
            return "redirect:/register";
        }

        // Kiểm tra email
        if (email == null || email.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email không được để trống!");
            return "redirect:/register";
        }

        // Kiểm tra mật khẩu
        if (matKhau == null || matKhau.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "redirect:/register";
        }

        // Kiểm tra xác nhận mật khẩu
        if (!matKhau.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            return "redirect:/register";
        }

        // Kiểm tra email đã tồn tại
        if (taiKhoanRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email đã tồn tại trong hệ thống!");
            return "redirect:/register";
        }

        try {
            // Lấy vai trò khách hàng
            VaiTro vaiTroUser = vaiTroRepository.findByTenVT("Khách hàng");
            if (vaiTroUser == null) {
                throw new RuntimeException("Không tìm thấy vai trò Khách hàng");
            }

            // Tạo tài khoản mới
            TaiKhoan taiKhoanMoi = new TaiKhoan();
            taiKhoanMoi.setHoTen(hoTen.trim());
            taiKhoanMoi.setEmail(email.trim().toLowerCase());
            taiKhoanMoi.setMatKhau(matKhau);
            taiKhoanMoi.setSoDienThoai(soDienThoai);
            taiKhoanMoi.setVaiTro(vaiTroUser);
            taiKhoanMoi.setTrangThai(true);
            taiKhoanMoi.setNgayTao(LocalDateTime.now());

            // Lưu vào database
            taiKhoanRepository.save(taiKhoanMoi);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Có lỗi xảy ra khi đăng ký. Vui lòng thử lại!");
            return "redirect:/register";
        }
    }

    /**
     * Hiển thị trang lỗi 403 - Không có quyền truy cập
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }
}
