package com.example.projectend.service;

import com.example.projectend.entity.*;
import com.example.projectend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * DON HANG SERVICE - Xử lý đơn hàng
 * <p>
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND (QUAN TRỌNG!)
 * =============================
 * TODO TV3 - CẦN LÀM (10 METHODS):
 * <p>
 * ⚠️ PRIORITY #1 (TV2 đang chờ):
 * 1. createDonHang(TaiKhoan, diaChiId, phuongThucId, items, ghiChu) - Tạo đơn hàng
 * → Validate địa chỉ, PTTT, tồn kho
 * → Tính tổng tiền từ items
 * → Tạo DonHang với trạng thái "Chờ xác nhận"
 * → Lưu DonHang
 * → Tạo DonHangChiTiet cho từng item
 * → Trigger tự động giảm tồn kho
 * → Return DonHang đã tạo
 * <p>
 * KHÁCH HÀNG (2-5):
 * 2. getChiTietDonHang(DonHang) - Lấy chi tiết sản phẩm trong đơn
 * 3. findByIdAndKhachHang(id, TaiKhoan) - Lấy đơn của khách (security)
 * 4. getDonHangByKhachHang(TaiKhoan, Pageable) - Lịch sử đơn hàng
 * 5. cancelOrder(donHangId, TaiKhoan) - Hủy đơn (chỉ khi Chờ xác nhận)
 * <p>
 * ADMIN (6-8):
 * 6. searchAdmin(keyword, trangThai, Pageable) - Tìm kiếm đơn hàng admin
 * 7. updateTrangThai(donHangId, trangThaiMoi) - Cập nhật trạng thái
 * 8. getPendingOrders(limit) - Đơn chờ xác nhận (dashboard)
 * <p>
 * PHÍ SHIP (9-10):
 * 9. tinhPhiShip(tongTien) - Tính phí ship (miễn phí > 300k)
 * 10. tinhPhiShipByDiaChi(DiaChi) - Phí ship theo tỉnh (optional)
 * <p>
 * THỜI GIAN: 4 ngày
 * LƯU Ý: Method 1 là QUAN TRỌNG NHẤT! TV2 cần để làm checkout
 * =============================
 * <p>
 * NOTE CHO TV4 (THỐNG KÊ):
 * TV4 sẽ thêm 4 methods thống kê vào file này:
 * - tinhDoanhThu(startDate, endDate)
 * - thongKeTheoThang(soThang)
 * - topSanPhamBanChay(limit)
 * - topKhachHangVIP(limit)
 * =============================
 */
@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;
    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;
    @Autowired
    private DiaChiRepository diaChiRepository;
    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    @Autowired
    private TrangThaiDonHangRepository trangThaiDonHangRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    // =============================
    // TODO TV3: Method 1 - Tạo đơn hàng ⚠️ PRIORITY!
    // HƯỚNG DẪN CHI TIẾT:
    // 1. Validate:
    //    DiaChi dc = diaChiRepository.findById(diaChiId).orElseThrow();
    //    PhuongThucThanhToan pttt = phuongThucThanhToanRepository.findById(phuongThucId).orElseThrow();
    //    TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTrangThai("Chờ xác nhận").orElseThrow();
    //
    // 2. Kiểm tra tồn kho:
    //    for (GioHang item : items) {
    //        if (item.getSanPham().getSoLuong() < item.getSoLuong()) {
    //            throw new RuntimeException("Sản phẩm " + item.getSanPham().getTenSP() + " không đủ số lượng");
    //        }
    //    }
    //
    // 3. Tính tổng tiền:
    //    BigDecimal tongTien = BigDecimal.ZERO;
    //    for (GioHang item : items) {
    //        tongTien = tongTien.add(item.getSanPham().getGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
    //    }
    //    BigDecimal phiShip = tinhPhiShip(tongTien);
    //    tongTien = tongTien.add(phiShip);
    //
    // 4. Tạo DonHang:
    //    DonHang dh = new DonHang();
    //    dh.setTaiKhoan(khachHang);
    //    dh.setDiaChi(dc);
    //    dh.setPhuongThucThanhToan(pttt);
    //    dh.setTrangThaiDonHang(ttdh);
    //    dh.setTongTien(tongTien);
    //    dh.setPhiShip(phiShip);
    //    dh.setGhiChu(ghiChu);
    //    dh.setNgayDatHang(LocalDateTime.now());
    //    dh = donHangRepository.save(dh);
    //
    // 5. Tạo DonHangChiTiet:
    //    for (GioHang item : items) {
    //        DonHangChiTiet dhct = new DonHangChiTiet();
    //        dhct.setDonHang(dh);
    //        dhct.setSanPham(item.getSanPham());
    //        dhct.setSoLuong(item.getSoLuong());
    //        dhct.setGia(item.getSanPham().getGia());
    //        donHangChiTietRepository.save(dhct);
    //        // Trigger tự động giảm tồn kho
    //    }
    //
    // 6. return dh;
    public DonHang createDonHang(TaiKhoan khachHang, Integer diaChiId, Integer phuongThucId, List<GioHang> items, String ghiChu) {
        return null; // TODO TV3: Implement - TV2 đang chờ method này!
    }

    // =============================
    // TODO TV3: Method 2 - Lấy chi tiết đơn hàng
    // HƯỚNG DẪN: return donHangChiTietRepository.findByDonHang(donHang);
    public List<DonHangChiTiet> getChiTietDonHang(DonHang donHang) {
        return Collections.emptyList(); // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 3 - Lấy đơn theo ID và khách hàng (security)
    // HƯỚNG DẪN: return donHangRepository.findByMaDHAndTaiKhoan(id, khachHang);
    public Optional<DonHang> findByIdAndKhachHang(Integer id, TaiKhoan khachHang) {
        return Optional.empty(); // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 4 - Lịch sử đơn hàng của khách
    // HƯỚNG DẪN: return donHangRepository.findByTaiKhoanOrderByNgayDatHangDesc(tk, pageable);
    public Page<DonHang> getDonHangByKhachHang(TaiKhoan tk, Pageable pageable) {
        return Page.empty(); // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 5 - Hủy đơn hàng (chỉ khi Chờ xác nhận)
    // HƯỚNG DẪN:
    // 1. Optional<DonHang> dhOpt = findByIdAndKhachHang(donHangId, khachHang);
    // 2. if (!dhOpt.isPresent()) return false;
    // 3. DonHang dh = dhOpt.get();
    // 4. if (!"Chờ xác nhận".equals(dh.getTrangThaiDonHang().getTenTrangThai())) return false;
    // 5. TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTrangThai("Đã hủy").orElse(null);
    // 6. dh.setTrangThaiDonHang(ttdh);
    // 7. donHangRepository.save(dh);
    // 8. return true;
    public boolean cancelOrder(Integer donHangId, TaiKhoan khachHang) {
        return false; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 6 - Tìm kiếm đơn hàng admin
    // HƯỚNG DẪN:
    // Specification<DonHang> spec = ...
    // if (keyword != null) spec = spec.and(maDH LIKE or hoTen khách LIKE)
    // if (trangThai != null) spec = spec.and(trangThai = trangThai)
    // return donHangRepository.findAll(spec, pageable);
    public Page<DonHang> searchAdmin(String keyword, String trangThai, Pageable pageable) {
        return Page.empty(); // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 7 - Cập nhật trạng thái đơn hàng
    // HƯỚNG DẪN:
    // 1. Optional<DonHang> dhOpt = donHangRepository.findById(donHangId);
    // 2. if (!dhOpt.isPresent()) return false;
    // 3. TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTrangThai(trangThaiMoi).orElse(null);
    // 4. if (ttdh == null) return false;
    // 5. dhOpt.get().setTrangThaiDonHang(ttdh);
    // 6. donHangRepository.save(dhOpt.get());
    // 7. return true;
    public boolean updateTrangThai(Integer donHangId, String trangThaiMoi) {
        return false; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 8 - Lấy đơn chờ xác nhận (dashboard)
    // HƯỚNG DẪN:
    // TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTrangThai("Chờ xác nhận").orElse(null);
    // return donHangRepository.findTopXByTrangThaiDonHangOrderByNgayDatHangDesc(ttdh, PageRequest.of(0, limit)).getContent();
    public List<DonHang> getPendingOrders(int limit) {
        return Collections.emptyList(); // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 9 - Tính phí ship đơn giản
    // HƯỚNG DẪN:
    // if (tongTien.compareTo(new BigDecimal("300000")) >= 0) {
    //     return BigDecimal.ZERO; // Miễn phí ship
    // }
    // return new BigDecimal("30000"); // Phí ship 30k
    public BigDecimal tinhPhiShip(BigDecimal tongTien) {
        return BigDecimal.ZERO; // TODO TV3: Implement
    }

    // =============================
    // TODO TV3: Method 10 - Tính phí ship theo địa chỉ (optional)
    // HƯỚNG DẪN:
    // if (diaChi.getTinhTP().contains("Hà Nội") || diaChi.getTinhTP().contains("TP.HCM")) {
    //     return new BigDecimal("30000");
    // }
    // return new BigDecimal("50000");
    public BigDecimal tinhPhiShipByDiaChi(DiaChi diaChi) {
        return BigDecimal.ZERO; // TODO TV3: Implement (optional)
    }

    // =============================
    // METHODS CƠ BẢN (đã có sẵn)
    public Optional<DonHang> findById(Integer id) {
        return donHangRepository.findById(id);
    }

    public long countAll() {
        return donHangRepository.count();
    }

    // =============================
    // TODO TV4: Thêm 4 methods thống kê vào đây (sau khi TV3 xong):
    // public BigDecimal tinhDoanhThu(LocalDateTime start, LocalDateTime end) { ... }
    // public Map<String, BigDecimal> thongKeTheoThang(int soThang) { ... }
    // public List<Map<String, Object>> topSanPhamBanChay(int limit) { ... }
    // public List<Map<String, Object>> topKhachHangVIP(int limit) { ... }
}
