package com.parmida98.school_webpage.security;

import com.parmida98.school_webpage.security.jwt.JwtUtils;
import com.parmida98.school_webpage.user.CustomUserDetails;
import com.parmida98.school_webpage.user.dto.CustomUserLoginDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController // klassen är en REST-controller, alla metoder returnerar data direkt (JSON, map, text), inte views.
public class AuthenticationRestController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationRestController(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;                               // används för att generera JWT-token.
        this.authenticationManager = authenticationManager;     // Spring Security-komponent som utför själva autentiseringen (kontrollerar användarnamn/lösenord).
    }

    // TODO - Test against permissions
    // TODO - Typed ResponseEntity (?)
    // @RequestBody CustomUserLoginDTO customUserLoginDTO:
    // Läser in JSON-body från requesten
    // Mappas till din CustomUserLoginDTO (som innehåller t.ex. username + password)
    // HttpServletResponse response:
    // Ger dig möjlighet att manipulera HTTP-svaret, t.ex. lägga till cookies.
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody CustomUserLoginDTO customUserLoginDTO,     // TODO - Sanitizing Input
            HttpServletResponse response
    ) {
        logger.debug("Attempting authentication for user: {}", customUserLoginDTO.username());

        // TODO - Status code for failure on authentication (for now we get 403)
        // Step 1: Perform authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        customUserLoginDTO.username(),
                        customUserLoginDTO.password())
        );

        // DEBUG: Print full Authentication result
        System.out.println("\n========= AUTHENTICATION RESULT =========");
        System.out.println("Class: " + authentication.getClass().getSimpleName());
        System.out.println("Authenticated: " + authentication.isAuthenticated());

        // Hämtar principal från Authentication (
        Object principal = authentication.getPrincipal();
        System.out.println("Principal type: " + principal.getClass().getSimpleName());
        // Om principal är CustomUserDetails, kasta den till userDetails och skriv ut detaljer:
        if (principal instanceof CustomUserDetails userDetails) {
            System.out.println("  Username: " + userDetails.getUsername());
            System.out.println("  Authorities: " + userDetails.getAuthorities());
            System.out.println("  Account Non Locked: " + userDetails.isAccountNonLocked());
            System.out.println("  Account Enabled: " + userDetails.isEnabled());
            System.out.println("  Password (hashed): " + userDetails.getPassword());
        } else {
            System.out.println("Principal value: " + principal);
        }

        System.out.println("Credentials: " + authentication.getCredentials());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("=========================================\n");

        // Step 2: Extract your custom principal
        // casta direkt till CustomUserDetails
        //detta ger dig tillgång till getCustomUser() och annat domain-specifikt.
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        /* Step 3: Generate JWT using your domain model (now includes roles)
        Hämtar din domänmodell (CustomUser) från CustomUserDetails.
        Skickar in den till jwtUtils.generateJwtToken(...).
        Får tillbaka en signerad JWT-sträng som innehåller:
        username
        roller
        issuedAt
        expiration */
        String token = jwtUtils.generateJwtToken(customUserDetails.getCustomUser());

        // Step 4: Set cookie
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);                           // Gör att JavaScript i browsern inte kan läsa cookien. Skyddar mot XSS-attacker som försöker sno token.
        cookie.setSecure(false);                            // cookien skickas även över HTTP (icke-krypterat). / change to true in production (HTTPS only)
        cookie.setAttribute("SameSite", "Lax"); // CSRF protection / cookien skickas även över HTTP (icke-krypterat).
        cookie.setPath("/");
        cookie.setMaxAge(3600);                             // 1 hour
        response.addCookie(cookie);                         // Lägger till cookien i HTTP-svaret → skickas till klienten (browsern).

        logger.info("Authentication successful for user: {}", customUserLoginDTO.username());

        /* Step 5: Return token - Optional
        Klienten (t.ex. din frontend) får token både:
        i cookie (för browser-autentisering)
        i response-body (om du vill spara den t.ex. i localStorage – även om cookie + HttpOnly är säkrare).*/
        return ResponseEntity.ok(Map.of(
                "username", customUserLoginDTO.username(),
                "authorities", customUserDetails.getAuthorities(),
                "token", token
        ));
    }
}

/*Helhetsflöde
Klient skickar POST /login med JSON { "username": "...", "password": "..." }
AuthenticationManager autentiserar med UsernamePasswordAuthenticationToken
CustomUserDetails hämtas från din UserDetailsService
JWT genereras baserat på CustomUser
JWT läggs både:
i cookie (authToken)
i response-body
Klient är nu inloggad och kan göra requests där JwtAuthenticationFilter plockar upp cookien.*/
