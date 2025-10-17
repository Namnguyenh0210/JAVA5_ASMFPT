package com.example.projectend.controller.admin;

import java.util.ArrayList;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projectend.entity.SanPham;

/**
 * ADMIN SAN PHAM CONTROLLER - Quản lý sản phẩm (Admin)
 * <p>
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND
 * =============================
 * TODO TV3 - CẦN LÀM (6 ENDPOINTS):
 * <p>
 * 1. GET /admin/sanpham - Danh sách sản phẩm (search, filter, paginate)
 * → Gọi sanPhamService.getAllSanPham(pageable)
 * <p>
 * 2. GET /admin/sanpham/add - Form thêm sản phẩm
 * → Load danh mục: loaiSanPhamService.findAll()
 * <p>
 * 3. POST /admin/sanpham/add - Lưu sản phẩm mới + upload ảnh
 * → Upload file ảnh lên /static/img/
 * → Gọi sanPhamService.save(sanPham)
 * <p>
 * 4. GET /admin/sanpham/edit/{id} - Form sửa sản phẩm
 * → Gọi sanPhamService.findById(id)
 * <p>
 * 5. POST /admin/sanpham/edit/{id} - Cập nhật sản phẩm
 * → Upload ảnh mới (nếu có)
 * → Gọi sanPhamService.save(sanPham)
 * <p>
 * 6. POST /admin/sanpham/delete/{id} - Xóa sản phẩm
 * → Gọi sanPhamService.deleteById(id)
 * <p>
 * THỜI GIAN: 2 ngày
 * LƯU Ý: Upload ảnh lưu vào /src/main/resources/static/img/
 * =============================
 */
@Controller
@RequestMapping("/admin/sanpham")
@PreAuthorize("hasRole('ADMIN') or hasRole('NHÂNVIÊN')")
public class AdminSanPhamController {

    // TODO TV3: Inject services
    // @Autowired private SanPhamService sanPhamService;
    // @Autowired private LoaiSanPhamService loaiSanPhamService;

    // =============================
    // TODO TV3: Endpoint 1 - Danh sách sản phẩm
    @GetMapping("")
    public String danhSachSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer loai,
            Model model) {

        // TODO TV3: HƯỚNG DẪN:
        // Pageable pageable = PageRequest.of(page, size);
        // Page<SanPham> sanPhamPage = sanPhamService.getAllSanPham(pageable);
        // model.addAttribute("sanPhamPage", sanPhamPage);
        //
        // List<LoaiSanPham> loaiList = loaiSanPhamService.findAll();
        // model.addAttribute("loaiList", loaiList);
    	model.addAttribute("products", new ArrayList<SanPham>());
        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        return "admin/products";
    }

    // =============================
    // TODO TV3: Endpoint 2 - Form thêm sản phẩm
    @GetMapping("/add")
    public String themSanPham(Model model) {

        // TODO TV3: HƯỚNG DẪN:
        // model.addAttribute("sanPham", new SanPham());
        // List<LoaiSanPham> loaiList = loaiSanPhamService.findAll();
        // model.addAttribute("loaiList", loaiList);

        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", "Thêm sản phẩm mới");
        return "admin/products";
    }

    // =============================
    // TODO TV3: Endpoint 3 - Lưu sản phẩm mới + upload ảnh
    // HƯỚNG DẪN:
    // @PostMapping("/add")
    // public String luuSanPham(
    //         @ModelAttribute SanPham sanPham,
    //         @RequestParam("file") MultipartFile file,
    //         RedirectAttributes redirectAttributes) {
    //
    //     try {
    //         // Upload ảnh
    //         if (!file.isEmpty()) {
    //             String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    //             String uploadDir = "src/main/resources/static/img/";
    //             Path uploadPath = Paths.get(uploadDir);
    //
    //             if (!Files.exists(uploadPath)) {
    //                 Files.createDirectories(uploadPath);
    //             }
    //
    //             Path filePath = uploadPath.resolve(fileName);
    //             Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    //
    //             sanPham.setHinhAnh("/img/" + fileName);
    //         }
    //
    //         sanPham.setNgayTao(LocalDateTime.now());
    //         sanPhamService.save(sanPham);
    //
    //         redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công");
    //         return "redirect:/admin/sanpham";
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", e.getMessage());
    //         return "redirect:/admin/sanpham/add";
    //     }
    // }

    // =============================
    // TODO TV3: Endpoint 4 - Form sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String suaSanPham(@PathVariable Integer id, Model model) {

        // TODO TV3: HƯỚNG DẪN:
        // Optional<SanPham> sanPhamOpt = sanPhamService.findById(id);
        // if (sanPhamOpt.isEmpty()) {
        //     return "redirect:/admin/sanpham?error=notfound";
        // }
        //
        // model.addAttribute("sanPham", sanPhamOpt.get());
        // List<LoaiSanPham> loaiList = loaiSanPhamService.findAll();
        // model.addAttribute("loaiList", loaiList);

        model.addAttribute("currentPage", "sanpham");
        model.addAttribute("pageTitle", "Sửa sản phẩm");
        return "admin/products";
    }

    // =============================
    // TODO TV3: Endpoint 5 - Cập nhật sản phẩm
    // HƯỚNG DẪN: Tương tự Endpoint 3, nhưng không set NgayTao

    // =============================
    // TODO TV3: Endpoint 6 - Xóa sản phẩm
    // HƯỚNG DẪN:
    // @PostMapping("/delete/{id}")
    // public String xoaSanPham(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    //     try {
    //         sanPhamService.deleteById(id);
    //         redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công");
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm: " + e.getMessage());
    //     }
    //     return "redirect:/admin/sanpham";
    // }
}
