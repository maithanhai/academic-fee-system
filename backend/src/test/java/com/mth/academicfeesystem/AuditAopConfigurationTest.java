package com.mth.academicfeesystem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class AuditAopConfigurationTest {

    @Test
    void shouldEnableSpringAopAndDependency() throws IOException {
        String pomContent = Files.readString(Path.of("pom.xml"));
        assertTrue(pomContent.contains("aspectjweaver"),
                "Project must include AspectJ weaving dependency for AOP support");

        String appClass = Files.readString(Path.of("src/main/java/com/mth/academicfeesystem/AcademicfeesystemApplication.java"));
        assertTrue(appClass.contains("@EnableAspectJAutoProxy"),
                "Application must enable Spring AOP via @EnableAspectJAutoProxy");
    }
}
