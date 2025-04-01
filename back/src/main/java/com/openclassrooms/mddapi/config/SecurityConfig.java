package com.openclassrooms.mddapi.config;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


import com.openclassrooms.mddapi.service.CustomUserDetailsService;

import io.github.cdimascio.dotenv.Dotenv;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

/**
 * Security configuration for the application.
 * Configures authentication, authorization, JWT handling, and password encoding.
 */
@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final String jwtKey;

    /**
     * Constructor for dependency injection of the custom user details service and loading environment variables.
     *
     * @param customUserDetailsService the service for loading user-specific data
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
        Dotenv dotenv = Dotenv.load();
        this.jwtKey = dotenv.get("JWT_SECRET_KEY");

        // Ensure the key is present
        if (this.jwtKey == null || this.jwtKey.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY environment variable is missing or empty.");
        }
    }
	
    /**
     * Configures the security filter chain for HTTP requests.
     *
     * - Disables CSRF (since this is a stateless application using JWTs).
     * - Configures session management as stateless.
     * - Allows public access to registration and login endpoints.
     * - Protects all other endpoints with authentication.
     * - Enables JWT-based OAuth2 resource server.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if there is an error in the configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            .csrf(csrf -> csrf.disable()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    /**
     * Configures the authentication manager to use the custom user details service
     * and a password encoder.
     *
     * Note: This is marked as a temporary fix and should be refactored if possible.
     *
     * @param http the HttpSecurity object
     * @param passwordEncoder the password encoder bean
     * @return the configured AuthenticationManager
     * @throws Exception if there is an error in the configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder).and().build();
    }

    /**
     * Configures the password encoder to use BCrypt hashing.
     * 
     * @return a BCryptPasswordEncoder instance
     */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
	}
    
    /**
     * Configures the JWT encoder with the secret key.
     *
     * @return a configured JwtEncoder
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }
    
    /**
     * Configures the JWT decoder to validate tokens signed with the secret key.
     * 
     * @return a configured JwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(jwtKey.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
}