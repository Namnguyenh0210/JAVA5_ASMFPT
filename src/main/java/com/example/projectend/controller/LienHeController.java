package com.example.projectend.controller;

import com.example.projectend.dto.ContactForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller xử lý trang liên hệ
 */
@Controller
public class LienHeController {

    /**
     * Hiển thị trang liên hệ
     */
    @GetMapping("/lienhe")
    public String lienHe(Model model) {
        model.addAttribute("currentPage", "lienhe");

        // Breadcrumb
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Liên Hệ");
        breadcrumbItem.put("url", null);
        List<Map<String, String>> breadcrumbItems = List.of(breadcrumbItem);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        // Khởi tạo form liên hệ
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }

        // Thông tin liên hệ cửa hàng
        model.addAttribute("shopAddress", "123 Quang Trung, Gò Vấp, TP.HCM");
        model.addAttribute("shopPhone", "0909 123 456");
        model.addAttribute("shopEmail", "contact@tetmarket.com");
        model.addAttribute("shopHours", "8:00 - 22:00 hàng ngày");
        model.addAttribute("pageTitle", "Liên hệ - Cửa hàng đồ Tết");
        model.addAttribute("tetYear", "2025");

        return "lienhe";
    }

    /**
     * Xử lý gửi form liên hệ
     */
    @PostMapping("/lienhe/gui")
    public String guiLienHe(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentPage", "lienhe");
            Map<String, String> breadcrumbItem = new HashMap<>();
            breadcrumbItem.put("name", "Liên Hệ");
            breadcrumbItem.put("url", null);
            model.addAttribute("breadcrumbItems", List.of(breadcrumbItem));
            model.addAttribute("shopAddress", "123 Quang Trung, Gò Vấp, TP.HCM");
            model.addAttribute("shopPhone", "0909 123 456");
            model.addAttribute("shopEmail", "contact@tetmarket.com");
            model.addAttribute("shopHours", "8:00 - 22:00 hàng ngày");
            return "lienhe";
        }

        // Log thông tin liên hệ
        System.out.println("Contact form received: " + contactForm);

        redirectAttributes.addFlashAttribute("successMessage",
                "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong vòng 24 giờ.");

        return "redirect:/lienhe";
    }
}
