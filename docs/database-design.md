# Database Design

Schema dưới đây được suy ra từ các entity JPA hiện tại. Hibernate đang dùng `spring.jpa.hibernate.ddl-auto: update`, vì vậy đây là mô hình runtime hiện tại, chưa phải migration schema cố định.

## Quy ước chung

- Khóa chính của entity kế thừa `BaseEntity` là `BIGINT` tự tăng.
- Entity kế thừa `AuditableEntity` có `created_date` và `updated_date`.
- Enum được lưu bằng tên chuỗi (`EnumType.STRING`).
- Quan hệ `ManyToOne` và `OneToOne` chủ yếu dùng lazy loading.
- Tên bảng lấy từ `@Table`; entity không khai báo `@Table` dùng tên mặc định của Hibernate.

## 1. Người dùng và nhân sự

### `users`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK, tự tăng |
| `username` | VARCHAR(50), bắt buộc, unique |
| `password` | VARCHAR(100), bắt buộc, BCrypt hash |
| `full_name` | VARCHAR(100), bắt buộc |
| `date_of_birth` | DATE, bắt buộc |
| `phone` | VARCHAR(15) |
| `email` | VARCHAR(100), unique nếu có giá trị |
| `gender` | Enum `Gender` |
| `active` | BOOLEAN, mặc định `true` |
| `role` | Enum `Role`: `ROLE_ADMIN`, `ROLE_TEACHER`, `ROLE_STUDENT` |
| `created_date`, `updated_date` | DATETIME |

### `teachers`

- `id`: PK và FK tới `users.id`.
- `department_id`: FK tới `departments.id`, bắt buộc.
- Quan hệ với chuyên môn qua `teacher_expertises`.

### `students`

- `id`: PK và FK tới `users.id`.
- `address`, `phone_parent`: thông tin bổ sung.
- `cohort_id`: FK tới `cohorts.id`, bắt buộc.

### `departments`

- `id`: BIGINT, PK tự tăng.
- `name`: tên tổ bộ môn.

### `teacher_expertises`

- `id`: BIGINT, PK tự tăng.
- `teacher_id`: FK tới giáo viên.
- `subject_id`: FK tới môn học.
- Entity hiện tại chưa khai báo unique constraint cho cặp này.

### `refresh_tokens`

- `id`: BIGINT, PK tự tăng.
- `token`: bắt buộc, unique.
- `expiry_date`: TIMESTAMP, bắt buộc.
- `revoked`: BOOLEAN, bắt buộc.
- `user_id`: FK tới `users.id`, bắt buộc.

## 2. Danh mục thời gian và học vụ

### `academic_years`

- `id`: BIGINT, PK tự tăng.
- `name`: VARCHAR(20), bắt buộc, unique.
- `active`: BOOLEAN, mặc định `true`.

### `semesters`

- `id`: BIGINT, PK tự tăng.
- `name`: enum tên học kỳ.
- `academic_year_id`: FK tới `academic_years`.

### `cohorts`

- `id`: BIGINT, PK tự tăng.
- Các cột còn lại được tạo theo entity `Cohort`.

### `subjects`

- `id`: BIGINT, PK tự tăng.
- `name`: tên môn học.
- `active`: BOOLEAN, mặc định `true`.

### `classes`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK tự tăng |
| `name` | VARCHAR(20), bắt buộc |
| `grade_level` | INT; service phân công giới hạn 10-12 |
| `academic_year_id` | FK tới `academic_years`, bắt buộc |

### `class_enrollments`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK tự tăng |
| `status` | Enum `EnrollmentStatus`, mặc định `ACTIVE` |
| `start_date` | DATE, tự ghi ngày tạo |
| `end_date` | DATE, nullable |
| `class_id` | FK tới `classes`, bắt buộc |
| `student_id` | FK tới `students`, bắt buộc |

## 3. Phân công

### `homeroom_assignments`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK tự tăng |
| `start_date`, `end_date` | DATE; ngày kết thúc nullable |
| `status` | `ACTIVE` hoặc `ENDED` |
| `class_id` | FK tới `classes`, bắt buộc |
| `teacher_id` | FK tới `teachers`, bắt buộc |

Assignment `ENDED` được giữ lại để tra cứu lịch sử. Quyền xem lớp lịch sử và quyền thao tác nghiệp vụ cần được phân biệt ở tầng authorization/service.

### `teaching_assignments`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK tự tăng |
| `teacher_id` | FK tới `teachers`, bắt buộc |
| `class_id` | FK tới `classes`, bắt buộc |
| `subject_id` | FK tới `subjects`, bắt buộc |

Có unique constraint `uk_teaching_assignment` trên `(class_id, subject_id)`.

## 4. Điểm

### `grade_configs`

- `id`: BIGINT, PK tự tăng.
- `subject_id`: FK tới `subjects`.
- `exam_type`: enum `ExamType`.
- `coefficient`: hệ số điểm.
- `max_column`: số cột tối đa.

### `grades`

- `id`: BIGINT, PK tự tăng.
- `student_id`: FK tới `students`.
- `subject_id`: FK tới `subjects`.
- `semester_id`: FK tới `semesters`.
- `exam_type`: enum `ExamType`.
- `ordinal_number`: thứ tự cột.
- `score_value`: Double; request hiện validation trong khoảng 0-10.

Constraint unique của `Grade` cần được đối chiếu trực tiếp với entity hiện tại trước khi viết migration chính thức; không dùng nguyên thiết kế cũ làm nguồn chuẩn.

## 5. Tài chính

### `fees`

- `id`: BIGINT, PK tự tăng.
- `name`, `fee_amount`, `due_date`, `active`.
- `fee_amount`: DECIMAL(12,0).
- `academic_year_id`: FK tới `academic_years`.
- `invoice_count`: giá trị `@Formula`, không phải cột lưu trữ thông thường.

### `fee_invoices`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK tự tăng |
| `student_id` | FK tới `students`, bắt buộc |
| `fee_id` | FK tới `fees`, bắt buộc |
| `amount` | DECIMAL(12,0), bắt buộc |
| `status` | Enum `InvoiceStatus`, bắt buộc |
| `payment_method` | Enum `PaymentMethod` |
| `undo_reason` | nullable |
| `action_by` | FK tới `users`, nullable |
| `version` | INT, optimistic locking |
| `created_date`, `updated_date` | DATETIME |

Unique constraint `uk_fee_invoice_student_fee` trên `(student_id, fee_id)`.

## 6. Thông báo và audit

### `notifications`

Entity hiện tại dùng bảng `notifications`, gồm nội dung, trạng thái active và các trường thời gian theo `Notification` entity. Chi tiết kiểu dữ liệu cần lấy theo entity khi tạo migration chính thức.

### `audit_logs`

| Cột | Kiểu/ý nghĩa |
|---|---|
| `id` | BIGINT, PK tự tăng |
| `user_id` | FK tới `users`, nullable |
| `action` | VARCHAR(50), bắt buộc |
| `target_table` | VARCHAR(50), bắt buộc |
| `payload` | TEXT, nullable; dữ liệu before/after/error theo aspect |
| `ip_address` | VARCHAR(45) |
| `created_date` | DATETIME, tự ghi |

Audit log hiện được tạo bởi Spring AOP với annotation `@Auditable` trên các nghiệp vụ nhạy cảm đã đánh dấu.

## 7. Cấu hình phát triển

Hiện tại `application.yml` dùng MySQL database `academic_fee_system` và `ddl-auto: update`. Trước khi triển khai production nên chuyển sang `validate` và dùng Flyway hoặc Liquibase để kiểm soát schema.
