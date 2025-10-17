package com.example.projectend.service.auth;

import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * USER DETAILS SERVICE IMPLEMENTATION
 * Người 2 - Authentication System
 * Load user từ SQL Server database và map roles
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== DEBUG: Đang tìm user với email: " + email);

        // Tìm user theo email trong database
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("=== DEBUG: KHÔNG TÌM THẤY USER với email: " + email);
                    return new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email);
                });

        System.out.println("=== DEBUG: Tìm thấy user: " + taiKhoan.getHoTen());
        System.out.println("=== DEBUG: Email: " + taiKhoan.getEmail());
        System.out.println("=== DEBUG: Password trong DB: " + taiKhoan.getMatKhau());
        System.out.println("=== DEBUG: Vai trò: " + taiKhoan.getVaiTro().getTenVT());
        System.out.println("=== DEBUG: Trạng thái: " + taiKhoan.getTrangThai());

        // Kiểm tra tài khoản có bị vô hiệu hóa không
        if (!taiKhoan.getTrangThai()) {
            System.out.println("=== DEBUG: Tài khoản bị vô hiệu hóa!");
            throw new UsernameNotFoundException("Tài khoản đã bị vô hiệu hóa: " + email);
        }

        // Map roles từ database
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Loại bỏ dấu cách trong tên vai trò để map role chính xác
        String roleName = "ROLE_" + taiKhoan.getVaiTro().getTenVT().toUpperCase()
                .replace(" ", "")
                .replace("Á", "A")
                .replace("Â", "A");
        System.out.println("=== DEBUG: Role name sau khi map: " + roleName);
        authorities.add(new SimpleGrantedAuthority(roleName));

        // Return Spring Security User object
        UserDetails userDetails = User.builder()
                .username(taiKhoan.getEmail())
                .password(taiKhoan.getMatKhau())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!taiKhoan.getTrangThai())
                .build();

        System.out.println("=== DEBUG: UserDetails created successfully");
        return userDetails;
    }

    /**
     * Helper method để lấy TaiKhoan entity từ email
     * Dùng cho các service khác cần thông tin user đầy đủ
     */
    public TaiKhoan getTaiKhoanByEmail(String email) {
        return taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + email));
    }
}
