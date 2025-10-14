# 📋 HƯỚNG DẪN IMPORT DATABASE - WebBanHangTet

## ⚠️ QUAN TRỌNG: BẠN PHẢI IMPORT FILE SQL TRƯỚC KHI CHẠY ỨNG DỤNG!

---

## 🎯 Các bước thực hiện:

### **Bước 1: Xác nhận SQL Server đang chạy**

```bash
# Kiểm tra Docker container đang chạy
docker ps
```

Bạn sẽ thấy container SQL Server đang chạy.

---

### **Bước 2: Import file SQL vào database**

#### **CÁCH 1: Dùng Azure Data Studio (Khuyên dùng - Dễ nhất)** ⭐

1. **Mở Azure Data Studio**
2. **Kết nối đến SQL Server**:
    - Server: `localhost` hoặc `127.0.0.1`
    - Port: `1433`
    - Authentication: `SQL Login`
    - Username: `sa`
    - Password: `Aa@123456`

3. **Mở file SQL**:
    - Menu **File** → **Open File**
    - Chọn file: `/Users/nam.nguyenh/Documents/Zalo Received Files/JAVAWEB_FPT/projectend-spring/asmfpt5 22.23.14.sql`

4. **Execute SQL**:
    - Nhấn **Run** (hoặc **F5**)
    - Đợi cho đến khi hiển thị "Commands completed successfully"

5. **Kiểm tra kết quả**:
    - Refresh danh sách Databases
    - Bạn sẽ thấy database **WebBanHangTet** với các bảng:
        - `TaiKhoan` (3 tài khoản: admin, staff, customer)
        - `VaiTro` (3 vai trò)
        - `SanPham` (4 sản phẩm)
        - `BaiViet`, `DonHang`, `DiaChi`, v.v.

---

#### **CÁCH 2: Dùng Command Line (Docker)**

```bash
# 1. Copy file SQL vào container
docker cp "/Users/nam.nguyenh/Documents/Zalo Received Files/JAVAWEB_FPT/projectend-spring/asmfpt5 22.23.14.sql" <CONTAINER_NAME>:/tmp/database.sql

# 2. Execute file SQL
docker exec -it <CONTAINER_NAME> /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P 'Aa@123456' \
  -i /tmp/database.sql

# 3. Kiểm tra database đã tạo
docker exec -it <CONTAINER_NAME> /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P 'Aa@123456' \
  -Q "SELECT name FROM sys.databases WHERE name = 'WebBanHangTet'"
```

**Lưu ý**: Thay `<CONTAINER_NAME>` bằng tên container SQL Server của bạn (xem bằng `docker ps`)

---

### **Bước 3: Verify data đã import đúng**

Sau khi import xong, kiểm tra:

```sql
-- Kiểm tra các bảng đã tạo
SELECT TABLE_NAME
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_CATALOG = 'WebBanHangTet';

-- Kiểm tra tài khoản
SELECT *
FROM TaiKhoan;

-- Kiểm tra sản phẩm
SELECT *
FROM SanPham;
```

Bạn sẽ thấy:

- ✅ 3 tài khoản (Admin, Nhân viên, Khách hàng)
- ✅ 4 sản phẩm Tết
- ✅ 3 vai trò (Admin, Nhân viên, Khách hàng)

---

### **Bước 4: Chạy ứng dụng Spring Boot**

Sau khi import xong database, chạy ứng dụng:

```bash
./run.sh
```

Ứng dụng sẽ:

1. ✅ Kết nối database thành công
2. ✅ Hiển thị danh sách tài khoản trong console
3. ✅ Mở browser tại http://localhost:8080

---

## 🔑 Tài khoản đăng nhập mẫu

Sau khi import xong, bạn có thể đăng nhập bằng:

### **Admin**

- Email: `admin@webtet.com`
- Password: `admin123`
- Vai trò: Quản trị viên (có quyền truy cập tất cả)

### **Nhân viên**

- Email: `staff@webtet.com`
- Password: `staff123`
- Vai trò: Nhân viên (xử lý đơn hàng)

### **Khách hàng**

- Email: `customer@webtet.com`
- Password: `cust123`
- Vai trò: Khách hàng (mua sắm)

---

## ❗ Lưu ý quan trọng

1. **Tên database**: `WebBanHangTet` (chính xác theo file SQL)
2. **Tên bảng**: PascalCase (`TaiKhoan`, `SanPham`, `BaiViet`) - KHÔNG phải snake_case
3. **Connection string**: `jdbc:sqlserver://localhost:1433;databaseName=WebBanHangTet`
4. **Username/Password**: `sa` / `Aa@123456`

---

## 🐛 Troubleshooting

### **Lỗi: "Invalid object name 'tai_khoan'"**

➡️ **Nguyên nhân**: Chưa import file SQL hoặc tên database sai
➡️ **Giải pháp**: Import lại file SQL theo hướng dẫn ở trên

### **Lỗi: "Login failed for user 'sa'"**

➡️ **Nguyên nhân**: Sai password
➡️ **Giải pháp**: Kiểm tra password trong `application.properties` phải là `Aa@123456`

### **Lỗi: "Cannot open database 'WebBanHangTet'"**

➡️ **Nguyên nhân**: Database chưa được tạo
➡️ **Giải pháp**: Chạy lại file SQL để tạo database

---

## ✅ Checklist hoàn thành

- [ ] SQL Server Docker container đang chạy (`docker ps`)
- [ ] Đã import file SQL thành công
- [ ] Database `WebBanHangTet` đã được tạo
- [ ] Các bảng `TaiKhoan`, `SanPham`, `BaiViet` đã có dữ liệu
- [ ] Đã cập nhật `application.properties` (đã làm tự động)
- [ ] Chạy `./run.sh` thành công
- [ ] Đăng nh��p được với tài khoản admin@webtet.com / admin123

---

**Chúc bạn thành công! 🎉**

