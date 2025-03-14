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

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;


    public ThemeService(ThemeRepository themeRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Crée un nouveau thème.
     */
    public Theme createTheme(ThemeDTO themeDTO) {
        if (themeRepository.findByTitle(themeDTO.getTitle()).isPresent()) {
            throw new RuntimeException("Le thème existe déjà !");
        }

        Theme theme = new Theme();
        theme.setTitle(themeDTO.getTitle());
        theme.setDescription(themeDTO.getDescription());
        theme.setCreatedAt(LocalDateTime.now());
        theme.setUpdatedAt(LocalDateTime.now());

        return themeRepository.save(theme);
    }


    /**
     * Récupère tous les thèmes avec statut d'abonnement.
     */
    public List<ThemeWithSubscriptionDTO> getAllThemesWithSubscription() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));

        return themeRepository.findAll().stream().map(theme -> {
            boolean isSubscribed = subscriptionRepository.findByUserAndTheme(user, theme).isPresent();
            return new ThemeWithSubscriptionDTO(theme.getId(), theme.getTitle(), theme.getDescription(), isSubscribed);
        }).collect(Collectors.toList());
    }
    /**
     * Récupère un thème par son ID.
     */
    public Theme getThemeById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé !"));
    }

    /**
     * Met à jour un thème existant.
     */
    public Theme updateTheme(Long id, ThemeDTO themeDTO) {
        Theme theme = getThemeById(id);
        theme.setTitle(themeDTO.getTitle());
        theme.setDescription(themeDTO.getDescription());
        theme.setUpdatedAt(LocalDateTime.now());

        return themeRepository.save(theme);
    }

    /**
     * Supprime un thème par son ID.
     */
    public void deleteTheme(Long id) {
        Theme theme = getThemeById(id);
        themeRepository.delete(theme);
    }
}
