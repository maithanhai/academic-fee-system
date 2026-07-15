### Chức năng chính
 - Đối với Học sinh:
    + Xem bảng điểm
    + Theo dõi tiền chưa đóng và thực hiện thanh toán online.
    + Nhận thông báo qua email từ nhà trường.
 - Đối với Giáo viên:
    + Có 2 nhiệm vụ là giáo viên chủ nhiệm lớp và giáo viên bộ môn
    + Giáo viên chủ nhiệm xác nhận thu tiền mặt và xem thống kê điểm lớp học
    + Giáo viên bộ môn nhập điểm thành phần cho các lớp được phân công giảng dạy.
 - Đối với Admin:
    + Tạo tài khoản, phân công giảng dạy.
    + Tạo danh mục môn học và các đợt thu phí.
    + Tạo thông báo tới các học sinh
### Giải pháp kỹ thuật
 - Thanh toán online: Tích hợp cổng thanh toán VNPay 
- Thông báo tới học sinh: Tích hợp dịch vụ SMTP để gửi Email thông báo trực tiếp đến học sinh.
- Bảo mật và Phân quyền: Sử dụng Spring Security kết hợp JWT.
- Minh bạch dữ liệu: Ứng dụng Spring AOP để tự động lưu lại lịch sử các thao tác nhạy cảm (Sửa điểm, Hủy thu tiền).
- Tối ưu hiệu năng: Xử lý truy vấn N+1 Select trong JPA/Hibernate.
- Các kỹ thuật khác: Quản lý lỗi tập trung (Global Exception Handling), tích hợp Swagger, DTO
### Công nghệ sử dụng
- Frontend: ReactJS
- Backend: Java Spring Boot, Hibernate
- Cơ sở dữ liệu: MySQL

