package com.mth.academicfeesystem.modules.user.service;

import com.mth.academicfeesystem.modules.user.dto.request.ChangePasswordRequest;
import com.mth.academicfeesystem.modules.user.dto.request.UpdateProfileRequest;
import com.mth.academicfeesystem.modules.user.dto.response.MyProfileResponse;
import com.mth.academicfeesystem.security.CustomUserPrincipal;

public interface UserService {
    MyProfileResponse getMyProfile(Long userId);
    MyProfileResponse updateMyProfile(CustomUserPrincipal principal,UpdateProfileRequest request);
    void changePassword(Long userId, ChangePasswordRequest request);
    void resetPassword(Long userId);
}
