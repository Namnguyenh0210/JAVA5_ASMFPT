package com.example.projectend.entity;

import jakarta.persistence.*;

/**
 * ENTITY DIA CHI
 * PHÂN CÔNG:
 * - THÀNH VIÊN 1: Mapping (ĐÃ HOÀN THÀNH)
 * - THÀNH VIÊN 3: CRUD địa chỉ khách hàng + set mặc định trong DiaChiService
 * - THÀNH VIÊN 4: Dùng để hiển thị địa chỉ khi duyệt đơn (không chỉnh)
 * <p>
 * TODO THÀNH VIÊN 3: Đảm bảo chỉ 1 địa chỉ MacDinh=true / user (logic service)
 */
@Entity
@Table(name = "DiaChi")
public class DiaChi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDC")
    private Integer maDC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK", nullable = false)
    private TaiKhoan taiKhoan;

    @Column(name = "DiaChiChiTiet", nullable = false, length = 255)
    private String diaChiChiTiet;

    @Column(name = "MacDinh")
    private Boolean macDinh = false;

    public DiaChi() {
    }

    public DiaChi(TaiKhoan taiKhoan, String diaChiChiTiet, Boolean macDinh) {
        this.taiKhoan = taiKhoan;
        this.diaChiChiTiet = diaChiChiTiet;
        this.macDinh = macDinh;
    }

    public Integer getMaDC() {
        return maDC;
    }

    public void setMaDC(Integer maDC) {
        this.maDC = maDC;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getDiaChiChiTiet() {
        return diaChiChiTiet;
    }

    public void setDiaChiChiTiet(String diaChiChiTiet) {
        this.diaChiChiTiet = diaChiChiTiet;
    }

    public Boolean getMacDinh() {
        return macDinh;
    }

    public void setMacDinh(Boolean macDinh) {
        this.macDinh = macDinh;
    }
}
