package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ThemeWithSubscriptionDTO;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository, ThemeRepository themeRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
    }

    /**
     * S'abonner à un thème.
     */
    public Subscription subscribe(Long userId, Long themeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé !"));

        if (subscriptionRepository.findByUserAndTheme(user, theme).isPresent()) {
            throw new RuntimeException("L'utilisateur est déjà abonné à ce thème !");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        subscription.setCreatedAt(LocalDateTime.now());

        return subscriptionRepository.save(subscription);
    }

    /**
     * Se désabonner d'un thème.
     */
    public void unsubscribe(Long userId, Long themeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new RuntimeException("Thème non trouvé !"));

        Subscription subscription = subscriptionRepository.findByUserAndTheme(user, theme)
                .orElseThrow(() -> new RuntimeException("L'utilisateur n'est pas abonné à ce thème !"));

        subscriptionRepository.delete(subscription);
    }

    /**
     * Récupérer tous les thèmes auxquels un utilisateur est abonné.
     */
    /**
     * Récupérer tous les thèmes auxquels un utilisateur est abonné (au format ThemeWithSubscriptionDTO).
     */
    public List<ThemeWithSubscriptionDTO> getSubscriptionsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));

        // Récupérer tous les abonnements de l'utilisateur
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);

        // Transformer chaque abonnement en ThemeWithSubscriptionDTO
        return subscriptions.stream().map(subscription -> {
            Theme theme = subscription.getTheme();
            return new ThemeWithSubscriptionDTO(
                    theme.getId(),
                    theme.getTitle(),
                    theme.getDescription(),
                    true
            );
        }).collect(Collectors.toList());
    }

}
