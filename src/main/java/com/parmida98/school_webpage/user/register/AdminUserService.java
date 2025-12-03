package com.parmida98.school_webpage.user.register;

import com.parmida98.school_webpage.error.exception.StudentNotFoundException;
import com.parmida98.school_webpage.error.exception.UserIsNotStudentException;
import com.parmida98.school_webpage.error.exception.UsernameAlreadyExistsException;
import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.UserRepository;
import com.parmida98.school_webpage.user.authority.UserRole;
import com.parmida98.school_webpage.user.dto.UserCreationDTO;
import com.parmida98.school_webpage.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CustomUser createUser(UserCreationDTO creatAsAdminDTO) {

        // Kolla om username redan finns
        var existing = userRepository.findUserByUsername(creatAsAdminDTO.username());

        if (existing.isPresent()) {
            throw new UsernameAlreadyExistsException(creatAsAdminDTO.username());
        }

        CustomUser customUser = userMapper.toAdminEntity(creatAsAdminDTO);

        return userRepository.save(customUser);
    }


    public void deleteStudentById(String id) {

        logger.info("Attempting to delete student with id: {}", id);

        // Konvertera String -> UUID
        UUID uuid = UUID.fromString(id);

        // Hämta användare eller kasta fel om den inte finns
        CustomUser customUser = userRepository.findById(uuid)
                .orElseThrow(() -> new StudentNotFoundException(id));

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            // Kastar en ny IllegalArgumentException med snyggare meddelande
            throw new IllegalArgumentException("Invalid UUID string: '" + id + "'");
        }

        // Säkerställ att det faktiskt är en student
        if (!customUser.getRoles().contains(UserRole.STUDENT)) {
            logger.warn("User with id {} exists but is not a STUDENT", id);
            throw new UserIsNotStudentException(id);
        }

        // Radera
        userRepository.delete(customUser);

        logger.info("Student with id {} successfully deleted", id);
    }

}
