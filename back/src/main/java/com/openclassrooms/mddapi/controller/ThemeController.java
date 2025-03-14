package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.dto.ThemeWithSubscriptionDTO;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.service.ThemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Crée un nouveau thème.
     */
    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeDTO themeDTO) {
        Theme theme = themeService.createTheme(themeDTO);
        return ResponseEntity.ok(theme);
    }

    /**
     * Récupère tous les thèmes.
     */
    @GetMapping
    public ResponseEntity<List<ThemeWithSubscriptionDTO>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemesWithSubscription());
    }


    /**
     * Récupère un thème par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long id) {
        return ResponseEntity.ok(themeService.getThemeById(id));
    }

    /**
     * Met à jour un thème.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable Long id, @RequestBody ThemeDTO themeDTO) {
        return ResponseEntity.ok(themeService.updateTheme(id, themeDTO));
    }

    /**
     * Supprime un thème.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
