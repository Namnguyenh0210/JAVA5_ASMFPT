# HƯỚNG DẪN SETUP VÀ CHẠY PROJECT TRÊN WINDOWS

## 🖥️ YÊU CẦU HỆ THỐNG

- Windows 10/11
- RAM: 4GB+ (khuyến nghị 8GB)
- Disk: 2GB free space
- Internet connection

---FSA
DSADSA
ĐASA

## 📋 BƯỚC 1: CÀI ĐẶT JAVA 17+

### Cách 1: Tải từ Adoptium (Khuyến nghị)

1. Truy cập: https://adoptium.net/
2. Chọn **OpenJDK 17 (LTS)**
3. Chọn **Windows**
4. Chọn **x64**
5. Download file `.msi`
6. Chạy file và làm theo hướng dẫn
7. **QUAN TRỌNG**: Tick vào "Set JAVA_HOME variable"

### Cách 2: Tải từ Oracle

1. Truy cập: https://www.oracle.com/java/technologies/downloads/
2. Chọn **Java 17**
3. Download **Windows x64 Installer**
4. Chạy và cài đặt

### Kiểm tra Java đã cài:

```cmd
# Mở Command Prompt (Win + R, gõ "cmd")
java -version
```

**Kết quả mong đợi:**

```
java version "17.0.x"
OpenJDK Runtime Environment...
```

---

## 📋 BƯỚC 2: CÀI ĐẶT MAVEN

### Cách 1: Tải Maven (Khuyến nghị)

1. Truy cập: https://maven.apache.org/download.cgi
2. Download **Binary zip archive** (apache-maven-3.x.x-bin.zip)
3. Giải nén vào `C:\Program Files\Apache\maven`
4. **Cài đặt Environment Variables:**
    - Mở **System Properties** (Win + R, gõ "sysdm.cpl")
    - Click **Environment Variables**
    - Trong **System Variables**, click **New**:
        - Variable name: `MAVEN_HOME`
        - Variable value: `C:\Program Files\Apache\maven`
    - Tìm variable **Path**, click **Edit**
    - Click **New** và thêm: `%MAVEN_HOME%\bin`
    - Click **OK** tất cả

### Cách 2: Dùng Chocolatey (Nếu có)

```cmd
choco install maven
```

### Kiểm tra Maven đã cài:

```cmd
mvn -version
```

**Kết quả mong đợi:**

```
Apache Maven 3.x.x
Maven home: C:\Program Files\Apache\maven
Java version: 17.0.x
```

---

## 📋 BƯỚC 3: CÀI ĐẶT SQL SERVER (Nếu dùng database)

### SQL Server Express (Miễn phí)

1. Truy cập: https://www.microsoft.com/en-us/sql-server/sql-server-downloads
2. Download **SQL Server 2022 Express**
3. Chọn **Basic** installation
4. Làm theo hướng dẫn

### SQL Server Management Studio (SSMS)

1. Truy cập: https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms
2. Download và cài đặt SSMS
3. Kết nối với server: `localhost` hoặc `.\SQLEXPRESS`

### Tạo Database:

```sql
CREATE DATABASE projectend_db;
```

---

## 📋 BƯỚC 4: CLONE VÀ SETUP PROJECT

### 1. Clone project (nếu dùng Git):

```cmd
git clone <repository-url>
cd projectend-spring
```

### 2. Hoặc copy project folder vào máy

### 3. Cập nhật application.properties:

Mở file `src/main/resources/application.properties` và sửa:

```properties
# Cập nhật connection string cho SQL Server của bạn
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=projectend_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YOUR_PASSWORD_HERE
```

---

## 🚀 BƯỚC 5: CHẠY PROJECT

### Cách 1: Dùng file run.bat (Dễ nhất)

1. Mở Windows Explorer
2. Navigate đến thư mục project
3. **Double-click** file `run.bat`
4. Chờ application khởi động
5. Mở browser: http://localhost:8080

### Cách 2: Dùng Command Prompt

1. Mở **Command Prompt** (Win + R, gõ "cmd")
2. Navigate đến thư mục project:
   ```cmd
   cd C:\path\to\your\projectend-spring
   ```
3. Chạy lệnh:
   ```cmd
   mvn clean compile
   mvn spring-boot:run
   ```

### Cách 3: Dùng IDE (IntelliJ IDEA / Eclipse)

1. **IntelliJ IDEA**:
    - File → Open → Chọn thư mục project
    - Wait for indexing
    - Click **Run** button hoặc Shift+F10

2. **Eclipse**:
    - File → Import → Existing Maven Projects
    - Browse to project folder
    - Right-click project → Run As → Spring Boot App

---

## 🛠️ TROUBLESHOOTING

### Lỗi "Java not found":

```cmd
# Kiểm tra JAVA_HOME
echo %JAVA_HOME%

# Kiểm tra PATH
echo %PATH%
```

**Fix**: Cài lại Java và set JAVA_HOME đúng

### Lỗi "Maven not found":

```cmd
# Kiểm tra MAVEN_HOME
echo %MAVEN_HOME%
```

**Fix**: Cài lại Maven và set environment variables

### Lỗi Database Connection:

1. Kiểm tra SQL Server đang chạy:
    - Services → SQL Server (SQLEXPRESS) → Start
2. Kiểm tra connection string trong application.properties
3. Kiểm tra firewall không block port 1433

### Lỗi Port 8080 đã được sử dụng:

```cmd
# Tìm process đang dùng port 8080
netstat -ano | findstr :8080

# Kill process (thay PID bằng số thực)
taskkill /PID <PID> /F
```

### Lỗi "Access Denied":

- Chạy Command Prompt **as Administrator**
- Hoặc thay đổi port trong application.properties:
  ```properties
  server.port=8081
  ```

---

## 📱 TRUY CẬP ỨNG DỤNG

Sau khi chạy thành công:

- **Trang chủ**: http://localhost:8080
- **Giỏ hàng**: http://localhost:8080/giohang
- **Sản phẩm**: http://localhost:8080/sanpham
- **Đăng nhập**: http://localhost:8080/login

---

## 🔄 UPDATE PROJECT

Khi có code mới:

1. Copy file mới vào project
2. Chạy lại:
   ```cmd
   mvn clean compile
   mvn spring-boot:run
   ```

---

## 💡 TIPS CHO WINDOWS

### 1. Dùng Windows Terminal (Khuyến nghị):

- Cài từ Microsoft Store
- Hỗ trợ multiple tabs
- Đẹp hơn Command Prompt

### 2. IDE Alternatives:

- **Visual Studio Code** (free) + Extension Pack for Java
- **NetBeans** (free)
- **IntelliJ IDEA Community** (free)

### 3. Package Managers:

- **Chocolatey**: https://chocolatey.org/ (cài packages dễ hơn)
- **Scoop**: https://scoop.sh/ (alternative)

### 4. Shortcuts hữu ích:

- `Win + R`: Run dialog
- `Win + X`: Admin menu
- `Ctrl + Shift + Esc`: Task Manager

---

## ❓ HỖ TRỢ

Nếu gặp vấn đề:

1. Kiểm tra **Console logs** trong terminal
2. Kiểm tra file **logs/application.log** (nếu có)
3. Google error message + "Spring Boot"
4. Hỏi trong group chat nhóm

---

## ✅ CHECKLIST SETUP

- [ ] Java 17+ installed và working
- [ ] Maven installed và working
- [ ] SQL Server running (nếu dùng database)
- [ ] Project files downloaded
- [ ] application.properties configured
- [ ] run.bat executes successfully
- [ ] Application starts on http://localhost:8080
- [ ] Website loads trong browser

**🎉 Chúc mừng! Bạn đã setup thành công!**
