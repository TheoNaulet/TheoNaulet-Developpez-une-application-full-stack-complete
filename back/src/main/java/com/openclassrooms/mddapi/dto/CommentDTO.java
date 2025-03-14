package com.openclassrooms.mddapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long articleId; 
    private Long userId;
    private String senderUsername;
    private LocalDateTime createdAt;

    public CommentDTO(Long id, String content, Long articleId, Long userId, String senderUsername, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.articleId = articleId;
        this.userId = userId;
        this.senderUsername = senderUsername;
        this.createdAt = createdAt;
    }

    public CommentDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
