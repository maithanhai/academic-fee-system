# Academic Fee System - Database Schema

## Dữ liệu cốt lõi và phân quyền

### 1. role
```sql
- id (PK)
- name ('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')
```

### 2. user - người dùng
```sql
- id (PK)
- username
- password
- full_name
- phone
- email
- gender
- role_id (FK -> role.id)
- created_date (DATE)
- updated_date (DATE)
- is_active (BOOLEAN)
```

### 3. student - học sinh
```sql
- id (PK, FK -> user.id)
- date_of_birth
- address
- phone_parent
- avatar (Cloudinary)
- cohort_id (FK -> cohort.id)
```

### 4. teacher - giáo viên
```sql
- id (PK, FK -> user.id)
- department_id (FK -> department.id)
```

### 4a. department - tổ bộ môn
```sql
- id (PK)
- name
```

### 4b. teacher_expertise - chuyên môn giảng dạy
```sql
- id (PK)
- teacher_id (FK -> teacher.id)
- subject_id (FK -> subject.id)
```
> Ràng buộc: UNIQUE (teacher_id, subject_id)

## Nhóm trục thời gian và danh mục

### 5. cohort - năm nhập học
```sql
- id (PK)
- name
- admission_year
```

### 6. academic_year - năm học
```sql
- id (PK)
- name
```

### 7. semester - học kì
```sql
- id (PK)
- name
- academic_year_id (FK -> academic_year.id)
```

## Nhóm học vụ (Lớp học và Điểm số)

### 8. subject - môn học
```sql
- id (PK)
- name
- created_date (DATE)
- updated_date (DATE)
- is_active (BOOLEAN)
```

### 9. class - lớp học
```sql
- id (PK)
- name
- grade_level (10, 11, 12)
- academic_year_id (FK -> academic_year.id)
```

### 10. class_enrollment - đăng kí lớp
```sql
- id (PK)
- student_id (FK -> student.id)
- class_id (FK -> class.id)
- status ('ACTIVE', 'TRANSFERRED', 'DROPPED')
- start_date (DATE)
- end_date (DATE)
```

### 11. homeroom_assignment - phân công chủ nhiệm
```sql
- id (PK)
- class_id (FK -> class.id)
- teacher_id (FK -> teacher.id)
- start_date (DATE)
- end_date (DATE)
- status ('ACTIVE', 'ENDED')
```

### 12. teaching_assignment - giáo viên bộ môn
```sql
- id (PK)
- teacher_id (FK -> teacher.id)
- class_id (FK -> class.id)
- subject_id (FK -> subject.id)
```
> Ràng buộc: UNIQUE (teacher_id, class_id, subject_id)

### 13. grade_config - cấu hình bảng điểm
```sql
- id (PK)
- subject_id (FK -> subject.id)
- exam_type ('MIENG', '15_PHUT', '1_TIET', 'HOC_KY')
- coefficient (hệ số)
- max_column
```

### 14. grade - bảng điểm
```sql
- id (PK)
- student_id (FK -> student.id)
- subject_id (FK -> subject.id)
- semester_id (FK -> semester.id)
- exam_type ('MIENG', '15_PHUT', '1_TIET', 'HOC_KY')
- ordinal_number
- score_value
- created_date (DATE)
- updated_date (DATE)
```
> Ràng buộc: UNIQUE (student_id, subject_id, semester_id, exam_type, ordinal_number)

## Nhóm tài chính

### 15. fee - tiền phí liên quan
```sql
- id (PK)
- name
- fee_amount
- academic_year_id (FK -> academic_year.id)
- created_date (DATE)
- due_date (DATE)
- is_active (BOOLEAN)
```

### 16. fee_invoice - hóa đơn phí liên quan
```sql
- id (PK)
- student_id (FK -> student.id)
- fee_id (FK -> fee.id)
- amount (= fee_id.fee_amount tại thời điểm phát hành)
- status ('UNPAID', 'PENDING', 'PAID', 'CANCELED')
- payment_method
- undo_reason
- action_by (FK -> user.id)
- created_date (DATE)
- updated_date (DATE)
- version (INT)
```

### 17. audit_log - nhật kí sửa điểm + hoàn tác đã thu
```sql
- id (PK)
- user_id (FK -> user.id)
- action
- target_table
- payload
- ip_address
- created_date (DATE)
```