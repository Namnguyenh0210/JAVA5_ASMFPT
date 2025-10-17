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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * GIO HANG SERVICE - Quản lý giỏ hàng người dùng
 * <p>
 * =============================
 * PHÂN CÔNG: TV2 - FRONTEND KHÁCH HÀNG
 * =============================
 * TODO TV2 - CẦN LÀM (8 METHODS):
 *  1. getGioHangByTaiKhoan(TaiKhoan tk) - Lấy tất cả items trong giỏ
 *     → return gioHangRepository.findByTaiKhoan(tk);
 * <p>
 * 2. themSanPham(TaiKhoan tk, SanPham sp, int soLuong) - Thêm sản phẩm vào giỏ
 * → Kiểm tra sản phẩm đã có chưa (composite key: MaTK + MaSP)
 * → Nếu có: cộng dồn số lượng
 * → Nếu chưa: tạo mới GioHang
 * <p>
 * 3. capNhatSoLuong(TaiKhoan tk, Integer maSP, int soLuong) - Cập nhật số lượng
 * → Nếu soLuong <= 0: gọi xoaSanPham()
 * → Nếu > 0: cập nhật số lượng
 * <p>
 * 4. xoaSanPham(TaiKhoan tk, Integer maSP) - Xóa sản phẩm khỏi giỏ
 * → Xóa theo composite key (MaTK, MaSP)
 * <p>
 * 5. countItems(TaiKhoan tk) - Đếm số items trong giỏ
 * → return gioHangRepository.countByTaiKhoan(tk);
 * <p>
 * 6. tinhTongTien(List<GioHang> items) - Tính tổng tiền
 * → Loop items: sum += item.getSanPham().getGia() * item.getSoLuong()
 * <p>
 * 7. tinhTongTienByTaiKhoan(TaiKhoan tk) - Tính tổng tiền theo user
 * → Gọi getGioHangByTaiKhoan() rồi tinhTongTien()
 * <p>
 * 8. clearGioHang(TaiKhoan tk) - Xóa toàn bộ giỏ (sau khi đặt hàng)
 * → gioHangRepository.deleteByTaiKhoan(tk);
 * <p>
 * THỜI GIAN: 2 ngày
 * LƯU Ý: Composite key (MaTK + MaSP) dùng GioHangId.java đã có sẵn
 * =============================
 */
@Service
public class GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;

    // =============================
    // TODO TV2: Method 1 - Lấy giỏ hàng theo user
    // HƯỚNG DẪN: Tạo query trong Repository:
    // List<GioHang> findByTaiKhoan(TaiKhoan tk);
    public List<GioHang> getGioHangByTaiKhoan(TaiKhoan tk) {
        return gioHangRepository.findByTaiKhoan(tk);
    }

    // =============================
    // TODO TV2: Method 2 - Thêm sản phẩm vào giỏ
    // HƯỚNG DẪN:
    // 1. Tạo GioHangId id = new GioHangId(tk.getMaTK(), sp.getMaSP());
    // 2. Optional<GioHang> existing = gioHangRepository.findById(id);
    // 3. if (existing.isPresent()) {
    //      existing.get().setSoLuong(existing.get().getSoLuong() + soLuong);
    //      gioHangRepository.save(existing.get());
    //    } else {
    //      GioHang newItem = new GioHang();
    //      newItem.setId(id); // hoặc setTaiKhoan/setSanPham tùy entity design
    //      newItem.setSoLuong(soLuong);
    //      gioHangRepository.save(newItem);
    //    }
    @Transactional
    public void themSanPham(TaiKhoan tk, SanPham sp, int soLuong) {
        Optional<GioHang> existing = gioHangRepository.findByTaiKhoanAndSanPham(tk, sp);

        if (existing.isPresent()) {
            // Sản phẩm đã có trong giỏ -> cộng dồn số lượng
            GioHang gioHang = existing.get();
            gioHang.setSoLuong(gioHang.getSoLuong() + soLuong);
            gioHangRepository.save(gioHang);
        } else {
            // Sản phẩm chưa có -> tạo mới
            GioHang newItem = new GioHang(tk, sp, soLuong);
            gioHangRepository.save(newItem);
        }
    }

    // =============================
    // TODO TV2: Method 3 - Cập nhật số lượng
    // HƯỚNG DẪN:
    // if (soLuong <= 0) { xoaSanPham(tk, maSP); return; }
    // GioHangId id = new GioHangId(tk.getMaTK(), maSP);
    // Optional<GioHang> item = gioHangRepository.findById(id);
    // if (item.isPresent()) {
    //     item.get().setSoLuong(soLuong);
    //     gioHangRepository.save(item.get());
    // }
    @Transactional
    public void capNhatSoLuong(TaiKhoan tk, Integer maSP, int soLuong) {
        if (soLuong <= 0) {
            xoaSanPham(tk, maSP);
            return;
        }

        Optional<GioHang> existing = gioHangRepository.findByTaiKhoanAndSanPham(tk,
                new SanPham() {{ setMaSP(maSP); }});

        if (existing.isPresent()) {
            GioHang gioHang = existing.get();
            gioHang.setSoLuong(soLuong);
            gioHangRepository.save(gioHang);
        }
    }

    // =============================
    // TODO TV2: Method 4 - Xóa sản phẩm khỏi giỏ
    // HƯỚNG DẪN:
    // GioHangId id = new GioHangId(tk.getMaTK(), maSP);
    // gioHangRepository.deleteById(id);
    @Transactional
    public void xoaSanPham(TaiKhoan tk, Integer maSP) {
        GioHangId id = new GioHangId(tk.getMaTK(), maSP);
        gioHangRepository.deleteById(id);
    }

    // =============================
    // TODO TV2: Method 5 - Đếm số items trong giỏ
    // HƯỚNG DẪN:
    // Option 1: return gioHangRepository.countByTaiKhoan(tk);
    // Option 2: return getGioHangByTaiKhoan(tk).size();
    public int countItems(TaiKhoan tk) {
        return (int) gioHangRepository.countByTaiKhoan(tk);
    }

    // =============================
    // TODO TV2: Method 6 - Tính tổng tiền từ list items
    // HƯỚNG DẪN:
    // BigDecimal total = BigDecimal.ZERO;
    // for (GioHang item : items) {
    //     total = total.add(item.getSanPham().getGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
    // }
    // return total;
    public BigDecimal tinhTongTien(List<GioHang> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (GioHang item : items) {
            BigDecimal itemTotal = item.getSanPham().getGia()
                    .multiply(BigDecimal.valueOf(item.getSoLuong()));
            total = total.add(itemTotal);
        }
        return total;
    }

    // =============================
    // TODO TV2: Method 7 - Tính tổng tiền theo user
    // HƯỚNG DẪN:
    // List<GioHang> items = getGioHangByTaiKhoan(tk);
    // return tinhTongTien(items);
    public BigDecimal tinhTongTienByTaiKhoan(TaiKhoan tk) {
        List<GioHang> items = getGioHangByTaiKhoan(tk);
        return tinhTongTien(items);
    }

    // =============================
    // TODO TV2: Method 8 - Xóa toàn bộ giỏ hàng (sau khi đặt hàng)
    // HƯỚNG DẪN:
    // Tạo query trong Repository: void deleteByTaiKhoan(TaiKhoan tk);
    // Hoặc: gioHangRepository.deleteAll(getGioHangByTaiKhoan(tk));
    @Transactional
    public void clearGioHang(TaiKhoan tk) {
        gioHangRepository.deleteByTaiKhoan(tk);
    }
}
