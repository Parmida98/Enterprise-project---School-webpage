package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
        @ValidUsername
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}

