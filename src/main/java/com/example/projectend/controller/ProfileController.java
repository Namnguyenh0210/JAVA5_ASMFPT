package com.example.projectend.controller;

import com.example.projectend.entity.DiaChi;
import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.DiaChiRepository;
import com.example.projectend.repository.DonHangRepository;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PROFILE CONTROLLER - Quản lý thông tin cá nhân & đơn hàng
 */
@Controller
public class ProfileController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangService donHangService;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        model.addAttribute("currentPage", "profile");

        // Lấy email của người dùng hiện tại
        String email = principal.getName();

        // Tìm thông tin tài khoản theo email
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);
        if (taiKhoan == null) {
            model.addAttribute("error", "Không tìm thấy tài khoản của bạn.");
            return "error/403";
        }

        model.addAttribute("taiKhoan", taiKhoan);

        // Tìm danh sách địa chỉ của tài khoản
        List<DiaChi> diaChiList = diaChiRepository.findAllByMaTK(taiKhoan.getMaTK());
        model.addAttribute("diaChiList", diaChiList);

        // Lấy danh sách đơn hàng thật từ database (mới nhất trước)
        List<DonHang> donHangList = donHangRepository.findByKhachHangOrderByNgayDatDesc(taiKhoan);
        model.addAttribute("donHangList", donHangList);

        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Thông tin cá nhân");
        breadcrumbItem.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));
        model.addAttribute("pageTitle", "Thông tin cá nhân - Tết Market");

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String hoTen,
            @RequestParam(required = false) String soDienThoai,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String email = principal.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);

            if (taiKhoan == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản!");
                return "redirect:/profile";
            }

            // Cập nhật thông tin
            taiKhoan.setHoTen(hoTen);
            taiKhoan.setSoDienThoai(soDienThoai);
            taiKhoanRepository.save(taiKhoan);

            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String email = principal.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);

            if (taiKhoan == null) {
                redirectAttributes.addFlashAttribute("errorPassword", "Không tìm thấy tài khoản!");
                return "redirect:/profile#password";
            }

            // Kiểm tra mật khẩu hiện tại
            if (!taiKhoan.getMatKhau().equals(currentPassword)) {
                redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu hiện tại không đúng!");
                return "redirect:/profile#password";
            }

            // Kiểm tra mật khẩu mới khớp
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorPassword", "Mật khẩu mới không khớp!");
                return "redirect:/profile#password";
            }

            // Cập nhật mật khẩu mới
            taiKhoan.setMatKhau(newPassword);
            taiKhoanRepository.save(taiKhoan);

            redirectAttributes.addFlashAttribute("successPassword", "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorPassword", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/profile#password";
    }

    @PostMapping("/profile/cancel-order")
    public String cancelOrder(
            @RequestParam Integer orderId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String email = principal.getName();
            TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);

            if (taiKhoan == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản!");
                return "redirect:/profile#orders";
            }

            boolean success = donHangService.cancelOrder(orderId, taiKhoan);

            if (success) {
                redirectAttributes.addFlashAttribute("successOrder", "Hủy đơn hàng thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorOrder", "Không thể hủy đơn hàng này!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorOrder", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/profile#orders";
    }
}
