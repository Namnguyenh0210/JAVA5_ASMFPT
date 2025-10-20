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
 * LIEN HE CONTROLLER - Trang liên hệ
 * PHÂN CÔNG:
 * - THÀNH VIÊN 3: Form gửi liên hệ (nếu triển khai) + validation đơn giản
 * - THÀNH VIÊN 4: (Optional) Ghi log liên hệ vào bảng riêng để admin xem (nếu thêm schema phụ ngoài phạm vi chính thì bỏ qua)
 */
@Controller
public class LienHeController {

    @GetMapping("/lienhe")
    public String lienHe(Model model) {
        // Add current page for navigation active state
        model.addAttribute("currentPage", "lienhe");

        // Add breadcrumb data - using HashMap to allow null values
        Map<String, String> breadcrumbItem = new HashMap<>();
        breadcrumbItem.put("name", "Liên Hệ");
        breadcrumbItem.put("url", null);

        List<Map<String, String>> breadcrumbItems = List.of(breadcrumbItem);
        model.addAttribute("breadcrumbItems", breadcrumbItems);

        // Initialize empty contact form
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

    @PostMapping("/lienhe/gui")
    public String guiLienHe(@Valid @ModelAttribute("contactForm") ContactForm contactForm,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (bindingResult.hasErrors()) {
            // Return to form with errors
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

        // TODO: Save contact form to database or send email
        // For now, just log and show success message
        System.out.println("Contact form received: " + contactForm);

        redirectAttributes.addFlashAttribute("successMessage",
            "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong vòng 24 giờ.");

        return "redirect:/lienhe";
    }
}
