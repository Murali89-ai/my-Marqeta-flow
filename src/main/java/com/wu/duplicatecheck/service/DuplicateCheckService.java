package com.wu.duplicatecheck.service;

import com.wu.duplicatecheck.dto.ProfileUpdateRequest;
import com.wu.duplicatecheck.dto.DuplicateCheckResponse;

public interface DuplicateCheckService {
    DuplicateCheckResponse processProfileUpdate(ProfileUpdateRequest request);
}
