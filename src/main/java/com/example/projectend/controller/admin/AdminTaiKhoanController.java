package com.example.projectend.controller.admin;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.entity.VaiTro;
import com.example.projectend.repository.VaiTroRepository;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/accounts")
@PreAuthorize("hasRole('Admin')")
public class AdminTaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    // Danh sách & form tạo tài khoản
    @GetMapping
    public String danhSachTaiKhoan(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TaiKhoan> accounts = taiKhoanService.getAllTaiKhoan(pageable);

        model.addAttribute("accounts", accounts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", accounts.getTotalPages());
        model.addAttribute("account", new TaiKhoan()); 
        model.addAttribute("vaiTros", vaiTroRepository.findAll()); 

        return "admin/accounts";
    }

    // Tạo tài khoản mới / Cập nhật
    @PostMapping("/save")
    public String saveTaiKhoan(@ModelAttribute TaiKhoan account, RedirectAttributes ra) {
        taiKhoanService.save(account);
        ra.addFlashAttribute("success", "Lưu tài khoản thành công!");
        return "redirect:/admin/accounts";
    }

    // Thay đổi vai trò
    @PostMapping("/{id}/change-role")
    public String changeRole(@PathVariable Integer id, @RequestParam Integer newVaiTroId,
                             RedirectAttributes ra) {
        if(taiKhoanService.changeRole(id, newVaiTroId)){
            ra.addFlashAttribute("success", "Đã thay đổi vai trò!");
        } else {
            ra.addFlashAttribute("error", "Thay đổi vai trò thất bại!");
        }
        return "redirect:/admin/accounts";
    }

    // Toggle trạng thái
    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Integer id, RedirectAttributes ra) {
        taiKhoanService.findById(id).ifPresent(tk -> {
            tk.setTrangThai(!tk.getTrangThai());
            taiKhoanService.save(tk);
        });
        ra.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        return "redirect:/admin/accounts";
    }
 // Form sửa tài khoản
    @GetMapping("/edit/{id}")
    public String editTaiKhoan(@PathVariable Integer id, Model model) {
        TaiKhoan tk = taiKhoanService.findById(id).orElse(null);
        if (tk == null) {
            return "redirect:/admin/accounts"; // hoặc show lỗi
        }
        
        model.addAttribute("account", tk);
        model.addAttribute("vaiTros", vaiTroRepository.findAll());
        return "admin/accounts";
    }

}
