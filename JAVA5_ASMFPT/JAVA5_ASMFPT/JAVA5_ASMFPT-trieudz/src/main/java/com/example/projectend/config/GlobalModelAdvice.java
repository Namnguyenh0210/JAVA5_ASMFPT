package com.example.projectend.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;

/**
 * GLOBAL MODEL ADVICE
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: ĐÃ HOÀN THÀNH khởi tạo core attribute (currentYear, tetYear)
 * - THÀNH VIÊN 2: Bổ sung currentUser (sau login) & quyền hiển thị menu phù hợp
 * - THÀNH VIÊN 3: (Optional) Preload categories cho header (cache) nếu cần ở mọi trang
 * - THÀNH VIÊN 4: (Optional) Mini thống kê (số đơn pending) hiển thị badge ở header admin
 * <p>
 * =============================
 * TODO THÀNH VIÊN 2: Thêm method private TaiKhoan getCurrentUser(Authentication)
 * TODO THÀNH VIÊN 2: addGlobalUser(Model) -> model.addAttribute("currentUser", ...)
 * TODO THÀNH VIÊN 3: Inject LoaiSanPhamService & thêm model.addAttribute("globalCategories", ...)
 * TODO THÀNH VIÊN 4: Inject DonHangService -> model.addAttribute("pendingOrderCount", ...)
 */
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        int currentYear = LocalDate.now().getYear();
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("tetYear", currentYear); // Năm hiển thị chung
        // TODO THÀNH VIÊN 2: model.addAttribute("currentUser", securityContextUser)
        // TODO THÀNH VIÊN 3: model.addAttribute("globalCategories", loaiSanPhamService.findAll())
        // TODO THÀNH VIÊN 4: model.addAttribute("pendingOrderCount", donHangService.countPending())
    }
}
