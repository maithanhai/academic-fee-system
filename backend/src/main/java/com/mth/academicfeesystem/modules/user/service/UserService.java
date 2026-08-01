package com.mth.academicfeesystem.modules.user.service;

import com.mth.academicfeesystem.modules.user.dto.request.ChangeActiveRequest;
import com.mth.academicfeesystem.modules.user.dto.request.ChangePasswordRequest;
import com.mth.academicfeesystem.modules.user.dto.request.UpdateProfileRequest;
import com.mth.academicfeesystem.modules.user.dto.response.MyProfileResponse;

public interface UserService {
    MyProfileResponse getMyProfile(Long userId);
    MyProfileResponse updateMyProfile(Long userId,UpdateProfileRequest request);
    void changePassword(Long userId, ChangePasswordRequest request);
    void changeActive(Long userId, ChangeActiveRequest request);
}
