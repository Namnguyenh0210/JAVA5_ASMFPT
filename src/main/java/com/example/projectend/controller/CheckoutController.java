package com.example.projectend.controller;

import com.example.projectend.entity.*;
import com.example.projectend.service.DiaChiService;
import com.example.projectend.service.DonHangService;
import com.example.projectend.service.GioHangService;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller xử lý thanh toán và đặt hàng
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private DiaChiService diaChiService;

    @Autowired
    private DonHangService donHangService;

    /**
     * Hiển thị trang thanh toán
     */
    @GetMapping("")
    public String checkout(Model model, Principal principal) {

        // Kiểm tra đăng nhập
        if (principal == null) {
            return "redirect:/login?returnUrl=/checkout";
        }

        TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
        if (tk == null) {
            return "redirect:/login";
        }

        // Lấy giỏ hàng
        List<GioHang> items = gioHangService.getGioHangByTaiKhoan(tk);
        if (items.isEmpty()) {
            return "redirect:/giohang?error=empty";
        }
        model.addAttribute("cartItems", items);

        // Tính tổng tiền
        BigDecimal tongTien = gioHangService.tinhTongTien(items);
        model.addAttribute("orderSubtotal", tongTien);
        model.addAttribute("orderTotal", tongTien);

        // Lấy danh sách địa chỉ
        List<DiaChi> diaChiList = diaChiService.getDiaChiByTaiKhoan(tk);
        model.addAttribute("diaChiList", diaChiList);

        // Địa chỉ mặc định
        Optional<DiaChi> defaultAddress = diaChiService.getDiaChiMacDinh(tk);
        if (defaultAddress.isPresent()) {
            model.addAttribute("defaultAddress", defaultAddress.get());
        }

        model.addAttribute("user", tk);

        // Breadcrumb
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

    /**
     * Xử lý đặt hàng
     */
    @PostMapping("/place-order")
    public String processCheckout(
            @RequestParam Integer diaChiId,
            @RequestParam(name = "paymentMethod", required = false, defaultValue = "1") Integer phuongThucId,
            @RequestParam(required = false) String ghiChu,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            if (principal == null) {
                return "redirect:/login";
            }

            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            if (tk == null) {
                return "redirect:/login";
            }

            // Lấy giỏ hàng
            List<GioHang> items = gioHangService.getGioHangByTaiKhoan(tk);
            if (items.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
                return "redirect:/giohang";
            }

            // Tạo đơn hàng
            DonHang donHang = donHangService.createDonHang(tk, diaChiId, phuongThucId, items, ghiChu);

            if (donHang == null) {
                redirectAttributes.addFlashAttribute("error", "Đặt hàng thất bại!");
                return "redirect:/checkout";
            }

            // Xóa giỏ hàng sau khi đặt thành công
            gioHangService.clearGioHang(tk);

            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
            return "redirect:/checkout/success?orderId=" + donHang.getMaDH();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    /**
     * Trang cảm ơn sau khi đặt hàng thành công
     */
    @GetMapping("/success")
    public String checkoutSuccess(@RequestParam Integer orderId, Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
        if (tk == null) {
            return "redirect:/login";
        }

        // Lấy thông tin đơn hàng
        Optional<DonHang> donHangOpt = donHangService.findByIdAndKhachHang(orderId, tk);
        if (!donHangOpt.isPresent()) {
            return "redirect:/profile";
        }

        DonHang donHang = donHangOpt.get();
        model.addAttribute("donHang", donHang);

        // Lấy chi tiết đơn hàng
        List<DonHangChiTiet> chiTiet = donHangService.getChiTietDonHang(donHang);
        model.addAttribute("chiTiet", chiTiet);

        model.addAttribute("pageTitle", "Đặt hàng thành công");
        model.addAttribute("currentPage", "checkout-success");
        return "checkout-success";
    }
}
