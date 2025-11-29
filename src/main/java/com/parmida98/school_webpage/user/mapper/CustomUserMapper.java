package com.parmida98.school_webpage.user.mapper;

import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.authority.UserRole;
import com.parmida98.school_webpage.user.dto.CustomUserCreationDTO;
import com.parmida98.school_webpage.user.dto.CustomUserResponseDTO;
import com.parmida98.school_webpage.user.dto.RegisterStudentDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

    /*
    Metod som tar emot en CustomUserCreationDTO och returnerar en CustomUser (entitet).
    Syfte:
    Omvandla data från frontend (DTO) till en databas-färdig entitet.
     */

@Component
public class CustomUserMapper {

    private final PasswordEncoder passwordEncoder;

    public CustomUserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public CustomUser toAdminEntity(CustomUserCreationDTO customUserCreationDTO) {

            CustomUser admin = new CustomUser(
                    customUserCreationDTO.username(),
                    "",
                    customUserCreationDTO.isAccountNonExpired(),
                    customUserCreationDTO.isAccountNonLocked(),
                    customUserCreationDTO.isCredentialsNonExpired(),
                    customUserCreationDTO.isEnabled(),
                    customUserCreationDTO.roles()
            );

            admin.setPassword(customUserCreationDTO.password(), passwordEncoder);

            return admin;
    }


    public CustomUser toStudentEntity(RegisterStudentDTO registerStudentDTO) {

        CustomUser student = new CustomUser(
                registerStudentDTO.username(),
                "",
                true,
                true,
                true,
                true,
                Set.of(UserRole.STUDENT)
        );

        // Hashar lösenordet
        student.setPassword(registerStudentDTO.password(), passwordEncoder);

        return student;
    }


    //Säker exponering av användardata till frontend.
    public CustomUserResponseDTO toUsernameDTO(CustomUser customUser) {

        // All känslig info (lösenord, roller, statusflaggor) filtreras bort här.
        return new CustomUserResponseDTO(customUser.getUsername());
    }

}

