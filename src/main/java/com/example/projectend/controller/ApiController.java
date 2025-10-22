package com.example.projectend.controller;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.service.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller cung cấp các endpoint API
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * API lấy thông tin vai trò của người dùng hiện tại
     */
    @GetMapping("/user/role")
    public ResponseEntity<Map<String, String>> getUserRole() {
        Map<String, String> response = new HashMap<>();

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                response.put("role", "Guest");
                return ResponseEntity.ok(response);
            }

            String email = auth.getName();
            TaiKhoan taiKhoan = userDetailsService.getTaiKhoanByEmail(email);
            String roleName = taiKhoan.getVaiTro().getTenVT();

            response.put("role", roleName);
            response.put("email", email);
            response.put("name", taiKhoan.getHoTen());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("role", "Guest");
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
