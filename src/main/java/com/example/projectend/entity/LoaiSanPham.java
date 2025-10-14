package com.example.projectend.entity;

import jakarta.persistence.*;

/**
 * ENTITY LOAI SAN PHAM
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: Hiển thị danh mục (menu, filter)
 * - THÀNH VIÊN 4: CRUD danh mục trong admin (nếu cần) – phải tuân thủ schema
 * <p>
 * TODO THÀNH VIÊN 3 (Optional): Cache danh mục (Spring Cache) tránh truy vấn lặp lại
 */
@Entity
@Table(name = "LoaiSanPham")
public class LoaiSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoai")
    private Integer maLoai;

    @Column(name = "TenLoai", nullable = false, unique = true, length = 100)
    private String tenLoai;

    // Constructors
    public LoaiSanPham() {
    }

    public LoaiSanPham(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    // Getters and Setters
    public Integer getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(Integer maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
}
