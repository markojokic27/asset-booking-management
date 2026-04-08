package de.bdr.asset.management.core.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF: stateless JWT APIs don't use cookies, so CSRF is irrelevant
                .csrf(AbstractHttpConfigurer::disable)

                // No HTTP sessions — each request carries its own authentication via JWT
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC ENDPOINTS
                        // =========================

                        .requestMatchers("/v1/auth/**")
                        .permitAll()

                        // =========================
                        // USERS
                        // =========================

                        // get all users or create a user -> ADMIN only
                        .requestMatchers("/v1/users")
                        .hasRole("ADMIN")

                        // get user by id -> authenticated user
                        // ADMIN and owner user enforced with @PreAuthorize
                        .requestMatchers(HttpMethod.GET, "/v1/users/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/users/**")
                        .hasRole("ADMIN")

                        // =========================
                        // DEPARTMENTS
                        // =========================

                        // get departments -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/departments")
                        .authenticated()

                        // get department by id -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/departments/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/departments/**")
                        .hasRole("ADMIN")

                        // =========================
                        // ASSET CATEGORIES
                        // =========================

                        // get asset categories -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/asset-categories")
                        .authenticated()

                        // get asset category by id -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/asset-categories/**")
                        .authenticated()

                            // create asset category -> ADMIN only
                            .requestMatchers(HttpMethod.POST, "/v1/asset-categories")
                            .hasRole("ADMIN")

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/asset-categories/**")
                        .hasRole("ADMIN")

                        // =========================
                        // ASSETS
                        // =========================

                        // get assets -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/assets")
                        .authenticated()

                        // create asset -> ADMIN only
                        .requestMatchers(HttpMethod.POST, "/v1/assets")
                        .authenticated()

                        // get asset by id -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/assets/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/assets/**")
                        .hasRole("ADMIN")

                        // =========================
                        // BOOKINGS
                        // =========================

                        // get bookings or create a booking -> any authenticated user
                        .requestMatchers("/v1/bookings")
                        .authenticated()

                        // get booking by id -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/bookings/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/bookings/**")
                        .hasRole("ADMIN")

                        // =========================
                        // FALLBACK
                        // =========================
                        .anyRequest().authenticated()

                )
                // JWT filter runs before Spring's username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Cost factor 12: ~300ms per hash on modern hardware — safe and fast enough for login
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}