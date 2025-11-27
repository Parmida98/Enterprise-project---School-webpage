package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.validation.ValidUsername;

/*
Denna DTO hjälper till att:
✅ Förebygga data leakage
✅ Skydda känslig användarinformation
✅ Tvinga backend att vara tydlig med vad som exponeras
 */
/** CustomUserResponseDTO
 *   Exposes username field only.
 *   Use CustomUserResponseDTO to hide sensitive information.
 * */

public record CustomUserResponseDTO(
        @ValidUsername
        String username
) {
}