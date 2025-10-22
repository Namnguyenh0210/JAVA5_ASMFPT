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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/sanpham")
public class AdminSanPhamController {

    private final String UPLOAD_DIR = "src/main/resources/static/images/";
    @Autowired
    private SanPhamService sanPhamService;
    @Autowired
    private LoaiSanPhamService loaiSanPhamService;

    // 1. Danh sách sản phẩm
    @GetMapping("")
    public String danhSachSanPham(Model model) {
        List<SanPham> products = sanPhamService.findAll();
        List<LoaiSanPham> categories = loaiSanPhamService.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new SanPham());
        model.addAttribute("isEdit", false); // Thêm dòng này
        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        return "admin/sanpham";
    }

    // 2. Form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String formSuaSanPham(@PathVariable Integer id, Model model) {
        SanPham product = sanPhamService.findById(id).orElse(new SanPham());
        List<LoaiSanPham> categories = loaiSanPhamService.findAll();
        List<SanPham> products = sanPhamService.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("isEdit", true);
        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", "Sửa sản phẩm");
        return "admin/sanpham";
    }

    // 3. Lưu sản phẩm (thêm mới hoặc cập nhật)
    @PostMapping("/save")
    public String luuSanPham(@ModelAttribute("product") SanPham product,
                             @RequestParam(value = "file", required = false) MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            // Xử lý upload ảnh
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
                product.setHinhAnh(fileName);
            } else if (product.getMaSP() != null) {
                // Nếu đang sửa và không upload ảnh mới, giữ ảnh cũ
                SanPham existingProduct = sanPhamService.findById(product.getMaSP()).orElse(null);
                if (existingProduct != null && existingProduct.getHinhAnh() != null) {
                    product.setHinhAnh(existingProduct.getHinhAnh());
                }
            }

            // Set ngày tạo cho sản phẩm mới
            if (product.getMaSP() == null || product.getNgayTao() == null) {
                product.setNgayTao(LocalDateTime.now());
            }

            sanPhamService.save(product);
            redirectAttributes.addFlashAttribute("success",
                    product.getMaSP() == null ? "Thêm sản phẩm thành công!" : "Cập nhật sản phẩm thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/sanpham";
    }

    // 4. Xóa sản phẩm
    @PostMapping("/delete/{id}")
    public String xoaSanPham(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            sanPhamService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/sanpham";
    }
}
