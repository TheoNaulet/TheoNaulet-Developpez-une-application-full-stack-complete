package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Ajouter un commentaire à un article.
     */
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.addComment(commentDTO));
    }

    /**
     * Récupérer tous les commentaires d'un article.
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentService.getCommentsByArticle(articleId));
    }

    /**
     * Supprimer un commentaire.
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
