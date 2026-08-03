package com.mth.academicfeesystem.modules.assignment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mth.academicfeesystem.modules.assignment.service.HomeroomAssignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeroomAssignmentController {
    private final HomeroomAssignmentService homeroomAssignmentService;

    // @PostMapping("/admin/homeroom-assignments")
}
