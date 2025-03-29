package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ThemeDTO;
import com.openclassrooms.mddapi.dto.ThemeWithSubscriptionDTO;
import com.openclassrooms.mddapi.model.Theme;
import com.openclassrooms.mddapi.service.ThemeService;

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
@RequestMapping("/api/themes")
@Tag(name = "Theme", description = "Theme management API")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    /**
     * Creates a new theme
     */
    @Operation(summary = "Create a new theme", description = "Creates a new theme in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Theme created successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Theme.class)))
    })
    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody ThemeDTO themeDTO) {
        Theme theme = themeService.createTheme(themeDTO);
        return ResponseEntity.ok(theme);
    }

    /**
     * Retrieves all themes
     */
    @Operation(summary = "Get all themes", description = "Retrieves a list of all themes with subscription information")
    @ApiResponse(responseCode = "200", description = "Themes retrieved successfully", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ThemeWithSubscriptionDTO.class)))
    @GetMapping
    public ResponseEntity<List<ThemeWithSubscriptionDTO>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemesWithSubscription());
    }

    /**
     * Retrieves a theme by its ID
     */
    @Operation(summary = "Get theme by ID", description = "Retrieves a theme by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Theme found", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Theme.class))),
        @ApiResponse(responseCode = "404", description = "Theme not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long id) {
        return ResponseEntity.ok(themeService.getThemeById(id));
    }

    /**
     * Updates a theme
     */
    @Operation(summary = "Update a theme", description = "Updates an existing theme by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Theme updated successfully", 
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Theme.class))),
        @ApiResponse(responseCode = "404", description = "Theme not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable Long id, @RequestBody ThemeDTO themeDTO) {
        return ResponseEntity.ok(themeService.updateTheme(id, themeDTO));
    }

    /**
     * Deletes a theme
     */
    @Operation(summary = "Delete a theme", description = "Deletes a theme by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Theme deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Theme not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
