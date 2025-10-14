package com.example.projectend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SECURITY CONFIG - ASM WEB BÁN HÀNG TẾT
 * ĐĂNG NHẬP BẰNG EMAIL + MẬT KHẨU PLAIN TEXT
 * <p>
 * PHÂN QUYỀN:
 * - ROLE_KHÁCHHÀNG: Khách hàng thông thường
 * - ROLE_NHÂNVIÊN: Nhân viên (có quyền admin)
 * - ROLE_ADMIN: Quản trị viên cao nhất
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public pages - ai cũng vào được
                        .requestMatchers("/", "/home", "/sanpham", "/sanpham/**").permitAll()
                        .requestMatchers("/gioithieu", "/kienthuc", "/lienhe").permitAll()
                        .requestMatchers("/login", "/register", "/403").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()

                        // Giỏ hàng - ai cũng vào được (kể cả chưa đăng nhập)
                        .requestMatchers("/giohang", "/giohang/**").permitAll()

                        // Admin pages - Chỉ ADMIN hoặc NHÂN VIÊN (không dấu cách)
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "NHÂNVIÊN")

                        // Checkout & Profile - Phải đăng nhập
                        .requestMatchers("/checkout", "/checkout/**").authenticated()
                        .requestMatchers("/profile", "/profile/**").authenticated()

                        // Còn lại phải đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // Chuyển hướng theo role
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
