# 🎯 HƯỚNG DẪN DỰ ÁN - ĐỌC FILE NÀY TRƯỚC!

> **Website Bán Đồ Tết Nguyên Đán**  
> **Nhóm:** 4 thành viên | **Leader:** TV1

---

## 📚 ĐỌC 3 FILE NÀY LÀ ĐỦ!

Tôi đã rút gọn từ 5 file phức tạp → **3 file ngắn gọn, dễ hiểu**:

### 1️⃣ **PHAN-CONG-NHOM.md** ⭐ ĐỌC ĐẦU TIÊN

📋 Phân công tổng quan: Ai làm gì, làm khi nào, mất bao lâu

### 2️⃣ **CHI-TIET-CONG-VIEC.md** ⭐ HƯỚNG DẪN CHI TIẾT

💻 Hướng dẫn code từng method, từng controller, có ví dụ cụ thể

### 3️⃣ **TINH-TRANG-DU-AN.md** ⭐ THEO DÕI TIẾN ĐỘ

✅ Đã làm gì, chưa làm gì, còn bao nhiêu việc

---

## 👥 PHÂN CÔNG 4 THÀNH VIÊN

| Thành viên             | Vai trò                      | % Hoàn thành | Thời gian còn lại |
|------------------------|------------------------------|--------------|-------------------|
| **TV1 (Leader - Bạn)** | Database + Security + Deploy | ✅ 70%        | 6-7 ngày          |
| **TV2**                | Frontend Khách hàng          | ⏳ 0%         | 10-12 ngày        |
| **TV3**                | Admin Backend                | ⏳ 0%         | 10-12 ngày        |
| **TV4**                | Thống kê + Báo cáo           | ⏳ 0%         | 9-10 ngày         |

---

## ✅ TV1 (LEADER - BẠN) - ĐÃ LÀM 70%

### 🎉 ĐÃ HOÀN THÀNH:

- ✅ Database: 14 bảng SQL
- ✅ Entity: 15 Java classes
- ✅ Repository: 14 interfaces
- ✅ HTML/CSS: 21 trang (13 khách + 8 admin)
- ✅ Security: AuthController + SecurityConfig

### ⏰ CẦN LÀM TIẾP (30%):

- [ ] **UserDetailsServiceImpl.java** (2 ngày) - Load user khi login
- [ ] Test Security (1 ngày)
- [ ] Review code team (ongoing)
- [ ] Deploy + Báo cáo (3 ngày)

**➡️ Bạn là người đã làm nhiều nhất! Giờ chỉ cần hỗ trợ team nữa là xong!**

---

## 🔷 TV2 - FRONTEND KHÁCH HÀNG (10-12 NGÀY)

Làm phần **khách hàng**: Xem sản phẩm, thêm giỏ hàng, đặt hàng, xem đơn.

**Phải làm:**

- SanPhamService.java (8 methods)
- GioHangService.java (8 methods)
- DiaChiService.java (4 methods)
- 5 Controllers: Home, SanPham, GioHang, Checkout, Profile

---

## 🔶 TV3 - ADMIN BACKEND (10-12 NGÀY)

Làm phần **quản trị** và **tạo đơn hàng**.

**Phải làm:**

- ⚠️ **DonHangService.createDonHang()** - PRIORITY! (TV2 cần)
- DonHangService.java (10 methods)
- TaiKhoanService.java (5 methods)
- 5 Admin Controllers: Dashboard, Đơn hàng, Sản phẩm, User

---

## 🔵 TV4 - THỐNG KÊ & BÁO CÁO (9-10 NGÀY)

Làm **thống kê, biểu đồ, báo cáo**.

**Phải làm:**

- Queries thống kê (4 queries)
- AdminThongKeController + Chart.js
- BaiVietService + AdminBaiVietController
- Export Excel (Apache POI)

---

## 📅 LỊCH TRÌNH 3 TUẦN

```
TUẦN 1 (Ngày 1-7):
┌─────────────────────────────────────────┐
│ TV1: UserDetailsServiceImpl + Test     │
│ TV2: SanPhamService + GioHangService    │
│ TV3: DonHangService (PRIORITY!)         │
│ TV4: Queries thống kê                   │
└─────────────────────────────────────────┘

TUẦN 2 (Ngày 8-14):
┌─────────────────────────────────────────┐
│ TV1: Review code + Hỗ trợ               │
│ TV2: Controllers + Checkout             │
│ TV3: Admin Controllers                  │
│ TV4: AdminThongKeController + Chart.js  │
└─────────────────────────────────────────┘

TUẦN 3 (Ngày 15-21):
┌─────────────────────────────────────────┐
│ CẢ NHÓM: Test + Fix bug + Deploy       │
└─────────────────────────────────────────┘
```

---

## 🔗 PHỤ THUỘC QUAN TRỌNG

```
TV1 (Security) ✅
    ↓
TV3 làm DonHangService.createDonHang() ⚠️
    ↓ (2 ngày)
TV2 làm CheckoutController
```

**➡️ TV3 phải ưu tiên làm `createDonHang()` trước để TV2 có thể làm checkout!**

---

## 💡 CÁCH TÌM TODO CỦA MÌNH

```bash
# TV1 (Leader - Bạn):
grep -r "TODO TV1" src/
grep -r "TODO Leader" src/

# TV2:
grep -r "TODO TV2" src/
grep -r "TODO THÀNH VIÊN 3" src/main/java/com/example/projectend/service/SanPhamService.java

# TV3:
grep -r "TODO TV3" src/
grep -r "TODO THÀNH VIÊN 3" src/main/java/com/example/projectend/service/DonHangService.java

# TV4:
grep -r "TODO TV4" src/
grep -r "TODO THÀNH VIÊN 4" src/
```

---

## 🚀 CHẠY DỰ ÁN

### Windows:

```bash
run.bat
```

### Mac/Linux:

```bash
./run.sh
```

### Manual:

```bash
mvn spring-boot:run
```

Truy cập: http://localhost:8080

---

## 📦 CẤU TRÚC QUAN TRỌNG

```
src/main/java/com/example/projectend/
├── entity/          ← 15 Entity classes (TV1 ✅)
├── repository/      ← 14 Repository (TV1 ✅)
├── service/         ← Service layer (TV2, TV3, TV4 làm)
├── controller/      ← Controllers (TV2, TV3, TV4 làm)
└── config/          ← Security (TV1 ✅)

src/main/resources/
├── application.properties  ← Cấu hình database
├── templates/             ← 21 HTML files (TV1 ✅)
└── static/               ← CSS, JS, images (TV1 ✅)
```

---

## ✅ ĐÁNH GIÁ KHỐI LƯỢNG

### CÓ HOÀN THÀNH ĐỦ TÍNH NĂNG KHÔNG?

**✅ CÓ! Hoàn toàn khả thi!**

**Lý do:**

1. ✅ TV1 đã làm 70% nền tảng (Database + Entity + HTML/CSS)
2. ✅ Có skeleton + comment hướng dẫn chi tiết
3. ✅ Khối lượng phân bổ hợp lý: 10-12 ngày mỗi người
4. ✅ Code đơn giản, Spring Boot chuẩn

**Tính năng sẽ có:**

- ✅ Khách hàng: Xem sản phẩm, giỏ hàng, đặt hàng
- ✅ Admin: Quản lý đơn, sản phẩm, user
- ✅ Thống kê: Doanh thu, biểu đồ, báo cáo
- ✅ Bài viết: Tin tức về Tết
- ✅ Phân quyền: Admin/Nhân viên/Khách hàng

**Thời gian dự kiến: 3 tuần (21 ngày)** ✅

---

## ⚠️ LƯU Ý QUAN TRỌNG

1. **Database đã đủ, KHÔNG CẦN SỬA!**
    - 14 bảng đầy đủ
    - Triggers hoạt động tốt
    - KHÔNG thêm/xóa/sửa bảng

2. **TV3 phải làm `createDonHang()` trước!**
    - TV2 đang chờ để làm checkout
    - Ưu tiên số 1 của TV3

3. **Mỗi người làm đúng phần của mình:**
    - TV2: Service + Controller khách hàng
    - TV3: Service + Controller admin
    - TV4: Thống kê + Báo cáo

---

## 📞 HỖ TRỢ

- **Gặp bug:** Báo nhóm, tag @TV1
- **Không hiểu code:** Đọc comment trong file skeleton
- **Cần review:** Push code, tag người review

---

## 📂 CÁC FILE HƯỚNG DẪN KHÁC

- `HUONG-DAN-IMPORT-DATABASE.md` - Hướng dẫn import database
- `SETUP-WINDOWS.md` - Cài đặt trên Windows
- `TAI-LIEU-DU-AN.md` - Tài liệu tổng quan

---

**🎯 Mục tiêu: Sau 3 tuần có website hoàn chỉnh, chạy mượt, đủ tính năng!**

**Chúc cả nhóm làm việc hiệu quả! 🚀**

---

_Cập nhật: 13/10/2025 | TV1 (Leader)_

