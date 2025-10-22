package com.example.projectend.service;

import com.example.projectend.entity.*;
import com.example.projectend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * DON HANG SERVICE - Xử lý đơn hàng
 * =============================
 * PHÂN CÔNG: TV3 - ADMIN BACKEND (QUAN TRỌNG!)
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
    @Transactional
    public DonHang createDonHang(TaiKhoan khachHang, Integer diaChiId, Integer phuongThucId, List<GioHang> items, String ghiChu) {
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không được trống.");
        }

        // 1. Validate Entities
        DiaChi dc = diaChiRepository.findById(diaChiId)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không hợp lệ."));
        PhuongThucThanhToan pttt = phuongThucThanhToanRepository.findById(phuongThucId)
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ."));
        TrangThaiDonHang ttdhChoXacNhan = trangThaiDonHangRepository.findByTenTTDH(
                "Chờ xác nhận").orElseThrow(() -> new RuntimeException("Trạng thái đơn hàng 'Chờ xác nhận' không tìm thấy."));


        // 2. Kiểm tra tồn kho VÀ Tính tổng tiền ban đầu
        BigDecimal tongTienHang = BigDecimal.ZERO;
        for (GioHang item : items) {
            SanPham sp = sanPhamRepository.findById(item.getSanPham().getMaSP())
                            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + item.getSanPham().getMaSP()));

            if (sp.getSoLuong() < item.getSoLuong()) {
                throw new RuntimeException("Sản phẩm " + sp.getTenSP() + " không đủ số lượng tồn kho (" + sp.getSoLuong() + ").");
            }
            
            // Tính tổng tiền hàng (chưa có phí ship)
            tongTienHang = tongTienHang.add(sp.getGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
        }

        // 3. Tính phí ship và Tổng tiền cuối cùng
        BigDecimal phiShip = tinhPhiShip(tongTienHang);
        BigDecimal tongTienCuoi = tongTienHang.add(phiShip);

        // 4. Tạo DonHang (Sử dụng Entity objects)
        DonHang dh = new DonHang();
        dh.setKhachHang(khachHang);
        dh.setDiaChiGiaoHang(dc);
        dh.setPhuongThucThanhToan(pttt);
        dh.setTrangThaiDonHang(ttdhChoXacNhan); // Set đối tượng Entity
        dh.setTongTien(tongTienCuoi);
        dh.setNgayDat(LocalDateTime.now());
        // dh.setGhiChu(ghiChu); // Giả sử Entity DonHang có trường ghi chú
        dh = donHangRepository.save(dh);

        // 5. Tạo DonHangChiTiet (Sử dụng Entity objects)
        for (GioHang item : items) {
            SanPham sp = sanPhamRepository.findById(item.getSanPham().getMaSP()).get();
            DonHangChiTiet dhct = new DonHangChiTiet();
            
            dhct.setDonHang(dh); // Set đối tượng DonHang
            dhct.setSanPham(sp); // Set đối tượng SanPham
            
            dhct.setSoLuong(item.getSoLuong());
            dhct.setDonGia(sp.getGia());
            donHangChiTietRepository.save(dhct);
        }

        return dh;
    }

    // =============================
    // TODO TV3: Method 2 - Lấy chi tiết đơn hàng
    public List<DonHangChiTiet> getChiTietDonHang(DonHang donHang) {
        // Giả định DonHangChiTietRepository có findByDonHang(DonHang donHang)
        return donHangChiTietRepository.findByDonHang(donHang);
    }

    // =============================
    // TODO TV3: Method 3 - Lấy đơn theo ID và khách hàng (security)
    public Optional<DonHang> findByIdAndKhachHang(Integer id, TaiKhoan khachHang) {
        // Sử dụng MaDH và MaKH (ID nguyên thủy)
        return donHangRepository.findByMaDHAndKhachHang_MaTK(id, khachHang.getMaTK());
    }

    // =============================
    // TODO TV3: Method 4 - Lịch sử đơn hàng của khách
    public Page<DonHang> getDonHangByKhachHang(TaiKhoan tk, Pageable pageable) {
        // Sử dụng MaKH (ID nguyên thủy)
    	return donHangRepository.findByKhachHang_MaTKOrderByNgayDatDesc(tk.getMaTK(), pageable);
    }

    // =============================
    // TODO TV3: Method 5 - Hủy đơn hàng (chỉ khi Chờ xác nhận)
    @Transactional
    public boolean cancelOrder(Integer donHangId, TaiKhoan khachHang) {
        Optional<DonHang> dhOpt = findByIdAndKhachHang(donHangId, khachHang);

        if (!dhOpt.isPresent()) {
            return false;
        }

        DonHang dh = dhOpt.get();
        
        // Kiểm tra trạng thái hiện tại
        Optional<TrangThaiDonHang> ttdhCurrentOpt = Optional.ofNullable(dh.getTrangThaiDonHang());
        if (!ttdhCurrentOpt.isPresent() || !"Chờ xác nhận".equals(ttdhCurrentOpt.get().getTenTTDH())) {
            return false;
        }

        // Cập nhật trạng thái thành "Đã hủy"
        TrangThaiDonHang ttdhHuy = trangThaiDonHangRepository.findByTenTTDH("Đã hủy").orElse(null);
        if (ttdhHuy == null) {
             throw new RuntimeException("Không tìm thấy trạng thái 'Đã hủy'.");
        }
        
        dh.setTrangThaiDonHang(ttdhHuy); // Set đối tượng Entity
        donHangRepository.save(dh);
        
        return true;
    }

    // =============================
    // TODO TV3: Method 6 - Tìm kiếm đơn hàng admin
    public Page<DonHang> searchAdmin(String keyword, String trangThai, Pageable pageable) {
        Specification<DonHang> spec = Specification.where(null);

        // 1. Lọc theo trạng thái
        if (trangThai != null && !trangThai.isEmpty()) {
            Optional<TrangThaiDonHang> ttdhOpt = trangThaiDonHangRepository.findByTenTTDH(trangThai.trim());
            if (ttdhOpt.isPresent()) {
                final int maTTDH = ttdhOpt.get().getMaTTDH();
                spec = spec.and((root, query, cb) -> cb.equal(root.get("trangThaiDonHang").get("maTTDH"), maTTDH));
            }
        }
        
        // 2. Tìm kiếm theo keyword (Mã DH)
        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                int maDH = Integer.parseInt(keyword.trim());
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("maDH"), maDH));
            } catch (NumberFormatException e) {
                // Bỏ qua tìm kiếm theo tên khách hàng để giữ đơn giản
            }
        }

        return donHangRepository.findAll(spec, pageable);
    }

    // =============================
    // TODO TV3: Method 7 - Cập nhật trạng thái đơn hàng
    public boolean updateTrangThai(Integer donHangId, String trangThaiMoi) {
        Optional<DonHang> dhOpt = donHangRepository.findById(donHangId);
        
        if (!dhOpt.isPresent()) {
            return false;
        }

        TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTTDH(trangThaiMoi).orElse(null);
        if (ttdh == null) {
            return false;
        }
        
        DonHang dh = dhOpt.get();
        dh.setTrangThaiDonHang(ttdh); // Set đối tượng Entity
        donHangRepository.save(dh);
        
        return true;
    }

    // =============================
    // TODO TV3: Method 7b - Cập nhật trạng thái và nhân viên
    @Transactional
    public boolean updateTrangThaiWithStaff(Integer donHangId, String trangThaiMoi, TaiKhoan nhanVien) {
        Optional<DonHang> dhOpt = donHangRepository.findById(donHangId);

        if (!dhOpt.isPresent()) {
            return false;
        }

        TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTTDH(trangThaiMoi).orElse(null);
        if (ttdh == null) {
            return false;
        }

        DonHang dh = dhOpt.get();
        dh.setTrangThaiDonHang(ttdh); // Set đối tượng Entity

        // Cập nhật nhân viên nếu có
        if (nhanVien != null) {
            dh.setNhanVien(nhanVien);
        }

        donHangRepository.save(dh);

        return true;
    }

    // =============================
    // TODO TV3: Method 8 - Lấy đơn chờ xác nhận (dashboard)
    public List<DonHang> getPendingOrders(int limit) {
        TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTTDH("Chờ xác nhận").orElse(null);
        if (ttdh == null) {
            return Collections.emptyList();
        }
        
        // Cần lấy MaTTDH từ Entity để tìm kiếm trong Repository
        return donHangRepository.findByTrangThaiDonHang_MaTTDHOrderByNgayDatDesc(ttdh.getMaTTDH(), PageRequest.of(0, limit)).getContent();
    }

    // =============================
    // TODO TV3: Method 9 - Tính phí ship đơn giản
    public BigDecimal tinhPhiShip(BigDecimal tongTien) {
        BigDecimal MUC_MIEN_PHI = new BigDecimal("300000");
        BigDecimal PHI_SHIP_CO_BAN = new BigDecimal("30000");

        if (tongTien.compareTo(MUC_MIEN_PHI) >= 0) {
            return BigDecimal.ZERO;
        }
        return PHI_SHIP_CO_BAN;
    }

    // =============================
    // TODO TV3: Method 10 - Tính phí ship theo địa chỉ (optional)
    public BigDecimal tinhPhiShipByDiaChi(DiaChi diaChi) {
        BigDecimal PHI_SHIP_HN_HCM = new BigDecimal("30000");
        BigDecimal PHI_SHIP_TINH_KHAC = new BigDecimal("50000");
        
        String chiTiet = diaChi.getDiaChiChiTiet().toUpperCase();

        if (chiTiet.contains("HÀ NỘI") || chiTiet.contains("TP.HCM") || chiTiet.contains("HỒ CHÍ MINH")) {
            return PHI_SHIP_HN_HCM;
        }
        return PHI_SHIP_TINH_KHAC;
    }

    // =============================
    // METHODS CƠ BẢN (đã có sẵn)
    public Optional<DonHang> findById(Integer id) {
        return donHangRepository.findById(id);
    }

    public long countAll() {
        return donHangRepository.count();
    }

    // Lấy đơn hàng gần đây
    public List<DonHang> getRecentOrders(int limit) {
        return donHangRepository.findAll(
            PageRequest.of(0, limit, org.springframework.data.domain.Sort.by("ngayDat").descending())
        ).getContent();
    }

    // =============================
    // TODO TV4: Thêm 4 methods thống kê vào đây (sau khi TV3 xong):
    // ...
}