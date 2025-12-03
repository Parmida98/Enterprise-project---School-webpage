package com.parmida98.school_webpage.security;

import com.parmida98.school_webpage.security.jwt.JwtUtils;
import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.CustomUserDetails;
import com.parmida98.school_webpage.user.dto.UserResponseDTO;
import com.parmida98.school_webpage.user.register.RegistrationService;
import com.parmida98.school_webpage.user.dto.UserLoginDTO;
import com.parmida98.school_webpage.user.dto.RegisterStudentDTO;
import com.parmida98.school_webpage.user.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // alla metoder returnerar data direkt (JSON, map, text)
public class AuthenticationRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;
    private final UserMapper userMapper;


    public AuthenticationRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, RegistrationService registrationService, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.registrationService = registrationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerStudent(@Valid @RequestBody RegisterStudentDTO registerStudentDTO) {

        CustomUser created = registrationService.registerStudent(registerStudentDTO);

        UserResponseDTO responseDTO = userMapper.toUsernameDTO(created); // För att vara framtidssäker om DTO ändras

        logger.info("New student created: {}", responseDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }


    // Läser in JSON-body från requesten
    // HttpServletResponse response: Ger möjlighet att manipulera HTTP-svaret, t.ex. lägga till cookies.
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO,HttpServletResponse response) {
        logger.debug("Attempting authentication for user: {}", userLoginDTO.username());

        Authentication authentication;
        try {
            //Försök autentisera
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.username(),
                            userLoginDTO.password()
                    )
            );
        } catch (Exception e) {
            logger.warn("Failed login attempt for username '{}': {}", userLoginDTO.username(), e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Username or password is invalid"
                    ));
        }


        if (logger.isDebugEnabled()) {
            logger.debug("========= AUTHENTICATION RESULT =========");
            logger.debug("Class: {}", authentication.getClass().getSimpleName());
            logger.debug("Authenticated: {}", authentication.isAuthenticated());

            Object principal = authentication.getPrincipal();
            logger.debug("Principal type: {}", principal.getClass().getSimpleName());

            if (principal instanceof CustomUserDetails customUserDetails) {
                logger.debug("Username: {}", customUserDetails.getUsername());
                logger.debug("Authorities: {}", customUserDetails.getAuthorities());
                logger.debug("Account Non Locked: {}", customUserDetails.isAccountNonLocked());
                logger.debug("Account Enabled: {}", customUserDetails.isEnabled());
            } else {
                logger.debug("Principal value: {}", principal);
            }

            logger.debug("Credentials: {}", authentication.getCredentials());
            logger.debug("Details: {}", authentication.getDetails());
            logger.debug("Authorities: {}", authentication.getAuthorities());
            logger.debug("=========================================");
        }

        // 2: Extract your custom principal
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        /* 3: Generate JWT using your domain model (now includes roles)
        Hämtar din domänmodell (CustomUser) från CustomUserDetails.
        Skickar in den till jwtUtils.generateJwtToken(...).
        Får tillbaka en signerad JWT-sträng som innehåller:
        username, roller, issuedAt, expiration */
        String token = jwtUtils.generateJwtToken(customUserDetails.getCustomUser());

        // 4: Set cookie
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);                           // Gör att JavaScript i browsern inte kan läsa cookien. Skyddar mot XSS-attacker som försöker sno token.
        cookie.setSecure(false);                            // change to true in production (HTTPS only)
        cookie.setAttribute("SameSite", "Lax"); // CSRF protection
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);                         // Lägger till cookien i HTTP-svaret → skickas till klienten (browsern).

        logger.info("Authentication successful for user: {}", userLoginDTO.username());

        /* 5: Return token
        Klienten (ex frontend) får token både:
        i cookie (för browser-autentisering)
        i response-body */
        return ResponseEntity.ok(Map.of(
                "username", userLoginDTO.username(),
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }
}
