package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment", description = "Comment management API")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Adds a comment to an article
     */
    @Operation(summary = "Add a comment", description = "Adds a new comment to an article")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment added successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Article not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated")
    })
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@Valid @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.addComment(commentDTO));
    }

    /**
     * Retrieves all comments for an article
     */
    @Operation(summary = "Get comments by article", description = "Retrieves all comments for a specific article")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comments retrieved successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentService.getCommentsByArticle(articleId));
    }

    /**
     * Deletes a comment
     */
    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - User not authorized to delete this comment")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
