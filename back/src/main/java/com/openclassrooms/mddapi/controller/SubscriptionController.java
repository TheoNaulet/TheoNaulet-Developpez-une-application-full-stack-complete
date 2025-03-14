package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * S'abonner à un thème.
     */
    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribe(@RequestParam Long userId, @RequestParam Long themeId) {
        return ResponseEntity.ok(subscriptionService.subscribe(userId, themeId));
    }

    /**
     * Se désabonner d'un thème.
     */
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam Long userId, @RequestParam Long themeId) {
        System.out.println("userId = " + userId);
        System.out.println("themeId = " + themeId);
        subscriptionService.unsubscribe(userId, themeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupérer tous les thèmes suivis par un utilisateur.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>> getUserSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(userId));
    }
}
