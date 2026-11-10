package com.mth.academicfeesystem.modules.audit.aspect;

import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mth.academicfeesystem.modules.audit.annotation.Auditable;
import com.mth.academicfeesystem.modules.audit.service.AuditLogService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Around("@annotation(auditable)")
    public Object logAction(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            saveAuditLog(auditable, "after", result);
            return result;
        } catch (Exception ex) {
            saveAuditLog(auditable, "error", ex.getMessage());
            throw ex;
        } finally {
            AuditContext.clear();
        }
    }

    private void saveAuditLog(Auditable auditable, String resultKey, Object resultValue) {
        Map<String, Object> payloadMap = new LinkedHashMap<>();
        payloadMap.put("before", AuditContext.getBefore());
        payloadMap.put(resultKey, resultValue);

        auditLogService.save(
                getCurrentUserId(),
                auditable.action(),
                auditable.targetTable(),
                toJson(payloadMap),
                getClientIp()
        );
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserPrincipal principal) {
            return principal.getId();
        }
        return null;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }

    private String getClientIp() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null)
            return null;
        HttpServletRequest request = attrs.getRequest();
        return request.getRemoteAddr();
    }
}