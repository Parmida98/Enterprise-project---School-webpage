package com.parmida98.school_webpage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints för ADMIN.
 * Visar hur både roll och permissions används.
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "Admin dashboard - endast ADMIN.";
    }

    @GetMapping("/grade")
    @PreAuthorize("hasAuthority('GRADE_ASSIGNMENT')")
    public String gradeAssignment() {
        return "Här kan ADMIN rätta uppgifter (endpoint).";
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public String deleteUser(@PathVariable String id) {
        return "Användare med id " + id + " raderad.";
    }
}

