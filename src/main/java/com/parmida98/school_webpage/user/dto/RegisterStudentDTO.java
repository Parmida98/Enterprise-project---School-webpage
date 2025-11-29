package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.validation.ValidUsername;
import com.parmida98.school_webpage.user.validation.ValidPassword;

public record RegisterStudentDTO(
        @ValidUsername
        String username,

        @ValidPassword
        String password
) {
}
