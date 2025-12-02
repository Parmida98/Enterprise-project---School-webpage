package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.validation.ValidUsername;
import com.parmida98.school_webpage.user.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterStudentDTO(

        @ValidUsername
        String username,

        @ValidPassword
        String password,

        @NotBlank
        @Email
        String email

) {
}
