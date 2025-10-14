package com.example.projectend.controller.admin;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.service.BaiVietService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TODO: THÀNH VIÊN 4 - QUẢN LÝ BÀI VIẾT/TIN TỨC
 * Controller xử lý CRUD bài viết (kiến thức về Tết, tin tức khuyến mãi)
 */
@Controller
@RequestMapping("/admin/baiviet")
@PreAuthorize("hasAnyRole('Admin', 'Nhân viên')")
public class AdminBaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    /**
     * TODO TV4: Danh sách bài viết
     * - Tìm kiếm theo tiêu đề
     * - Lọc theo trạng thái (nháp/xuất bản)
     * - Sort: Mới nhất, cũ nhất
     * <p>
     * HƯỚNG DẪN:
     * 1. Tạo Pageable: PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "ngayTao"))
     * 2. Gọi baiVietService.findAll(pageable) hoặc findWithFilters(...)
     * 3. Truyền Page<BaiViet> vào model
     */
    @GetMapping
    public String danhSachBaiViet(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // TODO TV4: Implement phân trang
        // Pageable pageable = PageRequest.of(page, size);
        // Page<BaiViet> baiVietPage = baiVietService.findWithFilters(search, trangThai, pageable);

        model.addAttribute("message", "TODO TV4: Danh sách bài viết - Cần thêm phân trang");
        return "admin/baiviet/list";
    }

    /**
     * TODO TV4: Form tạo bài viết mới
     */
    @GetMapping("/new")
    public String formTaoBaiViet(Model model) {
        model.addAttribute("baiViet", new BaiViet());
        return "admin/baiviet/form";
    }

    /**
     * TODO TV4: Xử lý tạo bài viết
     * <p>
     * HƯỚNG DẪN:
     * 1. Lấy email từ userDetails.getUsername()
     * 2. Tìm TaiKhoan từ email: taiKhoanRepository.findByEmail(email)
     * 3. Set baiViet.setTaiKhoan(taiKhoan)
     * 4. Set baiViet.setNgayTao(LocalDateTime.now())
     * 5. baiVietService.save(baiViet)
     */
    @PostMapping("/new")
    public String taoBaiViet(
            @Valid @ModelAttribute BaiViet baiViet,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/baiviet/form";
        }

        // TODO TV4:
        // 1. Lấy tài khoản hiện tại từ userDetails
        // 2. Set baiViet.setTaiKhoan(...)
        // 3. Set ngày tạo, ngày cập nhật
        // 4. baiVietService.save(baiViet)

        redirectAttributes.addFlashAttribute("success", "Đã tạo bài viết mới");
        return "redirect:/admin/baiviet";
    }

    /**
     * TODO TV4: Form sửa bài viết
     */
    @GetMapping("/{id}/edit")
    public String formSuaBaiViet(@PathVariable Integer id, Model model) {
        // TODO TV4: Tìm bài viết theo id
        // BaiViet baiViet = baiVietService.findById(id).orElseThrow();
        // model.addAttribute("baiViet", baiViet);

        model.addAttribute("message", "TODO TV4: Form sửa bài viết");
        return "admin/baiviet/form";
    }

    /**
     * TODO TV4: Xử lý cập nhật bài viết
     */
    @PostMapping("/{id}/edit")
    public String capNhatBaiViet(
            @PathVariable Integer id,
            @Valid @ModelAttribute BaiViet baiViet,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/baiviet/form";
        }

        // TODO TV4: Update bài viết
        // baiViet.setMaBV(id);
        // baiViet.setNgayCapNhat(LocalDateTime.now());
        // baiVietService.save(baiViet);

        redirectAttributes.addFlashAttribute("success", "Đã cập nhật bài viết");
        return "redirect:/admin/baiviet";
    }

    /**
     * TODO TV4: Xóa bài viết
     */
    @PostMapping("/{id}/delete")
    public String xoaBaiViet(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // TODO TV4: Xóa bài viết
        // baiVietService.deleteById(id);

        redirectAttributes.addFlashAttribute("success", "Đã xóa bài viết");
        return "redirect:/admin/baiviet";
    }

    /**
     * TODO TV4: Đổi trạng thái xuất bản
     */
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        // TODO TV4: Toggle trangThai
        // BaiViet baiViet = baiVietService.findById(id).orElseThrow();
        // baiViet.setTrangThai(!baiViet.getTrangThai());
        // baiVietService.save(baiViet);

        redirectAttributes.addFlashAttribute("success", "Đã thay đổi trạng thái");
        return "redirect:/admin/baiviet";
    }
}
