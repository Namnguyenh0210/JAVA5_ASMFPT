package com.example.projectend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for Contact Form
 * Used in contact page to collect customer inquiries
 */
public class ContactForm {

    @NotBlank(message = "Vui lòng nhập họ tên")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    private String hoTen;

    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Size(min = 10, max = 15, message = "Số điện thoại không hợp lệ")
    private String soDienThoai;

    @NotBlank(message = "Vui lòng nhập tiêu đề")
    @Size(min = 5, max = 200, message = "Tiêu đề phải từ 5-200 ký tự")
    private String tieuDe;

    @NotBlank(message = "Vui lòng nhập nội dung")
    @Size(min = 10, max = 1000, message = "Nội dung phải từ 10-1000 ký tự")
    private String noiDung;

    // Constructors
    public ContactForm() {
    }

    public ContactForm(String hoTen, String email, String soDienThoai, String tieuDe, String noiDung) {
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
    }

    // Getters and Setters
    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    @Override
    public String toString() {
        return "ContactForm{" +
                "hoTen='" + hoTen + '\'' +
                ", email='" + email + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", tieuDe='" + tieuDe + '\'' +
                '}';
    }
}

