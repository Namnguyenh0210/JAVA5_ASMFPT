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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service xử lý logic nghiệp vụ cho sản phẩm
 */
@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private LoaiSanPhamRepository loaiSanPhamRepository;

    /**
     * Lấy danh sách sản phẩm nổi bật cho trang chủ
     */
    public List<SanPham> getFeaturedProducts(int limit) {
        return sanPhamRepository.findTop8ByOrderByNgayTaoDesc();
    }

    /**
     * Tìm kiếm sản phẩm với bộ lọc và phân trang
     */
    public Page<SanPham> findWithFilters(String search, Integer loaiId,
                                         BigDecimal minPrice, BigDecimal maxPrice,
                                         String sort, Pageable pageable) {
        Specification<SanPham> spec = Specification.where(null);

        // Lọc theo từ khóa tìm kiếm
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("tenSP")), "%" + search.toLowerCase() + "%"));
        }

        // Lọc theo loại sản phẩm
        if (loaiId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("loaiSanPham").get("maLoai"), loaiId));
        }

        // Lọc theo khoảng giá
        if (minPrice != null && maxPrice != null) {
            if (minPrice.compareTo(maxPrice) > 0) {
                BigDecimal tmp = minPrice;
                minPrice = maxPrice;
                maxPrice = tmp;
            }
            final BigDecimal finalMin = minPrice;
            final BigDecimal finalMax = maxPrice;
            spec = spec.and((root, query, cb) -> cb.between(root.get("gia"), finalMin, finalMax));
        } else if (minPrice != null) {
            final BigDecimal finalMin = minPrice;
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("gia"), finalMin));
        } else if (maxPrice != null) {
            final BigDecimal finalMax = maxPrice;
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("gia"), finalMax));
        }

        // Sắp xếp
        Sort sortObj;
        if ("gia-tang".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "gia");
        } else if ("gia-giam".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "gia");
        } else {
            sortObj = Sort.by(Sort.Direction.DESC, "ngayTao");
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);
        return sanPhamRepository.findAll(spec, sortedPageable);
    }

    /**
     * Lấy sản phẩm liên quan cùng danh mục
     */
    public List<SanPham> findRelatedProducts(Integer loaiId, Integer excludeId, int limit) {
        return sanPhamRepository.findTop6ByLoaiSanPham_MaLoaiAndMaSPNotOrderByNgayTaoDesc(loaiId, excludeId);
    }

    /**
     * Tìm kiếm nhanh theo từ khóa (AJAX autocomplete)
     */
    public List<SanPham> searchByKeyword(String keyword, int limit) {
        return sanPhamRepository.findTop10ByTenSPContainingIgnoreCaseOrderByNgayTaoDesc(keyword);
    }

    /**
     * Lấy tất cả danh mục sản phẩm
     */
    public List<LoaiSanPham> getAllCategories() {
        return loaiSanPhamRepository.findAll();
    }

    /**
     * Tăng lượt xem sản phẩm
     */
    public void incrementLuotXem(Integer id) {
        Optional<SanPham> sp = sanPhamRepository.findById(id);
        if (sp.isPresent()) {
            sanPhamRepository.save(sp.get());
        }
    }

    /**
     * Lấy tất cả sản phẩm với phân trang
     */
    public Page<SanPham> getAllSanPham(Pageable pageable) {
        return sanPhamRepository.findAll(pageable);
    }

    /**
     * Tìm sản phẩm theo ID
     */
    public Optional<SanPham> findById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    /**
     * Lưu hoặc cập nhật sản phẩm
     */
    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    /**
     * Xóa sản phẩm theo ID
     */
    public void deleteById(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    /**
     * Đếm tổng số sản phẩm
     */
    public long countAll() {
        return sanPhamRepository.count();
    }

    /**
     * Lấy tất cả sản phẩm (sắp xếp theo ngày tạo mới nhất)
     */
    public List<SanPham> findAll() {
        return sanPhamRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayTao"));
    }
}
