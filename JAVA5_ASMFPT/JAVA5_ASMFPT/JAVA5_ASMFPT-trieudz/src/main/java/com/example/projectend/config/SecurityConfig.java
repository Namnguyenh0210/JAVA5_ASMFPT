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
 * PHÂN QUYỀN:
 * - ROLE_KHÁCHHÀNG: Khách hàng
 * - ROLE_NHÂNVIÊN: Nhân viên
 * - ROLE_ADMIN: Quản trị viên
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
                        .requestMatchers("/login", "/register", "/403").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        // ==================== GIỎ HÀNG ====================
                        .requestMatchers("/giohang", "/giohang/**").permitAll()

                        // ==================== PHÂN QUYỀN ADMIN ====================
                        .requestMatchers("/admin/accounts/**", "/admin/reports/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "NHÂNVIÊN")

                        // ==================== PHẢI ĐĂNG NHẬP ====================
                        .requestMatchers("/checkout", "/checkout/**").authenticated()
                        .requestMatchers("/profile", "/profile/**").authenticated()

                        // ==================== CÒN LẠI ====================
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // Điều hướng theo vai trò sau khi đăng nhập
                            String role = authentication.getAuthorities().iterator().next().getAuthority();
                            if (role.equals("ROLE_ADMIN") || role.equals("ROLE_NHÂNVIÊN")) {
                                response.sendRedirect("/admin");
                            } else {
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
                .exceptionHandling(ex -> ex.accessDeniedPage("/403"));

        return http.build();
    }
}
