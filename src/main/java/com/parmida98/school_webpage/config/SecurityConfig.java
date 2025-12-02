package com.parmida98.school_webpage.config;

import com.parmida98.school_webpage.error.DTOError;
import com.parmida98.school_webpage.error.ErrorFactory;
import com.parmida98.school_webpage.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity // säger att den här klassen innehåller säkerhetskonfiguration.
@EnableMethodSecurity // aktiverar säkerhet direkt på metoder i koden
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;
    private final ErrorFactory errorFactory;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, ObjectMapper objectMapper, ErrorFactory errorFactory) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
        this.errorFactory = errorFactory;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)                                  // CSRF stängs av eftersom vi använder JWT
                .authorizeHttpRequests( auth -> auth     // Startar konfigurationen för autorisering – dvs vilka requests som får göras av vem.
                        .requestMatchers("/", "/login","/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/student/**").hasRole("STUDENT")

                        .anyRequest().authenticated() // MUST exist AFTER matchers
                )

                // STATELESS -> lagrar inte inloggning i HTTP-sessioner utan förlitar på JWT i varje request
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(ex -> ex

                        // 401 – inte inloggad / ingen giltig authentication
                        .authenticationEntryPoint((request, response, authException) -> {
                            DTOError error = errorFactory.unauthorized(
                                    request,
                                    "You must be logged in to access this resource"
                            );

                            response.setStatus(error.status());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getOutputStream(), error);
                        })

                        // 403 – inloggad, men saknar rätt roll/authority
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            DTOError error = errorFactory.forbidden(
                                    request,
                                    "You do not have permission to access this"
                            );

                            response.setStatus(error.status());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getOutputStream(), error);
                        })
                )

                // Läs JWT från requesten och sätt Authentication innan standard-auth-filtret körs
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
