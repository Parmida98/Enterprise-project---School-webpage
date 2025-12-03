package com.parmida98.school_webpage.controller;

import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.CustomUserDetails;
import com.parmida98.school_webpage.user.dto.AuthMeDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public AuthMeDTO me(@AuthenticationPrincipal CustomUserDetails principal) {

        // Hämta din Entity från CustomUserDetails
        CustomUser user = principal.getCustomUser();

        return new AuthMeDTO(
                user.getUsername(),
                user.getRoles()
        );
    }
}
