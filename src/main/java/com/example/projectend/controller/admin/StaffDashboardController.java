package com.example.projectend.controller.admin;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.repository.TrangThaiDonHangRepository;
import com.example.projectend.service.BaiVietService;
import com.example.projectend.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * STAFF DASHBOARD CONTROLLER - Dashboard dành riêng cho Nhân viên
 * Nhân viên chỉ có quyền xem: Đơn hàng, Bài viết
 * KHÔNG có quyền: Quản lý sản phẩm, Tài khoản, Báo cáo doanh thu
 */
@Controller
@RequestMapping("/staff")
@PreAuthorize("hasRole('Nhân viên')")
public class StaffDashboardController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private TrangThaiDonHangRepository trangThaiDonHangRepository;

    @GetMapping("/dashboard")
    public String staffDashboard(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        // Lấy thông tin nhân viên hiện tại
        String email = userDetails.getUsername();
        TaiKhoan nhanVien = taiKhoanRepository.findByEmail(email).orElse(null);

        // Thống kê cho nhân viên
        long orderCount = donHangService.countAll();
        long postCount = baiVietService.countAll();

        // Đếm đơn chờ xử lý
        long pendingCount = trangThaiDonHangRepository.findByTenTTDH("Chờ xác nhận")
                .map(tt -> donHangService.getPendingOrders(1000).size())
                .orElse(0);

        // Đếm bài viết của nhân viên (nếu có)
        long myPostCount = 0;
        if (nhanVien != null) {
            myPostCount = baiVietService.getAllBaiVietAdmin(
                            org.springframework.data.domain.PageRequest.of(0, 1000)
                    ).stream()
                    .filter(bv -> bv.getTaiKhoan() != null && bv.getTaiKhoan().getMaTK().equals(nhanVien.getMaTK()))
                    .count();
        }

        model.addAttribute("orderCount", orderCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("myPostCount", myPostCount);

        // Lấy đơn hàng cần xử lý (chờ xác nhận)
        List<DonHang> recentOrders = donHangService.getPendingOrders(10);
        model.addAttribute("recentOrders", recentOrders);

        return "staff/staff-dashboard"; // Sửa từ "admin/staff-dashboard" thành "staff/staff-dashboard"
    }

    @GetMapping("")
    public String redirectToStaffDashboard() {
        return "redirect:/staff/dashboard";
    }
}
