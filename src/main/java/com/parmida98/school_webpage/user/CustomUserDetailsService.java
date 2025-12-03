package com.parmida98.school_webpage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Spring Security anropar denna metod när: någon försöker logga in, JWT ska verifieras, användardata behövs
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser customUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Student not found {}", username);
                    return new UsernameNotFoundException("Student with username " + username + " was not found");
                });
        return new CustomUserDetails(customUser); // CustomUserDetails contains an Entity
    }
}

/** CustomUserDetailsService
 *      Loads the CustomUser from the database through UserRepository
 *      Implements CustomUserDetailsService - loadByUsername() override method
 *      Implements Business Logic & Error Handling (preferably through Advice)
 * */
/*
Denna klass är länken mellan:
Spring Security och databas
 */