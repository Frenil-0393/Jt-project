package com.library.lms.config;

import com.library.lms.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration.
 *
 * Roles:
 *   ROLE_ADMIN  – full CRUD access to everything
 *   ROLE_USER   – read-only access to books/authors; can borrow and return books
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Stateless REST API – no session, no CSRF token needed
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .httpBasic(Customizer.withDefaults())  // HTTP Basic Auth (username:password)
            .authorizeHttpRequests(auth -> auth

                // ---- Public read access (everyone with valid credentials) ----
                .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/api/authors/**").hasAnyRole("ADMIN", "USER")

                // ---- Borrow / Return – both roles ----
                .requestMatchers(HttpMethod.POST, "/api/borrows/borrow").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.PUT, "/api/borrows/return/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/api/borrows/**").hasAnyRole("ADMIN", "USER")

                // ---- Everything else requires ADMIN ----
                .anyRequest().hasRole("ADMIN")
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }
}
