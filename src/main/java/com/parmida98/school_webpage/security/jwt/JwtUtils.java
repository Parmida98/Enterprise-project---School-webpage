package com.parmida98.school_webpage.security.jwt;

import com.parmida98.school_webpage.user.CustomUser;
import com.parmida98.school_webpage.user.authority.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

// Klassen samlar all logik kring skapande, läsning och validering av JWT.
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final SecretKey key;
    private final int jwtExpirationMs;

    public JwtUtils(
            @Value("${jwt.secret-key}") String base64EncodedSecretKey,
            @Value("${jwt.expiration-ms}") int jwtExpirationMs
    ) {
        this.jwtExpirationMs = jwtExpirationMs;

        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Definierar giltighetstiden för JWT till 1 timme uttryckt i millisekunder.
    public String generateJwtToken(CustomUser customUser) {
        logger.debug("Generating JWT for customUser: {} with roles: {}", customUser.getUsername(), customUser.getRoles());

        // Hämtar roller från användaren. Konverterar varje UserRole till dess strängnamn. Resultatet blir en lista av rollnamn som ska in i token.
        List<String> roles = customUser.getRoles().stream()
                .map(UserRole::getRoleName)
                .toList();

        // Skapar Header, Payload, Signature
        String token = Jwts.builder()
                .subject(customUser.getUsername())
                .claim("authorities", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)              // .signWith(key, SignatureAlgorithm är deprecated enligt Intellij
                .compact();

        logger.info("JWT generated successfully for customUser: {}", customUser.getUsername());
        return token;
    }

    public String getUsernameFromJwtToken(String token) {
        try {
            // Parsar token -> Verifierar signaturen med nyckeln -> Extraherar dess claims (innehåll)
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject(); // Subject, whom the token refers to, principal: whose currently authenticated (system)
            logger.debug("Extracted username '{}' from JWT token", username);
            return username;

        } catch (Exception e) {
            logger.warn("Failed to extract username from JWT token", e);
            return null;
        }
    }

    // Hämtar roller från JWT. Returnerar tom Set vid fel.
    public Set<UserRole> getRolesFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            List<String> authoritiesClaim = claims.get("authorities", List.class); // Hämtar claimen authorities som en lista.

            if (authoritiesClaim == null || authoritiesClaim.isEmpty()) {
                logger.warn("No authorities found in JWT token");
                return Set.of();
            }

            Set<UserRole> roles = authoritiesClaim.stream()
                    .map(role -> role.replace("ROLE_", ""))
                    .map(String::toUpperCase)
                    .map(UserRole::valueOf)
                    .collect(Collectors.toSet());

            logger.debug("Extracted roles from JWT token: {}", roles);
            return roles;

        } catch (Exception e) {
            logger.warn("Failed to extract roles from JWT token", e);
            return Set.of();
        }
    }


    // Used to pass in JWT token for Validation
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);

            logger.debug("JWT validation succeeded");
            return true;

        } catch (Exception e) {
            logger.warn("JWT validation failed", e);
        }

        return false;
    }


    // Helper: Extract JWT from cookie
    String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("authToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // Helper: Extract JWT from Authorization header
    String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);     // Tar bort "Bearer" och returnerar själva token.
        }
        return null;
    }
}

