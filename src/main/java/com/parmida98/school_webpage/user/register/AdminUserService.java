package com.parmida98.school_webpage.user.register;


import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.CustomUserRepository;
import com.parmida98.school_webpage.user.authority.UserRole;
import com.parmida98.school_webpage.user.dto.CustomUserCreationDTO;
import com.parmida98.school_webpage.user.mapper.CustomUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomUserRepository customUserRepository;
    private final CustomUserMapper customUserMapper;

    public AdminUserService(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
    }

    public CustomUser createUser(CustomUserCreationDTO creatAsAdminDTO) {

        // Kolla om username redan finns
        CustomUser existing = customUserRepository.findUserByUsername(creatAsAdminDTO.username())
                .orElse(null);

        if (existing != null) {
            throw new IllegalArgumentException("Username is already in use");
        }

        CustomUser user = customUserMapper.toAdminEntity(creatAsAdminDTO);

        return customUserRepository.save(user);
    }


    public void deleteStudentById(String id) {

        logger.info("Attempting to delete student with id: {}", id);

        // 1. Konvertera String -> UUID
        UUID uuid = UUID.fromString(id);
        logger.warn("Invalid UUID format: {}", id);


        // 2. Hämta användare eller kasta fel om den inte finns
        CustomUser user = customUserRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + id + " was not found"));

        logger.warn("Student with id {} not found", id);

        // 3. Säkerställ att det faktiskt är en student
        if (!user.getRoles().contains(UserRole.STUDENT)) {
            throw new IllegalArgumentException("User with id " + id + " is not a STUDENT");
        }
        logger.warn("User {} exists but is not a STUDENT", id);


        // 4. Radera
        customUserRepository.delete(user);

        logger.info("Student with id {} successfully deleted", id);
    }

}
