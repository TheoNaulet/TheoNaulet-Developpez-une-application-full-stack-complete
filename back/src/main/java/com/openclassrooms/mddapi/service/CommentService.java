package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing comments in the system.
 * Handles operations related to creating, retrieving, and managing comments.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    /**
     * Adds a new comment to an article.
     * 
     * @param commentDTO The DTO containing comment information
     * @return The created comment as a DTO
     * @throws RuntimeException if article or user is not found
     */
    public CommentDTO addComment(CommentDTO commentDTO) {
        // Retrieve the article associated with the comment
        Article article = articleRepository.findById(commentDTO.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article not found!"));
    
        // Retrieve the user who sent the comment
        User sender = userRepository.findById(commentDTO.getUserId()) 
                .orElseThrow(() -> new RuntimeException("User not found!"));
    
        // Create a new comment entity
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setSender(sender);
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
    
        // Save the comment to the database
        Comment savedComment = commentRepository.save(comment);
        
        // Convert the saved comment to its DTO representation
        return convertToDTO(savedComment);
    }

    /**
     * Converts a Comment entity to its DTO representation.
     * 
     * @param comment The Comment entity to convert
     * @return The corresponding CommentDTO
     */
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setArticleId(comment.getArticle().getId());
        dto.setUserId(comment.getSender().getId());
        dto.setSenderUsername(comment.getSender().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    /**
     * Retrieves all comments associated with a specific article.
     * 
     * @param articleId The ID of the article to retrieve comments for
     * @return A list of CommentDTOs representing the comments
     * @throws RuntimeException if article is not found
     */
    public List<CommentDTO> getCommentsByArticle(Long articleId) {
        // Retrieve the article associated with the comments
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found!"));

        // Retrieve all comments associated with the article
        List<Comment> comments = commentRepository.findByArticle(article);
        
        // Convert all comments to their DTO representations
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a comment by its ID.
     * 
     * @param commentId The ID of the comment to delete
     * @throws RuntimeException if comment is not found
     */
    public void deleteComment(Long commentId) {
        // Retrieve the comment to delete
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found!"));
        // Delete the comment from the database
        commentRepository.delete(comment);
    }
}
