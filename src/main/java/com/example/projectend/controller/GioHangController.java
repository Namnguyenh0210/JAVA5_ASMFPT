package com.example.projectend.controller;

import com.example.projectend.entity.GioHang;
import com.example.projectend.entity.SanPham;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.service.GioHangService;
import com.example.projectend.service.SanPhamService;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

/**
 * Controller quản lý giỏ hàng người dùng
 */
@Controller
public class GioHangController {

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    /**
     * Hiển thị giỏ hàng của người dùng
     */
    @GetMapping("/giohang")
    public String gioHang(Model model, Principal principal) {
        model.addAttribute("currentPage", "giohang");

        List<GioHang> items = new ArrayList<>();
        BigDecimal tongTien = BigDecimal.ZERO;
        int itemCount = 0;

        if (principal != null) {
            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            items = gioHangService.getGioHangByTaiKhoan(tk);
            tongTien = gioHangService.tinhTongTien(items);
            itemCount = items.size();
        }

        model.addAttribute("gioHangItems", items);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("itemCount", itemCount);

        // Breadcrumb
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Giỏ hàng");
        breadcrumbItem.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));

        model.addAttribute("pageTitle", "Giỏ hàng - Cửa hàng đồ Tết");
        return "giohang";
    }

    /**
     * API thêm sản phẩm vào giỏ hàng (AJAX)
     */
    @PostMapping("/api/giohang/add")
    @ResponseBody
    public ResponseEntity<?> themVaoGioHang(
            @RequestParam Integer sanPhamId,
            @RequestParam(defaultValue = "1") Integer soLuong,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Vui lòng đăng nhập"));
        }

        try {
            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            Optional<SanPham> spOpt = sanPhamService.findById(sanPhamId);

            if (spOpt.isEmpty()) {
                return ResponseEntity.ok(Map.of("success", false, "message", "Sản phẩm không tồn tại"));
            }

            SanPham sp = spOpt.get();

            // Kiểm tra tồn kho
            if (sp.getSoLuong() < soLuong) {
                return ResponseEntity.ok(Map.of("success", false, "message", "Sản phẩm không đủ số lượng"));
            }

            gioHangService.themSanPham(tk, sp, soLuong);

            int count = gioHangService.countItems(tk);
            BigDecimal total = gioHangService.tinhTongTienByTaiKhoan(tk);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Đã thêm vào giỏ hàng",
                    "count", count,
                    "total", total
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * API cập nhật số lượng sản phẩm trong giỏ (AJAX)
     */
    @PostMapping("/api/giohang/update")
    @ResponseBody
    public ResponseEntity<?> capNhatSoLuong(
            @RequestParam Integer sanPhamId,
            @RequestParam Integer soLuong,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Vui lòng đăng nhập"));
        }

        try {
            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            gioHangService.capNhatSoLuong(tk, sanPhamId, soLuong);

            BigDecimal newTotal = gioHangService.tinhTongTienByTaiKhoan(tk);
            int count = gioHangService.countItems(tk);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "newTotal", newTotal,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * API xóa sản phẩm khỏi giỏ hàng (AJAX)
     */
    @DeleteMapping("/api/giohang/remove/{id}")
    @ResponseBody
    public ResponseEntity<?> xoaSanPham(@PathVariable Integer id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Vui lòng đăng nhập"));
        }

        try {
            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            gioHangService.xoaSanPham(tk, id);

            int count = gioHangService.countItems(tk);
            BigDecimal newTotal = gioHangService.tinhTongTienByTaiKhoan(tk);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count,
                    "newTotal", newTotal
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * API đếm số lượng sản phẩm trong giỏ (AJAX badge)
     */
    @GetMapping("/api/giohang/count")
    @ResponseBody
    public ResponseEntity<?> demSoLuong(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(Map.of("count", 0));
        }

        try {
            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            int count = gioHangService.countItems(tk);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("count", 0));
        }
    }

    /**
     * API xóa tất cả sản phẩm trong giỏ hàng (AJAX)
     */
    @DeleteMapping("/api/giohang/clear")
    @ResponseBody
    public ResponseEntity<?> xoaTatCa(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Vui lòng đăng nhập"));
        }

        try {
            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            gioHangService.clearGioHang(tk);

            return ResponseEntity.ok(Map.of("success", true, "message", "Đã xóa toàn bộ giỏ hàng"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
