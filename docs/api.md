# Academic Fee System — API Specification (đầy đủ, kèm phân quyền)

>Phân quyền:
> - `PUBLIC` — không cần đăng nhập
> - `AUTHENTICATED` — chỉ cần đăng nhập, không phân biệt role
> - `ADMIN` — chỉ `ROLE_ADMIN`
> - `TEACHER` — chỉ `ROLE_TEACHER`
> - `STUDENT` — chỉ `ROLE_STUDENT`
> - `SELF` — chỉ chính chủ (dựa vào `userId` lấy từ JWT, không nhận từ path/body)
> - `HOMEROOM_GUARD` — `ROLE_TEACHER` + `AssignmentGuard.isHomeroomTeacherOf(...)` đúng lớp
> - `TEACHING_GUARD` — `ROLE_TEACHER` + `AssignmentGuard.canGradeSubject(...)` đúng lớp+môn

---

## 1. Module `user` (auth, tài khoản)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| POST | `/auth/login` | PUBLIC | `LoginRequest` | `LoginResponse` | Trả JWT access + refresh token |
| POST | `/auth/refresh` | PUBLIC | `RefreshTokenRequest` | `LoginResponse` | Cấp access token mới từ refresh token |
| POST | `/auth/logout` | AUTHENTICATED | - | - | Vô hiệu hóa refresh token (nếu lưu blacklist) |
| GET | `/users/me` | AUTHENTICATED | - | `UserResponse` | Xem hồ sơ chính mình |
| PUT | `/users/me/profile` | AUTHENTICATED + SELF | `UpdateProfileRequest` | `UserResponse` | Tự sửa `fullName`, `phone`, `email`, `gender` |
| PUT | `/users/me/password` | AUTHENTICATED + SELF | `ChangePasswordRequest` | - | Cần `oldPassword` để xác thực |
| GET | `/admin/users` | ADMIN | - (query params phân trang) | `PageResponse<UserResponse>` | Danh sách toàn bộ tài khoản |
| GET | `/admin/users/{id}` | ADMIN | - | `UserResponse` | Xem chi tiết 1 tài khoản |
| PUT | `/admin/users/{id}/status` | ADMIN | `UpdateUserStatusRequest` | - | Khóa/mở tài khoản (`is_active`) |
| PUT | `/admin/users/{id}/role` | ADMIN | `UpdateUserRoleRequest` | - | Đổi role (hiếm dùng, vẫn cần tách riêng theo phân quyền) |

---

## 2. Module `people` (department, teacher, student)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| GET | `/departments` | AUTHENTICATED | - | `List<DepartmentResponse>` | Danh mục, ai xem cũng được |
| POST | `/admin/departments` | ADMIN | `DepartmentRequest` | `DepartmentResponse` | |
| PUT | `/admin/departments/{id}` | ADMIN | `DepartmentRequest` | `DepartmentResponse` | |
| DELETE | `/admin/departments/{id}` | ADMIN | - | - | Chặn xóa nếu còn `teacher` tham chiếu (RESTRICT) |
| POST | `/admin/teachers` | ADMIN | `RegisterTeacherRequest` | `TeacherResponse` | Admin tự nhập `username`, hệ thống tự sinh password = ngày sinh |
| GET | `/admin/teachers` | ADMIN | - | `PageResponse<TeacherResponse>` | |
| GET | `/admin/teachers/{id}` | ADMIN | - | `TeacherResponse` | |
| PUT | `/admin/teachers/{id}` | ADMIN | `TeacherRequest` | `TeacherResponse` | Chỉ sửa `departmentId`, không đụng `User` |
| GET | `/teachers/me` | TEACHER + SELF | - | `TeacherResponse` | Giáo viên xem hồ sơ giảng dạy của mình |
| POST | `/admin/students/import` | ADMIN | Multipart file `.xlsx` | `BulkImportResult` | Import hàng loạt, tự sinh username theo tên+ngày sinh |
| GET | `/admin/students` | ADMIN | - | `PageResponse<StudentResponse>` | |
| GET | `/admin/students/{id}` | ADMIN | - | `StudentResponse` | |
| PUT | `/admin/students/{id}` | ADMIN | `StudentRequest` | `StudentResponse` | Chỉ sửa `address`, `phoneParent`, `cohortId` |
| GET | `/students/me` | STUDENT + SELF | - | `StudentResponse` | Học sinh xem hồ sơ của mình |

---

## 3. Module `academic` (cohort, academic_year, semester, subject, class, enrollment)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| GET | `/cohorts` | AUTHENTICATED | - | `List<CohortResponse>` | |
| POST | `/admin/cohorts` | ADMIN | `CohortRequest` | `CohortResponse` | |
| GET | `/academic-years` | AUTHENTICATED | - | `List<AcademicYearResponse>` | |
| POST | `/admin/academic-years` | ADMIN | `AcademicYearRequest` | `AcademicYearResponse` | |
| GET | `/semesters` | AUTHENTICATED | - (filter theo `academicYearId`) | `List<SemesterResponse>` | |
| POST | `/admin/semesters` | ADMIN | `SemesterRequest` | `SemesterResponse` | |
| GET | `/subjects` | AUTHENTICATED | - | `List<SubjectResponse>` | |
| POST | `/admin/subjects` | ADMIN | `SubjectRequest` | `SubjectResponse` | |
| PUT | `/admin/subjects/{id}` | ADMIN | `SubjectRequest` | `SubjectResponse` | |
| GET | `/classes` | AUTHENTICATED | - (filter theo `academicYearId`, `gradeLevel`) | `List<SchoolClassResponse>` | |
| POST | `/admin/classes` | ADMIN | `SchoolClassRequest` | `SchoolClassResponse` | |
| GET | `/classes/{id}/students` | HOMEROOM_GUARD hoặc ADMIN | - | `List<StudentResponse>` | Danh sách học sinh trong lớp |
| POST | `/admin/class-enrollments` | ADMIN | `ClassEnrollmentRequest` | `ClassEnrollmentResponse` | Ghi danh học sinh vào lớp |
| PUT | `/admin/class-enrollments/{id}/transfer` | ADMIN | `TransferEnrollmentRequest` | `ClassEnrollmentResponse` | Chuyển lớp, đổi `status` → `TRANSFERRED` + tạo bản ghi mới |

---

## 4. Module `assignment` (homeroom_assignment, teaching_assignment)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| POST | `/admin/homeroom-assignments` | ADMIN | `HomeroomAssignmentRequest` | `HomeroomAssignmentResponse` | Phân công chủ nhiệm |
| PUT | `/admin/homeroom-assignments/{id}/end` | ADMIN | - | - | Đổi `status` → `ENDED` |
| GET | `/teachers/me/homeroom-classes` | TEACHER + SELF | - | `List<SchoolClassResponse>` | Giáo viên xem lớp mình chủ nhiệm (status ACTIVE) |
| POST | `/admin/teaching-assignments` | ADMIN | `TeachingAssignmentRequest` | `TeachingAssignmentResponse` | Phân công giáo viên bộ môn |
| DELETE | `/admin/teaching-assignments/{id}` | ADMIN | - | - | |
| GET | `/teachers/me/teaching-assignments` | TEACHER + SELF | - | `List<TeachingAssignmentResponse>` | Giáo viên xem lớp+môn mình dạy |

---

## 5. Module `grade` (grade_config, grade)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| GET | `/grade-configs` | AUTHENTICATED | - (filter theo `subjectId`) | `List<GradeConfigResponse>` | |
| POST | `/admin/grade-configs` | ADMIN | `GradeConfigRequest` | `GradeConfigResponse` | Cấu hình hệ số + số cột từng loại điểm |
| PUT | `/admin/grade-configs/{id}` | ADMIN | `GradeConfigRequest` | `GradeConfigResponse` | |
| POST | `/teacher/grades` | TEACHING_GUARD | `GradeSubmitRequest` | `GradeResponse` | Nhập điểm — bắt buộc check đúng lớp+môn được phân công |
| PUT | `/teacher/grades/{id}` | TEACHING_GUARD | `GradeSubmitRequest` | `GradeResponse` | Sửa điểm — kèm `@Auditable(action="UPDATE_GRADE")` |
| GET | `/teacher/classes/{classId}/subjects/{subjectId}/grades` | TEACHING_GUARD | - | `List<GradeResponse>` | Xem bảng điểm lớp mình dạy |
| GET | `/teacher/homeroom-classes/{classId}/grade-summary` | HOMEROOM_GUARD | - | `List<GradeSummaryResponse>` | Thống kê điểm toàn lớp (chủ nhiệm) |
| GET | `/students/me/grades` | STUDENT + SELF | - (filter theo `semesterId`) | `List<GradeResponse>` | Học sinh xem bảng điểm của mình |

---

## 6. Module `finance` (fee, fee_invoice, vnpay)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| GET | `/fees` | AUTHENTICATED | - | `List<FeeResponse>` | |
| POST | `/admin/fees` | ADMIN | `FeeRequest` | `FeeResponse` | Tạo đợt thu phí mới |
| PUT | `/admin/fees/{id}` | ADMIN | `FeeRequest` | `FeeResponse` | |
| POST | `/admin/fee-invoices/generate` | ADMIN | `GenerateInvoicesRequest` (feeId + danh sách classId/cohortId) | `List<FeeInvoiceResponse>` | Sinh hóa đơn hàng loạt cho học sinh theo lớp/khóa |
| GET | `/students/me/fee-invoices` | STUDENT + SELF | - | `List<FeeInvoiceResponse>` | Học sinh xem hóa đơn của mình |
| POST | `/students/me/fee-invoices/{id}/pay` | STUDENT + SELF | - | `PaymentUrlResponse` | Tạo link thanh toán VNPay |
| GET | `/payments/vnpay-return` | PUBLIC | (query params từ VNPay redirect) | HTML/redirect Frontend | VNPay redirect người dùng về sau khi thanh toán |
| POST | `/payments/vnpay-ipn` | PUBLIC (nhưng validate chữ ký HMAC) | (query params từ VNPay server) | `{RspCode, Message}` | Server-to-server callback xác nhận giao dịch |
| PUT | `/teacher/fee-invoices/{id}/confirm-cash` | HOMEROOM_GUARD | `ConfirmCashPaymentRequest` | `FeeInvoiceResponse` | Chủ nhiệm xác nhận thu tiền mặt lớp mình |
| PUT | `/teacher/fee-invoices/{id}/cancel` | HOMEROOM_GUARD | `CancelInvoiceRequest` (`undoReason`) | `FeeInvoiceResponse` | Hủy/hoàn tác thu — kèm `@Auditable(action="CANCEL_INVOICE")` |

---

## 7. Module `notification`

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| POST | `/admin/notifications` | ADMIN | `NotificationRequest` (kèm danh sách `studentIds` hoặc `classId`/`cohortId`) | `NotificationResponse` | Tạo + gửi email async |
| GET | `/admin/notifications` | ADMIN | - | `PageResponse<NotificationResponse>` | Lịch sử thông báo đã gửi |
| GET | `/students/me/notifications` | STUDENT + SELF | - | `List<NotificationResponse>` | Học sinh xem thông báo của mình |

---

## 8. Module `audit` (chỉ đọc)

| Method | Endpoint | Quyền | Request | Response | Ghi chú |
|---|---|---|---|---|---|
| GET | `/admin/audit-logs` | ADMIN | - (filter theo `targetTable`, `userId`, khoảng ngày) | `PageResponse<AuditLogResponse>` | Không có POST/PUT/DELETE — chỉ AOP tự ghi |

---

## Ma trận quyền tổng hợp theo vai trò

| Nhóm chức năng | ADMIN | TEACHER (bộ môn) | TEACHER (chủ nhiệm) | STUDENT |
|---|---|---|---|---|
| Quản lý tài khoản, phân công | Toàn quyền | - | - | - |
| Danh mục (subject, cohort, class...) | Tạo/sửa | Chỉ xem | Chỉ xem | Chỉ xem |
| Nhập/sửa điểm | - | Đúng lớp+môn được phân công | - | - |
| Xem thống kê điểm lớp | - | - | Đúng lớp chủ nhiệm | - |
| Xem điểm cá nhân | - | - | - | Chỉ của mình |
| Xác nhận thu tiền mặt / hủy hóa đơn | - | - | Đúng lớp chủ nhiệm | - |
| Thanh toán online (VNPay) | - | - | - | Chỉ của mình |
| Xem/sửa hồ sơ cá nhân (`/users/me/*`) | Có | Có | Có | Có |
| Xem audit log | Có | - | - | - |

## Ghi chú triển khai quan trọng

1. **Tất cả endpoint `ADMIN` cần `@PreAuthorize("hasRole('ADMIN')")`** ở tầng Controller, không kiểm tra role bằng `if` trong Service.
2. **`HOMEROOM_GUARD`/`TEACHING_GUARD` dùng SpEL gọi `AssignmentGuard`**, ví dụ:
   ```java
   @PreAuthorize("hasRole('TEACHER') and @assignmentGuard.canGradeSubject(principal.id, #classId, #subjectId)")
   ```
3. **Mọi endpoint `SELF` lấy `userId` từ `Authentication`/JWT principal**, không nhận `id` từ path hoặc body — tránh 1 học sinh gọi API sửa/xem hồ sơ người khác bằng cách đổi `id` trên URL.
4. **`POST /payments/vnpay-ipn` là endpoint duy nhất PUBLIC nhưng xử lý dữ liệu nhạy cảm** — bắt buộc verify chữ ký HMAC (`VNPayUtils`) trước khi xử lý, không dựa vào `@PreAuthorize` vì đây là server-to-server call, không có JWT.
5. **`PUT .../grades/{id}` và `PUT .../fee-invoices/{id}/cancel` bắt buộc gắn `@Auditable`** — đây là 2 hành động nhạy cảm chính mà đề tài yêu cầu minh bạch qua AOP.
