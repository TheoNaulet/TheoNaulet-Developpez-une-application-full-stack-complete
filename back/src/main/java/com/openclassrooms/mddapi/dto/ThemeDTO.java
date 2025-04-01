package com.openclassrooms.mddapi.dto;

import lombok.Data;

/**
 * DTO representing a theme in the system.
 * Contains the essential information needed to create or update a theme.
 */
@Data
public class ThemeDTO {
    /**
     * Title of the theme.
     * Must be unique and descriptive of the theme's content.
     */
    private String title;

    /**
     * Detailed description of the theme.
     * Provides additional context about the theme's purpose and content.
     */
    private String description;
}
