package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.dto.ThemeWithSubscriptionDTO;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing themes in the system.
 * Handles operations related to creating, retrieving, updating, and deleting themes.
 */
@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Constructor injection for required dependencies.
     *
     * @param themeRepository Repository for managing themes
     * @param userRepository Repository for managing users
     * @param subscriptionRepository Repository for managing subscriptions
     */
    public ThemeService(ThemeRepository themeRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Creates a new theme.
     * 
     * @param themeDTO The DTO containing theme creation information
     * @return The created theme
     * @throws RuntimeException if theme with the same title already exists
     */
    public Theme createTheme(ThemeDTO themeDTO) {
        // Check if a theme with the same title already exists
        if (themeRepository.findByTitle(themeDTO.getTitle()).isPresent()) {
            throw new RuntimeException("Theme already exists!");
        }

        // Create a new theme
        Theme theme = new Theme();
        theme.setTitle(themeDTO.getTitle());
        theme.setDescription(themeDTO.getDescription());
        theme.setCreatedAt(LocalDateTime.now());
        theme.setUpdatedAt(LocalDateTime.now());

        // Save the theme to the database
        return themeRepository.save(theme);
    }

    /**
     * Retrieves all themes with subscription status for the current user.
     * 
     * @return List of themes with their subscription status
     * @throws RuntimeException if the current user is not found
     */
    public List<ThemeWithSubscriptionDTO> getAllThemesWithSubscription() {
        // Get the email of the current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Retrieve all themes and map them to ThemeWithSubscriptionDTO
        return themeRepository.findAll().stream().map(theme -> {
            // Check if the user is subscribed to the theme
            boolean isSubscribed = subscriptionRepository.findByUserAndTheme(user, theme).isPresent();
            return new ThemeWithSubscriptionDTO(theme.getId(), theme.getTitle(), theme.getDescription(), isSubscribed);
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves a theme by its ID.
     * 
     * @param id The ID of the theme to retrieve
     * @return The requested theme
     * @throws RuntimeException if the theme is not found
     */
    public Theme getThemeById(Long id) {
        // Find the theme by ID
        return themeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theme not found!"));
    }

    /**
     * Updates an existing theme.
     * 
     * @param id The ID of the theme to update
     * @param themeDTO The DTO containing updated theme information
     * @return The updated theme
     * @throws RuntimeException if the theme is not found
     */
    public Theme updateTheme(Long id, ThemeDTO themeDTO) {
        // Find the theme by ID
        Theme theme = getThemeById(id);
        
        // Update the theme's title and description
        theme.setTitle(themeDTO.getTitle());
        theme.setDescription(themeDTO.getDescription());
        theme.setUpdatedAt(LocalDateTime.now());

        // Save the updated theme to the database
        return themeRepository.save(theme);
    }

    /**
     * Deletes a theme by its ID.
     * 
     * @param id The ID of the theme to delete
     * @throws RuntimeException if the theme is not found
     */
    public void deleteTheme(Long id) {
        // Find the theme by ID
        Theme theme = getThemeById(id);
        
        // Delete the theme from the database
        themeRepository.delete(theme);
    }
}
