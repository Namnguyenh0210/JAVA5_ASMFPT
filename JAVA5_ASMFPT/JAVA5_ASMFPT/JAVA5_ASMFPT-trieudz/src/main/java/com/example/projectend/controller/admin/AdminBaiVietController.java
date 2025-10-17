package com.example.projectend.controller.admin;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.service.BaiVietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/admin/baiviet")
public class AdminBaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    // ================================
    // 1. Danh sách bài viết admin (phân trang)
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model) {
        Page<BaiViet> list = baiVietService.findAll(page, size);
        model.addAttribute("page", list);
        model.addAttribute("currentPage", "baiviet");
        return "admin/baiviet/list";
    }

    // ================================
    // 2. Form tạo bài viết
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("baiViet", new BaiViet());
        return "admin/baiviet/form";
    }

    // ================================
    // 3. Lưu bài viết (tạo / cập nhật)
    @PostMapping("/save")
    public String save(@ModelAttribute BaiViet baiViet, RedirectAttributes ra) {
        baiVietService.save(baiViet);
        ra.addFlashAttribute("success", "Lưu bài viết thành công");
        return "redirect:/admin/baiviet";
    }

    // ================================
    // 4. Xóa bài viết
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        baiVietService.deleteById(id);
        ra.addFlashAttribute("success", "Xóa thành công");
        return "redirect:/admin/baiviet";
    }
}
