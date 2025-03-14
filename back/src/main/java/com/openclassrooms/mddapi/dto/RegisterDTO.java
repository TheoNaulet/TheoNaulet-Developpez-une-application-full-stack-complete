package com.openclassrooms.mddapi.dto;

/**
 * Data Transfer Object for user registration.
 */
public class RegisterDTO {

    private String username; // User's username
    private String email; // User's email address
    private String password; // User's password (plain text, to be hashed later)

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
     * @param email a valid email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * This should be hashed before being stored in the database.
     * @param password the plain text password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
