package com.parmida98.school_webpage.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, UUID> {

    // Method will be called within CustomUserDetailsService
    Optional<CustomUser> findUserByUsername(String username);
}

