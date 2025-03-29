package com.openclassrooms.mddapi.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a user in the system.
 * Contains essential user information for data transfer between layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor  
public class UserDTO {
    /**
     * Unique identifier for the user.
     * Used to uniquely identify a user in the system.
     */
    private Long id;

    /**
     * Username of the user.
     * Must be unique and used for identification in the system.
     */
    private String username;

    /**
     * Email address of the user.
     * Must be unique and used for communication and authentication.
     */
    private String email;

    /**
     * Password of the user (hashed).
     * Used for authentication purposes.
     */
    private String password;

    /**
     * Timestamp when the user account was created.
     * Automatically set when the user is created.
     */
    private LocalDate createdAt;

    /**
     * Timestamp when the user account was last updated.
     * Automatically updated when user information changes.
     */
    private LocalDate updatedAt;
    
    /**
     * Constructor for creating a UserDTO with basic user information.
     * 
     * @param id The user's unique identifier
     * @param username The user's username
     * @param email The user's email address
     * @param createdAt Timestamp when the user was created
     * @param updatedAt Timestamp when the user was last updated
     */
    public UserDTO(Long id, String username, String email, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
