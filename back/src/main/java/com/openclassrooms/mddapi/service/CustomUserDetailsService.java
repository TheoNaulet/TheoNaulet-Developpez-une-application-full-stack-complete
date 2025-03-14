package com.openclassrooms.mddapi.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.repository.UserRepository;

import java.util.Optional;

/**
 * Custom implementation of the UserDetailsService interface to load user-specific data.
 * This class integrates with Spring Security for authentication and authorization.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor to inject the UserRepository dependency.
     * @param userRepository the repository used to fetch user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user's details by their email address or username.
     * @param emailOrUsername the email address or username of the user to load
     * @return a UserDetails object containing the user's information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Try to find the user by email first
        Optional<com.openclassrooms.mddapi.model.User> userByEmail = userRepository.findByEmail(emailOrUsername);
        
        // If not found by email, try to find by username
        Optional<com.openclassrooms.mddapi.model.User> userByUsername = userRepository.findByUsername(emailOrUsername);
        
        // Get the user entity from one of the results
        com.openclassrooms.mddapi.model.User userEntity = userByEmail.orElseGet(() -> 
            userByUsername.orElseThrow(() -> 
                new UsernameNotFoundException("User not found with email or username: " + emailOrUsername)
            )
        );

        System.out.println("User found: " + userEntity.getEmail());

        // Build and return a UserDetails object for Spring Security
        return User.builder()
            .username(userEntity.getEmail()) // Use email as the username
            .password(userEntity.getPassword()) // Use the encoded password
            .roles() // Add roles or authorities if applicable
            .build();
    }
}
