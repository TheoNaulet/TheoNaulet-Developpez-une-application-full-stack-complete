package com.openclassrooms.mddapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO representing a comment in the system.
 * Contains the essential information about a comment, including its content and metadata.
 */
@Data
public class CommentDTO {
    /**
     * Unique identifier for the comment.
     * Used to uniquely identify a comment in the system.
     */
    private Long id;

    /**
     * Content of the comment.
     * Contains the text written by the user.
     */
    private String content;

    /**
     * ID of the article this comment belongs to.
     * Used to associate the comment with a specific article.
     */
    private Long articleId;

    /**
     * ID of the user who created the comment.
     * Used to identify the author of the comment.
     */
    private Long userId;

    /**
     * Username of the user who created the comment.
     * Used to display the author's name in the UI.
     */
    private String senderUsername;

    /**
     * Timestamp when the comment was created.
     * Automatically set when the comment is created.
     */
    private LocalDateTime createdAt;

    /**
     * Constructor for CommentDTO.
     *
     * @param id The comment's unique identifier
     * @param content The text content of the comment
     * @param articleId The ID of the associated article
     * @param userId The ID of the user who created the comment
     * @param senderUsername The username of the comment's author
     * @param createdAt The timestamp when the comment was created
     */
    public CommentDTO(Long id, String content, Long articleId, Long userId, String senderUsername, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.articleId = articleId;
        this.userId = userId;
        this.senderUsername = senderUsername;
        this.createdAt = createdAt;
    }

    /**
     * No-arg constructor for CommentDTO.
     * Used for deserialization and other purposes where a constructor with no arguments is required.
     */
    public CommentDTO() {}

    /**
     * Gets the unique identifier for the comment.
     * @return The comment's ID
     */
    public Long getId() { return id; }

    /**
     * Sets the unique identifier for the comment.
     * @param id The comment's ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the content of the comment.
     * @return The comment's content
     */
    public String getContent() { return content; }

    /**
     * Sets the content of the comment.
     * @param content The comment's content
     */
    public void setContent(String content) { this.content = content; }

    /**
     * Gets the ID of the article this comment belongs to.
     * @return The article's ID
     */
    public Long getArticleId() { return articleId; }

    /**
     * Sets the ID of the article this comment belongs to.
     * @param articleId The article's ID
     */
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    /**
     * Gets the ID of the user who created the comment.
     * @return The user's ID
     */
    public Long getUserId() { return userId; }

    /**
     * Sets the ID of the user who created the comment.
     * @param userId The user's ID
     */
    public void setUserId(Long userId) { this.userId = userId; }

    /**
     * Gets the timestamp when the comment was created.
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Sets the timestamp when the comment was created.
     * @param createdAt The creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
