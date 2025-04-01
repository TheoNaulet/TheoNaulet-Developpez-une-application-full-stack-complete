package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@Tag(name = "Article", description = "Article management API")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService, UserRepository userRepository) {
        this.articleService = articleService;
    }

    /**
     * Creates a new article
     */
    @Operation(summary = "Create a new article", description = "Creates a new article with the authenticated user as author")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Article created successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Article.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - User not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<Article> createArticle(@Valid @RequestBody CreateArticleDTO createArticleDTO) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Article createdArticle = articleService.createArticleWithAuth(createArticleDTO, userEmail);
            return ResponseEntity.ok(createdArticle);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("User not authenticated!")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Retrieves all articles
     */
    @Operation(summary = "Get all articles", description = "Retrieves a list of all articles")
    @ApiResponse(responseCode = "200", description = "Articles retrieved successfully", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDTO.class)))
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
    
    /**
     * Retrieves an article by its ID
     */
    @Operation(summary = "Get article by ID", description = "Retrieves an article by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Article found", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) { 
        return ResponseEntity.ok(articleService.getArticleById(id));
    }
    
    /**
     * Updates an existing article
     */
    @Operation(summary = "Update an article", description = "Updates an existing article by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Article updated successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Article.class))),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@Valid @PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleDTO));
    }

    /**
     * Deletes an article by its ID
     */
    @Operation(summary = "Delete an article", description = "Deletes an article by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all articles from themes subscribed by a user
     */
    @Operation(summary = "Get articles by subscribed themes", description = "Retrieves all articles from themes subscribed by a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Articles retrieved successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/subscribed/{userId}")
    public ResponseEntity<List<ArticleDTO>> getArticlesBySubscribedThemes(@PathVariable Long userId) {
        List<ArticleDTO> articles = articleService.getArticlesBySubscribedThemes(userId);
        return ResponseEntity.ok(articles);
    }
}
