package com.example.projectend.controller.admin;

import com.example.projectend.service.DonHangService;
import com.example.projectend.service.SanPhamService;
import com.example.projectend.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

/**
 * ADMIN THONG KE CONTROLLER - Thống kê & báo cáo
 * <p>
 * =============================
 * PHÂN CÔNG: TV4 - THỐNG KÊ & BÁO CÁO
 * =============================
 * TODO TV4 - CẦN LÀM (5 ENDPOINTS):
 * <p>
 * 1. GET /admin/thongke - Dashboard thống kê tổng hợp
 * → Doanh thu theo thời gian (30 ngày, 6 tháng, 1 năm)
 * → Số đơn hàng, doanh thu hôm nay
 * → Biểu đồ Chart.js (doanh thu 6 tháng)
 * → Top sản phẩm bán chạy
 * → Top khách hàng VIP
 * <p>
 * 2. GET /admin/thongke/doanhthu - Báo cáo doanh thu chi tiết
 * → Lọc theo thời gian
 * → Gọi donHangService.tinhDoanhThu(startDate, endDate)
 * <p>
 * 3. GET /admin/thongke/sanpham - Top sản phẩm bán chạy
 * → Gọi donHangService.topSanPhamBanChay(limit)
 * <p>
 * 4. GET /admin/thongke/khachhang - Top khách hàng VIP
 * → Gọi donHangService.topKhachHangVIP(limit)
 * <p>
 * 5. GET /admin/thongke/export - Export Excel báo cáo
 * → Dùng Apache POI
 * → Export danh sách đơn hàng theo thời gian
 * <p>
 * THỜI GIAN: 3 ngày
 * LƯU Ý: Cần thêm 4 methods thống kê vào DonHangService trước!
 * =============================
 */
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
    // TODO TV4: Endpoint 1 - Dashboard thống kê tổng hợp
    @GetMapping
    public String thongKe(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "month") String type,
            Model model) {

        // TODO TV4: HƯỚNG DẪN:
        //
        // 1. Mặc định 30 ngày nếu không có filter
        // if (startDate == null) startDate = LocalDate.now().minusDays(30);
        // if (endDate == null) endDate = LocalDate.now();
        //
        // 2. Tính tổng doanh thu trong khoảng thời gian
        // BigDecimal tongDoanhThu = donHangService.tinhDoanhThu(
        //     startDate.atStartOfDay(),
        //     endDate.atTime(23, 59, 59)
        // );
        // model.addAttribute("tongDoanhThu", tongDoanhThu);
        //
        // 3. Đếm số đơn hàng
        // Long tongDonHang = donHangService.countAll();
        // model.addAttribute("tongDonHang", tongDonHang);
        //
        // 4. Doanh thu hôm nay
        // LocalDate today = LocalDate.now();
        // BigDecimal doanhThuHomNay = donHangService.tinhDoanhThu(
        //     today.atStartOfDay(),
        //     today.atTime(23, 59, 59)
        // );
        // model.addAttribute("doanhThuHomNay", doanhThuHomNay);
        //
        // 5. Biểu đồ doanh thu 6 tháng (cho Chart.js)
        // Map<String, BigDecimal> doanhThu6Thang = donHangService.thongKeTheoThang(6);
        // model.addAttribute("chartLabels", doanhThu6Thang.keySet());
        // model.addAttribute("chartData", doanhThu6Thang.values());
        //
        // 6. Top 5 sản phẩm bán chạy
        // List<Map<String, Object>> topSanPham = donHangService.topSanPhamBanChay(5);
        // model.addAttribute("topSanPham", topSanPham);
        //
        // 7. Top 5 khách hàng VIP
        // List<Map<String, Object>> topKhach = donHangService.topKhachHangVIP(5);
        // model.addAttribute("topKhach", topKhach);

        model.addAttribute("currentPage", "thongke");
        model.addAttribute("pageTitle", "Thống kê & Báo cáo");
        return "admin/thongke/dashboard";
    }

    // =============================
    // TODO TV4: Endpoint 2 - Báo cáo doanh thu chi tiết
    // HƯỚNG DẪN:
    // @GetMapping("/doanhthu")
    // public String baoCaoDoanhThu(
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
    //         Model model) {
    //
    //     BigDecimal doanhThu = donHangService.tinhDoanhThu(
    //         startDate.atStartOfDay(),
    //         endDate.atTime(23, 59, 59)
    //     );
    //
    //     model.addAttribute("doanhThu", doanhThu);
    //     model.addAttribute("startDate", startDate);
    //     model.addAttribute("endDate", endDate);
    //
    //     return "admin/thongke/doanhthu";
    // }

    // =============================
    // TODO TV4: Endpoint 3 - Top sản phẩm bán chạy
    // HƯỚNG DẪN:
    // @GetMapping("/sanpham")
    // @ResponseBody
    // public List<Map<String, Object>> topSanPham(@RequestParam(defaultValue = "10") int limit) {
    //     return donHangService.topSanPhamBanChay(limit);
    // }

    // =============================
    // TODO TV4: Endpoint 4 - Top khách hàng VIP
    // HƯỚNG DẪN:
    // @GetMapping("/khachhang")
    // @ResponseBody
    // public List<Map<String, Object>> topKhachHang(@RequestParam(defaultValue = "10") int limit) {
    //     return donHangService.topKhachHangVIP(limit);
    // }

    // =============================
    // TODO TV4: Endpoint 5 - Export Excel báo cáo
    // HƯỚNG DẪN:
    // @GetMapping("/export")
    // public ResponseEntity<byte[]> exportExcel(
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws IOException {
    //
    //     // Tạo workbook
    //     Workbook workbook = new XSSFWorkbook();
    //     Sheet sheet = workbook.createSheet("Báo cáo đơn hàng");
    //
    //     // Header row
    //     Row headerRow = sheet.createRow(0);
    //     headerRow.createCell(0).setCellValue("Mã đơn");
    //     headerRow.createCell(1).setCellValue("Ngày đặt");
    //     headerRow.createCell(2).setCellValue("Khách hàng");
    //     headerRow.createCell(3).setCellValue("Tổng tiền");
    //     headerRow.createCell(4).setCellValue("Trạng thái");
    //
    //     // Data rows
    //     // List<DonHang> donHangs = donHangRepository.findByDateRange(start, end);
    //     // int rowNum = 1;
    //     // for (DonHang dh : donHangs) {
    //     //     Row row = sheet.createRow(rowNum++);
    //     //     row.createCell(0).setCellValue(dh.getMaDH());
    //     //     row.createCell(1).setCellValue(dh.getNgayDatHang().toString());
    //     //     row.createCell(2).setCellValue(dh.getTaiKhoan().getHoTen());
    //     //     row.createCell(3).setCellValue(dh.getTongTien().doubleValue());
    //     //     row.createCell(4).setCellValue(dh.getTrangThaiDonHang().getTenTrangThai());
    //     // }
    //
    //     // Convert to byte array
    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //     workbook.write(outputStream);
    //     workbook.close();
    //
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    //     headers.setContentDispositionFormData("attachment", "bao-cao-" + startDate + "-" + endDate + ".xlsx");
    //
    //     return ResponseEntity.ok()
    //         .headers(headers)
    //         .body(outputStream.toByteArray());
    // }
}
