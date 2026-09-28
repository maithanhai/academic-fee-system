
## Công nghệ sử dụng

### Backend

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC và Spring Data JPA/Hibernate
- Spring Security và JWT access/refresh token
- Spring AOP cho audit log nghiệp vụ
- MySQL
- MapStruct, Lombok và Jakarta Bean Validation
- Springdoc OpenAPI/Swagger
- Apache POI cho các luồng import Excel
- Spring Mail cho thông báo email

### Frontend

- React 19
- Vite
- React Router
- Redux Toolkit và React Redux
- Ant Design
- Axios
- Day.js

## Chức năng chính

### Học sinh

- Đăng nhập và quản lý hồ sơ cá nhân.
- Xem bảng điểm theo năm học.
- Xem danh sách và chi tiết hóa đơn học phí.
- Thanh toán hóa đơn qua luồng thanh toán được triển khai trong backend.
- Xem thông báo của nhà trường.

### Giáo viên

- Giáo viên chủ nhiệm xem các lớp được phân công, danh sách học sinh và bảng điểm lịch sử của lớp.
- Giáo viên chủ nhiệm xem hóa đơn của học sinh trong lớp, xác nhận thu tiền mặt và hoàn tác hóa đơn theo quyền assignment.
- Giáo viên bộ môn xem lớp/môn được phân công và xem bảng điểm.
- Giáo viên bộ môn nhập hoặc cập nhật điểm theo lớp và môn được phân công.

### Admin

- Quản lý tài khoản giáo viên, học sinh và tổ bộ môn.
- Quản lý môn học, khóa học, năm học, học kỳ và lớp.
- Xếp lớp, chuyển lớp và lên lớp tự động.
- Phân công giáo viên chủ nhiệm và giáo viên bộ môn.
- Cấu hình cột điểm và quản lý điểm.
- Tạo đợt thu, phát hành hóa đơn và xử lý nghiệp vụ tài chính.
- Tạo/gửi thông báo.
- Xem audit log của các nghiệp vụ nhạy cảm như cập nhật điểm và xử lý hóa đơn.

## Hướng dẫn cài đặt

### Yêu cầu

- JDK 21.
- MySQL đang chạy ở `localhost:3306`.
- Node.js và npm.
- Git nếu clone project từ repository.

### 1. Cấu hình backend

Tạo database `academic_fee_system` hoặc để ứng dụng tự tạo database theo cấu hình hiện tại. Backend đọc cấu hình từ `backend/src/main/resources/application.yml` và có thể ghi đè bằng biến môi trường trong file `backend/.env`:

```env
DB_URL=your_url_database
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
JWT_SECRET=your_secret_key_at_least_256_bits
MAIL_USERNAME=
MAIL_PASSWORD=
```

Trong giai đoạn phát triển, Hibernate đang dùng `ddl-auto: update` để cập nhật bảng từ entity. Không dùng mật khẩu mặc định khi chạy môi trường thật.

### 2. Chạy backend

Windows:

```powershell
cd backend
./mvnw.cmd spring-boot:run
```

Backend mặc định chạy tại `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### 3. Chạy frontend

Mở terminal khác:

```powershell
cd frontend
npm install
npm run dev
```

Frontend mặc định chạy tại địa chỉ Vite in trong terminal là `http://localhost:5173`.

Các script frontend:

```powershell
npm run dev
npm run build
npm run lint
npm run preview
```

### 4. Mail local

Backend hiện cấu hình SMTP local ở `localhost:1025`, đây là cổng SMTP của MailDev để ứng dụng gửi email. Giao diện web MailDev thường chạy ở `http://localhost:1080` để xem email. Khi cần kiểm thử email, chạy MailDev hoặc một SMTP testing server tương thích; nếu dùng cổng khác, thay các giá trị `spring.mail` trong cấu hình.

### 5. Kiểm tra build

Backend:

```powershell
cd backend
./mvnw.cmd -DskipTests compile
```

Frontend:

```powershell
cd frontend
npm run build
npm run lint
```

## Tài liệu bổ sung

- [API documentation](docs/api-docs.md)
- [Database design](docs/database-design.md)
