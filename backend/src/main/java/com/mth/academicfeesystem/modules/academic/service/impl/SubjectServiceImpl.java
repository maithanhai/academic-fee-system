package com.mth.academicfeesystem.modules.academic.service.impl;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.request.SubjectRequest;
import com.mth.academicfeesystem.modules.academic.dto.response.SubjectResponse;
import com.mth.academicfeesystem.modules.academic.entity.Subject;
import com.mth.academicfeesystem.modules.academic.mapper.SubjectMapper;
import com.mth.academicfeesystem.modules.academic.repository.SubjectRepository;
import com.mth.academicfeesystem.modules.academic.service.SubjectService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepo;
    private final SubjectMapper subjectMapper;

    @Override
    public List<SubjectResponse> getAllSubjects(){
        return subjectMapper.toResponseList(subjectRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
    }
    @Transactional
    @Override
    public SubjectResponse addSubject(SubjectRequest request){
        Subject subject = new Subject();
        subjectMapper.toEntity(request, subject);
        if (subjectRepo.findByName(subject.getName()) != null){
            throw new DuplicateResourceException("Môn học " + subject.getName() + " đã tồn tại trong hệ thống");
        }
        return subjectMapper.toResponse(subjectRepo.save(subject));
    }


    @Transactional
    @Override
    public SubjectResponse updateSubject(Long id, SubjectRequest request){
        Subject subject = subjectRepo.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("Môn học không tồn tại"));
        subjectMapper.toEntity(request, subject);
        Subject existingSubject = subjectRepo.findByName(subject.getName());
        if (existingSubject != null && !existingSubject.getId().equals(id)) {
            throw new DuplicateResourceException("Môn học " + subject.getName() + " đã tồn tại trong hệ thống");
        }
        return subjectMapper.toResponse(subjectRepo.save(subject));
    }

    @Override
    public List<SubjectResponse> getActiveSubjects() {
        List<Subject> activeSubjects = subjectRepo.findByActiveTrue();
        return subjectMapper.toResponseList(activeSubjects);
    }
}
