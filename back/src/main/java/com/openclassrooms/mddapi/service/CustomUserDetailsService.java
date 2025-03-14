package com.openclassrooms.mddapi.service;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.repository.UserRepository;



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
     * Loads a user's details by their email address.
     * @param email the email address of the user to load
     * @return a UserDetails object containing the user's information
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the user entity from the database using the email
        com.openclassrooms.mddapi.model.User userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        System.out.println("User found: " + userEntity.getEmail());

        // Build and return a UserDetails object for Spring Security
        return User.builder()
            .username(userEntity.getEmail()) // Use email as the username
            .password(userEntity.getPassword()) // Use the encoded password
            .roles() // Add roles or authorities if applicable
            .build();
    }
}
