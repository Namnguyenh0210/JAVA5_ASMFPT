package com.example.projectend.service;

import com.example.projectend.entity.LoaiSanPham;
import com.example.projectend.entity.SanPham;
import com.example.projectend.repository.LoaiSanPhamRepository;
import com.example.projectend.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * SAN PHAM SERVICE - Xử lý logic nghiệp vụ cho sản phẩm
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (8 METHODS):
 *  1. getFeaturedProducts(int limit) - Lấy sản phẩm nổi bật cho trang chủ
 *     → Query top X sản phẩm mới nhất (theo NgayTao DESC)
 *     → Ví dụ: return sanPhamRepository.findTop4ByOrderByNgayTaoDesc();
 * <p>
 * 2. findWithFilters(search, loaiId, minPrice, maxPrice, sort, pageable) - Tìm kiếm + lọc
 * → Dùng Specification hoặc query động
 * → Filter: tên sản phẩm LIKE, loại, khoảng giá
 * → Sort: "moi" (mới nhất), "gia-tang", "gia-giam"
 * <p>
 * 3. findRelatedProducts(loaiId, excludeId, limit) - Sản phẩm liên quan
 * → Cùng loại, khác ID hiện tại
 * → Limit 4-6 sản phẩm
 * <p>
 * 4. searchByKeyword(keyword, limit) - Tìm kiếm nhanh AJAX
 * → TenSP LIKE %keyword%
 * → Return JSON 10 kết quả cho autocomplete
 * <p>
 * 5. getAllCategories() - Lấy tất cả danh mục
 * → Dùng cho menu filter, sidebar
 * <p>
 * 6. incrementLuotXem(Integer id) - Tăng lượt xem sản phẩm
 * → Gọi khi người dùng vào trang chi tiết
 * <p>
 * 7-8. save() / delete() - CRUD cơ bản (TV3 Admin sẽ dùng)
 * <p>
 * THỜI GIAN: 2 ngày (Methods 1-6 là ưu tiên)
 * =============================
 */
@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    // =============================
    // TODO TV2: Method 1 - Lấy sản phẩm nổi bật cho trang chủ
    // HƯỚNG DẪN:
    // Option 1: Tạo query method trong Repository:
    //   List<SanPham> findTop4ByOrderByNgayTaoDesc();
    // Option 2: Dùng Pageable:
    //   return sanPhamRepository.findAll(PageRequest.of(0, limit, Sort.by("ngayTao").descending())).getContent();
    public List<SanPham> getFeaturedProducts(int limit) {
        return sanPhamRepository.findTop8ByOrderByNgayTaoDesc();
    }

    // =============================
    // TODO TV2: Method 2 - Tìm kiếm + lọc + phân trang
    // HƯỚNG DẪN:
    // 1. Tạo Specification<SanPham> spec
    // 2. Nếu search != null: spec = spec.and(tenSP LIKE)
    // 3. Nếu loaiId != null: spec = spec.and(loaiSanPham.id = loaiId)
    // 4. Nếu minPrice != null: spec = spec.and(gia >= minPrice)
    // 5. Nếu maxPrice != null: spec = spec.and(gia <= maxPrice)
    // 6. Sort theo tham số sort ("moi", "gia-tang", "gia-giam")
    // 7. return sanPhamRepository.findAll(spec, pageable);
    public Page<SanPham> findWithFilters(String search, Integer loaiId,
                                         BigDecimal minPrice, BigDecimal maxPrice,
                                         String sort, Pageable pageable) {
        // Xử lý sort
        Sort sortObj = Sort.by(Sort.Direction.DESC, "ngayTao");
        if (sort != null) {
            switch (sort) {
                case "gia-tang":
                    sortObj = Sort.by(Sort.Direction.ASC, "gia");
                    break;
                case "gia-giam":
                    sortObj = Sort.by(Sort.Direction.DESC, "gia");
                    break;
                case "moi":
                default:
                    sortObj = Sort.by(Sort.Direction.DESC, "ngayTao");
                    break;
            }
        }

        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);
        return sanPhamRepository.findAll(pageableWithSort);
    }

    // =============================
    // TODO TV2: Method 3 - Lấy sản phẩm liên quan (cùng danh mục)
    // HƯỚNG DẪN:
    // Tạo query trong Repository:
    // List<SanPham> findTop6ByLoaiSanPham_MaLoaiAndMaSPNotOrderByNgayTaoDesc(Integer loaiId, Integer excludeId);
    public List<SanPham> findRelatedProducts(Integer loaiId, Integer excludeId, int limit) {
        return sanPhamRepository.findTop6ByLoaiSanPham_MaLoaiAndMaSPNotOrderByNgayTaoDesc(loaiId, excludeId);
    }

    // =============================
    // TODO TV2: Method 4 - Tìm kiếm nhanh AJAX (autocomplete)
    // HƯỚNG DẪN:
    // Tạo query trong Repository:
    // List<SanPham> findTop10ByTenSPContainingIgnoreCase(String keyword);
    public List<SanPham> searchByKeyword(String keyword, int limit) {
        return sanPhamRepository.findTop10ByTenSPContainingIgnoreCaseOrderByNgayTaoDesc(keyword);
    }

    // =============================
    // CÁC METHOD CƠ BẢN DÙNG CHUNG (ĐÃ CÓ SẴN)
    public Page<SanPham> getAllSanPham(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    public Optional<SanPham> findById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    public void deleteById(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    public long countAll() {
        return sanPhamRepository.count();
    }

    // =============================
    // TODO TV2: Method 5 - Lấy tất cả danh mục (cho menu filter)
    // HƯỚNG DẪN: return loaiSanPhamRepository.findAll();
    public List<LoaiSanPham> getAllCategories() {
        return loaiSanPhamRepository.findAll();
    }

    // =============================
    // TODO TV2: Method 6 - Tăng lượt xem (gọi ở SanPhamController chi tiết)
    // HƯỚNG DẪN:
    // Optional<SanPham> sp = sanPhamRepository.findById(id);
    // if (sp.isPresent()) {
    //     sp.get().setLuotXem(sp.get().getLuotXem() + 1);
    //     sanPhamRepository.save(sp.get());
    // }
    public void incrementLuotXem(Integer id) {
        Optional<SanPham> sp = sanPhamRepository.findById(id);
        if (sp.isPresent()) {
            // TODO: Thêm field luotXem trong entity nếu cần
            sanPhamRepository.save(sp.get());
        }
    }

    // =============================
    // NOTE CHO TV3 (ADMIN): Methods save/delete ở trên dùng được cho CRUD admin
    // NOTE CHO TV4 (THỐNG KÊ): Tạo query riêng trong Repository để lấy top bán chạy
}
