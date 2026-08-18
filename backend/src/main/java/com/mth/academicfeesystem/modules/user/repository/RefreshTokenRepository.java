package com.mth.academicfeesystem.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mth.academicfeesystem.modules.user.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long>{
    Optional<RefreshToken> findByToken(String token);
}
