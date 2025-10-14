# 📋 CHI TIẾT CÔNG VIỆC TỪNG THÀNH VIÊN

> **Dự án:** Website Bán Đồ Tết Nguyên Đán  
> **Nhóm:** 4 người (TV1 Leader)

---

## 👨‍💼 TV1 (LEADER) - DATABASE & SECURITY

### ✅ ĐÃ HOÀN THÀNH (70%):

#### 1. Database Layer:

- ✅ 14 Entity classes (TaiKhoan, SanPham, DonHang, GioHang...)
- ✅ 14 Repository interfaces
- ✅ File SQL với 14 bảng + 3 triggers

#### 2. Frontend:

- ✅ 13 trang HTML khách hàng (home, sanpham, giohang, checkout...)
- ✅ 8 trang HTML admin (dashboard, quản lý đơn, sản phẩm...)
- ✅ 13 file CSS đầy đủ

#### 3. Security:

- ✅ `SecurityConfig.java` - Cấu hình Spring Security
- ✅ `AuthController.java` - Đăng ký + Đăng nhập
- ✅ `GlobalModelAdvice.java` - Hiển thị user trên header

### ⏰ CẦN LÀM (30%):

#### 1. UserDetailsServiceImpl.java (2 ngày):

```java
// File: src/main/java/com/example/projectend/service/auth/UserDetailsServiceImpl.java
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Load user từ DB theo email
        // Map VaiTro thành GrantedAuthority
        // Return User object của Spring Security
    }
}
```

#### 2. Test Security (1 ngày):

- Test đăng ký tài khoản mới
- Test đăng nhập email + password
- Test phân quyền (admin vào /admin, khách vào /giohang)
- Test logout

#### 3. Hỗ trợ team (ongoing):

- Review code TV2, TV3, TV4
- Fix bug chung
- Tích hợp các module

#### 4. Deploy & Báo cáo (3 ngày):

- Deploy lên server
- Viết báo cáo dự án
- Tạo video demo

**⏱️ Tổng thời gian còn lại: 6-7 ngày**

---

## 👨‍💻 TV2 - FRONTEND KHÁCH HÀNG

### 📦 PHẢI LÀM:

#### 1. SanPhamService.java (8 methods - 2 ngày):

```java
// Đã có skeleton, cần implement:
✅findById(Integer id) -
Lấy sản
phẩm theo
ID
✅

getNoiBat(int limit) -
Sản phẩm
nổi bật
trang chủ
✅

searchAndFilter(...) -
Tìm kiếm +lọc +
phân trang
✅

getRelatedProducts(Integer id, int limit) -
Sản phẩm
liên quan
✅

quickSearch(String keyword, int limit) -
Tìm kiếm
nhanh Ajax
✅

incrementLuotXem(Integer id) -
Tăng lượt
xem
✅

getAllCategories() -
Lấy tất
cả danh
mục
✅save/update/delete -

Admin CRUD(TV3 sẽ dùng)
```

**Hướng dẫn:**

- Dùng `SanPhamRepository` đã có
- Implement query với Specification hoặc JPQL
- Phân trang dùng `Pageable`

#### 2. GioHangService.java (8 methods - 2 ngày):

```java
✅getGioHangByTaiKhoan(TaiKhoan tk)
✅

themSanPham(TaiKhoan tk, SanPham sp, int soLuong)
✅

capNhatSoLuong(TaiKhoan tk, Integer maSP, int soLuong)
✅

xoaSanPham(TaiKhoan tk, Integer maSP)
✅

countItems(TaiKhoan tk)
✅

tinhTongTien(List<GioHang> items)
✅

tinhTongTienByTaiKhoan(TaiKhoan tk)
✅

clearGioHang(TaiKhoan tk) -
Xóa sau
khi đặt
hàng
```

**Hướng dẫn:**

- Dùng composite key (MaTK + MaSP)
- Nếu sản phẩm đã có trong giỏ → cộng dồn số lượng
- Kiểm tra tồn kho trước khi thêm

#### 3. DiaChiService.java (4 methods - 1 ngày):

```java
✅getDiaChiByTaiKhoan(TaiKhoan tk)
✅

getDiaChiMacDinh(TaiKhoan tk)
✅

save(DiaChi diaChi)
✅

delete(Integer id, TaiKhoan tk)
```

#### 4. HomeController.java (1 ngày):

```java

@GetMapping("/")
public String home(Model model) {
    // Lấy 8 sản phẩm nổi bật
    List<SanPham> noiBat = sanPhamService.getNoiBat(8);
    model.addAttribute("sanPhamNoiBat", noiBat);

    // Lấy 3 bài viết mới
    List<BaiViet> baiViet = baiVietService.getFeaturedPosts(3);
    model.addAttribute("baiViet", baiViet);

    return "home";
}
```

#### 5. SanPhamController.java (1 ngày):

```java

@GetMapping("/sanpham")
public String danhSach(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer category,
        @RequestParam(defaultValue = "0") int page,
        Model model
) {
    // Gọi sanPhamService.searchAndFilter()
    // Phân trang 12 sản phẩm/trang
    return "sanpham";
}

@GetMapping("/sanpham/{id}")
public String chiTiet(@PathVariable Integer id, Model model) {
    // Lấy sản phẩm + sản phẩm liên quan
    // Tăng lượt xem
    return "sanpham-detail";
}

@GetMapping("/api/search") // Ajax
@ResponseBody
public List<SanPham> quickSearch(@RequestParam String q) {
    return sanPhamService.quickSearch(q, 10);
}
```

#### 6. GioHangController.java (2 ngày):

```java

@GetMapping("/giohang")
public String viewCart(Model model, Principal principal) {
    // Lấy user hiện tại
    // Lấy giỏ hàng
    // Tính tổng tiền
    return "giohang";
}

@PostMapping("/giohang/add") // Ajax
@ResponseBody
public Map<String, Object> addToCart(
        @RequestParam Integer productId,
        @RequestParam int quantity,
        Principal principal
) {
    // Thêm vào giỏ
    // Return số lượng items + tổng tiền
}

@PostMapping("/giohang/update") // Ajax
@ResponseBody
public Map<String, Object> updateQuantity(...) {
    // Cập nhật số lượng
}

@DeleteMapping("/giohang/delete/{id}") // Ajax
@ResponseBody
public Map<String, Object> removeItem(...) {
    // Xóa sản phẩm
}
```

#### 7. CheckoutController.java (2 ngày):

⚠️ **CHỜ TV3 HOÀN THÀNH `DonHangService.createDonHang()`**

```java

@GetMapping("/checkout")
public String checkoutPage(Model model, Principal principal) {
    // Lấy giỏ hàng
    // Lấy danh sách địa chỉ
    // Lấy phương thức thanh toán
    return "checkout";
}

@PostMapping("/checkout")
public String placeOrder(
        @RequestParam Integer diaChiId,
        @RequestParam Integer phuongThucId,
        @RequestParam(required = false) String ghiChu,
        Principal principal
) {
    // Lấy giỏ hàng
    // Gọi TV3's DonHangService.createDonHang()
    // Xóa giỏ hàng
    return "redirect:/checkout-success";
}
```

#### 8. ProfileController.java (1 ngày):

```java

@GetMapping("/profile")
public String profile(Model model, Principal principal) {
    // Hiển thị thông tin user
    return "profile";
}

@GetMapping("/profile/orders")
public String myOrders(Model model, Principal principal) {
    // Lấy danh sách đơn hàng
    return "order-list";
}

@GetMapping("/profile/orders/{id}")
public String orderDetail(@PathVariable Integer id, Model model) {
    // Chi tiết đơn hàng
    return "order-detail";
}
```

### 📅 Lịch làm việc TV2 (10-12 ngày):

- **Ngày 1-2:** SanPhamService (8 methods)
- **Ngày 3-4:** GioHangService (8 methods)
- **Ngày 5:** DiaChiService (4 methods)
- **Ngày 6:** HomeController + SanPhamController
- **Ngày 7-8:** GioHangController + Ajax
- **Ngày 9:** ProfileController
- **Ngày 10:** CheckoutController (đợi TV3)
- **Ngày 11-12:** Test + Fix bug

---

## 👨‍💼 TV3 - ADMIN BACKEND

### 📦 PHẢI LÀM:

#### 1. DonHangService.java (10 methods - 4 ngày):

⚠️ **PRIORITY #1: createDonHang() - TV2 đang chờ!**

```java
// Method 1: QUAN TRỌNG NHẤT!
public DonHang createDonHang(
        TaiKhoan khachHang,
        Integer diaChiId,
        Integer phuongThucId,
        List<GioHang> items,
        String ghiChu
) {
    // 1. Validate: địa chỉ, phương thức thanh toán
    // 2. Kiểm tra tồn kho
    // 3. Tính tổng tiền
    // 4. Tạo DonHang (trạng thái "Chờ xác nhận")
    // 5. Lưu DonHang
    // 6. Tạo DonHangChiTiet cho từng item
    // 7. Trigger tự động giảm tồn kho
    // 8. Return DonHang
}

// Method 2-5: Khách hàng
✅

getChiTietDonHang(DonHang donHang)
✅

findByIdAndKhachHang(Integer id, TaiKhoan khachHang)
✅

getDonHangByKhachHang(TaiKhoan, Pageable)
✅

cancelOrder(Integer donHangId, TaiKhoan khachHang)

// Method 6-8: Admin
✅

searchAdmin(String keyword, String trangThai, Pageable)
✅

updateTrangThai(Integer donHangId, String trangThaiMoi)
✅

getPendingOrders(int limit)

// Method 9-10: Phí ship
✅

tinhPhiShip(BigDecimal tongTien)
✅

tinhPhiShipByDiaChi(DiaChi diaChi)
```

#### 2. TaiKhoanService.java (5 methods - 2 ngày):

```java
✅findByEmail(String email)
✅

save(TaiKhoan taiKhoan)
✅

updateProfile(TaiKhoan tk, String hoTen, String sdt)
✅

changePassword(TaiKhoan tk, String newPassword)
✅

delete(Integer id) hoặc deactivate
```

#### 3. LoaiSanPhamService.java (2 methods - 1 ngày):

```java
✅findAll()
✅

save(LoaiSanPham loai)
```

#### 4. AdminDonHangController.java (2 ngày):

```java

@GetMapping("/admin/donhang")
public String listOrders(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String trangThai,
        @RequestParam(defaultValue = "0") int page,
        Model model
) {
    // Lấy danh sách đơn hàng có search + filter
    return "admin/donhang/list";
}

@GetMapping("/admin/donhang/{id}")
public String viewOrder(@PathVariable Integer id, Model model) {
    // Chi tiết đơn hàng
    // Lấy chi tiết sản phẩm
    return "admin/donhang/detail";
}

@PostMapping("/admin/donhang/{id}/status")
public String updateStatus(
        @PathVariable Integer id,
        @RequestParam String trangThai
) {
    // Cập nhật trạng thái
    // Chờ xác nhận → Đang giao → Đã giao
    return "redirect:/admin/donhang/" + id;
}
```

#### 5. AdminSanPhamController.java (2 ngày):

```java

@GetMapping("/admin/sanpham")
public String list(Model model) {
    // Lấy tất cả sản phẩm
    return "admin/sanpham/list";
}

@GetMapping("/admin/sanpham/create")
public String createForm(Model model) {
    // Form thêm mới
    return "admin/sanpham/form";
}

@PostMapping("/admin/sanpham/create")
public String create(
        @ModelAttribute SanPham sanPham,
        @RequestParam("file") MultipartFile file
) {
    // Upload ảnh
    // Lưu sản phẩm
    return "redirect:/admin/sanpham";
}

@GetMapping("/admin/sanpham/edit/{id}")
public String editForm(@PathVariable Integer id, Model model) {
    return "admin/sanpham/form";
}

@PostMapping("/admin/sanpham/edit/{id}")
public String update(...) {
    // Cập nhật sản phẩm
    return "redirect:/admin/sanpham";
}

@PostMapping("/admin/sanpham/delete/{id}")
public String delete(@PathVariable Integer id) {
    // Xóa sản phẩm
    return "redirect:/admin/sanpham";
}
```

#### 6. AdminTaiKhoanController.java (1 ngày):

```java

@GetMapping("/admin/taikhoan")
public String list(Model model) {
    // Danh sách user
    return "admin/taikhoan/list";
}

@PostMapping("/admin/taikhoan/{id}/role")
public String changeRole(
        @PathVariable Integer id,
        @RequestParam Integer vaiTroId
) {
    // Đổi vai trò user
    return "redirect:/admin/taikhoan";
}

@PostMapping("/admin/taikhoan/{id}/toggle")
public String toggleActive(@PathVariable Integer id) {
    // Kích hoạt / vô hiệu hóa tài khoản
    return "redirect:/admin/taikhoan";
}
```

#### 7. DashboardController.java (1 ngày):

```java

@GetMapping("/admin")
public String dashboard(Model model) {
    // Tổng doanh thu
    // Số đơn hàng mới
    // Đơn hàng chờ xử lý
    // Top sản phẩm (gọi TV4's queries)
    return "admin/dashboard";
}
```

### 📅 Lịch làm việc TV3 (10-12 ngày):

- **Ngày 1-2:** **DonHangService.createDonHang()** ⚠️
- **Ngày 3-4:** DonHangService hoàn thiện (10 methods)
- **Ngày 5-6:** AdminDonHangController
- **Ngày 7-8:** TaiKhoanService + AdminTaiKhoanController
- **Ngày 9-10:** AdminSanPhamController + Upload ảnh
- **Ngày 11:** DashboardController + LoaiSanPhamService
- **Ngày 12:** Test + Fix bug

---

## 👨‍🔬 TV4 - THỐNG KÊ & BÁO CÁO

### 📦 PHẢI LÀM:

#### 1. Queries thống kê trong DonHangService (2 ngày):

```java
// Thêm vào DonHangService.java (TV3 đã tạo file)

// Query 1: Doanh thu theo khoảng thời gian
public BigDecimal tinhDoanhThu(LocalDateTime start, LocalDateTime end) {
    // SELECT SUM(TongTien) FROM DonHang 
    // WHERE NgayDatHang BETWEEN start AND end
    // AND TrangThai = 'Đã giao'
}

// Query 2: Doanh thu theo tháng (X tháng gần nhất)
public Map<String, BigDecimal> thongKeTheoThang(int soThang) {
    // Return Map: {"2025-01": 10000000, "2025-02": 15000000, ...}
}

// Query 3: Top sản phẩm bán chạy
public List<Map<String, Object>> topSanPhamBanChay(int limit) {
    // SELECT sp.MaSP, sp.TenSP, SUM(dhct.SoLuong) as soLuongBan
    // FROM DonHangChiTiet dhct
    // JOIN SanPham sp ON dhct.MaSP = sp.MaSP
    // GROUP BY sp.MaSP
    // ORDER BY soLuongBan DESC
    // LIMIT limit
}

// Query 4: Top khách hàng VIP (mua nhiều nhất)
public List<Map<String, Object>> topKhachHangVIP(int limit) {
    // SELECT tk.MaTK, tk.HoTen, SUM(dh.TongTien) as tongChiTieu
    // FROM DonHang dh
    // JOIN TaiKhoan tk ON dh.MaTK = tk.MaTK
    // GROUP BY tk.MaTK
    // ORDER BY tongChiTieu DESC
    // LIMIT limit
}
```

#### 2. AdminThongKeController.java (3 ngày):

```java

@GetMapping("/admin/thongke")
public String dashboard(Model model) {
    // Doanh thu tháng này
    LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1);
    BigDecimal doanhThuThang = donHangService.tinhDoanhThu(startOfMonth, LocalDateTime.now());
    model.addAttribute("doanhThuThang", doanhThuThang);

    // Doanh thu 6 tháng gần nhất (cho Chart.js)
    Map<String, BigDecimal> doanhThu6Thang = donHangService.thongKeTheoThang(6);
    model.addAttribute("chartData", doanhThu6Thang);

    // Top 5 sản phẩm bán chạy
    List<Map<String, Object>> topSanPham = donHangService.topSanPhamBanChay(5);
    model.addAttribute("topSanPham", topSanPham);

    // Top 5 khách hàng VIP
    List<Map<String, Object>> topKhach = donHangService.topKhachHangVIP(5);
    model.addAttribute("topKhach", topKhach);

    return "admin/thongke/dashboard";
}

@GetMapping("/admin/thongke/baocao")
public String baoCao(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        Model model
) {
    // Báo cáo chi tiết theo khoảng thời gian
    return "admin/thongke/baocao";
}

@GetMapping("/admin/thongke/export")
public ResponseEntity<byte[]> exportExcel(
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate
) {
    // Export Excel bằng Apache POI
    // Return file Excel
}
```

#### 3. Chart.js integration (1 ngày):

```html
<!-- File: templates/admin/thongke/dashboard.html -->
<canvas id="doanhThuChart"></canvas>

<script>
    const ctx = document.getElementById('doanhThuChart');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: [/*[[ ${chartLabels} ]]*/],
            datasets: [{
                label: 'Doanh thu',
                data: [/*[[ ${chartValues} ]]*/],
                borderColor: 'rgb(75, 192, 192)',
            }]
        }
    });
</script>
```

#### 4. BaiVietService.java (1 ngày):

```java
// Đã có skeleton, cần implement:
✅findAll()
✅

findById(Integer id)
✅

getFeaturedPosts(int limit) -
Bài viết
nổi bật
✅

save(BaiViet baiViet)
✅

delete(Integer id)
```

#### 5. AdminBaiVietController.java (2 ngày):

```java

@GetMapping("/admin/baiviet")
public String list(Model model) {
    List<BaiViet> baiViet = baiVietService.findAll();
    model.addAttribute("baiViet", baiViet);
    return "admin/baiviet/list";
}

@GetMapping("/admin/baiviet/create")
public String createForm(Model model) {
    return "admin/baiviet/form";
}

@PostMapping("/admin/baiviet/create")
public String create(
        @ModelAttribute BaiViet baiViet,
        @RequestParam("file") MultipartFile file
) {
    // Upload ảnh
    // Lưu bài viết
    return "redirect:/admin/baiviet";
}

@GetMapping("/admin/baiviet/edit/{id}")
public String editForm(@PathVariable Integer id, Model model) {
    return "admin/baiviet/form";
}

@PostMapping("/admin/baiviet/edit/{id}")
public String update(...) {
    return "redirect:/admin/baiviet";
}

@PostMapping("/admin/baiviet/delete/{id}")
public String delete(@PathVariable Integer id) {
    return "redirect:/admin/baiviet";
}
```

#### 6. Export Excel (Apache POI) (1 ngày):

```java
// Thêm dependency vào pom.xml:
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>

// Method export:
public byte[] exportBaoCao(LocalDate start, LocalDate end) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Báo cáo");

    // Tạo header row
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("Mã đơn");
    headerRow.createCell(1).setCellValue("Ngày đặt");
    headerRow.createCell(2).setCellValue("Khách hàng");
    headerRow.createCell(3).setCellValue("Tổng tiền");

    // Lấy dữ liệu và fill vào sheet
    List<DonHang> donHangs = donHangRepository.findByDateRange(start, end);
    int rowNum = 1;
    for (DonHang dh : donHangs) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(dh.getMaDH());
        row.createCell(1).setCellValue(dh.getNgayDatHang().toString());
        row.createCell(2).setCellValue(dh.getTaiKhoan().getHoTen());
        row.createCell(3).setCellValue(dh.getTongTien().doubleValue());
    }

    // Convert to byte array
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    return outputStream.toByteArray();
}
```

### 📅 Lịch làm việc TV4 (9-10 ngày):

- **Ngày 1-2:** Viết 4 queries thống kê trong DonHangService
- **Ngày 3-4:** AdminThongKeController + Dashboard
- **Ngày 5:** Tích hợp Chart.js
- **Ngày 6:** BaiVietService (4 methods)
- **Ngày 7-8:** AdminBaiVietController
- **Ngày 9:** Export Excel (Apache POI)
- **Ngày 10:** Test + Fix bug

---

## 🔗 PHỤ THUỘC

```
TV1 (Security) ✅
    ↓
TV2, TV3, TV4 có thể bắt đầu
    ↓
TV3 làm DonHangService.createDonHang() ⚠️ PRIORITY
    ↓
TV2 làm CheckoutController
    ↓
TV3 hoàn thiện DonHangService
    ↓
TV4 viết queries thống kê
```

---

**File này chứa hướng dẫn chi tiết từng method, từng controller cho 4 thành viên!** 📚

