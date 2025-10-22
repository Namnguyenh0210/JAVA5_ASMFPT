package com.example.projectend.service;

import com.example.projectend.entity.GioHang;
import com.example.projectend.entity.GioHangId;
import com.example.projectend.entity.SanPham;
import com.example.projectend.entity.TaiKhoan;
import com.example.projectend.repository.GioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service quản lý giỏ hàng người dùng
 */
@Service
public class GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;

    /**
     * Lấy tất cả sản phẩm trong giỏ hàng của người dùng
     */
    public List<GioHang> getGioHangByTaiKhoan(TaiKhoan tk) {
        return gioHangRepository.findByTaiKhoan(tk);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @Transactional
    public void themSanPham(TaiKhoan tk, SanPham sp, int soLuong) {
        Optional<GioHang> existing = gioHangRepository.findByTaiKhoanAndSanPham(tk, sp);

        if (existing.isPresent()) {
            GioHang gioHang = existing.get();
            gioHang.setSoLuong(gioHang.getSoLuong() + soLuong);
            gioHangRepository.save(gioHang);
        } else {
            GioHang newItem = new GioHang(tk, sp, soLuong);
            gioHangRepository.save(newItem);
        }
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @Transactional
    public void capNhatSoLuong(TaiKhoan tk, Integer maSP, int soLuong) {
        if (soLuong <= 0) {
            xoaSanPham(tk, maSP);
            return;
        }

        Optional<GioHang> existing = gioHangRepository.findByTaiKhoanAndSanPham(tk,
                new SanPham() {{
                    setMaSP(maSP);
                }});

        if (existing.isPresent()) {
            GioHang gioHang = existing.get();
            gioHang.setSoLuong(soLuong);
            gioHangRepository.save(gioHang);
        }
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @Transactional
    public void xoaSanPham(TaiKhoan tk, Integer maSP) {
        GioHangId id = new GioHangId(tk.getMaTK(), maSP);
        gioHangRepository.deleteById(id);
    }

    /**
     * Đếm số lượng sản phẩm trong giỏ hàng
     */
    public int countItems(TaiKhoan tk) {
        return (int) gioHangRepository.countByTaiKhoan(tk);
    }

    /**
     * Tính tổng tiền từ danh sách giỏ hàng
     */
    public BigDecimal tinhTongTien(List<GioHang> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (GioHang item : items) {
            BigDecimal itemTotal = item.getSanPham().getGia()
                    .multiply(BigDecimal.valueOf(item.getSoLuong()));
            total = total.add(itemTotal);
        }
        return total;
    }

    /**
     * Tính tổng tiền giỏ hàng theo người dùng
     */
    public BigDecimal tinhTongTienByTaiKhoan(TaiKhoan tk) {
        List<GioHang> items = getGioHangByTaiKhoan(tk);
        return tinhTongTien(items);
    }

    /**
     * Xóa toàn bộ giỏ hàng (sau khi đặt hàng thành công)
     */
    @Transactional
    public void clearGioHang(TaiKhoan tk) {
        gioHangRepository.deleteByTaiKhoan(tk);
    }

    /**
     * Lấy các sản phẩm trong giỏ hàng của người dùng theo danh sách sản phẩm được chọn
     */
    public List<GioHang> getGioHangByTaiKhoanAndSanPhamIds(TaiKhoan tk, List<Map<String, Object>> selectedItems) {
        List<GioHang> allItems = getGioHangByTaiKhoan(tk);
        if (selectedItems == null || selectedItems.isEmpty()) {
            return allItems;
        }
        // Extract product IDs from selectedItems
        java.util.Set<Integer> selectedIds = new java.util.HashSet<>();
        for (Map<String, Object> item : selectedItems) {
            Object idObj = item.get("maSP");
            if (idObj instanceof Integer) {
                selectedIds.add((Integer) idObj);
            } else if (idObj instanceof Number) {
                selectedIds.add(((Number) idObj).intValue());
            } else if (idObj != null) {
                try {
                    selectedIds.add(Integer.parseInt(idObj.toString()));
                } catch (NumberFormatException ignored) {}
            }
        }
        // Filter cart items by selected product IDs
        List<GioHang> filtered = new java.util.ArrayList<>();
        for (GioHang gh : allItems) {
            if (gh.getSanPham() != null && selectedIds.contains(gh.getSanPham().getMaSP())) {
                filtered.add(gh);
            }
        }
        return filtered;
    }
}
