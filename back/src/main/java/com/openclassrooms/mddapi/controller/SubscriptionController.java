package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ThemeWithSubscriptionDTO;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription", description = "Theme subscription management API")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Subscribe to a theme
     */
    @Operation(summary = "Subscribe to a theme", description = "Creates a subscription for a user to a specific theme")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscription created successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class))),
        @ApiResponse(responseCode = "404", description = "User or theme not found"),
        @ApiResponse(responseCode = "400", description = "User is already subscribed to this theme")
    })
    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribe(@RequestParam Long userId, @RequestParam Long themeId) {
        return ResponseEntity.ok(subscriptionService.subscribe(userId, themeId));
    }

    /**
     * Unsubscribe from a theme
     */
    @Operation(summary = "Unsubscribe from a theme", description = "Removes a subscription for a user from a specific theme")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Subscription removed successfully"),
        @ApiResponse(responseCode = "404", description = "User, theme, or subscription not found")
    })
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam Long userId, @RequestParam Long themeId) {
        subscriptionService.unsubscribe(userId, themeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve all themes followed by a user
     */
    @Operation(summary = "Get user subscriptions", description = "Retrieves all themes a user is subscribed to")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ThemeWithSubscriptionDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ThemeWithSubscriptionDTO>> getUserSubscriptions(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(userId));
    }
}
