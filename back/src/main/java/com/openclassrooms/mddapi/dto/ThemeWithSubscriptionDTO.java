package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a theme with subscription information.
 * Contains information about a theme and whether the current user is subscribed to it.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeWithSubscriptionDTO {
    /**
     * Unique identifier for the theme.
     * Used to uniquely identify a theme in the system.
     */
    private Long id;

    /**
     * Title of the theme.
     * Must be unique and descriptive of the theme's content.
     */
    private String title;

    /**
     * Description of the theme.
     * Provides additional context about the theme's purpose and content.
     */
    private String description;
    
    /**
     * Indicates whether the current user is subscribed to this theme.
     * Used to display subscription status in the UI.
     */
    @JsonProperty("isSubscribed")
    private boolean isSubscribed;
}
