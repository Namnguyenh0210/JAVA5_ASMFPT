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

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String checkout(Model model, Principal principal, HttpSession session, @RequestParam(value = "selected", required = false) Integer selected) {

        // Kiểm tra đăng nhập
        if (principal == null) {
            return "redirect:/login?returnUrl=/checkout";
        }

        TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
        if (tk == null) {
            return "redirect:/login";
        }

        List<GioHang> items;
        // Nếu có selected=1 và session có selectedCartItems thì chỉ lấy các sản phẩm đã chọn
        if (selected != null && selected == 1 && session.getAttribute("selectedCartItems") != null) {
            Object selectedObj = session.getAttribute("selectedCartItems");
            List<Map<String, Object>> selectedItems = null;
            if (selectedObj instanceof List<?>) {
                boolean valid = true;
                for (Object o : (List<?>) selectedObj) {
                    if (!(o instanceof Map)) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    selectedItems = (List<Map<String, Object>>) selectedObj;
                }
            }
            if (selectedItems != null) {
                items = gioHangService.getGioHangByTaiKhoanAndSanPhamIds(tk, selectedItems);
                // LƯU items vào session để dùng khi place-order
                session.setAttribute("checkoutCartItems", items);
                System.out.println("=== DEBUG: Saved " + items.size() + " items to checkoutCartItems session");
            } else {
                items = gioHangService.getGioHangByTaiKhoan(tk);
                session.setAttribute("checkoutCartItems", items);
            }
            // Xóa selectedCartItems sau khi đã xử lý
            session.removeAttribute("selectedCartItems");
        } else {
            items = gioHangService.getGioHangByTaiKhoan(tk);
            // Lưu vào session
            session.setAttribute("checkoutCartItems", items);
        }

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
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            if (principal == null) {
                return "redirect:/login";
            }

            TaiKhoan tk = taiKhoanService.findByEmail(principal.getName());
            if (tk == null) {
                return "redirect:/login";
            }

            // LẤY sản phẩm từ session checkoutCartItems (đã lưu ở trang checkout)
            List<GioHang> items = null;
            Object checkoutItemsObj = session.getAttribute("checkoutCartItems");

            if (checkoutItemsObj instanceof List<?>) {
                try {
                    items = (List<GioHang>) checkoutItemsObj;
                    System.out.println("=== DEBUG: Lấy từ checkoutCartItems session: " + items.size() + " sản phẩm");
                } catch (ClassCastException e) {
                    System.out.println("=== DEBUG: Lỗi cast checkoutCartItems");
                }
            }

            // Nếu không có session, lấy tất cả giỏ hàng (fallback)
            if (items == null || items.isEmpty()) {
                System.out.println("=== DEBUG: Không có checkoutCartItems, lấy tất cả giỏ hàng");
                items = gioHangService.getGioHangByTaiKhoan(tk);
            }

            if (items.isEmpty()) {
                session.removeAttribute("checkoutCartItems");
                redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
                return "redirect:/giohang";
            }

            System.out.println("=== DEBUG: Tạo đơn hàng với " + items.size() + " sản phẩm");

            // Tạo đơn hàng
            DonHang donHang = donHangService.createDonHang(tk, diaChiId, phuongThucId, items, ghiChu);

            if (donHang == null) {
                session.removeAttribute("checkoutCartItems");
                redirectAttributes.addFlashAttribute("error", "Đặt hàng thất bại!");
                return "redirect:/checkout";
            }

            // Xóa các sản phẩm đã đặt khỏi giỏ hàng
            for (GioHang gh : items) {
                gioHangService.xoaSanPham(tk, gh.getSanPham().getMaSP());
            }

            // Xóa session sau khi đặt hàng thành công
            session.removeAttribute("checkoutCartItems");
            session.removeAttribute("selectedCartItems");

            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công!");
            return "redirect:/checkout/success?orderId=" + donHang.getMaDH();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== DEBUG: Lỗi đặt hàng: " + e.getMessage());
            session.removeAttribute("checkoutCartItems");
            session.removeAttribute("selectedCartItems");
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    /**
     * API nhận danh sách sản phẩm đã chọn từ giỏ hàng
     */
    @PostMapping("/api/checkout-selected")
    @ResponseBody
    public Map<String, Object> checkoutSelected(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");
            if (items == null || items.isEmpty()) {
                response.put("thanhCong", false);
                response.put("thongBao", "Vui lòng chọn ít nhất một sản phẩm!");
                return response;
            }

            // Lưu danh sách sản phẩm đã chọn vào session
            session.setAttribute("selectedCartItems", items);

            response.put("thanhCong", true);
            response.put("duongDanChuyenHuong", "/checkout?selected=1");
            return response;
        } catch (Exception e) {
            response.put("thanhCong", false);
            response.put("thongBao", "Có lỗi xảy ra: " + e.getMessage());
            return response;
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
