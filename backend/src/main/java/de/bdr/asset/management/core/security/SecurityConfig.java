package de.bdr.asset.management.core.security;

import de.bdr.asset.management.core.ratelimit.RateLimitFilter;
import org.springframework.boot.security.autoconfigure.actuate.web.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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

import de.bdr.asset.management.core.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    private final JwtAuthenticationFilter jwtFilter;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            RateLimitFilter rateLimitFilter
    ) {
        this.jwtFilter = jwtFilter;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http

                // Disable CSRF: stateless JWT APIs don't use cookies, so CSRF is irrelevant
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS:
                .cors(Customizer.withDefaults())

                // No HTTP sessions — each request carries its own authentication via JWT
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Allow all CORS preflight requests — must be FIRST
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // =========================
                        // PUBLIC ENDPOINTS
                        // =========================

                        .requestMatchers("/v1/auth/**")
                        .permitAll()

                        .requestMatchers("/swagger-ui/**")
                        .permitAll()

                        .requestMatchers("/v3/api-docs/**")
                        .permitAll()

                        .requestMatchers("/v3/api-docs.yaml")
                        .permitAll()

                        // =========================
                        // USERS
                        // =========================

                        // GET -> authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/users/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.PATCH, "/v1/users/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/users/**")
                        .hasRole(ADMIN)

                        // =========================
                        // DEPARTMENTS
                        // =========================

                        // GET -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/departments")
                        .authenticated()

                        // GET -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/departments/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/departments/**")
                        .hasRole(ADMIN)

                        // =========================
                        // ASSET CATEGORIES
                        // =========================

                        // GET -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/asset-categories/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/asset-categories/**")
                        .hasRole(ADMIN)

                        // =========================
                        // ASSETS
                        // =========================

                        // GET -> any authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/assets/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/assets/**")
                        .hasRole(ADMIN)

                        // =========================
                        // BOOKINGS
                        // =========================

                        // GET -> authenticated user
                        .requestMatchers(HttpMethod.GET, "/v1/bookings/**")
                        .authenticated()

                        // POST -> authenticated user
                        .requestMatchers(HttpMethod.POST, "/v1/bookings/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.PATCH, "/v1/bookings/**")
                        .authenticated()

                        // all other methods -> ADMIN only
                        .requestMatchers("/v1/bookings/**")
                        .hasRole(ADMIN)

                        // =========================
                        // REPORTS
                        // =========================

                        .requestMatchers("/v1/reports/**")
                        .authenticated()

                        // =========================
                        // ACTUATORS
                        // =========================
                        .requestMatchers(EndpointRequest.to("health")).permitAll()
                        .requestMatchers(EndpointRequest.to("prometheus")).permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN)
                        // =========================
                        // FALLBACK
                        // =========================
                        .anyRequest().authenticated()

                )
                // JWT filter runs before Spring's username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, JwtAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Cost factor 12: ~300ms per hash on modern hardware — safe and fast enough for
        // login
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }
}