package com.example.projectend.controller.admin;

import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODO: THÀNH VIÊN 3 - QUẢN LÝ TÀI KHOẢN
 * Controller xử lý CRUD tài khoản người dùng (admin/nhân viên/khách hàng)
 */
@Controller
@RequestMapping("/admin/taikhoan")
@PreAuthorize("hasAnyRole('Admin', 'Nhân viên')")
public class AdminTaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    /**
     * TODO TV3: Hiển thị danh sách tài khoản với phân trang
     * - Tìm kiếm theo: Họ tên, Email, SĐT
     * - Lọc theo: Vai trò, Trạng thái
     * - Sort: Mới nhất, Tên A-Z
     * <p>
     * HƯỚNG DẪN:
     * 1. Nhận params: search, vaiTroId, trangThai, page, size
     * 2. Gọi taiKhoanService.findWithFilters(...)
     * 3. Truyền Page<TaiKhoan> vào model
     * 4. Truyền danh sách vai trò để render dropdown filter
     */
    @GetMapping
    public String danhSachTaiKhoan(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer vaiTroId,
            @RequestParam(required = false) Boolean trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // TODO TV3: Implement logic
        model.addAttribute("message", "TODO TV3: Danh sách tài khoản - Thêm phân trang và filter");
        return "admin/taikhoan/list";
    }

    /**
     * TODO TV3: Xem chi tiết tài khoản
     * - Thông tin cá nhân
     * - Lịch sử đơn hàng (nếu là khách)
     * - Đơn đã xử lý (nếu là nhân viên)
     */
    @GetMapping("/{id}")
    public String chiTietTaiKhoan(@PathVariable Integer id, Model model) {
        // TODO TV3: Implement
        return "admin/taikhoan/detail";
    }

    /**
     * TODO TV3: Thay đổi vai trò tài khoản (chỉ Admin)
     * VÍ DỤ: Nâng cấp khách hàng thành nhân viên
     */
    @PostMapping("/{id}/change-role")
    @PreAuthorize("hasRole('Admin')")
    public String thayDoiVaiTro(
            @PathVariable Integer id,
            @RequestParam Integer newVaiTroId,
            RedirectAttributes redirectAttributes) {

        // TODO TV3:
        // 1. Tìm tài khoản
        // 2. Cập nhật vai trò
        // 3. Save
        // 4. Flash message thành công

        redirectAttributes.addFlashAttribute("success", "Đã thay đổi vai trò");
        return "redirect:/admin/taikhoan/" + id;
    }

    /**
     * TODO TV3: Kích hoạt/vô hiệu hóa tài khoản
     */
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // TODO TV3:
        // 1. Tìm tài khoản
        // 2. Toggle trangThai (true <-> false)
        // 3. Save

        redirectAttributes.addFlashAttribute("success", "Đã cập nhật trạng thái");
        return "redirect:/admin/taikhoan";
    }

    /**
     * TODO TV3: Reset mật khẩu (Admin only)
     * Mật khẩu mới: "123456" (yêu cầu đổi khi đăng nhập)
     */
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('Admin')")
    public String resetMatKhau(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // TODO TV3:
        // 1. Tìm tài khoản
        // 2. Mã hóa mật khẩu mới bằng passwordEncoder.encode("123456")
        // 3. Save

        redirectAttributes.addFlashAttribute("success", "Đã reset mật khẩu thành 123456");
        return "redirect:/admin/taikhoan/" + id;
    }
}
