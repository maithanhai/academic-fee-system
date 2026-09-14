package com.mth.academicfeesystem.modules.people.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.enums.Gender;
import com.mth.academicfeesystem.common.enums.Role;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.CohortRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.people.dto.request.StudentSearchRequest;
import com.mth.academicfeesystem.modules.people.dto.request.StudentAdminUpdateRequest;
import com.mth.academicfeesystem.modules.people.dto.response.ImportStudentsResult;
import com.mth.academicfeesystem.modules.people.dto.response.StudentDetailResponse;
import com.mth.academicfeesystem.modules.people.dto.response.StudentResponse;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.mapper.StudentMapper;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.people.service.StudentService;
import com.mth.academicfeesystem.modules.user.dto.request.RegisterStudentRequest;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.mapper.UserMapper;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepo;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepo;
    private final AcademicYearRepository academicYearRepo;
    private final SchoolClassRepository schoolClassRepo;
    private final CohortRepository cohortRepo;
    private final ClassEnrollmentRepository classEnrollmentRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponse<StudentResponse> searchStudents(StudentSearchRequest request, Pageable pageable) {
        Specification<Student> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", JoinType.LEFT);
                root.fetch("cohort", JoinType.LEFT);
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();
            var userJoin = root.join("user", JoinType.LEFT);
            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String searchPattern = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(userJoin.get("username")), searchPattern),
                        cb.like(cb.lower(userJoin.get("fullName")), searchPattern)));
            }

            if (request.getActive() != null) {
                predicates.add(cb.equal(userJoin.get("active"), request.getActive()));
            }

            if (request.getCohortId() != null) {
                var cohortJoin = root.join("cohort", JoinType.LEFT);
                predicates.add(cb.equal(cohortJoin.get("id"), request.getCohortId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending());
        Page<Student> studentPage = studentRepo.findAll(spec, sortedPageable);
        return studentMapper.toPageResponse(studentPage);
    }

    @Override
    public StudentDetailResponse getStudentById(Long id) {
        Student student = studentRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Student not found"));
        return studentMapper.toDetailResponse(student);
    }

    @Transactional
    @Override
    public StudentDetailResponse updateStudent(Long id, StudentAdminUpdateRequest request) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tồn tại học sinh"));
        User user = student.getUser();
        userMapper.toEntity(request, user);
        studentMapper.toEntity(request, student);
        userRepo.save(user);
        studentRepo.save(student);
        return studentMapper.toDetailResponse(student);
    }

    @Transactional
    public ImportStudentsResult importStudentsExcel(MultipartFile file, Long academicYearId, Long cohortId) {
        AcademicYear academicYear = academicYearRepo.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
        Cohort cohort = cohortRepo.findById(cohortId)
                .orElseThrow(() -> new ResourceNotFoundException("Khóa học không tồn tại"));
        List<SchoolClass> classesInYear = schoolClassRepo.findByAcademicYearId(academicYear.getId());

        Map<String, SchoolClass> classMap = classesInYear.stream()
                .collect(Collectors.toMap(c -> normalizeClassName(c.getName()), c -> c));

        long currentCount = studentRepo.countByCohortId(cohort.getId());
        long totalImported = 0;
        List<String> skippedRows = new ArrayList<>();

        List<User> usersToSave = new ArrayList<>();
        List<Student> studentsToSave = new ArrayList<>();
        List<ClassEnrollment> enrollmentsToSave = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = normalizeClassName(sheet.getSheetName());

                SchoolClass targetClass = classMap.get(sheetName);
                if (targetClass == null)
                    continue;

                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null || isRowEmpty(row))
                        continue;

                    String fullName = getCellValueAsString(row.getCell(1));
                    LocalDate dob = getCellValueAsDate(row.getCell(2));
                    String genderStr = getCellValueAsString(row.getCell(3));

                    if (isHeaderRow(fullName, genderStr, dob)) {
                        continue;
                    }
                    if (fullName.isBlank() || dob == null) {
                        skippedRows.add("Dòng " + (r + 1) + ": thiếu họ tên hoặc ngày sinh không hợp lệ");
                        continue;
                    }

                    totalImported++;

                    Gender gender = parseGender(genderStr);
                    User user = User.builder()
                            .fullName(fullName)
                            .dateOfBirth(dob)
                            .role(Role.ROLE_STUDENT)
                            .active(true)
                            .gender(gender)
                            .username("hs" + String.format("%03d", ++currentCount) + cohort.getName())
                            .password(passwordEncoder.encode(dob.format(DateTimeFormatter.ofPattern("ddMMyyyy"))))
                            .build();
                    usersToSave.add(user);

                    Student student = Student.builder()
                            .cohort(cohort)
                            .user(user)
                            .build();
                    studentsToSave.add(student);

                    ClassEnrollment enrollment = ClassEnrollment.builder()
                            .student(student)
                            .schoolClass(targetClass)
                            .status(EnrollmentStatus.ACTIVE)
                            .startDate(LocalDate.now())
                            .build();
                    enrollmentsToSave.add(enrollment);
                }
            }
            userRepo.saveAll(usersToSave);
            studentRepo.saveAll(studentsToSave);
            classEnrollmentRepo.saveAll(enrollmentsToSave);

        } catch (Exception e) {
            throw new BusinessException("Lỗi trong quá trình đọc file Excel: " + e.getMessage());
        }

        return new ImportStudentsResult(totalImported, skippedRows);
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && cell.getCellType() != CellType._NONE) {
                return false;
            }
        }
        return true;
    }

    private boolean isHeaderRow(String fullName, String genderStr, LocalDate dob) {
        if (fullName == null || fullName.isBlank())
            return true;
        String normalized = fullName.trim().toLowerCase();
        return normalized.contains("họ") || normalized.contains("ten") || normalized.contains("name")
                || (genderStr != null && genderStr.toLowerCase().contains("giới") && dob == null);
    }

    private String normalizeClassName(String value) {
        if (value == null)
            return "";
        return value.trim().replaceAll("\\s+", "").toUpperCase();
    }

    private Gender parseGender(String genderStr) {
        if (genderStr == null)
            return Gender.FEMALE;
        String normalized = genderStr.trim();
        if (normalized.equalsIgnoreCase("Nam") || normalized.equalsIgnoreCase("Male") || normalized.equalsIgnoreCase("M"))
            return Gender.MALE;
        return Gender.FEMALE;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "";

        switch (cell.getCellType()) {
            case STRING -> {
                return cell.getStringCellValue().trim();
            }
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            }
            case BOOLEAN -> {
                return String.valueOf(cell.getBooleanCellValue());
            }
            case FORMULA -> {
                return cell.toString().trim();
            }
            default -> {
                return cell.toString().trim();
            }
        }
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null)
            return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }

            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty())
                    return null;
                DateTimeFormatter[] formatters = {
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                        DateTimeFormatter.ofPattern("d/M/yyyy"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                };
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDate.parse(value, formatter);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Transactional
    @Override
    public StudentDetailResponse registerStudent(RegisterStudentRequest request) {
        Cohort cohort = cohortRepo.findById(request.cohortId())
                .orElseThrow(() -> new ResourceNotFoundException("Khóa học không tồn tại"));
        long currentStudentCount = studentRepo.countByCohortId(cohort.getId());
        User user = User.builder()
                .fullName(request.fullName())
                .dateOfBirth(request.dateOfBirth())
                .gender(request.gender())
                .username("hs" + String.format("%03d", currentStudentCount + 1) + cohort.getName())
                .password(passwordEncoder.encode(request.dateOfBirth().format(DateTimeFormatter.ofPattern("ddMMyyyy"))))
                .role(Role.ROLE_STUDENT)
                .build();
        userRepo.save(user);

        Student student = Student.builder()
                .cohort(cohort)
                .user(user)
                .build();
        studentRepo.save(student);
        return studentMapper.toDetailResponse(student);
    }

    @Override
    public List<StudentResponse> getStudentsUnenrolled(Long academicYearId) {
        AcademicYear academicYear = academicYearRepo.findById(academicYearId)
                .orElseThrow(() -> new ResourceNotFoundException("Năm học không tồn tại"));
        Integer currentYear = Integer.parseInt(academicYear.getName().substring(0, 4));
        Integer minAdmissionYear = currentYear - 2;
        List<Student> students = studentRepo.findStudentsWithoutActiveEnrollment(academicYearId,
                EnrollmentStatus.ACTIVE, minAdmissionYear);
        return studentMapper.toListResponse(students);
    }
}
