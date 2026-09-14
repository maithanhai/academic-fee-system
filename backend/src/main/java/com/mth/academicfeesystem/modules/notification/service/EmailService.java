package com.mth.academicfeesystem.modules.notification.service;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlContent);
    
} 