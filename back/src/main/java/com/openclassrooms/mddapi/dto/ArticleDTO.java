package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing an article in the system.
 * Contains the essential information about an article, including its content and metadata.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    /**
     * Unique identifier for the article.
     * Used to uniquely identify an article in the system.
     */
    private Long id;

    /**
     * Title of the article.
     * Must be unique and descriptive of the article's content.
     */
    private String title;

    /**
     * Content of the article.
     * Contains the main text of the article.
     */
    private String content;

    /**
     * Timestamp when the article was created.
     * Automatically set when the article is created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the article was last updated.
     * Automatically updated when the article is modified.
     */
    private LocalDateTime updatedAt;

    /**
     * Username of the author who created the article.
     * Used to identify the article's author.
     */
    private String authorUsername;

    /**
     * ID of the theme associated with the article.
     * Used to categorize the article under a specific theme.
     */
    private Long themeId;

    /**
     * Title of the theme associated with the article.
     * Used to display the theme information in the UI.
     */
    private String themeTitle;

    /**
     * List of comments associated with the article.
     * Contains all comments made on this article.
     */
    private List<CommentDTO> comments;
}
