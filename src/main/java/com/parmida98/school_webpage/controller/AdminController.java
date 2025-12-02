package com.parmida98.school_webpage.controller;

import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.dto.MessageDTO;
import com.parmida98.school_webpage.user.dto.UserCreationDTO;
import com.parmida98.school_webpage.user.dto.UserResponseDTO;
import com.parmida98.school_webpage.user.mapper.UserMapper;
import com.parmida98.school_webpage.user.register.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints för ADMIN.
 * Visar hur både roll och permissions används.
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminUserService adminUserService;
    private final UserMapper userMapper;

    public AdminController(AdminUserService adminUserService, UserMapper userMapper) {
        this.adminUserService = adminUserService;
        this.userMapper = userMapper;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<MessageDTO> adminDashboard() {
        return ResponseEntity.ok(new MessageDTO("Admin dashboard"));
    }

    @PostMapping("/register/student")
    @PreAuthorize("hasAuthority('REGISTER_STUDENT')")
    public ResponseEntity<UserResponseDTO> createStudent(@Valid @RequestBody UserCreationDTO createAsAdminDTO) {

        CustomUser created = adminUserService.createUser(createAsAdminDTO);

        UserResponseDTO responseDTO = userMapper.toUsernameDTO(created);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping("/grade")
    @PreAuthorize("hasAuthority('GRADE_ASSIGNMENT')")
    public ResponseEntity<MessageDTO> gradeAssignment() {
        return ResponseEntity.ok(new MessageDTO("ADMIN can grade assignments."));
    }


    @DeleteMapping("/student/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<MessageDTO> deleteStudent(@PathVariable String id) {
        adminUserService.deleteStudentById(id);

        return ResponseEntity.ok(
                new MessageDTO("Student with id " + id + " is deleted.")
        );
    }
}

