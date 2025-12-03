package com.parmida98.school_webpage.user.dto;

import com.parmida98.school_webpage.user.authority.UserRole;

import java.util.Set;

public record AuthMeDTO(
        String username,
        Set<UserRole> roles
) {
}

