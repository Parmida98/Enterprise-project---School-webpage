package com.parmida98.school_webpage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** CustomUserDetailsService
 *      Loads the User from the database through CustomUserRepository
 *      Implements UserDetailsService - loadByUsername() override method
 *      Implements Business Logic & Error Handling (preferably through Advice) TODO
 * */
/*
Denna klass är länken mellan:
🔐 Spring Security och
🗄️ din databas

CustomUserDetailsService:
✔ Implementerar Spring Securitys krav
✔ Hämtar användare från databasen
✔ Hanterar fel om användaren saknas
✔ Kopplar din entitet till Spring Security
✔ Utgör kärnan i inloggningsprocessen
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Detta används för att hämta användare från databasen.
    private final CustomUserRepository customUserRepository;

    @Autowired
    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    /*
    Spring Security anropar denna metod när:
    någon försöker logga in
    JWT ska verifieras
    användardata behövs
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser customUser = customUserRepository.findUserByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Student with username " + username + " Was not found")
                );

        // TODO - Possibility for MAPPING instead of Pushing an Entity within UserDetails

        return new CustomUserDetails(customUser); // CustomUserDetails contains an Entity
    }
}
