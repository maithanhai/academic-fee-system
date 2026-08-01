package com.mth.academicfeesystem.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.user.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
} 
