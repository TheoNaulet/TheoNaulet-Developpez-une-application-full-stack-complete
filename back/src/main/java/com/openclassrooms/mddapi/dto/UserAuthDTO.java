package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * DTO for user authentication credentials.
 * Contains the essential information needed to authenticate a user.
 */
@Data
public class UserAuthDTO {
    /**
     * Email address of the user.
     * Used for authentication and identification in the system.
     */
    private String email;

    /**
     * Password of the user.
     * Used for authentication verification.
     */
    private String password;
}
