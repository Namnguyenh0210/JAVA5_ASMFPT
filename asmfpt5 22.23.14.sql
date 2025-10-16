-- ===========================
-- FULL SQL: WebBanHangTet (SQL Server)
-- Chạy toàn bộ file này trong SSMS
-- ===========================

/* 1. Xoá DB cũ (nếu có) - cẩn thận trước khi chạy */
IF
EXISTS (SELECT name FROM master.dbo.sysdatabases WHERE name = N'WebBanHangTet')
BEGIN
    ALTER
DATABASE WebBanHangTet SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP
DATABASE WebBanHangTet;
END
GO

CREATE
DATABASE WebBanHangTet;
GO
USE WebBanHangTet;
GO

-- ==========================
-- 1. STRUCTURE (TABLES)
-- ==========================
-- Vai trò
CREATE TABLE VaiTro
(
    MaVT  INT PRIMARY KEY IDENTITY(1,1),
    TenVT NVARCHAR(50) NOT NULL UNIQUE
);

-- Tài khoản
CREATE TABLE TaiKhoan
(
    MaTK        INT PRIMARY KEY IDENTITY(1,1),
    HoTen       NVARCHAR(100) NOT NULL,
    Email       NVARCHAR(100) NOT NULL UNIQUE,
    MatKhau     NVARCHAR(255) NOT NULL,
    SoDienThoai NVARCHAR(20) NULL,
    MaVT        INT NOT NULL,
    TrangThai   BIT      DEFAULT 1,
    NgayTao     DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaVT) REFERENCES VaiTro (MaVT)
);

-- Địa chỉ
CREATE TABLE DiaChi
(
    MaDC          INT PRIMARY KEY IDENTITY(1,1),
    MaTK          INT NOT NULL,
    DiaChiChiTiet NVARCHAR(255) NOT NULL,
    MacDinh       BIT DEFAULT 0,
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan (MaTK)
);

-- Loại sản phẩm
CREATE TABLE LoaiSanPham
(
    MaLoai  INT PRIMARY KEY IDENTITY(1,1),
    TenLoai NVARCHAR(100) NOT NULL UNIQUE
);

-- Sản phẩm
CREATE TABLE SanPham
(
    MaSP    INT PRIMARY KEY IDENTITY(1,1),
    TenSP   NVARCHAR(200) NOT NULL,
    MoTa    NVARCHAR(MAX) NULL,
    Gia     DECIMAL(18, 2) NOT NULL,
    SoLuong INT            NOT NULL DEFAULT 0,
    HinhAnh NVARCHAR(500) NULL,
    MaLoai  INT            NOT NULL,
    NgayTao DATETIME                DEFAULT GETDATE(),
    FOREIGN KEY (MaLoai) REFERENCES LoaiSanPham (MaLoai)
);

-- Nhập kho
CREATE TABLE NhapKho
(
    MaNK     INT PRIMARY KEY IDENTITY(1,1),
    MaSP     INT NOT NULL,
    SoLuong  INT NOT NULL,
    NgayNhap DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP)
);

-- Giỏ hàng (khóa chính ghép)
CREATE TABLE GioHang
(
    MaTK    INT NOT NULL,
    MaSP    INT NOT NULL,
    SoLuong INT NOT NULL,
    PRIMARY KEY (MaTK, MaSP),
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan (MaTK),
    FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP)
);

-- Trạng thái đơn hàng
CREATE TABLE TrangThaiDonHang
(
    MaTTDH  INT PRIMARY KEY IDENTITY(1,1),
    TenTTDH NVARCHAR(50) NOT NULL
);

-- Phương thức thanh toán
CREATE TABLE PhuongThucThanhToan
(
    MaPTTT  INT PRIMARY KEY IDENTITY(1,1),
    TenPTTT NVARCHAR(100) NOT NULL UNIQUE
);

-- Đơn hàng
CREATE TABLE DonHang
(
    MaDH     INT PRIMARY KEY IDENTITY(1,1),
    MaKH     INT            NOT NULL,
    MaNV     INT NULL,
    MaDC     INT            NOT NULL,
    NgayDat  DATETIME DEFAULT GETDATE(),
    TongTien DECIMAL(18, 2) NOT NULL,
    MaTTDH   INT      DEFAULT 1,
    MaPTTT   INT            NOT NULL,
    FOREIGN KEY (MaKH) REFERENCES TaiKhoan (MaTK),
    FOREIGN KEY (MaNV) REFERENCES TaiKhoan (MaTK),
    FOREIGN KEY (MaDC) REFERENCES DiaChi (MaDC),
    FOREIGN KEY (MaTTDH) REFERENCES TrangThaiDonHang (MaTTDH),
    FOREIGN KEY (MaPTTT) REFERENCES PhuongThucThanhToan (MaPTTT)
);

-- Chi tiết đơn hàng
CREATE TABLE DonHangChiTiet
(
    MaDHCT  INT PRIMARY KEY IDENTITY(1,1),
    MaDH    INT            NOT NULL,
    MaSP    INT            NOT NULL,
    SoLuong INT            NOT NULL,
    DonGia  DECIMAL(18, 2) NOT NULL,
    FOREIGN KEY (MaDH) REFERENCES DonHang (MaDH),
    FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP)
);

-- Thống kê
CREATE TABLE ThongKe
(
    MaThongKe        INT PRIMARY KEY IDENTITY(1,1),
    NgayBaoCao       DATE           NOT NULL UNIQUE,
    TongDoanhThu     DECIMAL(18, 2) NOT NULL,
    TongDonHang      INT            NOT NULL,
    TongSanPhamBanRa INT            NOT NULL
);

-- Đánh giá
CREATE TABLE DanhGia
(
    MaDG     INT PRIMARY KEY IDENTITY(1,1),
    MaKH     INT NOT NULL,
    MaSP     INT NOT NULL,
    MaDH     INT NOT NULL,
    SoSao    INT CHECK (SoSao BETWEEN 1 AND 5),
    BinhLuan NVARCHAR(500) NULL,
    NgayDG   DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_DanhGia_KhachHang FOREIGN KEY (MaKH) REFERENCES TaiKhoan (MaTK),
    CONSTRAINT FK_DanhGia_SanPham FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP),
    CONSTRAINT FK_DanhGia_DonHang FOREIGN KEY (MaDH) REFERENCES DonHang (MaDH),
    CONSTRAINT UQ_DanhGia UNIQUE (MaKH, MaSP, MaDH)
);

-- Bài viết (blog)
CREATE TABLE BaiViet
(
    MaBV      INT PRIMARY KEY IDENTITY(1,1),
    MaTK      INT NOT NULL,
    TieuDe    NVARCHAR(200) NOT NULL,
    NoiDung   NVARCHAR(MAX) NOT NULL,
    NgayDang  DATETIME DEFAULT GETDATE(),
    HinhAnh   NVARCHAR(500) NULL,
    TrangThai NVARCHAR(20) DEFAULT N'Hiển thị',
    CONSTRAINT FK_BaiViet_TaiKhoan FOREIGN KEY (MaTK) REFERENCES TaiKhoan (MaTK)
);
GO

-- ==========================
-- 2. TRIGGERS (cập nhật tồn kho tự động)
-- ==========================
CREATE TRIGGER trg_NhapKho_Insert
    ON NhapKho AFTER INSERT
AS
BEGIN
    SET
NOCOUNT ON;
UPDATE sp
SET sp.SoLuong = sp.SoLuong + i.SoLuong FROM SanPham sp
    INNER JOIN inserted i
ON sp.MaSP = i.MaSP;
END;
GO

CREATE TRIGGER trg_DonHangChiTiet_Insert
    ON DonHangChiTiet AFTER INSERT
AS
BEGIN
    SET
NOCOUNT ON;
UPDATE sp
SET sp.SoLuong = sp.SoLuong - i.SoLuong FROM SanPham sp
    INNER JOIN inserted i
ON sp.MaSP = i.MaSP;
END;
GO

CREATE TRIGGER trg_DonHangChiTiet_Delete
    ON DonHangChiTiet AFTER
DELETE
AS
BEGIN
    SET
NOCOUNT ON;
UPDATE sp
SET sp.SoLuong = sp.SoLuong + d.SoLuong FROM SanPham sp
    INNER JOIN deleted d
ON sp.MaSP = d.MaSP;
END;
GO

-- ==========================
-- 3. DỮ LIỆU MẪU (nhiều)
-- ==========================
-- Vai trò
INSERT INTO VaiTro (TenVT) VALUES (N'Khách hàng'), (N'Nhân viên'), (N'Admin');

-- Tài khoản mẫu (Admin, Staff, 8 khách hàng)
INSERT INTO TaiKhoan (HoTen, Email, MatKhau, SoDienThoai, MaVT)
VALUES (N'Nguyễn Văn Admin', 'admin@webtet.com', 'admin123', '0909000001', 3),
       (N'Lê Thị Nhân Viên', 'staff@webtet.com', 'staff123', '0909000002', 2),
       (N'Trần Văn Khách', 'cust1@webtet.com', 'cust123', '0909000003', 1),
       (N'Phạm Thị Hoa', 'cust2@webtet.com', 'cust123', '0909000004', 1),
       (N'Hoàng Văn B', 'cust3@webtet.com', 'cust123', '0909000005', 1),
       (N'Ngô Thị C', 'cust4@webtet.com', 'cust123', '0909000006', 1),
       (N'Lý Văn D', 'cust5@webtet.com', 'cust123', '0909000007', 1),
       (N'Mai Thị E', 'cust6@webtet.com', 'cust123', '0909000008', 1),
       (N'Vũ Văn F', 'cust7@webtet.com', 'cust123', '0909000009', 1),
       (N'Bùi Thị G', 'cust8@webtet.com', 'cust123', '0909000010', 1);

-- Địa chỉ mẫu
INSERT INTO DiaChi (MaTK, DiaChiChiTiet, MacDinh)
VALUES (1, N'123 Quang Trung, Gò Vấp, TP.HCM', 1),
       (2, N'456 Lê Lợi, Quận 1, TP.HCM', 1),
       (3, N'789 Phạm Văn Đồng, Thủ Đức, TP.HCM', 1),
       (4, N'12 Nguyễn Huệ, Quận 1, TP.HCM', 1),
       (5, N'34 Lê Duẩn, TP. Đà Nẵng', 1),
       (6, N'56 Phan Đình Phùng, Hà Nội', 1),
       (7, N'78 Trần Hưng Đạo, Hải Phòng', 1),
       (8, N'90 Lương Khánh Thiện, TP.HCM', 1),
       (9, N'22 Nguyễn Văn Cừ, Cần Thơ', 1),
       (10, N'11 Phạm Ngũ Lão, Nha Trang', 1);

-- Loại sản phẩm (4 loại giữ nguyên)
INSERT INTO LoaiSanPham (TenLoai)
VALUES (N'Giỏ quà Tết'),
       (N'Hộp quà Tết'),
       (N'Mứt - Bánh kẹo'),
       (N'Bao lì xì & Trang trí');

-- ==========================
-- SẢN PHẨM: TỔNG >= 48 (12 mỗi danh mục)
-- Ảnh dùng placeholder (Unsplash / Picsum)
-- ==========================

-- ===== Giỏ quà Tết (12 sp) =====
INSERT INTO SanPham (TenSP, MoTa, Gia, SoLuong, HinhAnh, MaLoai)
VALUES (N'Giỏ quà Tết An Khang', N'Giỏ quà cao cấp gồm trà thượng hạng, bánh quy, hạt dinh dưỡng', 550000, 120,
        'https://images.unsplash.com/photo-1609261732154-0a6f9b5b7d49', 1),
       (N'Giỏ quà Tết Sum Vầy', N'Giỏ quà phong cách truyền thống, hộp trang trí đỏ, nhiều món ngon', 690000, 90,
        'https://images.unsplash.com/photo-1611143669185-5c6c1a2b174c', 1),
       (N'Giỏ quà Tết Lộc Phát', N'Giỏ gồm rượu vang, socola, bánh nhập khẩu', 820000, 60,
        'https://images.unsplash.com/photo-1589712189131-b9e7b7db3f59', 1),
       (N'Giỏ quà Tết Phúc Lộc Thọ', N'Giỏ quà sang trọng, thích hợp tặng đối tác và sếp', 750000, 50,
        'https://images.unsplash.com/photo-1600369671797-485c4e3765b9', 1),
       (N'Giỏ quà Tết Kim Cương', N'Giỏ cao cấp với trà cao cấp và hũ mứt thủ công', 1500000, 30,
        'https://images.unsplash.com/photo-1597740898170-d2b3a5e4cbec', 1),
       (N'Giỏ quà Tết Vạn Phúc', N'Giỏ quà truyền thống, nhiều loại mứt và bánh khô', 480000, 180,
        'https://images.unsplash.com/photo-1609072218762-d2b90d3c2e3b', 1),
       (N'Giỏ quà Tết Hạnh Phúc', N'Giỏ quà gồm trà, mứt dừa, hạt điều, hộp bánh', 580000, 130,
        'https://images.unsplash.com/photo-1597740898170-d2b3a5e4cbec', 1),
       (N'Giỏ quà Tết Bình An', N'Giỏ gọn nhẹ, phù hợp quà tặng gia đình nhỏ', 490000, 140,
        'https://picsum.photos/seed/gio1/400/300', 1),
       (N'Giỏ quà Tết Tài Lộc', N'Giỏ nhiều tầng, thiết kế đẹp, quà tặng doanh nghiệp', 990000, 45,
        'https://picsum.photos/seed/gio2/400/300', 1),
       (N'Giỏ quà Tết Hương Quê', N'Giỏ quà vùng miền: kẹo, mứt đặc sản', 420000, 160,
        'https://picsum.photos/seed/gio3/400/300', 1),
       (N'Giỏ quà Tết Thanh Lịch', N'Giỏ quà tối giản, hộp gỗ, phù hợp tặng sếp', 1250000, 35,
        'https://picsum.photos/seed/gio4/400/300', 1),
       (N'Giỏ quà Tết Gold', N'Giỏ quà Premium, hộp trang trí kim loại, nhiều món nhập khẩu', 1990000, 20,
        'https://picsum.photos/seed/gio5/400/300', 1);

-- ===== Hộp quà Tết (12 sp) =====
INSERT INTO SanPham (TenSP, MoTa, Gia, SoLuong, HinhAnh, MaLoai)
VALUES (N'Hộp quà Tết Phát Tài (S)', N'Hộp quà quyến rũ cho bạn bè, gồm trà và mứt cao cấp', 350000, 200,
        'https://images.unsplash.com/photo-1587300003388-59208cc962cb', 2),
       (N'Hộp quà Tết Phát Tài (M)', N'Phiên bản trung, thêm socola nhập khẩu', 650000, 150,
        'https://images.unsplash.com/photo-1610022191401-9b3b6b50b0aa', 2),
       (N'Hộp quà Tết Phát Tài (L)', N'Phiên bản lớn, sang trọng, thích hợp biếu đối tác', 1200000, 80,
        'https://images.unsplash.com/photo-1589712108067-1a347b2d7e33', 2),
       (N'Hộp quà Tết Ruby', N'Hộp quà thiết kế độc đáo, chất lượng cao', 1350000, 40,
        'https://images.unsplash.com/photo-1609599232800-d3d5f8c11b49', 2),
       (N'Hộp quà Tết Bình An', N'Gồm trà sen, hạt điều, mứt đặc sản', 950000, 70,
        'https://picsum.photos/seed/hop1/400/300', 2),
       (N'Hộp quà Premium Vang & Socola', N'Hộp sang trọng với rượu vang, socola cao cấp', 2200000, 15,
        'https://picsum.photos/seed/hop2/400/300', 2),
       (N'Hộp quà Tết Thiện Ý', N'Hộp quà thiết kế cây treo may mắn', 480000, 160,
        'https://picsum.photos/seed/hop3/400/300', 2),
       (N'Hộp quà Tết Xuân Về', N'Bộ quà gồm mứt, bánh, trà, hộp quà đỏ', 720000, 120,
        'https://picsum.photos/seed/hop4/400/300', 2),
       (N'Hộp quà Tết Quý Tộc', N'Hộp quà limited edition, hộp gỗ sơn mài', 3500000, 8,
        'https://picsum.photos/seed/hop5/400/300', 2),
       (N'Hộp quà Tết Mini (Corporate)', N'Hộp quà nhỏ gọn cho sự kiện công ty', 180000, 300,
        'https://picsum.photos/seed/hop6/400/300', 2),
       (N'Hộp quà Tết Hảo Hạng', N'Hộp kết hợp trà, mứt và bánh handmade', 860000, 95,
        'https://picsum.photos/seed/hop7/400/300', 2),
       (N'Hộp quà Tết An Vui', N'Hộp quà tinh tế, in logo theo yêu cầu (corporate)', 1500000, 25,
        'https://picsum.photos/seed/hop8/400/300', 2);

-- ===== Mứt - Bánh kẹo (12 sp) =====
INSERT INTO SanPham (TenSP, MoTa, Gia, SoLuong, HinhAnh, MaLoai)
VALUES (N'Mứt dừa non hộp 500g', N'Mứt dừa thơm, béo, đóng hộp sang trọng', 150000, 400,
        'https://images.unsplash.com/photo-1609599232800-d3d5f8c11b49', 3),
       (N'Mứt gừng cay ngọt 300g', N'Mứt gừng ấm áp, vị cay nhẹ', 120000, 350,
        'https://images.unsplash.com/photo-1576618148400-4a75e7e4c02e', 3),
       (N'Mứt hạt sen sấy 300g', N'Mứt làm từ hạt sen thơm, ngọt thanh', 180000, 240,
        'https://picsum.photos/seed/mut1/400/300', 3),
       (N'Bánh quy bơ Đan Mạch 250g', N'Bánh quy giòn, hương bơ tự nhiên', 250000, 200,
        'https://picsum.photos/seed/banh1/400/300', 3),
       (N'Kẹo dẻo trái cây 200g', N'Hộp kẹo dẻo tổng hợp, màu sắc bắt mắt', 90000, 500,
        'https://picsum.photos/seed/k1/400/300', 3),
       (N'Hạt điều rang muối 500g', N'Hạt điều tuyển chọn, rang muối giòn', 220000, 300,
        'https://picsum.photos/seed/hat1/400/300', 3),
       (N'Bánh chưng mini (3 cái)', N'Bánh chưng mini tiện lợi, vị truyền thống', 180000, 180,
        'https://picsum.photos/seed/bc1/400/300', 3),
       (N'Mứt cam sấy 250g', N'Mứt cam hương vị tươi mát', 130000, 260, 'https://picsum.photos/seed/mut2/400/300', 3),
       (N'Bánh phu thê hộp 10 cái', N'Bánh truyền thống, thích hợp mâm cỗ', 200000, 150,
        'https://picsum.photos/seed/banh2/400/300', 3),
       (N'Mứt dâu tằm 200g', N'Vị chua ngọt cân bằng, làm từ trái cây tươi', 140000, 210,
        'https://picsum.photos/seed/mut3/400/300', 3),
       (N'Kẹo hạnh nhân socola 200g', N'Kẹo socola bọc hạnh nhân', 190000, 220, 'https://picsum.photos/seed/k2/400/300',
        3),
       (N'Bánh biscotti hạt 250g', N'Bánh biscotti giòn, ăn cùng trà', 160000, 180,
        'https://picsum.photos/seed/banh3/400/300', 3);

-- ===== Bao lì xì & Trang trí (12 sp) =====
INSERT INTO SanPham (TenSP, MoTa, Gia, SoLuong, HinhAnh, MaLoai)
VALUES (N'Bao lì xì vàng 2025 (10 cái)', N'Bao lì xì sang trọng, hoa văn truyền thống', 30000, 1000,
        'https://images.unsplash.com/photo-1641373468097-2cfcb2a14c03', 4),
       (N'Bao lì xì đỏ may mắn (10 cái)', N'Bao lì xì đỏ họa tiết rồng, phượng', 25000, 900,
        'https://images.unsplash.com/photo-1611135903337-8a0b9a39d0e0', 4),
       (N'Bao lì xì handmade (5 cái)', N'Bao lì xì thủ công, vải và nơ', 45000, 700,
        'https://picsum.photos/seed/bao1/400/300', 4),
       (N'Đèn lồng giấy mini (bộ 3)', N'Đèn lồng trang trí phòng khách, treo ban công', 55000, 600,
        'https://images.unsplash.com/photo-1577680710819-4a9e868f9a05', 4),
       (N'Trang trí Tết treo cửa (bộ 1)', N'Trang trí cờ đỏ, chữ Phúc Lộc Thọ', 75000, 400,
        'https://picsum.photos/seed/tr1/400/300', 4),
       (N'Bình hoa Tết sứ', N'Bình hoa làm quà tặng, họa tiết ngày Tết', 220000, 250,
        'https://picsum.photos/seed/hoa1/400/300', 4),
       (N'Dây treo may mắn 3 tầng', N'Dây treo treo cửa, quà tặng doanh nghiệp', 98000, 300,
        'https://picsum.photos/seed/day1/400/300', 4),
       (N'Phong bao lì xì in hình (10 cái)', N'In theo yêu cầu, logo công ty', 120000, 150,
        'https://picsum.photos/seed/bao2/400/300', 4),
       (N'Bộ trang trí bàn thờ Tết', N'Gồm bộ lọ, đĩa, khay nhỏ', 320000, 120,
        'https://picsum.photos/seed/bantho/400/300', 4),
       (N'Set Nến Tết thơm (4 cây)', N'Nến thơm trang trí, hương dịu nhẹ', 180000, 260,
        'https://picsum.photos/seed/nen1/400/300', 4),
       (N'Chậu cảnh mini (Tết)', N'Chậu cảnh giả/mini trang trí bàn', 150000, 220,
        'https://picsum.photos/seed/cay1/400/300', 4),
       (N'Sticker phong thủy 2025', N'Sticker trang trí, may mắn năm mới', 22000, 800,
        'https://picsum.photos/seed/st1/400/300', 4);

-- ==========================
-- 4. NHẬP KHO BAN ĐẦU (tạo lịch sử nhập kho cho nhiều sp)
-- ==========================
-- Thêm nhập kho theo batches (chỉ vài dòng, trigger sẽ cập nhật SoLuong)
INSERT INTO NhapKho (MaSP, SoLuong)
VALUES (1, 120),
       (2, 90),
       (3, 60),
       (4, 50),
       (5, 30),
       (6, 180),
       (7, 130),
       (8, 140),
       (9, 45),
       (10, 160),
       (11, 35),
       (12, 20),
       (13, 200),
       (14, 150),
       (15, 80),
       (16, 40),
       (17, 15),
       (18, 160),
       (19, 120),
       (20, 300),
       (21, 240),
       (22, 260),
       (23, 180),
       (24, 150),
       (25, 100),
       (26, 350),
       (27, 240),
       (28, 180),
       (29, 150),
       (30, 180),
       (31, 100),
       (32, 600),
       (33, 900),
       (34, 400),
       (35, 260),
       (36, 220),
       (37, 300),
       (38, 150),
       (39, 120),
       (40, 250),
       (41, 220),
       (42, 300),
       (43, 120),
       (44, 260),
       (45, 220),
       (46, 120),
       (47, 800),
       (48, 700);
GO

-- ==========================
-- 5. TRẠNG THÁI, THANH TOÁN
-- ==========================
INSERT INTO TrangThaiDonHang (TenTTDH) VALUES (N'Chờ xác nhận'), (N'Đang giao'), (N'Hoàn tất'), (N'Đã hủy');

INSERT INTO PhuongThucThanhToan (TenPTTT)
VALUES (N'COD - Thanh toán khi nhận hàng'),
       (N'Chuyển khoản ngân hàng'),
       (N'Ví điện tử Momo'),
       (N'Ví ZaloPay');

-- ==========================
-- 6. ĐƠN HÀNG MẪU + CHI TIẾT (ví dụ 10 đơn)
-- ==========================
-- Tạo đơn (MaKH = 3, 4, 5... tương ứng ở TaiKhoan)
INSERT INTO DonHang (MaKH, MaNV, MaDC, TongTien, MaTTDH, MaPTTT)
VALUES (3, NULL, 3, 580000, 1, 1),
       (4, 2, 4, 1250000, 3, 2),
       (5, NULL, 5, 350000, 1, 3),
       (6, 2, 6, 1990000, 2, 1),
       (7, NULL, 7, 220000, 1, 1),
       (8, NULL, 8, 820000, 1, 3),
       (9, 2, 9, 450000, 2, 2),
       (10, NULL, 10, 300000, 1, 1),
       (3, 2, 3, 1500000, 3, 4),
       (4, NULL, 4, 270000, 1, 1);

-- Chi tiết đơn hàng (lưu ý: DonGia có thể bằng giá sản phẩm khi order)
INSERT INTO DonHangChiTiet (MaDH, MaSP, SoLuong, DonGia)
VALUES (1, 7, 1, 580000),   -- Giỏ quà Hạnh Phúc
       (2, 11, 1, 1250000), -- Giỏ quà Thanh Lịch
       (3, 3, 2, 175000),   -- giả lập mứt (MaSP 3)
       (4, 5, 1, 1990000),
       (5, 21, 2, 110000),
       (6, 6, 1, 820000),
       (7, 25, 2, 225000),
       (8, 30, 2, 150000),
       (9, 9, 1, 1500000),
       (10, 27, 1, 270000);

-- Trigger trg_DonHangChiTiet_Insert sẽ tự động trừ tồn kho
GO

-- ==========================
-- 7. ĐÁNH GIÁ MẪU
-- ==========================
INSERT INTO DanhGia (MaKH, MaSP, MaDH, SoSao, BinhLuan) VALUES
(3, 7, 1, 5, N'Giỏ quà đẹp, giao nhanh, rất hài lòng!'),
(4, 11, 2, 4, N'Chất lượng tốt nhưng hộp hơi nhỏ hơn tưởng tượng.'),
(5, 3, 3, 5, N'Mứt dừa thơm ngon, gói cẩn thận.');

-- ==========================
-- 8. THỐNG KÊ MẪU
-- ==========================
INSERT INTO ThongKe (NgayBaoCao, TongDoanhThu, TongDonHang, TongSanPhamBanRa)
VALUES ('2025-01-01', 12500000, 32, 85),
       ('2025-01-02', 8200000, 20, 54);

-- ==========================
-- 9. BÀI VIẾT MẪU (BLOG / TIN TỨC)
-- ==========================
INSERT INTO BaiViet (MaTK, TieuDe, NoiDung, HinhAnh)
VALUES (1, N'Mẹo chọn giỏ quà Tết phù hợp cho doanh nghiệp',
        N'Bài viết hướng dẫn chọn giỏ quà theo đối tượng: sếp, đối tác, khách hàng...',
        'https://picsum.photos/seed/blog1/800/400'),
       (2, N'Cách bảo quản mứt Tết lâu mà vẫn ngon', N'Những lưu ý khi bảo quản mứt, tránh ẩm mốc...',
        'https://picsum.photos/seed/blog2/800/400');

-- ==========================
-- 10. INDEX / TÙY CHỌN (tạo index đơn giản cho truy vấn)
-- ==========================
CREATE INDEX IDX_SanPham_TenSP ON SanPham (TenSP);
CREATE INDEX IDX_SanPham_MaLoai ON SanPham (MaLoai);
GO

-- ==========================
-- 11. KIỂM TRA NHANH (select mẫu)
-- ==========================
-- 11.1 Kiểm tra 10 sản phẩm top
SELECT TOP 10 MaSP, TenSP, Gia, SoLuong, MaLoai
FROM SanPham
ORDER BY NgayTao DESC;

-- 11.2 Tổng số sản phẩm
SELECT COUNT(*) AS TotalProducts
FROM SanPham;

-- 11.3 Kiểm tra đơn hàng mẫu
SELECT TOP 20 dh.MaDH, kh.HoTen AS KhachHang, dh.TongTien, dh.NgayDat, ttdh.TenTTDH
FROM DonHang dh
         JOIN TaiKhoan kh ON dh.MaKH = kh.MaTK
         JOIN TrangThaiDonHang ttdh ON dh.MaTTDH = ttdh.MaTTDH
ORDER BY dh.NgayDat DESC;
GO

-- END OF SCRIPT
