package com.openclassrooms.mddapi.dto;

/**
 * DTO for holding user login information.
 */
public class LoginDTO {
    private String emailOrUsername; // User's email address or username
    private String password; // User's password

    // Getters and setters
    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    /**
     * Sets the user's email address or username.
     * @param emailOrUsername a valid email address or username
     */
    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * @param password the plain-text password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
