package com.openclassrooms.mddapi.dto;

/**
 * DTO for encapsulating the authentication response.
 * Contains the JWT token and user information after successful authentication.
 */
public class AuthResponseDTO {
    /**
     * The JWT token for authentication.
     * Used to authenticate subsequent requests.
     */
    private String token;

    /**
     * Constructor for AuthResponseDTO.
     *
     * @param token the JWT token issued upon successful authentication
     */
    public AuthResponseDTO(String token) {
        this.token = token;
    }

    /**
     * Get the issued JWT token.
     *
     * @return the authentication token
     */
    public String getToken() {
        return token;
    }
}
