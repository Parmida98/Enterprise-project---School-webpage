package com.parmida98.school_webpage.user.register;

import com.parmida98.school_webpage.error.exception.UsernameAlreadyExistsException;
import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.UserRepository;
import com.parmida98.school_webpage.user.dto.RegisterStudentDTO;
import com.parmida98.school_webpage.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public RegistrationService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CustomUser registerStudent(RegisterStudentDTO registerStudentDTO) {

        var existing = userRepository.findUserByUsername(registerStudentDTO.username());

        if (existing.isPresent()) {
            throw new UsernameAlreadyExistsException(registerStudentDTO.username());
        }

        CustomUser student = userMapper.toStudentEntity(registerStudentDTO);

        return userRepository.save(student);
    }
}
