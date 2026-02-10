package isalem.dev.budget_buddy.configs;

import isalem.dev.budget_buddy.security.JWTRequestFilter;
import isalem.dev.budget_buddy.services.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final JWTRequestFilter jwtRequestFilter;

    /*
     * ✔️ Declares a Spring bean:
     *  - This tells Spring to create and manage a SecurityFilterChain object —
     *    the core of Spring Security’s HTTP protection.
     * ✔️ Configures HTTP security:
     *  - Everything chained after this modifies how Spring Security handles requests.
     * ✔️ Enables CORS:
     *  - Allows your backend to accept requests from your frontend (e.g., React, Angular).
     *  - Using defaults means:
     * 	    - Allows basic cross‑origin requests.
     *      - Uses Spring Boot’s auto‑configured CORS settings.
     * ✔️ Disables CSRF:
     *  - CSRF is needed for browser sessions, but not for stateless REST APIs using JWT.
     *    Since you're building an API → disabling CSRF is correct.
     * ✔️ Configures authorization rules:
     *  - requestMatchers() specifies which endpoints are open to everyone (permitAll()).
     *  - anyRequest().authenticated() means all other endpoints require authentication.
     * ✔️ Configures session management:
     *  - sessionCreationPolicy(SessionCreationPolicy.STATELESS) tells Spring Security not to create or use HTTP sessions.
     *  - This is essential for stateless APIs that rely on tokens (like JWT) instead of sessions.
     * ✔️ Overall:
     *  - this configuration sets up a secure foundation for your REST API,
     *    allowing unauthenticated access to specific endpoints while protecting the rest of your API with authentication.
     *  - It also ensures that your API is stateless and can be accessed from different origins (like a frontend app).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // Configure HTTP security
                .cors(Customizer.withDefaults()) // Enable CORS with default settings
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .authorizeHttpRequests( // Configure authorization rules
                        auth -> auth.requestMatchers( // Allow unauthenticated access to specific endpoints
                                "/status", // Health check endpoint
                                "/health", // Another health check endpoint
                                "/profiles/register", // Endpoint for user registration
                                "/profiles/activate", // Endpoint for profile activation
                                "/profiles/login" // Endpoint for user login
                        ).permitAll() // Allow unauthenticated access to the above endpoints
                        .anyRequest().authenticated() // Require authentication for all other endpoints
                )
                .sessionManagement( // Configure session management to be stateless
                        session -> session.sessionCreationPolicy( // Set session creation policy to stateless
                                SessionCreationPolicy.STATELESS // Do not create or use HTTP sessions
                        )
                )
                .addFilterBefore( // Add the JWTRequestFilter before the UsernamePasswordAuthenticationFilter
                        jwtRequestFilter, // The filter that processes JWT tokens
                        UsernamePasswordAuthenticationFilter.class // The filter before which the JWTRequestFilter should be added
                );

        return http.build();
    }

     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
         CorsConfiguration configuration = new CorsConfiguration(); // Create a new CORS configuration

         configuration.setAllowedOrigins(List.of("*")); // Allow all origins (you can specify your frontend URL here)
         configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow common HTTP methods
         configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept")); // Allow common headers
         configuration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)

         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Create a CORS configuration source

         source.registerCorsConfiguration("/**", configuration); // Apply CORS configuration to all endpoints

         return source;
    }

    /*
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(appUserDetailsService);

        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }
    **/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}