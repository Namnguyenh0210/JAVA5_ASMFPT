package com.example.projectend.controller.admin;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.DonHangChiTiet;
import com.example.projectend.entity.TrangThaiDonHang;
import com.example.projectend.repository.TrangThaiDonHangRepository;
import com.example.projectend.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * ADMIN DON HANG CONTROLLER - Quản lý đơn hàng (Admin)
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND
 * =============================
 */
@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ADMIN') or hasRole('NHÂNVIÊN')")
public class AdminDonHangController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private TrangThaiDonHangRepository trangThaiRepository;

    // =============================
    // Endpoint 1 - Danh sách đơn hàng
    // GET /admin/donhang
    // =============================
    @GetMapping("")
    public String danhSachDonHang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDat").descending());
        if (trangThai != null && trangThai.trim().isEmpty()) {
            trangThai = null;
        }
        Page<DonHang> donHangPage = donHangService.searchAdmin(keyword, trangThai, pageable);

        List<TrangThaiDonHang> trangThaiList = trangThaiRepository.findAll();

        model.addAttribute("donHangPage", donHangPage);
        model.addAttribute("trangThaiList", trangThaiList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThaiFilter", trangThai);
        model.addAttribute("currentPage", "donhang");
        model.addAttribute("pageTitle", "Quản lý đơn hàng");

        return "admin/orders"; // Thymeleaf view
    }

    // =============================
    // Endpoint 2 - Chi tiết đơn hàng
    // GET /admin/donhang/detail/{id}
    // =============================
    @GetMapping("/detail/{id}")
    public String chiTietDonHang(@PathVariable Integer id, Model model) {

        Optional<DonHang> donHangOpt = donHangService.findById(id);
        if (donHangOpt.isEmpty()) {
            return "redirect:/admin/donhang?error=notfound";
        }

        DonHang donHang = donHangOpt.get();
        List<DonHangChiTiet> chiTiet = donHangService.getChiTietDonHang(donHang);
        List<TrangThaiDonHang> trangThaiList = trangThaiRepository.findAll();

        model.addAttribute("donHang", donHang);
        model.addAttribute("chiTiet", chiTiet);
        model.addAttribute("trangThaiList", trangThaiList);
        model.addAttribute("currentPage", "donhang");
        model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + id);

        return "admin/order_detail"; // Thymeleaf view chi tiết
    }

    // =============================
    // Endpoint 3 - Cập nhật trạng thái đơn hàng
    // POST /admin/donhang/update-status/{id}
    // =============================
    @PostMapping("/update-status/{id}")
    public String capNhatTrangThai(
            @PathVariable Integer id,
            @RequestParam String trangThaiMoi,
            RedirectAttributes redirectAttributes) {

        try {
            boolean success = donHangService.updateTrangThai(id, trangThaiMoi);
            if (success) {
                redirectAttributes.addFlashAttribute("success",
                        "✅ Cập nhật trạng thái thành công cho đơn hàng #" + id);
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "❌ Không thể cập nhật trạng thái. ID đơn hàng hoặc trạng thái không hợp lệ.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Lỗi hệ thống: " + e.getMessage());
        }

        return "redirect:/admin/donhang/detail/" + id;
    }

    // =============================
    // Endpoint 4 - Đơn chờ xác nhận (dashboard widget)
    // GET /admin/donhang/pending
    // =============================
    @GetMapping("/pending")
    @ResponseBody
    public List<DonHang> getPendingOrders(@RequestParam(defaultValue = "10") int limit) {
        return donHangService.getPendingOrders(limit);
    }
}
