package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.AuthResponseDTO;
import com.openclassrooms.mddapi.dto.LoginDTO;
import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.UserService;

// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related actions such as login, registration, and user retrieval.
 */
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    public JwtService jwtService;
    public UserService userService;
    private AuthenticationManager authenticationManager;

    /**
     * Constructor injection for required dependencies.
     *
     * @param jwtService              service for JWT management
     * @param userService             service for user management
     * @param authenticationManager   authentication manager for Spring Security
     */
    public AuthController(JwtService jwtService, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // /**
    //  * Endpoint to register a new user and immediately return a JWT token.
    //  *
    //  * @param registerDTO the data for user registration
    //  * @return a ResponseEntity containing the generated JWT token or an error message
    //  */
    // @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token.")
    // @ApiResponses(value = {
    //     @ApiResponse(
    //         responseCode = "200",
    //         description = "User registered successfully and JWT token returned.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = """
    //             {
    //                 "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    //             }
    //         """))
    //     ),
    //     @ApiResponse(
    //         responseCode = "400",
    //         description = "Invalid input data.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = """
    //             {
    //                 "message": "Invalid input data: email already exists."
    //             }
    //         """))
    //     ),
    //     @ApiResponse(
    //         responseCode = "500",
    //         description = "Internal server error.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = """
    //             {
    //                 "message": "An unexpected error occurred."
    //             }
    //         """))
    //     )
    // })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody RegisterDTO registerDTO) {
        // Register the new user
        userService.registerUser(registerDTO);
    
        // Authenticate the user after registration
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword())
        );
    
        // Generate a JWT token
        String token = jwtService.generateToken(authenticate);

        // Return the response with the token
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    /**
     * Endpoint to retrieve the current authenticated user.
     *
     * @param authentication the current authentication object
     * @return a ResponseEntity containing the user's details
     */
    // @Operation(summary = "Get current authenticated user", description = "Fetches details of the currently authenticated user.")
    // @ApiResponses(value = {
    //     @ApiResponse(
    //         responseCode = "200",
    //         description = "User details retrieved successfully.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = """
    //             {
    //                 "id": 1,
    //                 "name": "John Doe",
    //                 "email": "johndoe@example.com",
    //                 "createdAt": "2024-01-01T12:00:00",
    //                 "updatedAt": "2024-01-02T12:00:00"
    //             }
    //         """))
    //     ),
    //     @ApiResponse(
    //         responseCode = "401",
    //         description = "Unauthorized access.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = """
    //             {
    //                 "message": "Unauthorized access."
    //             }
    //         """))
    //     ),
    //     @ApiResponse(
    //         responseCode = "500",
    //         description = "Internal server error.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = """
    //             {
    //                 "message": "Internal server error."
    //             }
    //         """))
    //     )
    // })
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
    // @Operation(summary = "Log in an existing user", description = "Authenticates a user and returns a JWT token.")
    // @ApiResponses(value = {
    //     @ApiResponse(
    //         responseCode = "200",
    //         description = "User logged in successfully and JWT token returned.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"token\": \"your-jwt-token-here\"}"))
    //     ),
    //     @ApiResponse(
    //         responseCode = "401",
    //         description = "Invalid credentials.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid credentials.\"}"))
    //     ),
    //     @ApiResponse(
    //         responseCode = "500",
    //         description = "Internal server error.",
    //         content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Internal server error.\"}"))
    //     )
    // })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginDTO loginDTO) {
        // Authenticate the user
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmailOrUsername(), loginDTO.getPassword())
        );

        // Generate a JWT token
        String token = jwtService.generateToken(authenticate);

        // Return the response with the token
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
