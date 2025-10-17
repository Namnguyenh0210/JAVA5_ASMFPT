package com.example.projectend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API CONTROLLER - Endpoints tổng hợp (chỉ giữ skeleton, dev tách nhỏ sau nếu cần)
 * PHÂN CÔNG:
 * - TV2 (Frontend Khách hàng): API phục vụ FE khách (products, categories, cart, quick search)
 * - TV3 (Admin Backend): API quản trị (admin products, orders)
 * - TV4 (Thống kê): API statistics
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    // =============================
    // TODO TV2 - CUSTOMER APIs
    // =============================

    // TODO TV2 - API lấy danh sách sản phẩm (có search, pagination)
    // @GetMapping("/products")
    // public ResponseEntity<Page<SanPham>> getProducts(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "12") int size,
    //         @RequestParam(required = false) String keyword) {
    //     // Logic tìm kiếm sản phẩm theo tên
    //     // Phân trang 12 sản phẩm/trang
    // }

    // TODO TV2 - API chi tiết sản phẩm
    // @GetMapping("/products/{id}")
    // public ResponseEntity<SanPham> getProductById(@PathVariable Integer id) {
    //     // Lấy thông tin chi tiết sản phẩm
    // }

    // TODO TV2 - API lấy danh mục sản phẩm
    // @GetMapping("/categories")
    // public ResponseEntity<List<LoaiSanPham>> getCategories() {
    //     // Lấy tất cả danh mục để hiển thị menu
    // }

    // TODO TV2 - API giỏ hàng - Thêm sản phẩm
    // @PostMapping("/cart/add")
    // public ResponseEntity<?> addToCart(@RequestParam Integer productId,
    //                                   @RequestParam Integer quantity) {
    //     // Thêm sản phẩm vào giỏ hàng
    //     // Kiểm tra số lượng tồn kho
    // }

    // TODO TV2 - API giỏ hàng - Xem giỏ hàng
    // @GetMapping("/cart")
    // public ResponseEntity<List<GioHang>> getCart() {
    //     // Lấy giỏ hàng của user hiện tại
    // }

    // TODO TV2 - API đặt hàng đơn giản
    // @PostMapping("/orders")
    // public ResponseEntity<?> createOrder(@RequestParam String diaChiGiaoHang,
    //                                     @RequestParam String ghiChu) {
    //     // Tạo đơn hàng từ giỏ hàng
    //     // Trạng thái mặc định: "Chờ xác nhận"
    // }

    // =============================
    // TODO TV3 - ADMIN / ORDER MANAGEMENT APIs
    // =============================

    // TODO TV3 - API quản lý sản phẩm - Lấy danh sách
    // @GetMapping("/admin/products")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<List<SanPham>> getAllProducts() {
    //     // Lấy tất cả sản phẩm cho admin
    // }

    // TODO TV3 - API quản lý sản phẩm - Thêm mới
    // @PostMapping("/admin/products")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> createProduct(@RequestBody SanPham sanPham) {
    //     // Thêm sản phẩm mới
    // }

    // TODO TV4 - API thống kê
    // @GetMapping("/admin/statistics")
    // @PreAuthorize("hasAnyRole('Admin', 'Nhân viên')")
    // public ResponseEntity<?> getStatistics() {
    //     // API trả về dữ liệu thống kê
    // }
}
