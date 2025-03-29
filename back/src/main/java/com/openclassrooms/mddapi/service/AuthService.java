package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication-related operations.
 * Provides methods for user registration and login, generating JWT tokens for authenticated users.
 */
@Service
public class AuthService {

    // Dependencies for JWT management, user management, and Spring Security authentication
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor injection for required dependencies.
     *
     * @param jwtService            service for JWT management
     * @param userService           service for user management
     * @param authenticationManager authentication manager for Spring Security
     */
    public AuthService(JwtService jwtService, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user and generates a JWT token for authentication.
     * 
     * @param registerDTO DTO containing the user registration details
     * @return AuthResponseDTO containing the generated JWT token
     */
    public AuthResponseDTO register(RegisterDTO registerDTO) {
        // Register the new user using the UserService
        userService.registerUser(registerDTO);

        // Authenticate the user after registration using Spring Security's AuthenticationManager
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword())
        );

        // Generate a JWT token using the JwtService
        String token = jwtService.generateToken(authenticate);

        // Return the response with the token
        return new AuthResponseDTO(token);
    }

    /**
     * Authenticates a user and generates a JWT token.
     * 
     * @param loginDTO DTO containing the user login credentials
     * @return AuthResponseDTO containing the generated JWT token
     */
    public AuthResponseDTO login(LoginDTO loginDTO) {
        // Authenticate the user using Spring Security's AuthenticationManager
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmailOrUsername(), loginDTO.getPassword())
        );

        // Generate a JWT token using the JwtService
        String token = jwtService.generateToken(authenticate);

        // Return the response with the token
        return new AuthResponseDTO(token);
    }
}
