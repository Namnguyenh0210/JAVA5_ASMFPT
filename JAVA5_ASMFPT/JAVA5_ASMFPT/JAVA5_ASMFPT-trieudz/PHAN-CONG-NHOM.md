# 🎯 PHÂN CÔNG NHÓM - WEBSITE BÁN ĐỒ TẾT

> **Nhóm:** 4 thành viên | **Leader:** TV1  
> **Cập nhật:** 13/10/2025

---

## 📊 TỔNG QUAN

| Thành viên       | Vai trò                                            | Trạng thái       | Thời gian |
|------------------|----------------------------------------------------|------------------|-----------|
| **TV1 (Leader)** | Database + Entity + HTML/CSS + Security            | ✅ 70% hoàn thành | 3 tuần    |
| **TV2**          | Frontend Khách hàng (Sản phẩm, Giỏ hàng, Checkout) | ⏳ Chưa bắt đầu   | 1.5 tuần  |
| **TV3**          | Admin Backend (Quản lý đơn, sản phẩm, user)        | ⏳ Chưa bắt đầu   | 1.5 tuần  |
| **TV4**          | Thống kê + Báo cáo + Bài viết                      | ⏳ Chưa bắt đầu   | 1 tuần    |

---

## ✅ TV1 (LEADER) - ĐÃ HOÀN THÀNH & CẦN LÀM

### 🎉 ĐÃ XONG (70%):

- ✅ **Database:** 14 bảng SQL (TaiKhoan, SanPham, DonHang, GioHang...)
- ✅ **Entity:** 15 Java class (TaiKhoan.java, SanPham.java...)
- ✅ **Repository:** 14 repository (TaiKhoanRepository, SanPhamRepository...)
- ✅ **HTML/CSS:** 21 trang (13 trang khách + 8 trang admin)
- ✅ **AuthController:** Đăng ký + Đăng nhập (hoàn thiện)
- ✅ **SecurityConfig:** Cấu hình Spring Security (hoàn thiện)
- ✅ **GlobalModelAdvice:** Hiển thị user trên header

### ⏰ CẦN LÀM TIẾP (30%):

1. **UserDetailsServiceImpl.java** - Load user khi login (chưa có)
2. **Test Security** - Đảm bảo login/register hoạt động
3. **Hỗ trợ TV2, TV3, TV4** - Review code, fix bug
4. **Tích hợp final** - Test toàn bộ hệ thống

**⏱️ Thời gian:** 3-4 ngày nữa

---

## 🔷 TV2 - FRONTEND KHÁCH HÀNG

### 📋 NHIỆM VỤ:

Làm **phần khách hàng** xem sản phẩm, thêm giỏ hàng, đặt hàng.

### 📦 PHẢI LÀM:

#### **1. Service Layer (3 file):**

- `SanPhamService.java` (8 methods) - đã có skeleton, cần implement
- `GioHangService.java` (8 methods) - đã có skeleton, cần implement
- `DiaChiService.java` (4 methods) - đã có skeleton, cần implement

#### **2. Controller Layer (5 file):**

- `HomeController.java` - Trang chủ (hiển thị sản phẩm nổi bật)
- `SanPhamController.java` - Danh sách + chi tiết sản phẩm
- `GioHangController.java` - Thêm/xóa/cập nhật giỏ hàng
- `CheckoutController.java` - Thanh toán (**cần đợi TV3 làm DonHangService**)
- `ProfileController.java` - Xem đơn hàng, cập nhật thông tin

#### **3. Tích hợp HTML:**

- Kết nối 5 trang HTML với controller (Thymeleaf)
- Ajax giỏ hàng (thêm/xóa không reload trang)

### 🎯 THỨ TỰ LÀM:

1. **Ngày 1-2:** SanPhamService (8 methods) + HomeController + SanPhamController
2. **Ngày 3-4:** GioHangService (8 methods) + GioHangController + Ajax
3. **Ngày 5-6:** DiaChiService + ProfileController
4. **Ngày 7-8:** CheckoutController (đợi TV3 xong DonHangService.createDonHang)
5. **Ngày 9-10:** Test + Fix bug

**⏱️ Thời gian:** 10-12 ngày

---

## 🔶 TV3 - ADMIN BACKEND

### 📋 NHIỆM VỤ:

Làm **phần quản trị** và **tạo đơn hàng** (TV2 cần method này!).

### 📦 PHẢI LÀM:

#### **1. Service Layer (3 file) - QUAN TRỌNG:**

- `DonHangService.java` - **createDonHang()** (TV2 CẦN!) + 9 methods khác
- `TaiKhoanService.java` - Quản lý user (5 methods)
- `LoaiSanPhamService.java` - Quản lý danh mục (2 methods)

#### **2. Controller Admin (5 file):**

- `DashboardController.java` - Trang chủ admin (tổng quan)
- `AdminDonHangController.java` - Quản lý đơn hàng (xem, duyệt, hủy)
- `AdminSanPhamController.java` - Quản lý sản phẩm (CRUD + upload ảnh)
- `AdminTaiKhoanController.java` - Quản lý user (CRUD, đổi vai trò)
- (Bonus) Nhập kho, phương thức thanh toán

#### **3. Tích hợp HTML Admin:**

- Kết nối 8 trang admin HTML với controller

### 🎯 THỨ TỰ LÀM:

1. **Ngày 1-2:** **DonHangService.createDonHang()** - TV2 đang chờ!
2. **Ngày 3-4:** DonHangService hoàn thiện (10 methods) + AdminDonHangController
3. **Ngày 5-6:** TaiKhoanService + AdminTaiKhoanController
4. **Ngày 7-8:** AdminSanPhamController (CRUD + upload ảnh)
5. **Ngày 9-10:** DashboardController + LoaiSanPhamService
6. **Ngày 11-12:** Test + Fix bug

**⏱️ Thời gian:** 10-12 ngày

---

## 🔵 TV4 - THỐNG KÊ & BÁO CÁO

### 📋 NHIỆM VỤ:

Làm **thống kê, biểu đồ, báo cáo** và **quản lý bài viết**.

### 📦 PHẢI LÀM:

#### **1. Service Layer (1 file):**

- `BaiVietService.java` - Quản lý bài viết (4 methods) - đã có skeleton

#### **2. Controller (2 file):**

- `AdminThongKeController.java` - Dashboard thống kê + biểu đồ
- `AdminBaiVietController.java` - Quản lý bài viết tin tức Tết

#### **3. Queries thống kê (trong DonHangService):**

- Doanh thu theo tháng
- Top sản phẩm bán chạy
- Top khách hàng VIP
- Biểu đồ (Chart.js)

#### **4. Export báo cáo:**

- Export Excel (Apache POI)
- In đơn hàng (PDF - optional)

### 🎯 THỨ TỰ LÀM:

1. **Ngày 1-2:** Viết query thống kê trong DonHangService (4 methods)
2. **Ngày 3-4:** AdminThongKeController + Dashboard + Chart.js
3. **Ngày 5-6:** BaiVietService + AdminBaiVietController
4. **Ngày 7-8:** Export Excel + Test
5. **Ngày 9:** Fix bug

**⏱️ Thời gian:** 9-10 ngày

---

## 📅 LỊCH TRÌNH TỔNG HỢP

```
TUẦN 1 (Ngày 1-7):
┌─────────────────────────────────────────┐
│ TV1: UserDetailsServiceImpl + Test     │ (2 ngày)
│ TV2: SanPhamService + GioHangService    │ (7 ngày)
│ TV3: DonHangService (PRIORITY!)         │ (7 ngày)
│ TV4: Queries thống kê                   │ (7 ngày)
└─────────────────────────────────────────┘

TUẦN 2 (Ngày 8-14):
┌─────────────────────────────────────────┐
│ TV1: Hỗ trợ team + Review code         │
│ TV2: CheckoutController + Profile       │
│ TV3: Admin Controllers                  │
│ TV4: AdminThongKeController + Chart.js  │
└─────────────────────────────────────────┘

TUẦN 3 (Ngày 15-21):
┌─────────────────────────────────────────┐
│ CẢ NHÓM: Test + Fix bug + Tích hợp     │
│ TV1: Deploy + Viết báo cáo              │
│ TV2, TV3, TV4: Test từng module         │
└─────────────────────────────────────────┘
```

---

## 🔗 PHỤ THUỘC QUAN TRỌNG

```
TV2 (CheckoutController) 
    ↓ CẦN
TV3 (DonHangService.createDonHang())
    ↓ CẦN
TV1 (Security - đã xong)

TV4 (Thống kê)
    ↓ CẦN
TV3 (DonHangService queries)
```

➡️ **TV3 phải làm DonHangService.createDonHang() TRƯỚC để TV2 làm Checkout!**

---

## 💡 CÁCH TÌM TODO CỦA MÌNH

```bash
# TV1 (Leader):
grep -r "TODO Leader" src/
grep -r "TODO TV1" src/

# TV2:
grep -r "TODO TV2" src/
grep -r "TODO THÀNH VIÊN 3" src/main/java/com/example/projectend/service/SanPhamService.java

# TV3:
grep -r "TODO TV3" src/
grep -r "TODO THÀNH VIÊN 3" src/main/java/com/example/projectend/service/

# TV4:
grep -r "TODO TV4" src/
grep -r "TODO THÀNH VIÊN 4" src/
```

---

## 📞 LIÊN HỆ & HỖ TRỢ

- **Gặp bug:** Báo trên nhóm, tag @TV1 (Leader)
- **Cần review code:** Push lên Git, tag người review
- **Không hiểu yêu cầu:** Đọc comment trong code skeleton

---

## ✅ CHECKLIST HOÀN THÀNH

### TV1 (Leader):

- [x] Database + Entity + Repository
- [x] HTML/CSS tất cả trang
- [x] AuthController + SecurityConfig
- [ ] UserDetailsServiceImpl
- [ ] Test Security hoạt động
- [ ] Review code team
- [ ] Deploy + Báo cáo

### TV2:

- [ ] SanPhamService (8 methods)
- [ ] GioHangService (8 methods)
- [ ] DiaChiService (4 methods)
- [ ] HomeController + SanPhamController
- [ ] GioHangController + Ajax
- [ ] CheckoutController
- [ ] ProfileController
- [ ] Test module khách hàng

### TV3:

- [ ] **DonHangService.createDonHang()** ⚠️ PRIORITY!
- [ ] DonHangService (10 methods)
- [ ] TaiKhoanService (5 methods)
- [ ] LoaiSanPhamService (2 methods)
- [ ] AdminDonHangController
- [ ] AdminSanPhamController + Upload ảnh
- [ ] AdminTaiKhoanController
- [ ] DashboardController
- [ ] Test module admin

### TV4:

- [ ] Query thống kê (4 methods)
- [ ] AdminThongKeController
- [ ] Dashboard + Chart.js
- [ ] BaiVietService (4 methods)
- [ ] AdminBaiVietController
- [ ] Export Excel
- [ ] Test module thống kê

---

## 🎯 MỤC TIÊU

✅ **Sau 3 tuần:** Website hoàn chỉnh, chạy được đầy đủ tính năng  
✅ **Khách hàng:** Xem sản phẩm, thêm giỏ hàng, đặt hàng, xem đơn  
✅ **Admin:** Quản lý đơn hàng, sản phẩm, user, xem thống kê  
✅ **Deploy:** Lên server, có báo cáo đầy đủ

---

**Chúc cả nhóm làm việc hiệu quả! 🚀**

