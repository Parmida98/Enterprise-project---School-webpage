package com.parmida98.school_webpage.user.dto;


public record UserResponseDTO(
        String username
) {
}

/*
Denna DTO hjälper till att:
Förebygga data leakage
Skydda känslig användarinformation
Tvinga backend att vara tydlig med vad som exponeras
 */