package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PROFILE CONTROLLER - Quản lý thông tin cá nhân & đơn hàng
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 + TV3 (PHỤ THUỘC!)
 * =============================
 * TODO TV2 - CẦN LÀM:
 * <p>
 * 1. GET /profile - Hiển thị thông tin cá nhân
 * → Load user info, địa chỉ, đơn hàng gần đây
 * → Gọi donHangService.getDonHangByKhachHang() (TV3 làm)
 * <p>
 * 2. POST /profile/update - Cập nhật thông tin
 * → Gọi taiKhoanService.updateProfile() (TV3 làm)
 * <p>
 * 3. POST /profile/change-password - Đổi mật khẩu
 * → Gọi taiKhoanService.changePassword() (TV3 làm)
 * <p>
 * TODO TV3 - CẦN LÀM:
 * <p>
 * 4. GET /profile/orders/{id} - Chi tiết đơn hàng
 * → Load đơn: donHangService.findByIdAndKhachHang()
 * → Load chi tiết: donHangService.getChiTietDonHang()
 * <p>
 * 5. POST /profile/orders/{id}/cancel - Hủy đơn
 * → Gọi donHangService.cancelOrder()
 * <p>
 * THỜI GIAN: TV2 (1 ngày) + TV3 (1 ngày)
 * =============================
 */
@Controller
public class ProfileController {

    // TODO TV2: Inject services
    // @Autowired private TaiKhoanService taiKhoanService;
    // @Autowired private DiaChiService diaChiService;

    // TODO TV3: Inject DonHangService
    // @Autowired private DonHangService donHangService;

    // =============================
    // TODO TV2: Endpoint 1 - Trang profile
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("currentPage", "profile");

        // TODO TV2: Load dữ liệu
        // HƯỚNG DẪN:
        // TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
        // model.addAttribute("user", tk);
        //
        // List<DiaChi> diaChiList = diaChiService.getDiaChiByTaiKhoan(tk);
        // model.addAttribute("diaChiList", diaChiList);
        //
        // // Đơn hàng gần đây (TV3 cần làm method này)
        // Page<DonHang> donHangPage = donHangService.getDonHangByKhachHang(tk, PageRequest.of(0, 5));
        // model.addAttribute("donHangPage", donHangPage);

        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Thông tin cá nhân");
        breadcrumbItem.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));
        model.addAttribute("pageTitle", "Thông tin cá nhân - Cửa hàng đồ Tết");
        return "profile";
    }

    // =============================
    // TODO TV2: Endpoint 2 - Cập nhật thông tin
    // HƯỚNG DẪN:
    // @PostMapping("/profile/update")
    // public String updateProfile(
    //         @RequestParam String hoTen,
    //         @RequestParam String soDienThoai,
    //         Principal principal,
    //         RedirectAttributes redirectAttributes) {
    //
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //         taiKhoanService.updateProfile(tk, hoTen, soDienThoai); // TV3 làm method này
    //
    //         redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", e.getMessage());
    //     }
    //
    //     return "redirect:/profile";
    // }

    // =============================
    // TODO TV2: Endpoint 3 - Đổi mật khẩu
    // HƯỚNG DẪN:
    // @PostMapping("/profile/change-password")
    // public String changePassword(
    //         @RequestParam String currentPassword,
    //         @RequestParam String newPassword,
    //         @RequestParam String confirmPassword,
    //         Principal principal,
    //         RedirectAttributes redirectAttributes) {
    //
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //
    //         // Validate
    //         if (!tk.getMatKhau().equals(currentPassword)) {
    //             redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng");
    //             return "redirect:/profile";
    //         }
    //
    //         if (!newPassword.equals(confirmPassword)) {
    //             redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp");
    //             return "redirect:/profile";
    //         }
    //
    //         taiKhoanService.changePassword(tk, newPassword); // TV3 làm method này
    //         redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", e.getMessage());
    //     }
    //
    //     return "redirect:/profile";
    // }

    // =============================
    // TODO TV3: Endpoint 4 - Chi tiết đơn hàng
    // HƯỚNG DẪN:
    // @GetMapping("/profile/orders/{id}")
    // public String orderDetail(@PathVariable Integer id, Model model, Principal principal) {
    //
    //     TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //
    //     Optional<DonHang> donHangOpt = donHangService.findByIdAndKhachHang(id, tk);
    //     if (donHangOpt.isEmpty()) {
    //         return "redirect:/profile?error=notfound";
    //     }
    //
    //     DonHang donHang = donHangOpt.get();
    //     model.addAttribute("donHang", donHang);
    //
    //     List<DonHangChiTiet> chiTiet = donHangService.getChiTietDonHang(donHang);
    //     model.addAttribute("chiTiet", chiTiet);
    //
    //     model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + id);
    //     return "order-detail";
    // }

    // =============================
    // TODO TV3: Endpoint 5 - Hủy đơn hàng
    // HƯỚNG DẪN:
    // @PostMapping("/profile/orders/{id}/cancel")
    // public String cancelOrder(@PathVariable Integer id, Principal principal, RedirectAttributes redirectAttributes) {
    //
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //         boolean success = donHangService.cancelOrder(id, tk);
    //
    //         if (success) {
    //             redirectAttributes.addFlashAttribute("success", "Hủy đơn hàng thành công");
    //         } else {
    //             redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng này");
    //         }
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", e.getMessage());
    //     }
    //
    //     return "redirect:/profile";
    // }
}
