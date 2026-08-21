package com.mth.academicfeesystem.modules.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import jakarta.servlet.http.HttpServletResponse;

import com.mth.academicfeesystem.common.response.ApiResponse;
import com.mth.academicfeesystem.modules.user.dto.request.LoginRequest;
import com.mth.academicfeesystem.modules.user.dto.request.RefreshTokenRequest;
import com.mth.academicfeesystem.modules.user.dto.response.LoginResponse;
import com.mth.academicfeesystem.modules.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request, HttpServletResponse servletResponse) {
        LoginResponse response=authService.login(request);
        addTokenCookie(servletResponse, ACCESS_TOKEN_COOKIE, response.accessToken());
        addTokenCookie(servletResponse, REFRESH_TOKEN_COOKIE, response.refreshToken());
        return ResponseEntity.ok(new ApiResponse<>("Đăng nhập thành công",response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
        @CookieValue(name = REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
        HttpServletResponse servletResponse
    ){
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Phiên đăng nhập không hợp lệs"));
        }
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        LoginResponse response = authService.refreshToken(request);
        addTokenCookie(servletResponse, ACCESS_TOKEN_COOKIE, response.accessToken());
        addTokenCookie(servletResponse, REFRESH_TOKEN_COOKIE, response.refreshToken());
        return ResponseEntity.ok(new ApiResponse<>("Lấy token mới thành công",response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(
        @CookieValue(name = REFRESH_TOKEN_COOKIE, required = false) String refreshToken,
        HttpServletResponse servletResponse
    ){
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }
        clearTokenCookie(servletResponse, ACCESS_TOKEN_COOKIE);
        clearTokenCookie(servletResponse, REFRESH_TOKEN_COOKIE);
        return ResponseEntity.ok(new ApiResponse<>("Đăng xuất thành công"));
    }

    private void addTokenCookie(HttpServletResponse response, String name, String value) {
        response.addHeader("Set-Cookie", ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .build()
                .toString());
    }

    private void clearTokenCookie(HttpServletResponse response, String name) {
        response.addHeader("Set-Cookie", ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build()
                .toString());
    }

}
