package com.mth.academicfeesystem.modules.academic.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.DuplicateResourceException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.academic.dto.request.SubjectActiveRequest;
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
        return subjectMapper.toResponseList(subjectRepo.findAll());
    }
    @Transactional
    @Override
    public void addSubject(SubjectRequest request){
        Subject subject = new Subject();
        subjectMapper.toEntity(request, subject);
        if (subjectRepo.existsByName(subject.getName())){
            throw new DuplicateResourceException("Subject with name " + subject.getName() + " already exists!");
        }
        subjectRepo.save(subject);
    }

    @Transactional
    @Override
    public void changeActiveSubject(Long subjectId, SubjectActiveRequest request){
        Subject subject = subjectRepo.findById(subjectId)
            .orElseThrow(()-> new ResourceNotFoundException("Subject not found"));
        if (subject.getActive().equals(request.active()))
            throw new BusinessException("Subject can not change active");
        subject.setActive(request.active());
        subjectRepo.save(subject);
    }

    @Transactional
    @Override
    public SubjectResponse updateSubject(Long subjectId, SubjectRequest request){
        Subject subject = subjectRepo.findById(subjectId)
            .orElseThrow(()->new ResourceNotFoundException("Subject with id " + subjectId + " not found"));
        subjectMapper.toEntity(request, subject);
        if (subjectRepo.existsByName(subject.getName())){
            throw new DuplicateResourceException("Subject with name " + subject.getName() + " already exists!");
        }
        subjectRepo.save(subject);
        return subjectMapper.toResponse(subject);
    }
}
