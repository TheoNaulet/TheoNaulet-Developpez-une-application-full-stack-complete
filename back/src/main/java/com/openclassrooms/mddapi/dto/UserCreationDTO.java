package com.openclassrooms.mddapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO for creating a new user account.
 * Contains the essential information needed to create a user in the system.
 */
@Data
public class UserCreationDTO {
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
     * Timestamp when the user account was created.
     * Automatically set when the user is created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user account was last updated.
     * Automatically set when the user is created and updated on changes.
     */
    private LocalDateTime updatedAt;
}
