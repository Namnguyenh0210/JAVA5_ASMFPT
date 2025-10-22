package com.example.projectend.controller.admin;

import com.example.projectend.entity.DonHang;
import com.example.projectend.entity.DonHangChiTiet;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.TrangThaiDonHang;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.repository.TrangThaiDonHangRepository;
import com.example.projectend.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * ADMIN DON HANG CONTROLLER - Quản lý đơn hàng (Admin & Staff)
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND
 * =============================
 */
@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('Admin') or hasRole('Nhân viên')")
public class AdminDonHangController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private TrangThaiDonHangRepository trangThaiRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    /**
     * Helper method để kiểm tra user có phải Nhân viên không
     */
    private boolean isStaff(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Nhân viên"));
    }

    // =============================
    // Endpoint 1 - Danh sách đơn hàng
    // GET /admin/orders
    // =============================
    @GetMapping("")
    public String danhSachDonHang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Authentication auth,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDat").descending());
        if (trangThai != null && trangThai.trim().isEmpty()) {
            trangThai = null;
        }

        // Admin thấy tất cả đơn hàng
        Page<DonHang> donHangPage = donHangService.searchAdmin(keyword, trangThai, pageable);

        List<TrangThaiDonHang> trangThaiList = trangThaiRepository.findAll();

        model.addAttribute("donHangPage", donHangPage);
        model.addAttribute("orders", donHangPage.getContent());
        model.addAttribute("trangThaiList", trangThaiList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThaiFilter", trangThai);
        model.addAttribute("currentPage", "donhang");
        model.addAttribute("pageTitle", "Quản lý đơn hàng");

        // ADMIN luôn dùng template admin/orders (không phân biệt)
        return "admin/orders";
    }

    // =============================
    // Endpoint 2 - Chi tiết đơn hàng
    // GET /admin/orders/detail/{id}
    // =============================
    @GetMapping("/detail/{id}")
    public String chiTietDonHang(@PathVariable Integer id, Authentication auth, Model model) {

        Optional<DonHang> donHangOpt = donHangService.findById(id);
        if (donHangOpt.isEmpty()) {
            return "redirect:/admin/orders?error=notfound";
        }

        DonHang donHang = donHangOpt.get();

        // Lấy thông tin user hiện tại
        String email = auth.getName();
        TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

        List<DonHangChiTiet> chiTiet = donHangService.getChiTietDonHang(donHang);
        List<TrangThaiDonHang> trangThaiList = trangThaiRepository.findAll();

        model.addAttribute("donHang", donHang);
        model.addAttribute("chiTiet", chiTiet);
        model.addAttribute("trangThaiList", trangThaiList);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentPage", "donhang");
        model.addAttribute("pageTitle", "Chi tiết đơn hàng #" + id);

        // ADMIN luôn dùng template admin/order_detail (không phân biệt)
        return "admin/order_detail";
    }

    // =============================
    // Endpoint 3 - Cập nhật trạng thái đơn hàng
    // POST /admin/orders/update-status/{id}
    // =============================
    @PostMapping("/update-status/{id}")
    public String capNhatTrangThai(
            @PathVariable Integer id,
            @RequestParam String trangThaiMoi,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            // Lấy thông tin user hiện tại
            String email = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy thông tin người dùng!");
                return "redirect:/admin/orders/detail/" + id;
            }

            // Lấy đơn hàng
            Optional<DonHang> donHangOpt = donHangService.findById(id);
            if (donHangOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy đơn hàng!");
                return "redirect:/admin/orders";
            }

            DonHang donHang = donHangOpt.get();
            String trangThaiHienTai = donHang.getTrangThaiDonHang().getTenTTDH();

            // Kiểm tra nếu là nhân viên
            boolean isStaff = isStaff(auth);

            // Logic đặc biệt cho nhân viên
            if (isStaff) {
                // Nếu đơn hàng đang ở trạng thái "Chờ xác nhận" và nhân viên chuyển sang "Đang giao"
                if ("Chờ xác nhận".equals(trangThaiHienTai) && "Đang giao".equals(trangThaiMoi)) {
                    // Tự động gán nhân viên này cho đơn hàng
                    donHang.setNhanVien(currentUser);
                    System.out.println("=== GÁN NHÂN VIÊN: " + currentUser.getHoTen() + " cho đơn hàng #" + id);
                }
                // Nếu đơn hàng đã có nhân viên khác được gán
                else if (donHang.getNhanVien() != null && !donHang.getNhanVien().getMaTK().equals(currentUser.getMaTK())) {
                    redirectAttributes.addFlashAttribute("error",
                            "❌ Đơn hàng này đã được gán cho nhân viên: " + donHang.getNhanVien().getHoTen() +
                                    ". Chỉ nhân viên đó mới có thể cập nhật!");
                    return "redirect:/admin/orders/detail/" + id;
                }

                // Kiểm tra workflow: chỉ cho phép chuyển theo thứ tự
                if (!isValidTransition(trangThaiHienTai, trangThaiMoi)) {
                    redirectAttributes.addFlashAttribute("error",
                            "❌ Không thể chuyển từ '" + trangThaiHienTai + "' sang '" + trangThaiMoi + "'. " +
                                    "Vui lòng tuân thủ thứ tự: Chờ xác nhận → Đang giao → Hoàn tất");
                    return "redirect:/admin/orders/detail/" + id;
                }
            }

            // Cập nhật trạng thái (gọi service để cập nhật trạng thái và lưu đơn hàng)
            boolean success = donHangService.updateTrangThaiWithStaff(id, trangThaiMoi, donHang.getNhanVien());
            if (success) {
                redirectAttributes.addFlashAttribute("success",
                        "✅ Cập nhật trạng thái thành công cho đơn hàng #" + id);
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "❌ Không thể cập nhật trạng thái. Trạng thái không hợp lệ.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/admin/orders/detail/" + id;
    }

    /**
     * Kiểm tra workflow hợp lệ cho nhân viên
     */
    private boolean isValidTransition(String from, String to) {
        // Chỉ cho phép chuyển theo thứ tự: Chờ xác nhận → Đang giao → Hoàn tất
        if ("Chờ xác nhận".equals(from) && "Đang giao".equals(to)) return true;
        if ("Đang giao".equals(from) && "Hoàn tất".equals(to)) return true;
        if ("Đang giao".equals(from) && "Đã hủy".equals(to)) return true; // Cho phép hủy khi đang giao
        return false;
    }

    // =============================
    // Endpoint 4 - Đơn chờ xác nhận (dashboard widget)
    // GET /admin/orders/pending
    // =============================
    @GetMapping("/pending")
    @ResponseBody
    public List<DonHang> getPendingOrders(@RequestParam(defaultValue = "10") int limit) {
        return donHangService.getPendingOrders(limit);
    }
}
