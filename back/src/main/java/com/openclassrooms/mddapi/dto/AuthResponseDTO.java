package com.openclassrooms.mddapi.dto;
/**
 * DTO for encapsulating the authentication response.
 */
public class AuthResponseDTO {
    
    // Token issued after successful authentication
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
