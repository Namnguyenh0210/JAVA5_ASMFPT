package com.example.projectend.controller;

import com.example.projectend.entity.DonHang;
import com.example.projectend.service.BaiVietService;
import com.example.projectend.service.DonHangService;
import com.example.projectend.service.SanPhamService;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * DASHBOARD CONTROLLER - Trang quản trị
 * Tự động chuyển hướng dựa trên vai trò:
 * - Admin -> /admin/dashboard (full quyền)
 * - Nhân viên -> /staff/dashboard (quyền hạn chế)
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('Admin') or hasRole('Nhân viên')")
public class DashboardController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    /**
     * Trang dashboard chính - tự động chuyển hướng theo vai trò
     */
    @GetMapping("")
    public String redirectToDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        // Kiểm tra vai trò và chuyển hướng
        boolean isAdmin = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_Admin"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/staff/dashboard";
        }
    }

    /**
     * Dashboard cho ADMIN - Full quyền
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('Admin')")
    public String adminDashboard(Model model) {
        // Thống kê tổng quan
        long productCount = sanPhamService.countAll();
        long orderCount = donHangService.countAll();
        long postCount = baiVietService.countAll();

        model.addAttribute("productCount", productCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("postCount", postCount);
        model.addAttribute("revenueToday", "0");

        // Lấy 10 đơn hàng gần đây
        List<DonHang> recentOrders = donHangService.getRecentOrders(10);
        model.addAttribute("recentOrders", recentOrders);

        return "admin/dashboard";
    }
}
