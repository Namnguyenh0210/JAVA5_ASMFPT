package com.example.projectend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller hiển thị trang giới thiệu
 */
@Controller
public class GioiThieuController {

    /**
     * Hiển thị trang giới thiệu về cửa hàng
     */
    @GetMapping("/gioithieu")
    public String gioiThieu(Model model) {
        model.addAttribute("currentPage", "gioithieu");
        model.addAttribute("pageTitle", "Giới thiệu - Cửa hàng đồ Tết");

        // Breadcrumb
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Giới Thiệu");
        breadcrumbItem.put("url", null);
        List<Map<String, String>> breadcrumbItems = List.of(breadcrumbItem);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        return "gioithieu";
    }
}
