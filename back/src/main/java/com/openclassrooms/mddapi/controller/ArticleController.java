package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserRepository userRepository;

    public ArticleController(ArticleService articleService, UserRepository userRepository) {
        this.articleService = articleService;
        this.userRepository = userRepository;
    }

    /**
     * Crée un nouvel article.
     */
    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleDTO createArticleDTO) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName(); 

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    
        User author = userRepository.findByEmail(user)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    
        Article createdArticle = articleService.createArticle(createArticleDTO, author);
        return ResponseEntity.ok(createdArticle);
    }
    
    /**
     * Récupère tous les articles.
     */
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
    
    /**
     * Récupère un article par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) { 
        System.out.println("ID RECU = " + id);
        return ResponseEntity.ok(articleService.getArticleById(id));
    }
    

    /**
     * Met à jour un article existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        return ResponseEntity.ok(articleService.updateArticle(id, articleDTO));
    }

    /**
     * Supprime un article par son ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer tous les articles des thèmes suivis par un utilisateur.
     */
    @GetMapping("/subscribed/{userId}")
    public ResponseEntity<List<ArticleDTO>> getArticlesBySubscribedThemes(@PathVariable Long userId) {
        List<ArticleDTO> articles = articleService.getArticlesBySubscribedThemes(userId);
        return ResponseEntity.ok(articles);
    }
    
}
