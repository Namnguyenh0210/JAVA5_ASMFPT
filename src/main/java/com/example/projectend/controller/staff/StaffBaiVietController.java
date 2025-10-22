package com.example.projectend.controller.staff;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.TaiKhoanRepository;
import com.example.projectend.service.BaiVietService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * STAFF BAI VIET CONTROLLER - Quản lý bài viết (NHÂN VIÊN)
 * =============================
 * Giao diện riêng cho nhân viên - Màu xanh
 * =============================
 */
@Controller
@RequestMapping("/staff/baiviet")
@PreAuthorize("hasRole('Nhân viên')")
public class StaffBaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    // =============================
    // Endpoint 1 - Danh sách bài viết (STAFF)
    // GET /staff/baiviet
    // =============================
    @GetMapping("")
    public String danhSachBaiViet(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            Authentication auth,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDang").descending());

        // Nhân viên chỉ thấy bài viết của mình
        String email = auth.getName();
        TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

        Page<BaiViet> baiVietPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            baiVietPage = baiVietService.searchByTacGiaAndKeyword(currentUser, keyword, pageable);
        } else {
            baiVietPage = baiVietService.findByTacGia(currentUser, pageable);
        }

        model.addAttribute("baiVietPage", baiVietPage);
        model.addAttribute("posts", baiVietPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", "baiviet");
        model.addAttribute("pageTitle", "Quản lý bài viết");

        return "staff/baiviet";
    }

    // =============================
    // Endpoint 2 - Trang tạo bài viết mới (STAFF)
    // GET /staff/baiviet/create
    // =============================
    @GetMapping("/create")
    public String trangTaoBaiViet(Model model) {
        model.addAttribute("baiViet", new BaiViet());
        model.addAttribute("currentPage", "baiviet");
        model.addAttribute("pageTitle", "Tạo bài viết mới");
        return "staff/baiviet-form";
    }

    // =============================
    // Endpoint 3 - Xử lý tạo bài viết (STAFF)
    // POST /staff/baiviet/create
    // =============================
    @PostMapping("/create")
    public String taoBaiViet(
            @ModelAttribute BaiViet baiViet,
            @RequestParam(required = false) MultipartFile imageFile,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            String email = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy thông tin người dùng!");
                return "redirect:/staff/baiviet";
            }

            baiViet.setTaiKhoan(currentUser);
            baiVietService.createBaiViet(baiViet, imageFile);

            redirectAttributes.addFlashAttribute("success", "✅ Tạo bài viết thành công!");
            return "redirect:/staff/baiviet";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi: " + e.getMessage());
            return "redirect:/staff/baiviet/create";
        }
    }

    // =============================
    // Endpoint 4 - Trang sửa bài viết (STAFF)
    // GET /staff/baiviet/edit/{id}
    // =============================
    @GetMapping("/edit/{id}")
    public String trangSuaBaiViet(
            @PathVariable Integer id,
            Authentication auth,
            Model model,
            RedirectAttributes redirectAttributes) {

        String email = auth.getName();
        TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

        Optional<BaiViet> baiVietOpt = baiVietService.findById(id);

        if (baiVietOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy bài viết!");
            return "redirect:/staff/baiviet";
        }

        BaiViet baiViet = baiVietOpt.get();

        // Chỉ cho phép sửa bài viết của mình
        if (!baiViet.getTaiKhoan().getMaTK().equals(currentUser.getMaTK())) {
            redirectAttributes.addFlashAttribute("error", "❌ Bạn không có quyền sửa bài viết này!");
            return "redirect:/staff/baiviet";
        }

        model.addAttribute("baiViet", baiViet);
        model.addAttribute("currentPage", "baiviet");
        model.addAttribute("pageTitle", "Sửa bài viết");
        return "staff/baiviet-form";
    }

    // =============================
    // Endpoint 5 - Xử lý sửa bài viết (STAFF)
    // POST /staff/baiviet/edit/{id}
    // =============================
    @PostMapping("/edit/{id}")
    public String suaBaiViet(
            @PathVariable Integer id,
            @ModelAttribute BaiViet baiViet,
            @RequestParam(required = false) MultipartFile imageFile,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            String email = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

            Optional<BaiViet> existingBaiVietOpt = baiVietService.findById(id);

            if (existingBaiVietOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy bài viết!");
                return "redirect:/staff/baiviet";
            }

            BaiViet existingBaiViet = existingBaiVietOpt.get();

            // Chỉ cho phép sửa bài viết của mình
            if (!existingBaiViet.getTaiKhoan().getMaTK().equals(currentUser.getMaTK())) {
                redirectAttributes.addFlashAttribute("error", "❌ Bạn không có quyền sửa bài viết này!");
                return "redirect:/staff/baiviet";
            }

            baiViet.setMaBV(id);
            baiViet.setTaiKhoan(currentUser);
            baiVietService.updateBaiViet(baiViet, imageFile);

            redirectAttributes.addFlashAttribute("success", "✅ Cập nhật bài viết thành công!");
            return "redirect:/staff/baiviet";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi: " + e.getMessage());
            return "redirect:/staff/baiviet/edit/" + id;
        }
    }

    // =============================
    // Endpoint 6 - Xóa bài viết (STAFF)
    // POST /staff/baiviet/delete/{id}
    // =============================
    @PostMapping("/delete/{id}")
    public String xoaBaiViet(
            @PathVariable Integer id,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            String email = auth.getName();
            TaiKhoan currentUser = taiKhoanRepository.findByEmail(email).orElse(null);

            Optional<BaiViet> baiVietOpt = baiVietService.findById(id);

            if (baiVietOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "❌ Không tìm thấy bài viết!");
                return "redirect:/staff/baiviet";
            }

            BaiViet baiViet = baiVietOpt.get();

            // Chỉ cho phép xóa bài viết của mình
            if (!baiViet.getTaiKhoan().getMaTK().equals(currentUser.getMaTK())) {
                redirectAttributes.addFlashAttribute("error", "❌ Bạn không có quyền xóa bài viết này!");
                return "redirect:/staff/baiviet";
            }

            baiVietService.deleteBaiViet(id);
            redirectAttributes.addFlashAttribute("success", "✅ Xóa bài viết thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Lỗi: " + e.getMessage());
        }

        return "redirect:/staff/baiviet";
    }
}
