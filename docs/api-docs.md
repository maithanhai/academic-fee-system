# API Checklist - Hệ thống Quản lý Học vụ & Tài chính THPT

> **Tổng số API:** 60 Endpoints
> **Tiến độ:** Đang cập nhật...

### 1. Module Auth & Cá nhân (Tài khoản)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 1 | `[x]` | `[x]` | **POST** | `/api/auth/login` | Đăng nhập, cấp Access + Refresh Token |
| 2 | `[x]` | `[x]` | **POST** | `/api/auth/refresh` | Xin cấp Token mới bằng Refresh Token |
| 3 | `[x]` | `[x]` | **POST** | `/api/auth/logout` | Đăng xuất, vô hiệu hóa Token |
| 4 | `[x]` | `[ ]` | **GET** | `/api/users/me` | Lấy thông tin hồ sơ của chính mình |
| 5 | `[x]` | `[ ]` | **PUT** | `/api/users/me/profile` | Tự cập nhật hồ sơ cá nhân |
| 6 | `[x]` | `[ ]` | **PUT** | `/api/users/me/password` | Đổi mật khẩu cá nhân |

### 2. Module Quản lý Nhân sự (Admin)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 7 | `[x]` | `[ ]` | **GET** | `/api/admin/teachers` | Lấy danh sách Giáo viên |
| 8 | `[x]` | `[ ]` | **GET** | `/api/admin/teachers/{id}` | Xem chi tiết 1 Giáo viên |
| 9 | `[x]` | `[ ]` | **POST** | `/api/admin/teachers` | Tạo mới Giáo viên |
| 10 | `[ ]` | `[ ]` | **PUT** | `/api/admin/teachers/{id}` | Cập nhật hồ sơ Giáo viên |
| 11 | `[x]` | `[ ]` | **GET** | `/api/admin/students` | Lấy danh sách Học sinh |
| 12 | `[x]` | `[ ]` | **GET** | `/api/admin/students/{id}` | Xem chi tiết 1 Học sinh |
| 13 | `[x]` | `[ ]` | **POST** | `/api/admin/students` | Tạo mới Học sinh |
| 14 | `[ ]` | `[ ]` | **PUT** | `/api/admin/students/{id}` | Cập nhật toàn bộ thông tin Học sinh |
| 15 | `[x]` | `[ ]` | **PATCH** | `/api/admin/students/{id}/status` | Đổi trạng thái Học sinh (Bảo lưu/Đình chỉ) |

### 3. Module Khung Đào Tạo (Academic)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 16 | `[x]` | `[x]` | **GET** | `/api/admin/subjects` | Lấy danh sách Môn học |
| 17 | `[x]` | `[ ]` | **POST** | `/api/admin/subjects` | Tạo Môn học mới |
| 18 | `[x]` | `[ ]` | **PUT** | `/api/admin/subjects/{id}` | Cập nhật thông tin Môn học |
| 19 | `[x]` | `[ ]` | **PATCH** | `/api/admin/subjects/{id}/active`| Ẩn/Hiện môn học (Soft Delete) |
| 20 | `[x]` | `[ ]` | **GET** | `/api/admin/cohorts` | Lấy danh sách Khóa học |
| 21 | `[x]` | `[ ]` | **POST** | `/api/admin/cohorts` | Tạo Khóa học mới |
| 22 | `[x]` | `[ ]` | **GET** | `/api/admin/academic-years` | Lấy danh sách Năm học |
| 23 | `[x]` | `[ ]` | **POST** | `/api/admin/academic-years` | Tạo Năm học mới |
| 24 | `[x]` | `[ ]` | **POST** | `/api/admin/academic-years/initialize`| Khởi tạo đồng loạt Năm + HK + Khóa |
| 25 | `[ ]` | `[ ]` | **GET** | `/api/admin/semesters` | Lấy danh sách Học kỳ |

### 4. Module Lớp học & Xếp lớp (Class & Enrollment)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 26 | `[x]` | `[ ]` | **GET** | `/api/admin/classes` | Lấy danh sách Lớp |
| 27 | `[x]` | `[ ]` | **POST** | `/api/admin/classes` | Tạo 1 Lớp học mới |
| 28 | `[x]` | `[ ]` | **POST** | `/api/admin/classes/auto-promote` | Lên lớp tự động (Xét duyệt cuối năm) |
| 29 | `[x]` | `[ ]` | **GET** | `/api/admin/classes/{classId}/students`| Xem danh sách Học sinh của 1 Lớp |
| 30 | `[x]` | `[ ]` | **POST** | `/api/admin/class-enrollments/import` | Import danh sách học sinh vào lớp |
| 31 | `[x]` | `[ ]` | **POST** | `/api/admin/class-enrollments/transfer`| Chuyển lớp cho học sinh |

### 5. Module Phân công (Assignment)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 32 | `[x]` | `[ ]` | **POST** | `/api/admin/homeroom-assignments` | Phân công GVCN cho Lớp |
| 33 | `[x]` | `[ ]` | **PUT** | `/api/admin/homeroom-assignments/{id}/end`| Kết thúc nhiệm kỳ GVCN |
| 34 | `[x]` | `[ ]` | **GET** | `/api/teachers/me/homeroom-classes` | GV xem danh sách Lớp mình chủ nhiệm |
| 35 | `[x]` | `[ ]` | **POST** | `/api/admin/teaching-assignments` | Phân công Giáo viên dạy bộ môn cho Lớp |
| 36 | `[x]` | `[ ]` | **DELETE**| `/api/admin/teaching-assignments/{id}` | Gỡ phân công dạy bộ môn |
| 37 | `[x]` | `[ ]` | **GET** | `/api/teachers/me/teaching-assignments`| GV xem lịch/danh sách lớp+môn mình dạy |

### 6. Module Quản lý Điểm số (Grade)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 38 | `[x]` | `[ ]` | **GET** | `/api/admin/grade-configs` | Xem cấu hình điểm (Hệ số, số cột) |
| 39 | `[x]` | `[ ]` | **POST** | `/api/admin/grade-configs` | Tạo cấu hình điểm cho môn học |
| 40 | `[x]` | `[ ]` | **PUT** | `/api/admin/grade-configs/{id}` | Sửa cấu hình điểm |
| 41 | `[x]` | `[ ]` | **POST** | `/api/teachers/grades` | GV nhập điểm (Chỉ được nhập lớp mình dạy)|
| 42 | `[x]` | `[ ]` | **PUT** | `/api/teachers/grades/{id}` | GV sửa điểm (Yêu cầu lưu Audit log) |
| 43 | `[x]` | `[ ]` | **GET** | `/api/teachers/classes/{classId}/subjects/{subjectId}/grades`| GV xem bảng điểm lớp mình dạy |
| 44 | `[ ]` | `[ ]` | **GET** | `/api/teachers/homeroom-classes/{classId}/grade-summary`| GVCN xem thống kê điểm toàn lớp chủ nhiệm|
| 45 | `[x]` | `[ ]` | **GET** | `/api/students/me/grades` | Học sinh tự xem bảng điểm cá nhân |

### 7. Module Tài chính & Học phí (Finance)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 46 | `[ ]` | `[ ]` | **GET** | `/api/admin/fees` | Admin xem danh sách đợt thu học phí |
| 47 | `[ ]` | `[ ]` | **POST** | `/api/admin/fees` | Admin tạo đợt thu học phí mới |
| 48 | `[ ]` | `[ ]` | **PUT** | `/api/admin/fees/{id}` | Admin sửa thông tin đợt thu (hạn chót, giá)|
| 49 | `[ ]` | `[ ]` | **POST** | `/api/admin/fee-invoices/generate` | Admin phát hành hóa đơn hàng loạt |
| 50 | `[ ]` | `[ ]` | **GET** | `/api/teachers/me/homeroom-classes/{classId}/fee-invoices`| GVCN xem danh sách thu tiền của lớp mình |
| 51 | `[ ]` | `[ ]` | **PATCH** | `/api/teachers/fee-invoices/{id}/confirm-cash`| GVCN xác nhận đã thu tiền mặt của HS |
| 52 | `[ ]` | `[ ]` | **PATCH** | `/api/teachers/fee-invoices/{id}/cancel` | GVCN hủy/hoàn tác hóa đơn (Có Audit Log) |
| 53 | `[ ]` | `[ ]` | **GET** | `/api/students/me/fee-invoices` | HS xem hóa đơn học phí cá nhân |
| 54 | `[ ]` | `[ ]` | **POST** | `/api/students/me/fee-invoices/{id}/pay` | HS bấm thanh toán online VNPay |
| 55 | `[ ]` | `[ ]` | **GET** | `/api/public/payments/vnpay-return` | Mở Public: Frontend bắt kết quả VNPay |
| 56 | `[ ]` | `[ ]` | **POST** | `/api/public/payments/vnpay-ipn` | Mở Public: Webhook VNPay báo kết quả |

### 8. Module Thông báo & Lịch sử hệ thống (Notification & Audit)

| STT | Code | Test | Method | API Endpoint | Mô tả chức năng |
| :---: | :---: | :---: | :--- | :--- | :--- |
| 57 | `[ ]` | `[ ]` | **POST** | `/api/admin/notifications` | Tạo và gửi thông báo (tới Học sinh/Lớp) |
| 58 | `[ ]` | `[ ]` | **GET** | `/api/admin/notifications` | Xem lịch sử các thông báo đã gửi |
| 59 | `[ ]` | `[ ]` | **GET** | `/api/students/me/notifications` | Học sinh xem thông báo (của trường/GVCN) |
| 60 | `[ ]` | `[ ]` | **GET** | `/api/admin/audit-logs` | Xem log hệ thống (Sửa điểm, Hủy tiền) |