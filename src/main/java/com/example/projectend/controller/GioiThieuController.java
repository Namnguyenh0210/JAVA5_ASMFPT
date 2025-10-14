package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * GIOI THIEU CONTROLLER - Trang giới thiệu
 * PHÂN CÔNG:
 * - THÀNH VIÊN 3: Nội dung trang giới thiệu (company info, mission, vision, team, timeline)
 * - THÀNH VIÊN 4: (Optional) Thêm số liệu thống kê nhanh / badge
 * <p>
 * =============================
 * TODO THÀNH VIÊN 3:
 *  1. Bổ sung model attributes: companyInfo, mission, vision
 *  2. (Optional) Thêm thống kê: totalProducts, totalOrders (gọi service tương ứng)
 *  3. (Optional) Thêm danh sách teamMembers, milestones (nếu tạo bảng/giả lập JSON)
 *  4. (Optional) Tạo thêm trang chính sách / điều khoản (route riêng)
 * TODO THÀNH VIÊN 4 (Optional): Mini widget số đơn pending ở header (qua GlobalModelAdvice)
 */
@Controller
public class GioiThieuController {

    @GetMapping("/gioithieu")
    public String gioiThieu(Model model) {
        model.addAttribute("currentPage", "gioithieu");
        // TODO THÀNH VIÊN 3: model.addAttribute("companyInfo", "Thông tin cửa hàng đồ Tết");
        // TODO THÀNH VIÊN 3: model.addAttribute("mission", "Mang hương vị Tết đến mọi nhà");
        // TODO THÀNH VIÊN 3: model.addAttribute("vision", "Trở thành nền tảng quà Tết hàng đầu");
        model.addAttribute("pageTitle", "Giới thiệu - Cửa hàng đồ Tết");
        return "gioithieu";
    }

    // TODO THÀNH VIÊN 3 (Optional): Trang chính sách /chinh-sach
    // TODO THÀNH VIÊN 3 (Optional): Trang điều khoản /dieu-khoan
}
