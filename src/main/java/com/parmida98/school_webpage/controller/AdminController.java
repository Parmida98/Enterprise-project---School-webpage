package com.parmida98.school_webpage.controller;

import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.dto.CustomUserCreationDTO;
import com.parmida98.school_webpage.user.dto.CustomUserResponseDTO;
import com.parmida98.school_webpage.user.mapper.CustomUserMapper;
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
    private final CustomUserMapper customUserMapper;

    public AdminController(AdminUserService adminUserService, CustomUserMapper customUserMapper) {
        this.adminUserService = adminUserService;
        this.customUserMapper = customUserMapper;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {

        return "Admin dashboard - only ADMIN.";
    }

    @PostMapping("/register/student")
    @PreAuthorize("hasAuthority('REGISTER_STUDENT')")
    public ResponseEntity<CustomUserResponseDTO> createUser(@Valid @RequestBody CustomUserCreationDTO createAsAdminDTO) {

        CustomUser created = adminUserService.createUser(createAsAdminDTO);

        CustomUserResponseDTO responseDTO = customUserMapper.toUsernameDTO(created);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }


    @GetMapping("/grade")
    @PreAuthorize("hasAuthority('GRADE_ASSIGNMENT')")
    public String gradeAssignment() {
        return "ADMIN can grade assignments.";
    }


    @DeleteMapping("/student/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String deleteStudent(@PathVariable String id) {

        adminUserService.deleteStudentById(id);

        return "Student with id " + id + " is deleted.";
    }
}

