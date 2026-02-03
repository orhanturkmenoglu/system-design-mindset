package com.systemdesign.mindset.config;

import com.systemdesign.mindset.security.JwtAuthenticationFilter;
import com.systemdesign.mindset.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration - JWT-based STATELESS
 * <p>
 * Önemli Ayarlar:
 * - SessionCreationPolicy.STATELESS → Session yok!
 * - JWT Filter ekle
 * - Public endpoints (login, h2-console)
 * - Protected endpoints (account/*)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF disable (JWT kullanıyoruz, session yok)
                .csrf(AbstractHttpConfigurer::disable)

                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/**",      // Login endpoint public
                                "/h2-console/**",    // H2 Console public
                                "/error"
                        ).permitAll()

                        // Protected endpoints
                        .requestMatchers("/api/account/**").authenticated()

                        // Diğer her şey authenticated
                        .anyRequest().authenticated()
                )

                // ⚠️ KRİTİK: Session STATELESS!
                // Spring Security session oluşturmasın
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authentication provider
                .authenticationProvider(authenticationProvider())

                // JWT filter'ı ekle
                // UsernamePasswordAuthenticationFilter'dan ÖNCE çalışsın
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // H2 Console için frame options
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }

    /**
     * Authentication Provider
     * UserDetailsService ve PasswordEncoder kullanır
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Authentication Manager
     * Login işlemi için gerekli
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Password Encoder
     * ⚠️ PRODUCTION'DA BCryptPasswordEncoder KULLAN!
     * Öğrenme amaçlı NoOp kullanıyoruz
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();  // Production için
        return NoOpPasswordEncoder.getInstance();  // Learning için
    }
}
