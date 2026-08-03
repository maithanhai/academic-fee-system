package com.mth.academicfeesystem.modules.academic.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mth.academicfeesystem.common.enums.EnrollmentStatus;
import com.mth.academicfeesystem.common.enums.Gender;
import com.mth.academicfeesystem.common.enums.Role;
import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.common.response.PageResponse;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassRequest;
import com.mth.academicfeesystem.modules.academic.dto.request.SchoolClassSearchRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SchoolClassResponse;
import com.mth.academicfeesystem.modules.academic.entity.AcademicYear;
import com.mth.academicfeesystem.modules.academic.entity.ClassEnrollment;
import com.mth.academicfeesystem.modules.academic.entity.Cohort;
import com.mth.academicfeesystem.modules.academic.entity.SchoolClass;
import com.mth.academicfeesystem.modules.academic.mapper.SchoolClassMapper;
import com.mth.academicfeesystem.modules.academic.repository.AcademicYearRepository;
import com.mth.academicfeesystem.modules.academic.repository.ClassEnrollmentRepository;
import com.mth.academicfeesystem.modules.academic.repository.CohortRepository;
import com.mth.academicfeesystem.modules.academic.repository.SchoolClassRepository;
import com.mth.academicfeesystem.modules.academic.service.SchoolClassService;
import com.mth.academicfeesystem.modules.people.entity.Student;
import com.mth.academicfeesystem.modules.people.repository.StudentRepository;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.repository.UserRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchoolClassServiceImpl implements SchoolClassService {
    private final SchoolClassRepository schoolClassRepo;
    private final SchoolClassMapper schoolClassMapper;
    private final AcademicYearRepository academicYearRepo;
    private final ClassEnrollmentRepository classEnrollmentRepo;
    private final CohortRepository cohortRepo;
    private final StudentRepository studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    // Nằm trong SchoolClassService.java

    @Override
    public PageResponse<SchoolClassResponse> searchClasses(SchoolClassSearchRequest request, Pageable pageable) {
        Specification<SchoolClass> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getClassName() != null && !request.getClassName().trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("className")),
                        "%" + request.getClassName().trim().toLowerCase() + "%"));
            }
            if (request.getGradeLevel() != null) {
                predicates.add(cb.equal(root.get("gradeLevel"), request.getGradeLevel()));
            }
            if (request.getAcademicYearId() != null) {
                predicates.add(cb.equal(root.get("academicYear").get("id"), request.getAcademicYearId()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<SchoolClass> classPage = schoolClassRepo.findAll(spec, pageable);
        return schoolClassMapper.toPageResponse(classPage);
    }

    @Transactional
    @Override
    public SchoolClassResponse createClass(SchoolClassRequest request) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(request.className());
        schoolClass.setGradeLevel(request.gradeLevel());
        AcademicYear academicYear = academicYearRepo.findById(request.academicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));
        schoolClass.setAcademicYear(academicYear);
        SchoolClass savedClass = schoolClassRepo.save(schoolClass);
        return schoolClassMapper.toResponse(savedClass);
    }

    @Transactional()
    public void autoPromoteStudents() {
        int currentYear = LocalDate.now().getYear();
        String newAcademicYear = currentYear + "-" + (currentYear + 1);
        String oldAcademicYear = (currentYear - 1) + "-" + currentYear;

        AcademicYear oldYear = academicYearRepo.findByName(oldAcademicYear)
                .orElseThrow(() -> new ResourceNotFoundException("Old academic year not found: " + oldAcademicYear));

        AcademicYear newYear = academicYearRepo.findByName(newAcademicYear)
                .orElseThrow(() -> new ResourceNotFoundException("New academic year not exists: " + newAcademicYear));

        if (schoolClassRepo.existsByAcademicYearIdAndGradeLevelIn(newYear.getId(), Arrays.asList(11, 12))) {
            throw new DuplicateResourceException(
                    "Hệ thống đã thực hiện lên lớp cho năm học " + newAcademicYear + " rồi!");
        }

        List<SchoolClass> oldClasses = schoolClassRepo.findByAcademicYearIdAndGradeLevelIn(oldYear.getId(),
                Arrays.asList(10, 11));
        if (oldClasses.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Không có lớp Khối 10 hoặc 11 trong năm " + oldAcademicYear + " để lên lớp.");
        }

        List<Long> oldClassIds = oldClasses.stream()
                .map(schoolClass -> schoolClass.getId())
                .collect(Collectors.toList());

        List<ClassEnrollment> allActiveStudents = classEnrollmentRepo.findBySchoolClassIdInAndStatus(oldClassIds,
                EnrollmentStatus.ACTIVE);

        Map<Long, List<ClassEnrollment>> studentsByOldClass = allActiveStudents.stream()
                .collect(Collectors.groupingBy(enrollment -> enrollment.getSchoolClass().getId()));
        List<ClassEnrollment> allEnrollmentsToSave = new ArrayList<>();

        for (SchoolClass oldClass : oldClasses) {

            Integer newGrade = oldClass.getGradeLevel() + 1;
            String newName = oldClass.getName().replaceFirst(
                    String.valueOf(oldClass.getGradeLevel()),
                    String.valueOf(newGrade));

            SchoolClass newClass = SchoolClass.builder()
                    .name(newName)
                    .gradeLevel(newGrade)
                    .academicYear(newYear)
                    .build();

            newClass = schoolClassRepo.save(newClass);

            List<ClassEnrollment> activeStudentsInClass = studentsByOldClass.getOrDefault(oldClass.getId(),
                    Collections.emptyList());

            for (ClassEnrollment oldEnrollment : activeStudentsInClass) {
                oldEnrollment.setStatus(EnrollmentStatus.TRANSFERRED);
                oldEnrollment.setEndDate(LocalDate.now());
                allEnrollmentsToSave.add(oldEnrollment);

                ClassEnrollment newEnrollment = ClassEnrollment.builder()
                        .student(oldEnrollment.getStudent())
                        .schoolClass(newClass)
                        .status(EnrollmentStatus.ACTIVE)
                        .startDate(LocalDate.now())
                        .build();
                allEnrollmentsToSave.add(newEnrollment);
            }
        }
        classEnrollmentRepo.saveAll(allEnrollmentsToSave);
    }

    @Transactional
    public int importExcel(MultipartFile file) {
        int currentYear = LocalDate.now().getYear();
        String currentAcademicYearName = currentYear + "-" + (currentYear + 1);

        AcademicYear academicYear = academicYearRepo.findByName(currentAcademicYearName)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Academic year " + currentAcademicYearName + " not exists"));

        Cohort cohort = cohortRepo.findByName(String.valueOf(currentYear))
                .orElseThrow(() -> new ResourceNotFoundException("Cohort " + currentYear + " not exists"));

        List<SchoolClass> classesInYear = schoolClassRepo.findByAcademicYearId(academicYear.getId());
        Map<String, SchoolClass> classMap = classesInYear.stream()
                .collect(Collectors.toMap(c -> c.getName().trim().toUpperCase(), c -> c));

        long currentCount = studentRepo.countByCohortId(cohort.getId());
        int totalImported = 0;

        List<User> usersToSave = new ArrayList<>();
        List<Student> studentsToSave = new ArrayList<>();
        List<ClassEnrollment> enrollmentsToSave = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName().trim().toUpperCase();
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
                    String address = getCellValueAsString(row.getCell(4));

                    if (fullName.isEmpty() || dob == null)
                        continue;

                    currentCount++;
                    totalImported++;

                    User user = new User();
                    user.setFullName(fullName);
                    user.setDateOfBirth(dob);
                    user.setRole(Role.ROLE_STUDENT);
                    user.setActive(true);
                    user.setGender(genderStr.trim().equalsIgnoreCase("Nam") ? Gender.MALE : Gender.FEMALE);

                    String username = "hs" + cohort.getName() + String.format("%05d", currentCount);
                    user.setUsername(username);
                    user.setPassword(passwordEncoder.encode(dob.toString()));
                    usersToSave.add(user);

                    Student student = new Student();
                    student.setAddress(address);
                    student.setCohort(cohort);
                    student.setUser(user);
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

        return totalImported;
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null)
            return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return "";
    }

    private LocalDate getCellValueAsDate(Cell cell) {
        if (cell == null)
            return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue().trim());
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }
}
