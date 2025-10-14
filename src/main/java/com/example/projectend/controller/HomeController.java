package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    // TODO TV2: Bước 1 - Inject services (gỡ comment)
    // @Autowired
    // private SanPhamService sanPhamService;
    //
    // @Autowired
    // private BaiVietService baiVietService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");

        // TODO TV2: Bước 2 - Gỡ comment sau khi implement Service
        // HƯỚNG DẪN:
        // List<SanPham> sanPhamNoiBat = sanPhamService.getFeaturedProducts(8);
        // model.addAttribute("sanPhamNoiBat", sanPhamNoiBat);
        //
        // List<BaiViet> tinTuc = baiVietService.getFeaturedPosts(3);
        // model.addAttribute("tinTuc", tinTuc);
        //
        // List<LoaiSanPham> danhMuc = sanPhamService.getAllCategories();
        // model.addAttribute("danhMuc", danhMuc);

        model.addAttribute("pageTitle", "Trang chủ - Cửa hàng đồ Tết Nguyên Đán 2025");
        return "home";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return home(model);
    }
}
