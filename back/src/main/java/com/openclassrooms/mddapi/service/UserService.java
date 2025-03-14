package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.dto.UserCreationDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée un nouvel utilisateur.
     */
    public User createUser(UserCreationDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà !");
        }

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà !");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // user.setCreatedAt(LocalDateTime.now());
        // user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }


    /**
     * Récupère tous les utilisateurs.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    }

    /**
     * Met à jour un utilisateur.
     */
    // public User updateUser(Long id, UserDTO userDTO) {
    //     User user = getUserById(id);

    //     user.setUsername(userDTO.getUsername());
    //     user.setEmail(userDTO.getEmail());
    //     user.setPassword(userDTO.getPassword());
    //     user.setUpdatedAt(LocalDate.now());

    //     return userRepository.save(user);
    // }

    /**
     * Supprime un utilisateur par ID.
     */
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Registers a new user in the system.
     * @param registerDTO the data transfer object containing user registration details
     * @return the saved User entity
     */
    public User registerUser(RegisterDTO registerDTO) {
        // Create a new User object and populate it with data from the DTO
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        
        // Encode the password for security
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        // Set creation and update timestamps
        user.setCreatedAt(LocalDate.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()));
        user.setUpdatedAt(LocalDate.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()));

        // Save the user in the database
        return userRepository.save(user);
    }

    /**
     * Retrieves a user's details by their email.
     * @param email the email address of the user
     * @return a UserDTO containing the user's details
     * @throws RuntimeException if the user is not found
     */
    public UserDTO getUserByEmail(String email) {
        // Fetch the user by email, throw an exception if not found
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert the User entity to a UserDTO
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
