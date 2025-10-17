package com.example.projectend.controller.admin;

import com.example.projectend.entity.DonHang;
import com.example.projectend.service.DonHangService;
import com.example.projectend.service.SanPhamService;
import com.example.projectend.service.TaiKhoanService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/thongke")
@PreAuthorize("hasAnyRole('ADMIN', 'NHÂNVIÊN')")
public class AdminThongKeController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    // =============================
    // ✅ 1. Dashboard thống kê tổng hợp
    @GetMapping
    public String thongKe(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate == null) startDate = LocalDate.now().minusDays(30);
        if (endDate == null) endDate = LocalDate.now();

        // Tổng doanh thu
        BigDecimal tongDoanhThu = donHangService.tinhDoanhThu(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );
        model.addAttribute("tongDoanhThu", tongDoanhThu);

        // Tổng đơn hàng
        Long tongDonHang = donHangService.countAll();
        model.addAttribute("tongDonHang", tongDonHang);

        // Doanh thu hôm nay
        LocalDate today = LocalDate.now();
        BigDecimal doanhThuHomNay = donHangService.tinhDoanhThu(
                today.atStartOfDay(),
                today.atTime(23, 59, 59)
        );
        model.addAttribute("doanhThuHomNay", doanhThuHomNay);

        // Biểu đồ doanh thu 6 tháng
        Map<String, BigDecimal> doanhThu6Thang = donHangService.thongKeTheoThang(6);
        model.addAttribute("chartLabels", doanhThu6Thang.keySet());
        model.addAttribute("chartData", doanhThu6Thang.values());

        // Top 5 sản phẩm bán chạy
        List<Map<String, Object>> topSanPham = donHangService.topSanPhamBanChay(5);
        model.addAttribute("topSanPham", topSanPham);

        // Top 5 khách hàng VIP
        List<Map<String, Object>> topKhach = donHangService.topKhachHangVIP(5);
        model.addAttribute("topKhach", topKhach);

        model.addAttribute("currentPage", "thongke");
        model.addAttribute("pageTitle", "Thống kê & Báo cáo");
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "admin/reports";
    }

    // =============================
    // ✅ 2. Báo cáo doanh thu chi tiết
    @GetMapping("/doanhthu")
    public String baoCaoDoanhThu(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        BigDecimal doanhThu = donHangService.tinhDoanhThu(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        model.addAttribute("doanhThu", doanhThu);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "admin/thongke/doanhthu";
    }

    // =============================
    // ✅ 3. Top sản phẩm bán chạy (API JSON)
    @GetMapping("/sanpham")
    @ResponseBody
    public List<Map<String, Object>> topSanPham(@RequestParam(defaultValue = "10") int limit) {
        return donHangService.topSanPhamBanChay(limit);
    }

    // =============================
    // ✅ 4. Top khách hàng VIP (API JSON)
    @GetMapping("/khachhang")
    @ResponseBody
    public List<Map<String, Object>> topKhachHang(@RequestParam(defaultValue = "10") int limit) {
        return donHangService.topKhachHangVIP(limit);
    }

    // =============================
    // ✅ 5. Export Excel báo cáo
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {

        List<DonHang> donHangs = donHangService.findByDateRange(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59)
        );

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Báo cáo đơn hàng");

        // Header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Mã đơn", "Ngày đặt", "Khách hàng", "Tổng tiền", "Trạng thái"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Dữ liệu
        int rowNum = 1;
        for (DonHang dh : donHangs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dh.getMaDH());
            row.createCell(1).setCellValue(dh.getNgayDat().toString());
            row.createCell(2).setCellValue(dh.getKhachHang().getHoTen());
            row.createCell(3).setCellValue(dh.getTongTien().doubleValue());
            row.createCell(4).setCellValue(dh.getTrangThaiDonHang().getTenTTDH());
        }

        // Tự động giãn cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headersExcel = new HttpHeaders();
        headersExcel.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersExcel.setContentDispositionFormData("attachment",
                "bao-cao-" + startDate + "-" + endDate + ".xlsx");

        return ResponseEntity.ok()
                .headers(headersExcel)
                .body(outputStream.toByteArray());
    }
}
