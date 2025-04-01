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

/**
 * Service class for managing articles in the system.
 * Handles operations related to creating, retrieving, updating, and deleting articles.
 */
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * Constructor injection for required dependencies.
     *
     * @param articleRepository Repository for managing articles
     * @param themeRepository Repository for managing themes
     * @param subscriptionRepository Repository for managing subscriptions
     * @param userRepository Repository for managing users
     * @param commentRepository Repository for managing comments
     */
    public ArticleService(ArticleRepository articleRepository, ThemeRepository themeRepository, SubscriptionRepository subscriptionRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.themeRepository = themeRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Creates a new article with a specific author.
     * 
     * @param createArticleDTO The DTO containing article creation information
     * @param author The author of the article
     * @return The created article
     * @throws RuntimeException if theme is not found
     */
    public Article createArticle(CreateArticleDTO createArticleDTO, User author) {
        // Retrieve the theme by its ID
        Theme theme = themeRepository.findById(createArticleDTO.getThemeId())
                .orElseThrow(() -> new RuntimeException("Theme not found!"));

        // Create a new article
        Article article = new Article();
        article.setTitle(createArticleDTO.getTitle());
        article.setContent(createArticleDTO.getContent());
        article.setTheme(theme);
        article.setAuthor(author); 
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // Save the article to the database
        return articleRepository.save(article);
    }

    /**
     * Creates a new article with authentication.
     * 
     * @param createArticleDTO The DTO containing article creation information
     * @param userEmail The email of the user creating the article
     * @return The created article
     * @throws RuntimeException if user is not authenticated or not found
     */
    public Article createArticleWithAuth(CreateArticleDTO createArticleDTO, String userEmail) {
        // Check if the user is authenticated
        if (userEmail == null) {
            throw new RuntimeException("User not authenticated!");
        }
    
        // Retrieve the user by their email
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    
        // Create the article with the authenticated user as the author
        return createArticle(createArticleDTO, author);
    }

    /**
     * Retrieves all articles.
     * 
     * @return A list of ArticleDTOs containing articles
     */
    public List<ArticleDTO> getAllArticles() {
        // Retrieve all articles from the database
        List<Article> articles = articleRepository.findAll();
    
        // Convert each article to its DTO representation
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an article by its ID and converts it to a DTO with its comments.
     * 
     * @param id The ID of the article to retrieve
     * @return The article as a DTO with its comments
     * @throws RuntimeException if article is not found
     */
    public ArticleDTO getArticleById(Long id) {
        // Retrieve the article by its ID
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found!"));

        // Retrieve the comments for the article
        List<CommentDTO> comments = commentRepository.findByArticle(article).stream()
                .map(this::convertCommentToDTO)
                .collect(Collectors.toList());

        // Convert the article to its DTO representation with comments
        return convertToDTO(article, comments);
    }

    /**
     * Converts an Article entity to its DTO representation with comments.
     * 
     * @param article The Article entity to convert
     * @param comments The comments for the article
     * @return The corresponding ArticleDTO with comments
     */
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

    /**
     * Converts a Comment entity to its DTO representation.
     * 
     * @param comment The Comment entity to convert
     * @return The corresponding CommentDTO
     */
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
     * Updates an existing article.
     * 
     * @param id The ID of the article to update
     * @param articleDTO The DTO containing updated article information
     * @return The updated article
     * @throws RuntimeException if article or theme is not found
     */
    public Article updateArticle(Long id, ArticleDTO articleDTO) {
        // Retrieve the article by its ID
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found!"));

        // Retrieve the theme by its ID
        Theme theme = themeRepository.findById(articleDTO.getId())
                .orElseThrow(() -> new RuntimeException("Theme not found!"));

        // Update the article's information
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setTheme(theme);
        article.setUpdatedAt(LocalDateTime.now());

        // Save the updated article to the database
        return articleRepository.save(article);
    }

    /**
     * Deletes an article by its ID.
     * 
     * @param id The ID of the article to delete
     * @throws RuntimeException if article is not found
     */
    public void deleteArticle(Long id) {
        // Retrieve the article by its ID
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found!"));

        // Delete the article from the database
        articleRepository.delete(article);
    }

    /**
     * Retrieves all articles from themes subscribed by a user.
     * 
     * @param userId The ID of the user
     * @return A list of ArticleDTOs containing articles from subscribed themes
     * @throws RuntimeException if user is not found
     */
    public List<ArticleDTO> getArticlesBySubscribedThemes(Long userId) {
        // Retrieve the user by their ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Retrieve the subscriptions for the user
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);

        // Retrieve the themes from the subscriptions
        List<Theme> themes = subscriptions.stream().map(Subscription::getTheme).collect(Collectors.toList());

        // Retrieve the articles from the themes
        List<Article> articles = articleRepository.findByThemeIn(themes);

        // Convert each article to its DTO representation
        return articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts an Article entity to its DTO representation.
     * 
     * @param article The Article entity to convert
     * @return The corresponding ArticleDTO
     */
    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());  
        dto.setTitle(article.getTitle());  
        dto.setContent(article.getContent());
        
        dto.setAuthorUsername(article.getAuthor().getUsername());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());

        // Retrieve the comments for the article
        List<CommentDTO> commentDTOs = article.getComments().stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setSenderUsername(comment.getSender().getUsername());
            commentDTO.setCreatedAt(comment.getCreatedAt());
            return commentDTO;
        }).collect(Collectors.toList());

        // Set the comments for the article DTO
        dto.setComments(commentDTOs);
        return dto;
    }
}
