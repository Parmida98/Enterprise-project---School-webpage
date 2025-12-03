package com.parmida98.school_webpage.user.mapper;

import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.authority.UserRole;
import com.parmida98.school_webpage.user.dto.UserCreationDTO;
import com.parmida98.school_webpage.user.dto.UserResponseDTO;
import com.parmida98.school_webpage.user.dto.RegisterStudentDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

    /*
    Metod som tar emot en UserCreationDTO och returnerar en CustomUser (entitet).
    Syfte: Omvandla data från frontend (DTO) till en databas-färdig entitet.
     */

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public CustomUser toAdminEntity(UserCreationDTO userCreationDTO) {
        String encodedPassword = passwordEncoder.encode(userCreationDTO.password());

            CustomUser admin = new CustomUser(
                    userCreationDTO.username(),
                    encodedPassword,
                    userCreationDTO.isAccountNonExpired(),
                    userCreationDTO.isAccountNonLocked(),
                    userCreationDTO.isCredentialsNonExpired(),
                    userCreationDTO.isEnabled(),
                    userCreationDTO.roles()
            );

            return admin;
    }


    public CustomUser toStudentEntity(RegisterStudentDTO registerStudentDTO) {
        String encodedPassword = passwordEncoder.encode(registerStudentDTO.password());

        CustomUser student = new CustomUser(
                registerStudentDTO.username(),
                encodedPassword,
                true,
                true,
                true,
                true,
                Set.of(UserRole.STUDENT)
        );

        student.setEmail(registerStudentDTO.email());

        return student;
    }


    //Säker exponering av användardata till frontend.
    public UserResponseDTO toUsernameDTO(CustomUser customUser) {

        // All känslig info (lösenord, roller, statusflaggor) filtreras bort här.
        return new UserResponseDTO(customUser.getUsername());
    }

}

