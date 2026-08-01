package com.mth.academicfeesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("API - Hệ thống Quản lý Học vụ và tài chính nội bộ THPT")
                        .version("1.0.0")
                        .description("Toàn bộ danh sách API")
                        .contact(new Contact()
                                .name("Mai Thanh Hải")
                                .email("maiithanhai@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                        .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
