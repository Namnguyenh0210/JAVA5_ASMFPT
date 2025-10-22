package com.example.projectend.controller.admin;

import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.entity.SanPham;
import com.example.projectend.service.LoaiSanPhamService;
import com.example.projectend.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminSanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    private final String UPLOAD_DIR = "src/main/resources/static/images/";

    // 1. Danh sách sản phẩm
    @GetMapping("")
    public String danhSachSanPham(Model model) {
        List<SanPham> products = sanPhamService.findAll();
        List<LoaiSanPham> categories = loaiSanPhamService.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new SanPham());
        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        return "admin/products";
    }

    // 2. Form thêm / sửa sản phẩm
    @GetMapping({"/add", "/edit/{id}"})
    public String formSanPham(@PathVariable(required = false) Integer id, Model model) {
        SanPham product = (id != null) ? sanPhamService.findById(id).orElse(new SanPham()) : new SanPham();
        List<LoaiSanPham> categories = loaiSanPhamService.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", (id != null) ? "Sửa sản phẩm" : "Thêm sản phẩm mới");
        return "admin/products";
    }

    // 3. Lưu sản phẩm
    @PostMapping("/save")
    public String luuSanPham(@ModelAttribute("product") SanPham product,
                             @RequestParam("file") MultipartFile file,   // 👈 đổi sang "file"
                             RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
                product.setHinhAnh(fileName);
            }

            if (product.getNgayTao() == null) {
                product.setNgayTao(LocalDateTime.now());
            }

            sanPhamService.save(product);
            redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // 4. Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String xoaSanPham(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }
}
