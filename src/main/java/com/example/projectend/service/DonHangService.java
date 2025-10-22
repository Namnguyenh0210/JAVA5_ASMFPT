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
 * Service xử lý logic nghiệp vụ cho đơn hàng
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

    /**
     * Tạo đơn hàng mới từ giỏ hàng
     */
    @Transactional
    public DonHang createDonHang(TaiKhoan khachHang, Integer diaChiId, Integer phuongThucId, List<GioHang> items, String ghiChu) {
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Giỏ hàng không được trống.");
        }

        // Validate các entity cần thiết
        DiaChi dc = diaChiRepository.findById(diaChiId)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không hợp lệ."));
        PhuongThucThanhToan pttt = phuongThucThanhToanRepository.findById(phuongThucId)
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ."));
        TrangThaiDonHang ttdhChoXacNhan = trangThaiDonHangRepository.findByTenTTDH("Chờ xác nhận")
                .orElseThrow(() -> new RuntimeException("Trạng thái đơn hàng 'Chờ xác nhận' không tìm thấy."));

        // Kiểm tra tồn kho và tính tổng tiền
        BigDecimal tongTienHang = BigDecimal.ZERO;
        for (GioHang item : items) {
            SanPham sp = sanPhamRepository.findById(item.getSanPham().getMaSP())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + item.getSanPham().getMaSP()));

            if (sp.getSoLuong() < item.getSoLuong()) {
                throw new RuntimeException("Sản phẩm " + sp.getTenSP() + " không đủ số lượng tồn kho (" + sp.getSoLuong() + ").");
            }

            tongTienHang = tongTienHang.add(sp.getGia().multiply(BigDecimal.valueOf(item.getSoLuong())));
        }

        // Tính phí ship và tổng tiền cuối
        BigDecimal phiShip = tinhPhiShip(tongTienHang);
        BigDecimal tongTienCuoi = tongTienHang.add(phiShip);

        // Tạo đơn hàng
        DonHang dh = new DonHang();
        dh.setKhachHang(khachHang);
        dh.setDiaChiGiaoHang(dc);
        dh.setPhuongThucThanhToan(pttt);
        dh.setTrangThaiDonHang(ttdhChoXacNhan);
        dh.setTongTien(tongTienCuoi);
        dh.setNgayDat(LocalDateTime.now());
        dh = donHangRepository.save(dh);

        // Tạo chi tiết đơn hàng
        for (GioHang item : items) {
            SanPham sp = sanPhamRepository.findById(item.getSanPham().getMaSP()).get();
            DonHangChiTiet dhct = new DonHangChiTiet();
            dhct.setDonHang(dh);
            dhct.setSanPham(sp);
            dhct.setSoLuong(item.getSoLuong());
            dhct.setDonGia(sp.getGia());
            donHangChiTietRepository.save(dhct);
        }

        return dh;
    }

    /**
     * Lấy chi tiết đơn hàng
     */
    public List<DonHangChiTiet> getChiTietDonHang(DonHang donHang) {
        return donHangChiTietRepository.findByDonHang(donHang);
    }

    /**
     * Tìm đơn hàng theo ID và khách hàng (bảo mật)
     */
    public Optional<DonHang> findByIdAndKhachHang(Integer id, TaiKhoan khachHang) {
        return donHangRepository.findByMaDHAndKhachHang_MaTK(id, khachHang.getMaTK());
    }

    /**
     * Lấy lịch sử đơn hàng của khách hàng
     */
    public Page<DonHang> getDonHangByKhachHang(TaiKhoan tk, Pageable pageable) {
        return donHangRepository.findByKhachHang_MaTKOrderByNgayDatDesc(tk.getMaTK(), pageable);
    }

    /**
     * Hủy đơn hàng (chỉ khi đang chờ xác nhận)
     */
    @Transactional
    public boolean cancelOrder(Integer donHangId, TaiKhoan khachHang) {
        Optional<DonHang> dhOpt = findByIdAndKhachHang(donHangId, khachHang);

        if (!dhOpt.isPresent()) {
            return false;
        }

        DonHang dh = dhOpt.get();
        Optional<TrangThaiDonHang> ttdhCurrentOpt = Optional.ofNullable(dh.getTrangThaiDonHang());
        if (!ttdhCurrentOpt.isPresent() || !"Chờ xác nhận".equals(ttdhCurrentOpt.get().getTenTTDH())) {
            return false;
        }

        TrangThaiDonHang ttdhHuy = trangThaiDonHangRepository.findByTenTTDH("Đã hủy").orElse(null);
        if (ttdhHuy == null) {
            throw new RuntimeException("Không tìm thấy trạng thái 'Đã hủy'.");
        }

        dh.setTrangThaiDonHang(ttdhHuy);
        donHangRepository.save(dh);

        return true;
    }

    /**
     * Tìm kiếm đơn hàng cho admin
     */
    public Page<DonHang> searchAdmin(String keyword, String trangThai, Pageable pageable) {
        Specification<DonHang> spec = Specification.where(null);

        // Lọc theo trạng thái
        if (trangThai != null && !trangThai.isEmpty()) {
            Optional<TrangThaiDonHang> ttdhOpt = trangThaiDonHangRepository.findByTenTTDH(trangThai.trim());
            if (ttdhOpt.isPresent()) {
                final int maTTDH = ttdhOpt.get().getMaTTDH();
                spec = spec.and((root, query, cb) -> cb.equal(root.get("trangThaiDonHang").get("maTTDH"), maTTDH));
            }
        }

        // Tìm kiếm theo mã đơn hàng
        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                int maDH = Integer.parseInt(keyword.trim());
                spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("maDH"), maDH));
            } catch (NumberFormatException e) {
                // Bỏ qua nếu không phải số
            }
        }

        return donHangRepository.findAll(spec, pageable);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
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
        dh.setTrangThaiDonHang(ttdh);
        donHangRepository.save(dh);

        return true;
    }

    /**
     * Cập nhật trạng thái và gán nhân viên xử lý
     */
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
        dh.setTrangThaiDonHang(ttdh);

        if (nhanVien != null) {
            dh.setNhanVien(nhanVien);
        }

        donHangRepository.save(dh);

        return true;
    }

    /**
     * Lấy danh sách đơn hàng chờ xác nhận
     */
    public List<DonHang> getPendingOrders(int limit) {
        TrangThaiDonHang ttdh = trangThaiDonHangRepository.findByTenTTDH("Chờ xác nhận").orElse(null);
        if (ttdh == null) {
            return Collections.emptyList();
        }

        return donHangRepository.findByTrangThaiDonHang_MaTTDHOrderByNgayDatDesc(ttdh.getMaTTDH(), PageRequest.of(0, limit)).getContent();
    }

    /**
     * Tính phí ship đơn giản theo tổng tiền
     */
    public BigDecimal tinhPhiShip(BigDecimal tongTien) {
        BigDecimal MUC_MIEN_PHI = new BigDecimal("300000");
        BigDecimal PHI_SHIP_CO_BAN = new BigDecimal("30000");

        if (tongTien.compareTo(MUC_MIEN_PHI) >= 0) {
            return BigDecimal.ZERO;
        }
        return PHI_SHIP_CO_BAN;
    }

    /**
     * Tính phí ship theo địa chỉ giao hàng
     */
    public BigDecimal tinhPhiShipByDiaChi(DiaChi diaChi) {
        BigDecimal PHI_SHIP_HN_HCM = new BigDecimal("30000");
        BigDecimal PHI_SHIP_TINH_KHAC = new BigDecimal("50000");

        String chiTiet = diaChi.getDiaChiChiTiet().toUpperCase();

        if (chiTiet.contains("HÀ NỘI") || chiTiet.contains("TP.HCM") || chiTiet.contains("HỒ CHÍ MINH")) {
            return PHI_SHIP_HN_HCM;
        }
        return PHI_SHIP_TINH_KHAC;
    }

    /**
     * Tìm đơn hàng theo ID
     */
    public Optional<DonHang> findById(Integer id) {
        return donHangRepository.findById(id);
    }

    /**
     * Đếm tổng số đơn hàng
     */
    public long countAll() {
        return donHangRepository.count();
    }

    /**
     * Lấy danh sách đơn hàng gần đây
     */
    public List<DonHang> getRecentOrders(int limit) {
        return donHangRepository.findAll(
                PageRequest.of(0, limit, org.springframework.data.domain.Sort.by("ngayDat").descending())
        ).getContent();
    }
}

