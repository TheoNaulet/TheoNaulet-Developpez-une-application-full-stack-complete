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
    public Comment addComment(CommentDTO commentDTO) {
        Article article = articleRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));

        User sender = userRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setSender(sender);
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    /**
     * Récupérer tous les commentaires d'un article.
     */
    public List<Comment> getCommentsByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));

        return commentRepository.findByArticle(article);
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
