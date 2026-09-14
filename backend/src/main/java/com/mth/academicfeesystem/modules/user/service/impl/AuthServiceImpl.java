package com.mth.academicfeesystem.modules.user.service.impl;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.mth.academicfeesystem.common.exception.BusinessException;
import com.mth.academicfeesystem.common.exception.ResourceNotFoundException;
import com.mth.academicfeesystem.modules.user.dto.request.LoginRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RefreshTokenRequest;
import com.mth.academicfeesystem.modules.user.dto.response.LoginResponse;
import com.mth.academicfeesystem.modules.user.entity.RefreshToken;
import com.mth.academicfeesystem.modules.user.entity.User;
import com.mth.academicfeesystem.modules.user.repository.RefreshTokenRepository;
import com.mth.academicfeesystem.modules.user.service.AuthService;
import com.mth.academicfeesystem.security.CustomUserPrincipal;
import com.mth.academicfeesystem.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepo;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication;
        // authenticate ném ngoại lệ nếu xảy ra lỗi
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("Tài khoản hoặc mật khẩu không hợp lệ");
        } catch (DisabledException | LockedException e) {
            throw new BusinessException("Account is blocked");
        }
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        String username = userPrincipal.getUsername();
        String fullName = userPrincipal.getUser().getFullName();
        String role = userPrincipal.getUser().getRole().name();
        String accessToken = jwtTokenProvider.generateAccessToken(userId, username, role);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, username);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .user(userPrincipal.getUser())
                .build();
        refreshTokenRepo.save(refreshTokenEntity);

        new LoginResponse(accessToken, refreshToken, "Bearer", userId, username, fullName, role);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(userId)
                .username(username)
                .fullname(fullName)
                .role(role)
                .build();
    }


    @Transactional
    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException("Refresh token không hợp lệ hoặc hết hạn");
        }
        RefreshToken refreshTokenEntity = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token không tồn tại"));
        if (refreshTokenEntity.isRevoked()) {
            throw new BusinessException("Refresh token đã bị thu hồi. Vui lòng đăng nhập lại");
        }
        User user = refreshTokenEntity.getUser();

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getUsername(),
                user.getRole().name());
        String newRefreshTokenString = jwtTokenProvider.generateRefreshToken(user.getId(), user.getUsername());

        refreshTokenRepo.delete(refreshTokenEntity);
        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .token(newRefreshTokenString)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .user(user)
                .build();
        refreshTokenRepo.save(newRefreshTokenEntity);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenString)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .fullname(user.getFullName())
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    @Override
    public void logout(String refreshToken) {
        refreshTokenRepo.findByToken(refreshToken).ifPresent(token -> {
            refreshTokenRepo.delete(token);
        });
    }
}
