package com.example.projectend.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ADMIN DON HANG CONTROLLER - Quản lý đơn hàng (Admin)
 * <p>
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND
 * =============================
 * TODO TV3 - CẦN LÀM (5 ENDPOINTS):
 * <p>
 * 1. GET /admin/donhang - Danh sách đơn hàng (filter, search, paginate)
 * → Gọi donHangService.searchAdmin(keyword, trangThai, pageable)
 * <p>
 * 2. GET /admin/donhang/detail/{id} - Chi tiết đơn hàng
 * → Gọi donHangService.findById(id)
 * → Gọi donHangService.getChiTietDonHang(donHang)
 * <p>
 * 3. POST /admin/donhang/update-status/{id} - Cập nhật trạng thái
 * → Gọi donHangService.updateTrangThai(id, trangThaiMoi)
 * <p>
 * 4. GET /admin/donhang/pending - Đơn chờ xác nhận (dashboard widget)
 * → Gọi donHangService.getPendingOrders(limit)
 * <p>
 * 5. POST /admin/donhang/delete/{id} - Xóa đơn (optional)
 * <p>
 * THỜI GIAN: 2 ngày
 * =============================
 */
@Controller
@RequestMapping("/admin/donhang")
@PreAuthorize("hasRole('ADMIN') or hasRole('NHÂNVIÊN')")
public class AdminDonHangController {

    // TODO TV3: Inject services
    // @Autowired private DonHangService donHangService;
    // @Autowired private TrangThaiDonHangRepository trangThaiRepository;

    // =============================
    // TODO TV3: Endpoint 1 - Danh sách đơn hàng
    @GetMapping("")
    public String danhSachDonHang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Model model) {

        // TODO TV3: HƯỚNG DẪN:
        // Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDatHang").descending());
        // Page<DonHang> donHangPage = donHangService.searchAdmin(keyword, trangThai, pageable);
        // model.addAttribute("donHangPage", donHangPage);
        //
        // List<TrangThaiDonHang> trangThaiList = trangThaiRepository.findAll();
        // model.addAttribute("trangThaiList", trangThaiList);
        //
        // model.addAttribute("keyword", keyword);
        // model.addAttribute("trangThaiFilter", trangThai);

        model.addAttribute("currentPage", "donhang");
        model.addAttribute("pageTitle", "Quản lý đơn hàng");
        return "admin/orders";
    }

    // =============================
    // TODO TV3: Endpoint 2 - Chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String chiTietDonHang(@PathVariable Integer id, Model model) {

        // TODO TV3: HƯỚNG DẪN:
        // Optional<DonHang> donHangOpt = donHangService.findById(id);
        // if (donHangOpt.isEmpty()) {
        //     return "redirect:/admin/donhang?error=notfound";
        // }
        //
        // DonHang donHang = donHangOpt.get();
        // model.addAttribute("donHang", donHang);
        //
        // List<DonHangChiTiet> chiTiet = donHangService.getChiTietDonHang(donHang);
        // model.addAttribute("chiTiet", chiTiet);
        //
        // List<TrangThaiDonHang> trangThaiList = trangThaiRepository.findAll();
        // model.addAttribute("trangThaiList", trangThaiList);

        model.addAttribute("currentPage", "donhang");
        model.addAttribute("pageTitle", "Chi tiết đơn hàng");
        return "admin/orders";
    }
//
    // =============================
    // TODO TV3: Endpoint 3 - Cập nhật trạng thái
    // HƯỚNG DẪN:
    // @PostMapping("/update-status/{id}")
    // public String capNhatTrangThai(
    //         @PathVariable Integer id,
    //         @RequestParam String trangThaiMoi,
    //         RedirectAttributes redirectAttributes) {
    //
    //     try {
    //         boolean success = donHangService.updateTrangThai(id, trangThaiMoi);
    //
    //         if (success) {
    //             redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công");
    //         } else {
    //             redirectAttributes.addFlashAttribute("error", "Không thể cập nhật trạng thái");
    //         }
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", e.getMessage());
    //     }
    //
    //     return "redirect:/admin/donhang/detail/" + id;
    // }

    // =============================
    // TODO TV3: Endpoint 4 - Đơn chờ xác nhận (cho dashboard)
    // HƯỚNG DẪN:
    // @GetMapping("/pending")
    // @ResponseBody
    // public List<DonHang> getPendingOrders(@RequestParam(defaultValue = "10") int limit) {
    //     return donHangService.getPendingOrders(limit);
    // }
}
