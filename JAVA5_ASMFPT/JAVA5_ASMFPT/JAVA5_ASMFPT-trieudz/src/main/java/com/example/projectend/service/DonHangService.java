package com.example.projectend.service;

import com.example.projectend.entity.DonHang;
import com.example.projectend.repository.DonHangChiTietRepository;
import com.example.projectend.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DonHangChiTietRepository donHangChiTietRepository;

    // ==========================
    // ✅ 1. Tính doanh thu trong khoảng ngày
    public BigDecimal tinhDoanhThu(LocalDateTime start, LocalDateTime end) {
        List<DonHang> donHangs = donHangRepository.findAll();
        return donHangs.stream()
                .filter(dh -> dh.getNgayDat() != null
                        && !dh.getNgayDat().isBefore(start)
                        && !dh.getNgayDat().isAfter(end))
                .map(DonHang::getTongTien)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==========================
    // ✅ 2. Đếm tổng đơn hàng
    public Long countAll() {
        return donHangRepository.count();
    }

    // ==========================
    // ✅ 3. Thống kê doanh thu theo tháng (mặc định 6 tháng gần nhất)
    public Map<String, BigDecimal> thongKeTheoThang(int soThang) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, BigDecimal> result = new LinkedHashMap<>();

        for (int i = soThang - 1; i >= 0; i--) {
            LocalDateTime start = now.minusMonths(i).withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime end = start.plusMonths(1).minusSeconds(1);
            BigDecimal doanhThu = tinhDoanhThu(start, end);

            String label = start.getMonth().getDisplayName(TextStyle.SHORT, new Locale("vi", "VN"))
                    + " " + start.getYear();
            result.put(label, doanhThu);
        }

        return result;
    }

    // ==========================
    // ✅ 4. Lấy danh sách sản phẩm bán chạy nhất
    public List<Map<String, Object>> topSanPhamBanChay(int limit) {
        List<Object[]> data = donHangChiTietRepository.getTopSellingProducts();

        return data.stream()
                .limit(limit)
                .map(obj -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tenSP", obj[0]);
                    map.put("soLuong", obj[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    // ==========================
    // ✅ 5. Top khách hàng VIP (tổng chi tiêu cao nhất)
    public List<Map<String, Object>> topKhachHangVIP(int limit) {
        List<DonHang> donHangs = donHangRepository.findAll();

        Map<String, BigDecimal> tongChiTieu = new HashMap<>();
        for (DonHang dh : donHangs) {
            if (dh.getKhachHang() != null && dh.getTongTien() != null) {
                String ten = dh.getKhachHang().getHoTen(); // ✅ sửa chỗ này
                tongChiTieu.merge(ten, dh.getTongTien(), BigDecimal::add);
            }
        }

        return tongChiTieu.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("tenKhachHang", e.getKey());
                    map.put("tongChiTieu", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // ==========================
    // ✅ 6. Tìm đơn hàng theo khoảng ngày
    public List<DonHang> findByDateRange(LocalDateTime start, LocalDateTime end) {
        List<DonHang> donHangs = donHangRepository.findAll();
        return donHangs.stream()
                .filter(dh -> dh.getNgayDat() != null
                        && !dh.getNgayDat().isBefore(start)
                        && !dh.getNgayDat().isAfter(end))
                .collect(Collectors.toList());
    }
}
