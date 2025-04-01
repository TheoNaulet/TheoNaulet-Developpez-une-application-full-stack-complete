package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.RegisterDTO;
import com.openclassrooms.mddapi.dto.UserCreationDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * Service class for managing users in the system.
 * Handles operations related to creating, retrieving, updating, and deleting users.
 */
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    /**
     * Constructor injection for required dependencies.
     *
     * @param userRepository Repository for managing users
     * @param passwordEncoder Password encoder for secure password storage
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user.
     * 
     * @param userDTO The DTO containing user creation information
     * @return The created user
     * @throws RuntimeException if user with the same email or username already exists
     */
    public User createUser(UserCreationDTO userDTO) {
        // Check if user with the same email already exists
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà !");
        }

        // Check if user with the same username already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Un utilisateur avec ce nom d'utilisateur existe déjà !");
        }

        // Create a new user
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // user.setCreatedAt(LocalDateTime.now());
        // user.setUpdatedAt(LocalDateTime.now());

        // Save the user to the database
        return userRepository.save(user);
    }

    /**
     * Retrieves all users in the system.
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param id The ID of the user to retrieve
     * @return The requested user
     * @throws RuntimeException if user is not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    }

    /**
     * Updates an existing user.
     * 
     * @param id The ID of the user to update
     * @param userDTO The DTO containing updated user information
     * @return The updated user
     * @throws RuntimeException if user is not found or email/username is already in use
     */
    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);
        
        // Check if username is being updated and if it's already taken by another user
        if (!user.getUsername().equals(userDTO.getUsername()) && 
            userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà pris !");
        }
        
        // Check if email is being updated and if it's already taken by another user
        if (!user.getEmail().equals(userDTO.getEmail()) && 
            userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        
        // Only update password if it's provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        user.setUpdatedAt(LocalDate.now());

        // Save the updated user
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     * 
     * @param id The ID of the user to delete
     * @throws RuntimeException if user is not found
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
