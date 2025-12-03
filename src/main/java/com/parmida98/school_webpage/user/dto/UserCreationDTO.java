package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.authority.UserRole;
import com.parmida98.school_webpage.user.validation.ValidPassword;
import com.parmida98.school_webpage.user.validation.ValidUsername;
import jakarta.validation.constraints.*;

import java.util.Set;

public record UserCreationDTO(

        @ValidUsername String username,

        @ValidPassword String password,

        @NotNull boolean isAccountNonExpired,
        @NotNull boolean isAccountNonLocked,
        @NotNull boolean isCredentialsNonExpired,
        @NotNull boolean isEnabled,

        @NotEmpty
        Set<UserRole> roles

) {
}

/*
Records är immutable dataobjekt.
Används typiskt för att ta emot data från frontend (request body).
Spring mappar JSON automatiskt till denna record.
 */

