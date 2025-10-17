package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GIO HANG CONTROLLER - Quản lý giỏ hàng
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (5 ENDPOINTS):
 * <p>
 * 1. GET /giohang - Hiển thị giỏ hàng
 * → Load giỏ hàng của user: gioHangService.getGioHangByTaiKhoan(tk)
 * → Tính tổng tiền: gioHangService.tinhTongTien(items)
 * <p>
 * 2. POST /api/giohang/add - Thêm sản phẩm (AJAX)
 * → gioHangService.themSanPham(tk, sp, soLuong)
 * → Return JSON: {success: true, count: X, total: Y}
 * <p>
 * 3. PUT /api/giohang/update - Cập nhật số lượng (AJAX)
 * → gioHangService.capNhatSoLuong(tk, maSP, soLuong)
 * → Return JSON: {success: true, newTotal: Y}
 * <p>
 * 4. DELETE /api/giohang/remove/{id} - Xóa sản phẩm (AJAX)
 * → gioHangService.xoaSanPham(tk, id)
 * → Return JSON: {success: true, count: X}
 * <p>
 * 5. GET /api/giohang/count - Đếm số items (AJAX badge)
 * → gioHangService.countItems(tk)
 * → Return JSON: {count: X}
 * <p>
 * THỜI GIAN: 2 ngày
 * LƯU Ý: 4 AJAX endpoints cần @ResponseBody
 * =============================
 */
@Controller
public class GioHangController {

    // TODO TV2: Bước 1 - Inject services (gỡ comment)
    // @Autowired private GioHangService gioHangService;
    // @Autowired private SanPhamService sanPhamService;
    // @Autowired private TaiKhoanService taiKhoanService;

    // =============================
    // TODO TV2: Endpoint 1 - Hiển thị giỏ hàng
    @GetMapping("/giohang")
    public String gioHang(Model model, Principal principal) {
        model.addAttribute("currentPage", "giohang");

        // TODO TV2: Bước 2 - Gỡ comment sau khi implement Service
        // HƯỚNG DẪN:
        // if (principal != null) {
        //     TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
        //     List<GioHang> items = gioHangService.getGioHangByTaiKhoan(tk);
        //     model.addAttribute("gioHangItems", items);
        //
        //     BigDecimal tongTien = gioHangService.tinhTongTien(items);
        //     model.addAttribute("tongTien", tongTien);
        //
        //     int itemCount = items.size();
        //     model.addAttribute("itemCount", itemCount);
        // }

        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Giỏ hàng");
        breadcrumbItem.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));

        model.addAttribute("pageTitle", "Giỏ hàng - Cửa hàng đồ Tết");
        return "giohang";
    }

    // =============================
    // TODO TV2: Endpoint 2 - Thêm sản phẩm vào giỏ (AJAX)
    // HƯỚNG DẪN:
    // @PostMapping("/api/giohang/add")
    // @ResponseBody
    // public ResponseEntity<?> themVaoGioHang(
    //         @RequestParam Integer sanPhamId,
    //         @RequestParam(defaultValue = "1") Integer soLuong,
    //         Principal principal) {
    //
    //     if (principal == null) {
    //         return ResponseEntity.status(401).body(Map.of("success", false, "message", "Vui lòng đăng nhập"));
    //     }
    //
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //         Optional<SanPham> spOpt = sanPhamService.findById(sanPhamId);
    //
    //         if (spOpt.isEmpty()) {
    //             return ResponseEntity.ok(Map.of("success", false, "message", "Sản phẩm không tồn tại"));
    //         }
    //
    //         SanPham sp = spOpt.get();
    //
    //         // Kiểm tra tồn kho
    //         if (sp.getSoLuong() < soLuong) {
    //             return ResponseEntity.ok(Map.of("success", false, "message", "Sản phẩm không đủ số lượng"));
    //         }
    //
    //         gioHangService.themSanPham(tk, sp, soLuong);
    //
    //         int count = gioHangService.countItems(tk);
    //         BigDecimal total = gioHangService.tinhTongTienByTaiKhoan(tk);
    //
    //         return ResponseEntity.ok(Map.of(
    //             "success", true,
    //             "message", "Đã thêm vào giỏ hàng",
    //             "count", count,
    //             "total", total
    //         ));
    //     } catch (Exception e) {
    //         return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
    //     }
    // }

    // =============================
    // TODO TV2: Endpoint 3 - Cập nhật số lượng (AJAX)
    // HƯỚNG DẪN:
    // @PutMapping("/api/giohang/update")
    // @ResponseBody
    // public ResponseEntity<?> capNhatSoLuong(
    //         @RequestParam Integer sanPhamId,
    //         @RequestParam Integer soLuong,
    //         Principal principal) {
    //
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //         gioHangService.capNhatSoLuong(tk, sanPhamId, soLuong);
    //
    //         BigDecimal newTotal = gioHangService.tinhTongTienByTaiKhoan(tk);
    //
    //         return ResponseEntity.ok(Map.of("success", true, "newTotal", newTotal));
    //     } catch (Exception e) {
    //         return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
    //     }
    // }

    // =============================
    // TODO TV2: Endpoint 4 - Xóa sản phẩm (AJAX)
    // HƯỚNG DẪN:
    // @DeleteMapping("/api/giohang/remove/{id}")
    // @ResponseBody
    // public ResponseEntity<?> xoaSanPham(@PathVariable Integer id, Principal principal) {
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //         gioHangService.xoaSanPham(tk, id);
    //
    //         int count = gioHangService.countItems(tk);
    //         BigDecimal newTotal = gioHangService.tinhTongTienByTaiKhoan(tk);
    //
    //         return ResponseEntity.ok(Map.of("success", true, "count", count, "newTotal", newTotal));
    //     } catch (Exception e) {
    //         return ResponseEntity.ok(Map.of("success", false, "message", e.getMessage()));
    //     }
    // }

    // =============================
    // TODO TV2: Endpoint 5 - Đếm số items (AJAX badge)
    // HƯỚNG DẪN:
    // @GetMapping("/api/giohang/count")
    // @ResponseBody
    // public ResponseEntity<?> countItems(Principal principal) {
    //     if (principal == null) return ResponseEntity.ok(Map.of("count", 0));
    //
    //     TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //     int count = gioHangService.countItems(tk);
    //
    //     return ResponseEntity.ok(Map.of("count", count));
    // }
}
