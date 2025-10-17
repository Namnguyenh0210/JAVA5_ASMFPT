package com.example.projectend.controller;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.entity.SanPham;
import com.example.projectend.service.BaiVietService;
import com.example.projectend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

/**
 * HOME CONTROLLER - Trang chủ website bán đồ Tết
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM:
 * <p>
 * 1. @Autowired các service:
 * - SanPhamService sanPhamService;
 * - BaiVietService baiVietService;
 * <p>
 * 2. Trong method home():
 * a) Lấy 8 sản phẩm nổi bật:
 * List<SanPham> featured = sanPhamService.getFeaturedProducts(8);
 * model.addAttribute("sanPhamNoiBat", featured);
 * <p>
 * b) Lấy 3 bài viết mới:
 * List<BaiViet> posts = baiVietService.getFeaturedPosts(3);
 * model.addAttribute("tinTuc", posts);
 * <p>
 * c) Lấy danh mục (cho menu):
 * List<LoaiSanPham> categories = sanPhamService.getAllCategories();
 * model.addAttribute("danhMuc", categories);
 * <p>
 * 3. Cập nhật template home.html để hiển thị dữ liệu
 * <p>
 * THỜI GIAN: 1 giờ (rất đơn giản)
 * =============================
 */
@Controller
public class HomeController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private BaiVietService baiVietService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");

        // Lấy 8 sản phẩm nổi bật
        List<SanPham> sanPhamNoiBat = sanPhamService.getFeaturedProducts(8);
        model.addAttribute("sanPhamNoiBat", sanPhamNoiBat);

        // Lấy 3 bài viết mới nhất
        List<BaiViet> tinTuc = baiVietService.getFeaturedPosts(3);
        model.addAttribute("tinTuc", tinTuc);

        // Lấy danh mục (cho menu)
        List<LoaiSanPham> danhMuc = sanPhamService.getAllCategories();
        model.addAttribute("danhMuc", danhMuc);

        // Thêm năm hiện tại và năm Tết
        int currentYear = LocalDate.now().getYear();
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("tetYear", currentYear + 1);

        model.addAttribute("pageTitle", "Trang chủ - Cửa hàng đồ Tết Nguyên Đán 2025");
        return "home";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return home(model);
    }
}
