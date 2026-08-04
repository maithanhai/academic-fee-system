package com.mth.academicfeesystem.modules.audit.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mth.academicfeesystem.modules.audit.annotation.Auditable;
import com.mth.academicfeesystem.modules.audit.service.AuditLogService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterReturning(value = "@annotation(auditable)", returning = "result")
    public void logAction(JoinPoint joinPoint, Auditable auditable, Object result) {
        try {
            Long userId = getCurrentUserId();
            String payload = toJson(joinPoint.getArgs());
            String ipAddress = getClientIp();

            auditLogService.save(userId, auditable.action(), auditable.targetTable(), payload, ipAddress);
        } catch (Exception e) {
            log.error("Lỗi khi ghi Audit Log: {}", e.getMessage());
        }
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserPrincipal principal) {
            return principal.getId();
        }
        return null;
    }

    private String toJson(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;
        HttpServletRequest request = attrs.getRequest();
        return request.getRemoteAddr();
    }
}