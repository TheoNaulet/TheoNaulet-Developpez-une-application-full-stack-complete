package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for user login credentials.
 * Contains the information needed to authenticate a user in the system.
 */
@Data
public class LoginDTO {
    /**
     * Email or username of the user attempting to log in.
     * Supports both email and username for flexibility in authentication.
     */
    @NotBlank(message = "Email or username is required")
    private String emailOrUsername;

    /**
     * Password of the user attempting to log in.
     * Should be sent in plain text and will be hashed for verification.
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Getter for the email or username field.
     * 
     * @return The email or username used for authentication
     */
    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    /**
     * Setter for the email or username field.
     * 
     * @param emailOrUsername a valid email address or username
     */
    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    /**
     * Getter for the password field.
     * 
     * @return The plain-text password used for authentication
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password field.
     * 
     * @param password the plain-text password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
