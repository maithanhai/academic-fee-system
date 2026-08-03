
---

## 1. Module Auth & Cá nhân (Tài khoản)
- [x] `POST /api/auth/login` — Đăng nhập, cấp Access + Refresh Token
- [ ] `POST /api/auth/refresh` — Xin cấp Token mới bằng Refresh Token
- [ ] `POST /api/auth/logout` — Đăng xuất, vô hiệu hóa Refresh Token
- [x] `GET /api/users/me` — Lấy thông tin hồ sơ của chính mình
- [x] `PUT /api/users/me/profile` — Tự cập nhật hồ sơ (Tên, SĐT, Email...)
- [x] `PUT /api/users/me/password` — Đổi mật khẩu (cần mật khẩu cũ)

---

## 2. Module Quản lý Nhân sự (Admin)
- [x] `GET /api/admin/teachers` — Lấy danh sách Giáo viên
- [x] `GET /api/admin/teachers/{id}` — Xem chi tiết 1 Giáo viên
- [x] `POST /api/admin/teachers` — Tạo mới Giáo viên
- [ ] `PUT /api/admin/teachers/{id}` — Cập nhật hồ sơ Giáo viên (Chưa có)
- [x] `GET /api/admin/students` — Lấy danh sách Học sinh
- [x] `GET /api/admin/students/{id}` — Xem chi tiết 1 Học sinh
- [x] `POST /api/admin/students` — Tạo mới Học sinh
- [ ] `PUT /api/admin/students/{id}` — Cập nhật toàn bộ thông tin Học sinh (Chưa có)
- [x] `PATCH /api/admin/students/{id}` — Thay đổi trạng thái Học sinh (Đang học/Đình chỉ)

---

## 3. Module Khung Đào Tạo (Academic)
- [x] `GET /api/admin/subjects` — Lấy danh sách Môn học
- [x] `POST /api/admin/subjects` — Tạo Môn học mới
- [x] `PUT /api/admin/subjects/{id}` — Cập nhật thông tin Môn học
- [x] `PATCH /api/admin/subjects/{id}/active` — Ẩn/Hiện môn học (Soft Delete)
- [x] `GET /api/admin/cohorts` — Lấy danh sách Khóa học
- [x] `POST /api/admin/cohorts` — Tạo lẻ 1 Khóa học mới
- [x] `GET /api/admin/academic-years` — Lấy danh sách Năm học
- [x] `POST /api/admin/academic-years` — Tạo lẻ 1 Năm học mới
- [x] `POST /api/admin/academic-years/initialize` — Khởi tạo đồng loạt Năm học + Học kỳ + Khóa
- [ ] `GET /api/admin/semesters` — Lấy danh sách Học kỳ (Theo năm học)

---

## 4. Module Lớp học & Xếp lớp (Class & Enrollment)
- [x] `GET /api/admin/classes` — Lấy danh sách Lớp (Lọc theo Năm, Khóa)
- [x] `POST /api/admin/classes` — Tạo thủ công 1 Lớp học mới
- [x] `POST /api/admin/classes/auto-promote` — Lên lớp tự động ( VD: 10A1 -> 11A1)
- [x] `GET /api/admin/classes/{classId}/students` — Xem danh sách học sinh của 1 Lớp
- [x] `POST /api/admin/class-enrollments/import` — Import học sinh vào lớp (Khối 10 đầu vào)
- [x] `PUT /api/admin/class-enrollments/transfer` — Chuyển 1 học sinh sang lớp khác

---

## 5. Module Phân công (Assignment)
- [ ] `POST /api/admin/homeroom-assignments` — Phân công Giáo viên chủ nhiệm cho Lớp
- [ ] `PUT /api/admin/homeroom-assignments/{id}/end` — Kết thúc/Hủy phân công chủ nhiệm
- [ ] `GET /api/teachers/me/homeroom-classes` — GV xem danh sách Lớp mình đang chủ nhiệm
- [ ] `POST /api/admin/teaching-assignments` — Phân công Giáo viên dạy bộ môn cho Lớp
- [ ] `DELETE /api/admin/teaching-assignments/{id}` — Gỡ phân công bộ môn
- [ ] `GET /api/teachers/me/teaching-assignments` — GV xem lịch/danh sách lớp+môn mình dạy

---

## 6. Module Quản lý Điểm số (Grade)
- [ ] `GET /api/admin/grade-configs` — Xem cấu hình điểm (Hệ số, số cột)
- [ ] `POST /api/admin/grade-configs` — Tạo cấu hình điểm cho môn học
- [ ] `PUT /api/admin/grade-configs/{id}` — Sửa cấu hình điểm
- [ ] `POST /api/teachers/grades` — GV nhập điểm (Chỉ được nhập lớp mình dạy)
- [ ] `PUT /api/teachers/grades/{id}` — GV sửa điểm (Yêu cầu lưu Audit log)
- [ ] `GET /api/teachers/classes/{classId}/subjects/{subjectId}/grades` — GV xem bảng điểm lớp mình dạy
- [ ] `GET /api/teachers/homeroom-classes/{classId}/grade-summary` — GVCN xem thống kê điểm toàn lớp mình chủ nhiệm
- [ ] `GET /api/students/me/grades` — Học sinh tự xem bảng điểm cá nhân

---

## 7. Module Tài chính & Học phí (Finance)
- [ ] `GET /api/admin/fees` — Xem danh sách đợt thu học phí
- [ ] `POST /api/admin/fees` — Tạo đợt thu học phí mới
- [ ] `PUT /api/admin/fees/{id}` — Sửa thông tin đợt thu (hạn chót, số tiền)
- [ ] `POST /api/admin/fee-invoices/generate` — Phát hành hóa đơn hàng loạt cho học sinh
- [ ] `GET /api/students/me/fee-invoices` — Học sinh xem hóa đơn học phí của mình
- [ ] `POST /api/students/me/fee-invoices/{id}/pay` — Bấm thanh toán, Server trả về Link VNPay
- [ ] `GET /api/payments/vnpay-return` — Frontend dùng để bắt kết quả VNPay trả về
- [ ] `POST /api/payments/vnpay-ipn` — VNPay Server gọi ẩn về hệ thống để confirm tiền
- [ ] `PUT /api/teachers/fee-invoices/{id}/confirm-cash` — GVCN xác nhận đã thu tiền mặt của học sinh
- [ ] `PUT /api/teachers/fee-invoices/{id}/cancel` — GVCN hủy hóa đơn (Sai sót/hoàn tiền) - Có Audit Log

---

## 8. Module Thông báo & Lịch sử hệ thống (Notification & Audit)
- [ ] `POST /api/admin/notifications` — Tạo và gửi thông báo (tới Học sinh/Lớp)
- [ ] `GET /api/admin/notifications` — Xem lịch sử các thông báo đã gửi
- [ ] `GET /api/students/me/notifications` — Học sinh xem thông báo (của trường/GVCN gửi)
- [ ] `GET /api/admin/audit-logs` — Xem log hệ thống (Ai đã sửa điểm, Ai đã hủy hóa đơn)