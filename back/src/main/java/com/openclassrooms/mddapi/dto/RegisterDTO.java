package com.openclassrooms.mddapi.dto;

/**
 * DTO for user registration information.
 * Contains the essential information needed to create a new user account.
 */
public class RegisterDTO {
    /**
     * Username for the new user.
     * Must be unique and used for identification in the system.
     */
    private String username;

    /**
     * Email address for the new user.
     * Must be unique and used for communication and authentication.
     */
    private String email;

    /**
     * Password for the new user.
     * Will be hashed before storage for security.
     */
    private String password;

    /**
     * Gets the user's username.
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     * @param username the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's email address.
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password (plain text).
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
