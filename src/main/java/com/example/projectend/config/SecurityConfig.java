package com.example.projectend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SECURITY CONFIG - ASM WEB BÁN HÀNG
 * ĐĂNG NHẬP BẰNG EMAIL + MẬT KHẨU PLAIN TEXT
 *
 * PHÂN QUYỀN (theo SQL):
 * - ROLE_Khách hàng: Khách hàng
 * - ROLE_Nhân viên: Nhân viên
 * - ROLE_Admin: Quản trị viên
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ⚠️ Chỉ dùng NoOp cho demo — thực tế nên dùng BCrypt
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // ==================== PUBLIC ====================
                        .requestMatchers("/", "/home", "/sanpham", "/sanpham/**").permitAll()
                        .requestMatchers("/gioithieu", "/kienthuc", "/lienhe").permitAll()
                        .requestMatchers("/login", "/perform-login", "/register", "/403").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/user/role").authenticated()
                        .requestMatchers("/error").permitAll()

                        // ==================== GIỎ HÀNG ====================
                        .requestMatchers("/giohang", "/giohang/**").permitAll()

                        // ==================== PHÂN QUYỀN STAFF (CHỈ NHÂN VIÊN) ====================
                        .requestMatchers("/staff/**").hasRole("Nhân viên")

                        // ==================== PHÂN QUYỀN ADMIN (CHỈ ADMIN) ====================
                        // Admin Dashboard, Sản phẩm, Tài khoản, Báo cáo - CHỈ ADMIN
                        .requestMatchers("/admin/dashboard", "/admin/sanpham/**", "/admin/accounts/**", "/admin/reports/**").hasRole("Admin")

                        // Đơn hàng và Bài viết - CẢ ADMIN VÀ NHÂN VIÊN đều vào được
                        .requestMatchers("/admin/orders/**", "/admin/baiviet/**").hasAnyRole("Admin", "Nhân viên")

                        // ==================== PHẢI ĐĂNG NHẬP ====================
                        .requestMatchers("/checkout", "/checkout/**").authenticated()
                        .requestMatchers("/profile", "/profile/**").authenticated()

                        // ==================== CÒN LẠI ====================
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform-login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // Điều hướng theo vai trò sau khi đăng nhập
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));
                            boolean isStaff = authentication.getAuthorities().stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_Nhân viên"));

                            System.out.println("=== LOGIN DEBUG ===");
                            System.out.println("User: " + authentication.getName());
                            System.out.println("Authorities: " + authentication.getAuthorities());
                            System.out.println("Is Admin: " + isAdmin);
                            System.out.println("Is Staff: " + isStaff);

                            if (isAdmin) {
                                System.out.println("Redirecting to: /admin/dashboard");
                                response.sendRedirect("/admin/dashboard");
                            } else if (isStaff) {
                                System.out.println("Redirecting to: /staff/dashboard");
                                response.sendRedirect("/staff/dashboard");
                            } else {
                                System.out.println("Redirecting to: /");
                                response.sendRedirect("/");
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=success")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403"));

        return http.build();
    }
}
