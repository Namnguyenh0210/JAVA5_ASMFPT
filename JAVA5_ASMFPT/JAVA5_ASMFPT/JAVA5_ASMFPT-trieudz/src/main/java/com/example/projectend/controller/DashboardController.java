package com.example.projectend.controller;

import com.example.projectend.service.DanhGiaService;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

/**
 * DASHBOARD CONTROLLER - Quản trị admin
 * GIAO DIỆN TĨNH - MOCK DATA để các thành viên khác thấy giao diện hoàn chỉnh
 * <p>
 * PHÂN CÔNG:
 * - THÀNH VIÊN 4: Hoàn thiện dashboard, thống kê, CRUD sản phẩm / tài khoản / đơn hàng
 * - THÀNH VIÊN 3: Không chỉnh (trừ khi cần dữ liệu hiển thị chung header)
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN') or hasRole('NHANVIEN')")
public class DashboardController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private DanhGiaService danhGiaService;

    @GetMapping("")
    public String dashboard(Model model) {
        // ===== MOCK DATA - Dữ liệu tĩnh để hiển thị giao diện =====

        // Thống kê tổng quan
        model.addAttribute("totalUsers", 1234);
        model.addAttribute("totalOrders", 856);
        model.addAttribute("totalProducts", 325);
        model.addAttribute("totalPosts", 48);

        // Doanh thu
        model.addAttribute("doanhThuHomNay", 2500000.0);
        model.addAttribute("doanhThuTuan", 12800000.0);
        model.addAttribute("doanhThuThang", 45200000.0);

        // Thống kê nhanh
        model.addAttribute("pendingOrderCount", 15);
        model.addAttribute("lowStockCount", 8);

        // Dữ liệu biểu đồ doanh thu 7 ngày
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", Arrays.asList("7/10", "8/10", "9/10", "10/10", "11/10", "12/10", "13/10"));
        chartData.put("data", Arrays.asList(3.2, 4.5, 3.8, 5.2, 4.1, 6.3, 5.8));
        model.addAttribute("chartData", chartData);

        // Top 5 sản phẩm bán chạy
        List<Map<String, Object>> topProducts = new ArrayList<>();

        Map<String, Object> product1 = new HashMap<>();
        product1.put("tenSP", "Giỏ quà Tết An Khang");
        product1.put("soLuongBan", 145);
        product1.put("doanhThu", 7250000.0);
        product1.put("hinhAnh", "https://picsum.photos/200/300");
        topProducts.add(product1);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("tenSP", "Hộp quà Tết Phát Tài");
        product2.put("soLuongBan", 98);
        product2.put("doanhThu", 11760000.0);
        product2.put("hinhAnh", "https://picsum.photos/200/301");
        topProducts.add(product2);

        Map<String, Object> product3 = new HashMap<>();
        product3.put("tenSP", "Mứt dừa non hộp 500g");
        product3.put("soLuongBan", 267);
        product3.put("doanhThu", 4005000.0);
        product3.put("hinhAnh", "https://picsum.photos/200/302");
        topProducts.add(product3);

        Map<String, Object> product4 = new HashMap<>();
        product4.put("tenSP", "Bao lì xì vàng 2025");
        product4.put("soLuongBan", 523);
        product4.put("doanhThu", 1569000.0);
        product4.put("hinhAnh", "https://picsum.photos/200/303");
        topProducts.add(product4);

        Map<String, Object> product5 = new HashMap<>();
        product5.put("tenSP", "Bánh kẹo hỗn hợp");
        product5.put("soLuongBan", 189);
        product5.put("doanhThu", 3780000.0);
        product5.put("hinhAnh", "https://picsum.photos/200/304");
        topProducts.add(product5);

        model.addAttribute("topProducts", topProducts);

        // Đơn hàng chờ xử lý
        List<Map<String, Object>> pendingOrders = new ArrayList<>();

        Map<String, Object> order1 = new HashMap<>();
        order1.put("maDH", "DH001");
        order1.put("khachHang", "Nguyễn Văn A");
        order1.put("tongTien", 1250000.0);
        order1.put("ngayDat", "13/10/2025 09:30");
        order1.put("trangThai", "Chờ xác nhận");
        pendingOrders.add(order1);

        Map<String, Object> order2 = new HashMap<>();
        order2.put("maDH", "DH002");
        order2.put("khachHang", "Trần Thị B");
        order2.put("tongTien", 850000.0);
        order2.put("ngayDat", "13/10/2025 08:15");
        order2.put("trangThai", "Chờ xác nhận");
        pendingOrders.add(order2);

        Map<String, Object> order3 = new HashMap<>();
        order3.put("maDH", "DH003");
        order3.put("khachHang", "Lê Văn C");
        order3.put("tongTien", 2100000.0);
        order3.put("ngayDat", "12/10/2025 16:45");
        order3.put("trangThai", "Chờ xác nhận");
        pendingOrders.add(order3);

        model.addAttribute("pendingOrders", pendingOrders);

        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("pageTitle", "Dashboard - Quản lý cửa hàng");
        return "admin/dashboard";
    }

    @GetMapping("/danhgia")
    public String quanLyDanhGia(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        // TODO THÀNH VIÊN 4: Phân trang danh sách đánh giá (danhGiaService.getAll...)
        model.addAttribute("currentPage", "danhgia");
        model.addAttribute("pageTitle", "Quản lý đánh giá");
        return "admin/danhgia/list";
    }

    @PostMapping("/danhgia/delete/{id}")
    public String xoaDanhGia(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            danhGiaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đánh giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/danhgia";
    }

    // NOTE: /admin/thongke endpoint removed - now handled by AdminThongKeController
    // TODO THÀNH VIÊN 4: Use AdminThongKeController for detailed statistics functionality

    // TODO THÀNH VIÊN 4: Tạo thêm controllers con:
    //  - /admin/sanpham/* (CRUD + upload ảnh)
    //  - /admin/donhang/* (list + update status)
    //  - /admin/taikhoan/* (CRUD user, reset password)
}
