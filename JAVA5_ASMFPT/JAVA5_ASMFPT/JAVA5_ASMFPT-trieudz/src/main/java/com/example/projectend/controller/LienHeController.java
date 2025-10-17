package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LIEN HE CONTROLLER - Trang liên hệ
 * PHÂN CÔNG:
 * - THÀNH VIÊN 3: Form gửi liên hệ (nếu triển khai) + validation đơn giản
 * - THÀNH VIÊN 4: (Optional) Ghi log liên hệ vào bảng riêng để admin xem (nếu thêm schema phụ ngoài phạm vi chính thì bỏ qua)
 * <p>
 * =============================
 * TODO THÀNH VIÊN 3: Implement POST /lienhe/gui xử lý form (hiện đang comment ở dưới - optional)
 */
@Controller
public class LienHeController {

    @GetMapping("/lienhe")
    public String lienHe(Model model) {
        // Add current page for navigation active state
        model.addAttribute("currentPage", "lienhe");

        // Add breadcrumb data - using HashMap to allow null values
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Liên Hệ");
        breadcrumbItem.put("url", null);

        List<Map<String, String>> breadcrumbItems = List.of(breadcrumbItem);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        // Thông tin liên hệ cửa hàng
        model.addAttribute("shopAddress", "123 Đường ABC, Quận XYZ, TP.HCM");
        model.addAttribute("shopPhone", "0909 123 456");
        model.addAttribute("shopEmail", "contact@tetmarket.com");
        model.addAttribute("shopHours", "8:00 - 22:00 hàng ngày");

        model.addAttribute("pageTitle", "Liên hệ - Cửa hàng đồ Tết");
        model.addAttribute("tetYear", "2025");

        return "lienhe";
    }

    // TODO THÀNH VIÊN 3 (Optional): POST /lienhe/gui xử lý gửi feedback
}
