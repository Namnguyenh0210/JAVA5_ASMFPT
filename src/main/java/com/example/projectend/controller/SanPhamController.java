package com.example.projectend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projectend.entity.SanPham;
import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.service.SanPhamService;
import com.example.projectend.service.LoaiSanPhamService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * SAN PHAM CONTROLLER - Hiển thị sản phẩm phía khách hàng
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (3 ENDPOINTS):
 * <p>
 * 1. GET /sanpham - Danh sách sản phẩm (filter, search, sort)
 * → Inject SanPhamService, LoaiSanPhamService
 * → Gọi sanPhamService.findWithFilters(search, loai, minPrice, maxPrice, sort, pageable)
 * → Lấy danh mục: loaiSanPhamService.findAll()
 * → Truyền vào model để render
 * <p>
 * 2. GET /sanpham/{id} - Chi tiết sản phẩm
 * → Load sản phẩm theo ID
 * → Lấy sản phẩm liên quan: sanPhamService.findRelatedProducts()
 * → Tăng lượt xem: sanPhamService.incrementLuotXem(id)
 * → (Optional) Lấy đánh giá: danhGiaService.getDanhGiaBySanPham()
 * <p>
 * 3. GET /api/sanpham/search - AJAX tìm kiếm nhanh (autocomplete)
 * → @ResponseBody return List<SanPham>
 * → Gọi sanPhamService.searchByKeyword(keyword, 10)
 * <p>
 * THỜI GIAN: 1 ngày
 * =============================
 */
@Controller
public class SanPhamController {

    @Autowired private SanPhamService sanPhamService;
    @Autowired private LoaiSanPhamService loaiSanPhamService;

    // =============================
    // TODO TV2: Endpoint 1 - Danh sách sản phẩm
    @GetMapping("/sanpham")
    public String sanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer loai,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "moi") String sort,
            Model model) {

        model.addAttribute("currentPage", "sanpham");
        PageRequest pageable = PageRequest.of(page, size);
        Page<SanPham> sanPhamPage = sanPhamService.findWithFilters(search, loai, minPrice, maxPrice, sort, pageable);
        model.addAttribute("sanPhamPage", sanPhamPage);
        List<LoaiSanPham> categories = loaiSanPhamService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        model.addAttribute("loai", loai);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);

        // Thêm năm Tết
        model.addAttribute("tetYear", java.time.LocalDate.now().getYear() + 1);

        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Sản phẩm");
        breadcrumbItem.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));

        model.addAttribute("pageTitle", "Sản phẩm - Cửa hàng đồ Tết");
        return "sanpham";
    }

    // =============================
    // TODO TV2: Endpoint 2 - Chi tiết sản phẩm
    @GetMapping("/sanpham/{id}")
    public String chiTietSanPham(@PathVariable Integer id, Model model) {

        // Lấy thông tin sản phẩm
        Optional<SanPham> sanPhamOpt = sanPhamService.findById(id);
        if (sanPhamOpt.isEmpty()) {
            return "redirect:/sanpham?error=notfound";
        }

        SanPham sanPham = sanPhamOpt.get();
        model.addAttribute("sanPham", sanPham);

        // Tăng lượt xem
        sanPhamService.incrementLuotXem(id);

        // Sản phẩm liên quan (cùng danh mục)
        List<SanPham> relatedProducts = sanPhamService.findRelatedProducts(
            sanPham.getLoaiSanPham().getMaLoai(),
            sanPham.getMaSP(),
            6
        );
        model.addAttribute("relatedProducts", relatedProducts);

        Map<String, String> breadcrumb1 = new HashMap<>();
        breadcrumb1.put("name", "Sản phẩm");
        breadcrumb1.put("url", "/sanpham");
        Map<String, String> breadcrumb2 = new HashMap<>();
        breadcrumb2.put("name", sanPham.getTenSP());
        breadcrumb2.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumb1, breadcrumb2));

        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", sanPham.getTenSP() + " - Cửa hàng đồ Tết");
        return "sanpham-detail";
    }

    // =============================
    // TODO TV2: Endpoint 3 - AJAX tìm kiếm nhanh (autocomplete)
    @GetMapping("/api/sanpham/search")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<SanPham> quickSearch(@RequestParam String q) {
        return sanPhamService.searchByKeyword(q, 10);
    }
}
