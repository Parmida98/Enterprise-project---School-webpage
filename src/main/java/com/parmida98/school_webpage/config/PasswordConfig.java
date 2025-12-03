package com.parmida98.school_webpage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/* Password Hashing: Encoder / Encoder = processen att hasha lösenord
 * The Encoder == password hashing (Generic Term)
 * Class Abstraction to be used == PasswordEncoder
 */

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder defaultPasswordEncoder() { // Spring kommer automatiskt att använda denna bean för att: hasha lösenord vid registrering verifiera lösenord vid inloggning

        return new BCryptPasswordEncoder(12); // Default Strength 10 (Iterations)
    }

}

