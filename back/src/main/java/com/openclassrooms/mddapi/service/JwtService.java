package com.openclassrooms.mddapi.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * Service for generating JSON Web Tokens (JWT) for authenticated users.
 */
@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    /**
     * Constructor to inject the JwtEncoder.
     * @param jwtEncoder the encoder used for signing the JWT
     */
    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT token for the authenticated user.
     * 
     * @param authentication the current authentication context
     * @return the generated JWT token as a String
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now(); // Current time for token generation

        // Define the JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self") // The issuer of the token
            .issuedAt(now) // Token issuance time
            .expiresAt(now.plus(1, ChronoUnit.DAYS)) // Token expiration time (1 day)
            .subject(authentication.getName()) // The subject of the token (username)
            .build();

        // Define JWT header and combine with claimsisTokenValid
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(), // Use HS256 signing algorithm
            claims
        );

        // Encode and return the JWT token
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
