package com.parmida98.school_webpage.user;

import com.parmida98.school_webpage.user.dto.RegisterStudentDTO;
import com.parmida98.school_webpage.user.mapper.CustomUserMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;

    public RegistrationService(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper1) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper1;
    }

    public CustomUser registerStudent(RegisterStudentDTO registerStudentDTO) {

        CustomUser existing = customUserRepository.findUserByUsername(registerStudentDTO.username())
                .orElse(null);

        // Kollar om användarnamnet redan finns
        if (existing != null) {
            throw new IllegalArgumentException("Username is already in use");
        }

        CustomUser student = customUserMapper.toStudentEntity(registerStudentDTO);

        return customUserRepository.save(student);
    }
}
