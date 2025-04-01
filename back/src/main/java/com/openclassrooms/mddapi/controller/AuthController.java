package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related actions such as login, registration, and user retrieval.
 */
@RestController
@RequestMapping("/auth")
@Validated
@Tag(name = "Authentication", description = "Authentication API for user registration, login, and profile management")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    /**
     * Constructor injection for required dependencies.
     *
     * @param jwtService              service for JWT management
     * @param userService             service for user management
     * @param authenticationManager   authentication manager for Spring Security
     * @param authService             service for authentication operations
     */
    public AuthController(JwtService jwtService, UserService userService, AuthenticationManager authenticationManager, AuthService authService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    /**
     * Endpoint to register a new user and immediately return a JWT token.
     *
     * @param registerDTO the data for user registration
     * @return a ResponseEntity containing the generated JWT token or an error message
     */
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User registered successfully and JWT token returned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data"
        )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            AuthResponseDTO authResponse = authService.register(registerDTO);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            // Handle specific exceptions and return appropriate status codes
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint to retrieve the current authenticated user.
     *
     * @param authentication the current authentication object
     * @return a ResponseEntity containing the user's details
     */
    @Operation(summary = "Get current authenticated user", description = "Fetches details of the currently authenticated user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User details retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized access"
        )
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        // Retrieve the user's email from the authentication object
        String email = authentication.getName();

        // Fetch the user's details using the email
        UserDTO userResponse = userService.getUserByEmail(email);

        // Return the user's details as a response
        return ResponseEntity.ok(userResponse);
    }

    /**
     * Endpoint to log in an existing user and return a JWT token.
     *
     * @param loginDTO the data for user login
     * @return a ResponseEntity containing the generated JWT token
     */
    @Operation(summary = "Log in an existing user", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User logged in successfully and JWT token returned",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid credentials"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            AuthResponseDTO authResponse = authService.login(loginDTO);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // Authentication failure
            return ResponseEntity.status(401).build();
        }
    }
}
