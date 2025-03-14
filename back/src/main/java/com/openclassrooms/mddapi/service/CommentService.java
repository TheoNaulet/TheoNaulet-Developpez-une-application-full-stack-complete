package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    /**
     * Ajouter un commentaire à un article.
     */
    public CommentDTO addComment(CommentDTO commentDTO) {
        Article article = articleRepository.findById(commentDTO.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));
    
        User sender = userRepository.findById(commentDTO.getUserId()) 
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setSender(sender);
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
    
        Comment savedComment = commentRepository.save(comment);
        
        return convertToDTO(savedComment);
    }
    
    /**
     * Convertir un Comment en CommentDTO
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
     * Récupérer tous les commentaires d'un article.
     */
    public List<CommentDTO> getCommentsByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));

        List<Comment> comments = commentRepository.findByArticle(article);
        
        // Convert all comments to DTOs
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Supprimer un commentaire par ID.
     */
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé !"));
        commentRepository.delete(comment);
    }
}
