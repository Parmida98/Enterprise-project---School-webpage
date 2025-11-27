package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.authority.UserRole;
import com.parmida98.school_webpage.user.validation.ValidPassword;
import com.parmida98.school_webpage.user.validation.ValidUsername;
import jakarta.validation.constraints.*;

import java.util.Set;
/*
Denna record:
✔ Tar emot användardata vid registrering
✔ Validerar användarnamn och lösenord
✔ Säkerställer korrekt kontostatus
✔ Kräver minst en roll
✔ Skyddar systemet från ogiltig input
 */
/*
Records är immutable dataobjekt.
Används typiskt för att ta emot data från frontend (request body).
Spring mappar JSON automatiskt till denna record.
 */
public record CustomUserCreationDTO(

        @ValidUsername String username,

        @ValidPassword String password,

        @NotNull boolean isAccountNonExpired,
        @NotNull boolean isAccountNonLocked,
        @NotNull boolean isCredentialsNonExpired,
        @NotNull boolean isEnabled,
        // @NotNull @AssertTrue boolean acceptAppTerms, // Expect the result NOT to be null, NOT to be False

        @NotEmpty // Map, Collections, Array
        Set<UserRole> roles

) {
}

