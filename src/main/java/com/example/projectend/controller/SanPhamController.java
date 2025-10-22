package com.example.projectend.controller;

import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.entity.SanPham;
import com.example.projectend.service.LoaiSanPhamService;
import com.example.projectend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller xử lý hiển thị sản phẩm phía khách hàng
 */
@Controller
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    /**
     * Hiển thị danh sách sản phẩm với bộ lọc và phân trang
     */
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

        // Lấy danh sách sản phẩm với bộ lọc
        PageRequest pageable = PageRequest.of(page, size);
        Page<SanPham> sanPhamPage = sanPhamService.findWithFilters(search, loai, minPrice, maxPrice, sort, pageable);
        model.addAttribute("sanPhamPage", sanPhamPage);

        // Lấy danh mục
        List<LoaiSanPham> categories = loaiSanPhamService.findAll();
        model.addAttribute("categories", categories);

        // Giữ lại giá trị filter
        model.addAttribute("search", search);
        model.addAttribute("loai", loai);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);
        model.addAttribute("tetYear", java.time.LocalDate.now().getYear() + 1);

        // Breadcrumb
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Sản phẩm");
        breadcrumbItem.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));

        model.addAttribute("pageTitle", "Sản phẩm - Cửa hàng đồ Tết");
        return "sanpham";
    }

    /**
     * Hiển thị chi tiết sản phẩm
     */
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

        // Lấy sản phẩm liên quan cùng danh mục
        List<SanPham> relatedProducts = sanPhamService.findRelatedProducts(
                sanPham.getLoaiSanPham().getMaLoai(),
                sanPham.getMaSP(),
                6
        );
        model.addAttribute("relatedProducts", relatedProducts);

        // Breadcrumb
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

    /**
     * API tìm kiếm nhanh sản phẩm (AJAX autocomplete)
     */
    @GetMapping("/api/sanpham/search")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<SanPham> quickSearch(@RequestParam String q) {
        return sanPhamService.searchByKeyword(q, 10);
    }
}
