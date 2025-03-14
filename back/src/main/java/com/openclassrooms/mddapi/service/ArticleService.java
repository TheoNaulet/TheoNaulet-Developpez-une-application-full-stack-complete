package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.dto.CreateArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;


    public ArticleService(ArticleRepository articleRepository, ThemeRepository themeRepository, SubscriptionRepository subscriptionRepository,  UserRepository userRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.themeRepository = themeRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Crée un nouvel article avec un auteur spécifique.
     */
    public Article createArticle(CreateArticleDTO createArticleDTO, User author) {
        Theme theme = themeRepository.findById(createArticleDTO.getThemeId())
                .orElseThrow(() -> new RuntimeException("Thème non trouvé !"));

        Article article = new Article();
        article.setTitle(createArticleDTO.getTitle());
        article.setContent(createArticleDTO.getContent());
        article.setTheme(theme);
        article.setAuthor(author); 
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        return articleRepository.save(article);
    }

    /**
     * Récupère tous les articles.
     */
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
    
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    

   /**
     * Récupérer un article par son ID et le convertir en DTO avec ses commentaires.
     */
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));


        List<CommentDTO> comments = commentRepository.findByArticle(article).stream()
                .map(this::convertCommentToDTO)
                .collect(Collectors.toList());

        return convertToDTO(article, comments);
    }


    private ArticleDTO convertToDTO(Article article, List<CommentDTO> comments) {
        return new ArticleDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getAuthor().getUsername(),
                article.getTheme() != null ? article.getTheme().getId() : null,
                article.getTheme() != null ? article.getTheme().getTitle() : null,
                comments
        );
    }

    private CommentDTO convertCommentToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getArticle().getId(),
                comment.getSender().getId(),
                comment.getSender().getUsername(),
                comment.getCreatedAt()
        );
    }
    
    /**
     * Met à jour un article existant.
     * 
     */
    public Article updateArticle(Long id, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));
    
        Theme theme = themeRepository.findById(articleDTO.getId())
                .orElseThrow(() -> new RuntimeException("Thème non trouvé !"));
    
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setTheme(theme);
        article.setUpdatedAt(LocalDateTime.now());
    
        return articleRepository.save(article);
    }
    

    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé !"));
        articleRepository.delete(article);
    }
    
     /**
     * Récupérer tous les articles des thèmes suivis par un utilisateur.
     */
    public List<ArticleDTO> getArticlesBySubscribedThemes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);
        List<Theme> themes = subscriptions.stream().map(Subscription::getTheme).collect(Collectors.toList());
    
        List<Article> articles = articleRepository.findByThemeIn(themes);
    
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convertir un article en DTO avec ses commentaires.
     */
    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());  
        dto.setTitle(article.getTitle());  
        dto.setContent(article.getContent());
        
        dto.setAuthorUsername(article.getAuthor().getUsername());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());
    
        List<CommentDTO> commentDTOs = article.getComments().stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setSenderUsername(comment.getSender().getUsername());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            return commentDTO;
        }).collect(Collectors.toList());
    
        dto.setComments(commentDTOs);
        return dto;
    }
}
