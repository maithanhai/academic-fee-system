# API Documentation

Tài liệu này được đối chiếu với các controller hiện tại. Tất cả endpoint bên dưới dùng tiền tố `/api`. Trừ các endpoint xác thực, request cần gửi access token theo dạng `Authorization: Bearer <access-token>`.

## Quy ước quyền

| Quyền | Ý nghĩa |
|---|---|
| `ADMIN` | Quản trị hệ thống, danh mục, nhân sự, phân công, tài chính và audit |
| `TEACHER` | Giáo viên; quyền GVCN và GVBM tiếp tục được kiểm tra theo assignment của lớp/môn |
| `STUDENT` | Học sinh xem bảng điểm, hóa đơn và thông báo của mình |
| `AUTHENTICATED` | Đã đăng nhập; quyền cụ thể có thể được kiểm tra thêm ở service |

## 1. Xác thực và tài khoản

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| POST | `/api/auth/login` | Public | Đăng nhập, cấp access token và refresh token |
| POST | `/api/auth/refresh` | Public | Cấp access token mới từ refresh token |
| POST | `/api/auth/logout` | Authenticated | Xóa refresh token hiện tại |
| GET | `/api/users/me` | Authenticated | Xem hồ sơ cá nhân |
| PUT | `/api/users/me/profile` | Authenticated | Cập nhật thông tin cá nhân |
| PUT | `/api/users/me/password` | Authenticated | Đổi mật khẩu |

## 2. Quản lý người dùng và nhân sự

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/admin/students` | ADMIN | Danh sách học sinh có phân trang/lọc |
| GET | `/api/admin/teachers` | ADMIN | Danh sách giáo viên có phân trang/lọc |
| GET | `/api/admin/students/{id}` | ADMIN | Chi tiết học sinh |
| GET | `/api/admin/teachers/{id}` | ADMIN | Chi tiết giáo viên |
| POST | `/api/admin/students` | ADMIN | Tạo học sinh |
| POST | `/api/admin/students/import` | ADMIN | Import học sinh bằng multipart file |
| PUT | `/api/admin/students/{id}` | ADMIN | Cập nhật học sinh |
| PUT | `/api/admin/students/{id}/reset-password` | ADMIN | Đặt lại mật khẩu học sinh |
| GET | `/api/admin/students/unenrollments` | ADMIN | Danh sách học sinh chưa xếp lớp |
| POST | `/api/admin/teachers` | ADMIN | Tạo giáo viên |
| PUT | `/api/admin/teachers/{id}` | ADMIN | Cập nhật giáo viên |
| PUT | `/api/admin/teachers/{id}/reset-password` | ADMIN | Đặt lại mật khẩu giáo viên |
| GET | `/api/admin/teachers/active` | ADMIN | Danh sách giáo viên đang hoạt động |
| GET | `/api/departments` | Authenticated | Danh sách tổ bộ môn |
| POST | `/api/admin/departments` | ADMIN | Tạo tổ bộ môn |
| PUT | `/api/admin/departments/{id}` | ADMIN | Cập nhật tổ bộ môn |
| GET | `/api/admin/teacher-expertises/workloads` | ADMIN | Xem chuyên môn và khối lượng giảng dạy |

## 3. Khung đào tạo và lớp học

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/academic-years` | Authenticated | Danh sách năm học |
| POST | `/api/admin/academic-years` | ADMIN | Tạo năm học và dữ liệu liên quan |
| PUT | `/api/admin/academic-years/{id}` | ADMIN | Cập nhật năm học |
| GET | `/api/academic-years/{academicYearId}/classes` | Authenticated | Danh sách lớp theo năm học |
| GET | `/api/academic-years/{academicYearId}/semesters` | Authenticated | Danh sách học kỳ theo năm học |
| GET | `/api/student/academic-years` | STUDENT | Năm học mà học sinh có dữ liệu |
| GET | `/api/cohorts` | Authenticated | Danh sách khóa học |
| POST | `/api/admin/cohorts` | ADMIN | Tạo khóa học |
| GET | `/api/subjects` | Authenticated | Danh sách môn học |
| GET | `/api/admin/subjects` | ADMIN | Danh sách môn học quản trị |
| POST | `/api/admin/subjects` | ADMIN | Tạo môn học |
| PUT | `/api/admin/subjects/{id}` | ADMIN | Cập nhật môn học |
| GET | `/api/classes` | Authenticated | Danh sách lớp theo bộ lọc |
| POST | `/api/classes` | Authenticated | Tạo lớp theo controller hiện tại |
| POST | `/api/classes/auto-promote` | Authenticated | Tự động lên lớp |
| GET | `/api/classes/academic-years/{academicYearId}/total` | Authenticated | Tổng hợp số lớp theo năm học |
| GET | `/api/classes/{classId}/students` | Authenticated | Danh sách học sinh của lớp |
| POST | `/api/admin/class-enrollments` | ADMIN | Import/xếp học sinh vào lớp |
| POST | `/api/admin/class-enrollments/transfer` | ADMIN | Chuyển học sinh sang lớp khác |

## 4. Phân công giáo viên

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| POST | `/api/admin/homeroom-assignments` | Theo controller hiện tại | Phân công GVCN hàng loạt |
| PUT | `/api/admin/homeroom-assignments/{id}/end` | Theo controller hiện tại | Kết thúc phân công GVCN |
| GET | `/api/admin/homeroom-assignments` | ADMIN | Xem phân công GVCN theo năm học |
| GET | `/api/teacher/homeroom-classes` | TEACHER | Xem lớp chủ nhiệm, gồm assignment lịch sử |
| GET | `/api/teacher/homeroom-classes/{classId}/students` | Assignment GVCN | Xem học sinh trong lớp chủ nhiệm |
| GET | `/api/teacher/homeroom-classes/{schoolClassId}/students/{studentId}/transcript` | Assignment GVCN | Xem bảng điểm học sinh trong lớp |
| GET | `/api/admin/teaching-assignments` | ADMIN | Xem phân công GVBM |
| GET | `/api/admin/teaching-assignments/copy-previous` | ADMIN | Chuẩn bị phân công từ năm trước |
| GET | `/api/admin/teaching-assignments/preview-auto` | ADMIN | Xem trước phân công tự động |
| POST | `/api/admin/teaching-assignments/bulk` | ADMIN | Lưu phân công GVBM hàng loạt |
| GET | `/api/teacher/teaching-assignments/academic-years/{academicYearId}` | Theo controller hiện tại | Xem lớp và môn giáo viên được phân công |

## 5. Điểm số

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| POST | `/api/admin/grade-configs` | ADMIN | Tạo cấu hình cột điểm |
| PUT | `/api/admin/grade-configs` | ADMIN | Cập nhật cấu hình cột điểm |
| GET | `/api/admin/grade-configs/{subjectId}` | ADMIN | Xem cấu hình điểm theo môn |
| GET | `/api/admin/grades/students/{studentId}/transcript` | ADMIN | Xem bảng điểm học sinh |
| GET | `/api/teacher/classes/{classId}/subjects/{subjectId}/grades` | GVBM assignment | Xem bảng điểm lớp/môn |
| POST | `/api/teacher/classes/{classId}/subjects/{subjectId}/grades/auto-save` | GVBM assignment | Tạo hoặc cập nhật một điểm |
| GET | `/api/student/grades/transcript` | STUDENT | Học sinh xem bảng điểm theo năm học |

## 6. Học phí và hóa đơn

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/admin/fees` | ADMIN | Danh sách đợt thu |
| POST | `/api/admin/fees` | ADMIN | Tạo đợt thu |
| PUT | `/api/admin/fees/{id}` | ADMIN | Cập nhật đợt thu |
| POST | `/api/admin/fees/{feeId}/invoices/generate` | ADMIN | Phát hành hóa đơn hàng loạt |
| GET | `/api/admin/invoices` | ADMIN | Danh sách hóa đơn |
| GET | `/api/admin/invoices/{invoiceId}` | ADMIN | Chi tiết hóa đơn |
| POST | `/api/admin/invoices/confirm-cash` | ADMIN | Xác nhận thu tiền mặt |
| POST | `/api/admin/invoices/undo` | ADMIN | Hoàn tác hóa đơn |
| POST | `/api/teacher/invoices/confirm-cash` | Assignment GVCN | GVCN xác nhận thu tiền mặt |
| POST | `/api/teacher/invoices/undo` | Assignment GVCN | GVCN hoàn tác hóa đơn |
| GET | `/api/teacher/students/{studentId}/invoices` | Assignment GVCN | Xem hóa đơn của học sinh trong lớp |
| GET | `/api/student/invoices` | STUDENT | Xem danh sách hóa đơn cá nhân |
| GET | `/api/student/invoices/{invoiceId}` | STUDENT | Xem chi tiết hóa đơn cá nhân |
| POST | `/api/student/invoices/{invoiceId}/pay` | STUDENT | Thanh toán hóa đơn theo service hiện tại |

## 7. Thông báo và audit

| Method | Endpoint | Quyền | Mô tả |
|---|---|---|---|
| GET | `/api/admin/notifications` | ADMIN | Danh sách thông báo |
| POST | `/api/admin/notifications` | ADMIN | Tạo thông báo |
| PUT | `/api/admin/notifications/{id}` | ADMIN | Cập nhật thông báo |
| GET | `/api/admin/notifications/{id}` | ADMIN | Chi tiết thông báo |
| POST | `/api/admin/notifications/{id}/send-to-students` | ADMIN | Gửi thông báo tới học sinh |
| GET | `/api/student/notifications` | STUDENT | Danh sách thông báo học sinh |
| GET | `/api/student/notifications/{notificationId}` | STUDENT | Chi tiết thông báo học sinh |
| GET | `/api/admin/audit-logs/grades` | ADMIN | Audit log liên quan đến điểm |
| GET | `/api/admin/audit-logs/grades/{id}` | ADMIN | Chi tiết audit log điểm |
| GET | `/api/admin/audit-logs/invoices` | ADMIN | Audit log liên quan đến hóa đơn |
| GET | `/api/admin/audit-logs/invoices/{id}` | ADMIN | Chi tiết audit log hóa đơn |

## 8. Swagger

- OpenAPI JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html`

Trạng thái test HTTP cần được cập nhật sau khi kiểm thử thực tế; controller tồn tại không đồng nghĩa endpoint đã được test end-to-end.
