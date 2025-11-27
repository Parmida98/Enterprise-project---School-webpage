package com.parmida98.school_webpage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints som bara STUDENT ska nå.
 * Visar både roll (ROLE_STUDENT) och permissions (SEE_SCHEDULE, SUBMIT_ASSIGNMENT).
 */
@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')") // Alla endpoints i denna controller kräver ROLE_STUDENT
public class StudentController {

    @GetMapping("/schedule")
    @PreAuthorize("hasAuthority('SEE_SCHEDULE')")
    public String getSchedule() {
        return "Här är ditt schema (datum).";
    }

    @PostMapping("/assignments/submit")
    @PreAuthorize("hasAuthority('SUBMIT_ASSIGNMENT')")
    public String submitAssignment() {
        return "Din inlämning har registrerats.";
    }
}

