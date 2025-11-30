package com.parmida98.school_webpage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints som bara STUDENT ska nå.
 * Visar både roll och permissions.
 */

@RestController
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @GetMapping("/schedule")
    @PreAuthorize("hasAuthority('SEE_SCHEDULE')")
    public String getSchedule() {
        return "Your schedule for the week.";
    }

    @PostMapping("/assignments/submit")
    @PreAuthorize("hasAuthority('SUBMIT_ASSIGNMENT')")
    public String submitAssignment() {
        return "Your assignments have been submitted.";
    }
}

