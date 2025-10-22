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
 * Controller xử lý trang chủ website
 */
@Controller
public class HomeController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private BaiVietService baiVietService;

    /**
     * Hiển thị trang chủ với sản phẩm nổi bật và tin tức
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");

        // Lấy 8 sản phẩm nổi bật
        List<SanPham> sanPhamNoiBat = sanPhamService.getFeaturedProducts(8);
        model.addAttribute("sanPhamNoiBat", sanPhamNoiBat);

        // Lấy 3 bài viết mới nhất
        List<BaiViet> tinTuc = baiVietService.getFeaturedPosts(3);
        model.addAttribute("tinTuc", tinTuc);

        // Lấy danh mục cho menu
        List<LoaiSanPham> danhMuc = sanPhamService.getAllCategories();
        model.addAttribute("danhMuc", danhMuc);

        // Thông tin năm
        int currentYear = LocalDate.now().getYear();
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("tetYear", currentYear + 1);

        model.addAttribute("pageTitle", "Trang chủ - Cửa hàng đồ Tết Nguyên Đán 2025");
        return "home";
    }

    /**
     * Redirect từ /home về trang chủ
     */
    @GetMapping("/home")
    public String homePage(Model model) {
        return home(model);
    }
}
