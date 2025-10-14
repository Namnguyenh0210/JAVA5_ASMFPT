CREATE
DATABASE WebBanHangTet;
GO
USE WebBanHangTet;
GO

-- ==========================
-- BẢNG HỆ THỐNG
-- ==========================

-- Vai Trò
CREATE TABLE VaiTro
(
    MaVT  INT PRIMARY KEY IDENTITY(1,1),
    TenVT NVARCHAR(50) NOT NULL UNIQUE
);

-- Tài Khoản
CREATE TABLE TaiKhoan
(
    MaTK        INT PRIMARY KEY IDENTITY(1,1),
    HoTen       NVARCHAR(100) NOT NULL,
    Email       NVARCHAR(100) UNIQUE NOT NULL,
    MatKhau     NVARCHAR(255) NOT NULL,
    SoDienThoai NVARCHAR(20) NULL,
    MaVT        INT NOT NULL, -- Phân biệt Nhân viên / Khách hàng
    TrangThai   BIT      DEFAULT 1,
    NgayTao     DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaVT) REFERENCES VaiTro (MaVT)
);

-- Địa Chỉ
CREATE TABLE DiaChi
(
    MaDC          INT PRIMARY KEY IDENTITY(1,1),
    MaTK          INT NOT NULL,
    DiaChiChiTiet NVARCHAR(255) NOT NULL,
    MacDinh       BIT DEFAULT 0,
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan (MaTK)
);

-- ==========================
-- SẢN PHẨM
-- ==========================

-- Loại Sản Phẩm
CREATE TABLE LoaiSanPham
(
    MaLoai  INT PRIMARY KEY IDENTITY(1,1),
    TenLoai NVARCHAR(100) NOT NULL UNIQUE
);

-- Sản Phẩm
CREATE TABLE SanPham
(
    MaSP    INT PRIMARY KEY IDENTITY(1,1),
    TenSP   NVARCHAR(200) NOT NULL,
    MoTa    NVARCHAR(MAX) NULL,
    Gia     DECIMAL(18, 2) NOT NULL,
    SoLuong INT            NOT NULL DEFAULT 0,
    HinhAnh NVARCHAR(255) NULL,
    MaLoai  INT            NOT NULL,
    NgayTao DATETIME                DEFAULT GETDATE(),
    FOREIGN KEY (MaLoai) REFERENCES LoaiSanPham (MaLoai)
);

-- Nhập Kho
CREATE TABLE NhapKho
(
    MaNK     INT PRIMARY KEY IDENTITY(1,1),
    MaSP     INT NOT NULL,
    SoLuong  INT NOT NULL,
    NgayNhap DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP)
);

-- ==========================
-- GIỎ HÀNG & ĐƠN HÀNG
-- ==========================

-- Giỏ Hàng (khóa chính ghép)
CREATE TABLE GioHang
(
    MaTK    INT NOT NULL,
    MaSP    INT NOT NULL,
    SoLuong INT NOT NULL,
    PRIMARY KEY (MaTK, MaSP),
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan (MaTK),
    FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP)
);

-- Trạng Thái Đơn Hàng
CREATE TABLE TrangThaiDonHang
(
    MaTTDH  INT PRIMARY KEY IDENTITY(1,1),
    TenTTDH NVARCHAR(50) NOT NULL
);

-- Phương Thức Thanh Toán
CREATE TABLE PhuongThucThanhToan
(
    MaPTTT  INT PRIMARY KEY IDENTITY(1,1),
    TenPTTT NVARCHAR(100) NOT NULL UNIQUE
);

-- Đơn Hàng
CREATE TABLE DonHang
(
    MaDH     INT PRIMARY KEY IDENTITY(1,1),
    MaKH     INT            NOT NULL, -- Khách đặt
    MaNV     INT NULL,                -- Nhân viên xử lý (ban đầu NULL)
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

-- Chi Tiết Đơn Hàng
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

-- ==========================
-- THỐNG KÊ
-- ==========================
CREATE TABLE ThongKe
(
    MaThongKe        INT PRIMARY KEY IDENTITY(1,1),
    NgayBaoCao       DATE           NOT NULL UNIQUE,
    TongDoanhThu     DECIMAL(18, 2) NOT NULL,
    TongDonHang      INT            NOT NULL,
    TongSanPhamBanRa INT            NOT NULL
);

CREATE TABLE DanhGia
(
    MaDG     INT PRIMARY KEY IDENTITY(1,1),
    MaKH     INT NOT NULL,                          -- Khách hàng nào đánh giá
    MaSP     INT NOT NULL,                          -- Sản phẩm nào được đánh giá
    MaDH     INT NOT NULL,                          -- Đơn hàng liên quan (chỉ đánh giá khi mua thành công)
    SoSao    INT CHECK (SoSao BETWEEN 1 AND 5),     -- Số sao (1-5)
    BinhLuan NVARCHAR(500) NULL,                    -- Nội dung bình luận
    NgayDG   DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_DanhGia_KhachHang FOREIGN KEY (MaKH) REFERENCES TaiKhoan (MaTK),
    CONSTRAINT FK_DanhGia_SanPham FOREIGN KEY (MaSP) REFERENCES SanPham (MaSP),
    CONSTRAINT FK_DanhGia_DonHang FOREIGN KEY (MaDH) REFERENCES DonHang (MaDH),
    CONSTRAINT UQ_DanhGia UNIQUE (MaKH, MaSP, MaDH) --mỗi khách chỉ đánh giá 1 lần
);

CREATE TABLE BaiViet
(
    MaBV      INT PRIMARY KEY IDENTITY(1,1),
    MaTK      INT NOT NULL,                     -- Người đăng (Admin/Nhân viên)
    TieuDe    NVARCHAR(200) NOT NULL,           -- Tiêu đề bài viết
    NoiDung   NVARCHAR(MAX) NOT NULL,           -- Nội dung chi tiết
    NgayDang  DATETIME DEFAULT GETDATE(),
    HinhAnh   NVARCHAR(255) NULL,               -- Đường dẫn ảnh minh họa (nếu có)
    TrangThai NVARCHAR(20) DEFAULT N'Hiển thị', -- Hiển thị / Ẩn
    CONSTRAINT FK_BaiViet_TaiKhoan FOREIGN KEY (MaTK) REFERENCES TaiKhoan (MaTK)
);
go
-- Khi thêm chi tiết nhập kho, số lượng sản phẩm tăng
CREATE TRIGGER trg_NhapKho_Insert
    ON NhapKho AFTER INSERT
AS
BEGIN
UPDATE SanPham
SET SoLuong = SoLuong + i.SoLuong FROM SanPham sp
    INNER JOIN Inserted i
ON sp.MaSP = i.MaSP;
END;
GO

-- Khi thêm chi tiết đơn hàng, số lượng sản phẩm giảm
CREATE TRIGGER trg_DonHangChiTiet_Insert
    ON DonHangChiTiet AFTER INSERT
AS
BEGIN
UPDATE SanPham
SET SoLuong = SoLuong - i.SoLuong FROM SanPham sp
    INNER JOIN Inserted i
ON sp.MaSP = i.MaSP;
END;
GO

-- Khi xóa chi tiết đơn hàng, số lượng sản phẩm tăng lại
CREATE TRIGGER trg_DonHangChiTiet_Delete
    ON DonHangChiTiet
    AFTER
DELETE
AS
BEGIN
UPDATE SanPham
SET SoLuong = SoLuong + d.SoLuong FROM SanPham sp
    INNER JOIN Deleted d
ON sp.MaSP = d.MaSP;
END;
GO


-- Vai Trò
INSERT INTO VaiTro (TenVT) VALUES
(N'Khách hàng'),
(N'Nhân viên'),
(N'Admin');

-- Tài Khoản
INSERT INTO TaiKhoan (HoTen, Email, MatKhau, SoDienThoai, MaVT)
VALUES (N'Nguyễn Văn Admin', 'admin@webtet.com', 'admin123', '0909000001', 3),
       (N'Lê Thị Nhân Viên', 'staff@webtet.com', 'staff123', '0909000002', 2),
       (N'Trần Văn Khách', 'customer@webtet.com', 'cust123', '0909000003', 1);

-- Địa chỉ
INSERT INTO DiaChi (MaTK, DiaChiChiTiet, MacDinh)
VALUES (1, N'123 Quang Trung, Gò Vấp, TP.HCM', 1),
       (2, N'456 Lê Lợi, Quận 1, TP.HCM', 1),
       (3, N'789 Phạm Văn Đồng, Thủ Đức, TP.HCM', 1);

-- Loại sản phẩm
INSERT INTO LoaiSanPham (TenLoai)
VALUES (N'Giỏ quà Tết'),
       (N'Hộp quà Tết'),
       (N'Mứt - Bánh kẹo'),
       (N'Bao lì xì & Trang trí');

-- Sản phẩm (KHÔNG còn MaTTSP)
INSERT INTO SanPham (TenSP, MoTa, Gia, SoLuong, HinhAnh, MaLoai)
VALUES (N'Giỏ quà Tết An Khang', N'Giỏ quà với bánh kẹo và trà cao cấp', 500000, 50, 'https://picsum.photos/200/300',
        1),
       (N'Hộp quà Tết Phát Tài', N'Hộp quà sang trọng với rượu vang và socola', 1200000, 20,
        'https://picsum.photos/200/301', 2),
       (N'Mứt dừa non hộp 500g', N'Mứt dừa non thơm ngon, ngọt béo', 150000, 100, 'https://picsum.photos/200/302', 3),
       (N'Bao lì xì vàng 2025 (10 cái)', N'Bao lì xì sang trọng, may mắn năm mới', 30000, 200,
        'https://picsum.photos/200/303', 4);

-- Nhập kho
INSERT INTO NhapKho (MaSP, SoLuong)
VALUES (1, 50),
       (2, 20),
       (3, 100),
       (4, 200);

-- Trạng thái đơn hàng
INSERT INTO TrangThaiDonHang (TenTTDH)
VALUES (N'Chờ xác nhận'),
       (N'Đang giao'),
       (N'Hoàn tất'),
       (N'Đã hủy');

-- Phương thức thanh toán
INSERT INTO PhuongThucThanhToan (TenPTTT)
VALUES (N'COD - Thanh toán khi nhận hàng'),
       (N'Chuyển khoản ngân hàng'),
       (N'Ví điện tử Momo');

-- Đơn hàng (giờ có MaKH và MaNV)
-- Lúc checkout khách hàng đặt => MaNV = NULL
INSERT INTO DonHang (MaKH, MaNV, MaDC, TongTien, MaTTDH, MaPTTT)
VALUES (3, NULL, 3, 530000, 1, 1), -- khách hàng mua giỏ quà + bao lì xì
       (3, NULL, 3, 1200000, 2, 2);
-- khách hàng mua hộp quà

-- Sau khi nhân viên nhận xử lý, có thể cập nhật MaNV, ví dụ:
-- UPDATE DonHang SET MaNV = 2 WHERE MaDH = 1;

-- Chi tiết đơn hàng
INSERT INTO DonHangChiTiet (MaDH, MaSP, SoLuong, DonGia)
VALUES (1, 1, 1, 500000),
       (1, 4, 1, 30000),
       (2, 2, 1, 1200000);

-- Thống kê (có thể tính lại theo đơn hàng ở trên)
INSERT INTO ThongKe (NgayBaoCao, TongDoanhThu, TongDonHang, TongSanPhamBanRa)
VALUES ('2025-01-01', 1730000, 2, 3);
