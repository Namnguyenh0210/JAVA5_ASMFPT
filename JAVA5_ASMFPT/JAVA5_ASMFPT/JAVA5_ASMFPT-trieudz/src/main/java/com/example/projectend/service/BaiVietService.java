package com.example.projectend.service;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.repository.BaiVietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BaiVietService {

    @Autowired
    private BaiVietRepository baiVietRepository;

    // ================================
    // 1. Admin: Phân trang tất cả bài viết
    public Page<BaiViet> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayDang").descending());
        return baiVietRepository.findAll(pageable);
    }

    // ================================
    // 2. Customer: Phân trang tất cả bài viết
    public Page<BaiViet> getAllBaiViet(Pageable pageable) {
        return baiVietRepository.findAll(pageable);
    }

    // ================================
    // 3. Bài viết nổi bật (featured posts)
    public List<BaiViet> getFeaturedPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return baiVietRepository.findTopByOrderByNgayDangDesc(pageable);
    }

    // ================================
    // 4. Tìm kiếm bài viết theo tiêu đề
    public Page<BaiViet> searchByTitle(String keyword, Pageable pageable) {
        return baiVietRepository.findByTieuDeContainingIgnoreCase(keyword, pageable);
    }

    // ================================
    // 5. CRUD cơ bản
    public Optional<BaiViet> findById(Integer id) {
        return baiVietRepository.findById(id);
    }

    public BaiViet save(BaiViet baiViet) {
        return baiVietRepository.save(baiViet);
    }

    public void deleteById(Integer id) {
        baiVietRepository.deleteById(id);
    }
}
