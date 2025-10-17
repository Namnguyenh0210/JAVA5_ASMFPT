package com.example.projectend.controller;

import com.example.projectend.entity.BaiViet;
import com.example.projectend.service.BaiVietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * KIEN THUC CONTROLLER - Trang kiến thức về văn hóa Tết
 * Người 1 - Database Design & Backend Core (ĐÃ HOÀN THÀNH)
 * Người 3 - Frontend & Customer Website (ĐÃ HOÀN THÀNH)
 */
@Controller
public class KienThucController {

    @Autowired
    private BaiVietService baiVietService;

    @GetMapping("/kienthuc")
    public String kienThuc(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "6") int size,
                           Model model) {
        // Add current page for navigation active state
        model.addAttribute("currentPage", "kienthuc");

        // Add breadcrumb data
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Kiến Thức");
        breadcrumbItem.put("url", null);
        List<Map<String, String>> breadcrumbItems = List.of(breadcrumbItem);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        // Lấy danh sách bài viết với phân trang
        Pageable pageable = PageRequest.of(page, size);
        Page<BaiViet> baiVietPage = baiVietService.getAllBaiViet(pageable);
        model.addAttribute("baiVietPage", baiVietPage);

        // Lấy bài viết nổi bật (3 bài mới nhất)
        List<BaiViet> featuredPosts = baiVietService.getFeaturedPosts(3);
        model.addAttribute("featuredPosts", featuredPosts);

        model.addAttribute("pageTitle", "Kiến thức - Cửa hàng đồ Tết");
        model.addAttribute("tetYear", "2025");

        return "kienthuc";
    }

    // Chi tiết bài viết
    @GetMapping("/kienthuc/{id}")
    public String chiTietBaiViet(@PathVariable Integer id, Model model) {
        Optional<BaiViet> baiVietOpt = baiVietService.findById(id);
        if (baiVietOpt.isEmpty()) {
            return "redirect:/kienthuc?notfound";
        }

        BaiViet baiViet = baiVietOpt.get();
        model.addAttribute("baiViet", baiViet);
        model.addAttribute("currentPage", "kienthuc");
        model.addAttribute("pageTitle", baiViet.getTieuDe() + " - Kiến thức Tết");

        // Breadcrumb cho chi tiết bài viết
        Map<String, String> breadcrumb1 = new HashMap<>();
        breadcrumb1.put("name", "Kiến Thức");
        breadcrumb1.put("url", "/kienthuc");

        Map<String, String> breadcrumb2 = new HashMap<>();
        breadcrumb2.put("name", baiViet.getTieuDe());
        breadcrumb2.put("url", null);

        List<Map<String, String>> breadcrumbItems = List.of(breadcrumb1, breadcrumb2);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        return "kienthuc-detail";
    }

    // Tìm kiếm bài viết
    @GetMapping("/kienthuc/search")
    public String timKiemBaiViet(@RequestParam String keyword,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "6") int size,
                                 Model model) {
        model.addAttribute("currentPage", "kienthuc");

        // Breadcrumb cho tìm kiếm
        Map<String, String> breadcrumb1 = new HashMap<>();
        breadcrumb1.put("name", "Kiến Thức");
        breadcrumb1.put("url", "/kienthuc");

        Map<String, String> breadcrumb2 = new HashMap<>();
        breadcrumb2.put("name", "Tìm kiếm: " + keyword);
        breadcrumb2.put("url", null);

        List<Map<String, String>> breadcrumbItems = List.of(breadcrumb1, breadcrumb2);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        // Tìm kiếm bài viết
        Pageable pageable = PageRequest.of(page, size);
        Page<BaiViet> baiVietPage = baiVietService.searchByTitle(keyword, pageable);

        model.addAttribute("baiVietPage", baiVietPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Tìm kiếm: " + keyword + " - Kiến thức Tết");

        return "kienthuc";
    }
}
