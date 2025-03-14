package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.dto.UserCreationDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crée un nouvel utilisateur.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody RegisterDTO registerDTO) {
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername(registerDTO.getUsername());
        userCreationDTO.setEmail(registerDTO.getEmail());
        userCreationDTO.setPassword(registerDTO.getPassword());

        return ResponseEntity.ok(userService.createUser(userCreationDTO)); 
    }

    /**
     * Récupère tous les utilisateurs.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Récupère un utilisateur par ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // /**
    //  * Met à jour un utilisateur.
    //  */
    // @PutMapping("/{id}")
    // public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
    //     return ResponseEntity.ok(userService.updateUser(id, userDTO));
    // }

    /**
     * Supprime un utilisateur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
