package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CHECKOUT CONTROLLER - Đặt hàng & thanh toán
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 + TV3 (PHỤ THUỘC!)
 * =============================
 * TODO TV2 - CẦN LÀM (2 ENDPOINTS):
 * <p>
 * 1. GET /checkout - Hiển thị trang thanh toán
 * → Load giỏ hàng: gioHangService.getGioHangByTaiKhoan(tk)
 * → Load địa chỉ: diaChiService.getDiaChiByTaiKhoan(tk)
 * → Load PTTT: phuongThucThanhToanService.findAll()
 * → Tính tổng tiền tạm: gioHangService.tinhTongTien(items)
 * <p>
 * 2. POST /checkout - Xử lý đặt hàng ⚠️ CẦN ĐỢI TV3!
 * → Gọi donHangService.createDonHang() (TV3 làm)
 * → Xóa giỏ hàng: gioHangService.clearGioHang(tk)
 * → Redirect: /checkout/success?orderId=X
 * <p>
 * TODO TV3 - CẦN LÀM:
 * <p>
 * 3. GET /checkout/success?orderId=X - Trang cảm ơn
 * → Load đơn hàng: donHangService.findById(orderId)
 * → Load chi tiết: donHangService.getChiTietDonHang(donHang)
 * <p>
 * THỜI GIAN: TV2 (1 ngày) + TV3 (1 ngày)
 * ⚠️ TV2 PHẢI ĐỢI TV3 HOÀN THÀNH donHangService.createDonHang()!
 * =============================
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    // TODO TV2: Inject services cho hiển thị form
    // @Autowired private GioHangService gioHangService;
    // @Autowired private TaiKhoanService taiKhoanService;
    // @Autowired private DiaChiService diaChiService;
    // @Autowired private PhuongThucThanhToanService phuongThucThanhToanService;

    // TODO TV3: Inject DonHangService
    // @Autowired private DonHangService donHangService;

    // =============================
    // TODO TV2: Endpoint 1 - Trang checkout
    @GetMapping("")
    public String checkout(Model model, Principal principal) {

        // TODO TV2: Kiểm tra đăng nhập
        // if (principal == null) {
        //     return "redirect:/login?returnUrl=/checkout";
        // }

        // TODO TV2: Load dữ liệu cho form
        // HƯỚNG DẪN:
        // TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
        //
        // // Giỏ hàng
        // List<GioHang> items = gioHangService.getGioHangByTaiKhoan(tk);
        // if (items.isEmpty()) {
        //     return "redirect:/giohang?error=empty";
        // }
        // model.addAttribute("gioHangItems", items);
        //
        // // Tổng tiền tạm
        // BigDecimal tongTien = gioHangService.tinhTongTien(items);
        // model.addAttribute("tongTien", tongTien);
        //
        // // Địa chỉ giao hàng
        // List<DiaChi> diaChiList = diaChiService.getDiaChiByTaiKhoan(tk);
        // model.addAttribute("diaChiList", diaChiList);
        //
        // // Phương thức thanh toán
        // List<PhuongThucThanhToan> ptttList = phuongThucThanhToanService.findAll();
        // model.addAttribute("ptttList", ptttList);
        //
        // // User info
        // model.addAttribute("user", tk);

        Map<String, String> breadcrumb1 = new HashMap<>();
        breadcrumb1.put("name", "Giỏ hàng");
        breadcrumb1.put("url", "/giohang");
        Map<String, String> breadcrumb2 = new HashMap<>();
        breadcrumb2.put("name", "Thanh toán");
        breadcrumb2.put("url", null);
        model.addAttribute("breadcrumbItems", List.of(breadcrumb1, breadcrumb2));

        model.addAttribute("currentPage", "checkout");
        model.addAttribute("pageTitle", "Thanh toán - Cửa hàng đồ Tết");
        return "checkout";
    }

    // =============================
    // TODO TV2: Endpoint 2 - Xử lý đặt hàng ⚠️ ĐỢI TV3!
    // HƯỚNG DẪN:
    // @PostMapping("")
    // public String processCheckout(
    //         @RequestParam Integer diaChiId,
    //         @RequestParam Integer phuongThucId,
    //         @RequestParam(required = false) String ghiChu,
    //         Principal principal,
    //         RedirectAttributes redirectAttributes) {
    //
    //     try {
    //         TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //
    //         // Lấy giỏ hàng
    //         List<GioHang> items = gioHangService.getGioHangByTaiKhoan(tk);
    //         if (items.isEmpty()) {
    //             redirectAttributes.addFlashAttribute("error", "Giỏ hàng rỗng");
    //             return "redirect:/giohang";
    //         }
    //
    //         // ⚠️ GỌI METHOD CỦA TV3 - PHẢI ĐỢI TV3 LÀM XONG!
    //         DonHang donHang = donHangService.createDonHang(tk, diaChiId, phuongThucId, items, ghiChu);
    //
    //         if (donHang == null) {
    //             redirectAttributes.addFlashAttribute("error", "Đặt hàng thất bại");
    //             return "redirect:/checkout";
    //         }
    //
    //         // Xóa giỏ hàng sau khi đặt hàng thành công
    //         gioHangService.clearGioHang(tk);
    //
    //         redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
    //         return "redirect:/checkout/success?orderId=" + donHang.getMaDH();
    //
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", e.getMessage());
    //         return "redirect:/checkout";
    //     }
    // }

    // =============================
    // TODO TV3: Endpoint 3 - Trang cảm ơn (sau khi đặt hàng)
    // HƯỚNG DẪN:
    // @GetMapping("/success")
    // public String checkoutSuccess(@RequestParam Integer orderId, Model model, Principal principal) {
    //
    //     TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
    //
    //     // Load đơn hàng
    //     Optional<DonHang> donHangOpt = donHangService.findByIdAndKhachHang(orderId, tk);
    //     if (donHangOpt.isEmpty()) {
    //         return "redirect:/profile";
    //     }
    //
    //     DonHang donHang = donHangOpt.get();
    //     model.addAttribute("donHang", donHang);
    //
    //     // Load chi tiết đơn
    //     List<DonHangChiTiet> chiTiet = donHangService.getChiTietDonHang(donHang);
    //     model.addAttribute("chiTiet", chiTiet);
    //
    //     model.addAttribute("pageTitle", "Đặt hàng thành công");
    //     return "checkout-success";
    // }
}
