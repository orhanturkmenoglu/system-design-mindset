package com.systemdesign.mindset.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        final String requestPath = request.getRequestURI();

        // 1. Authorization header'ı al
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Header yoksa veya Bearer ile başlamıyorsa, filter chain'e devam et
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No JWT token found for path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 2. Token'ı çıkar (Bearer prefix'ini kaldır)
            final String jwt = authHeader.substring(7);

            log.debug("Processing JWT token for path: {}", requestPath);

            // 3. Token'dan username'i çıkar
            final String username = jwtService.extractUsername(jwt);

            // 4. User zaten authenticate edilmemişse
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. User'ı yükle (Database'den)
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // 6. Token geçerli mi kontrol et
                if (jwtService.validateToken(jwt, userDetails)) {

                    log.debug("✅ JWT token valid, authenticating user: {}", username);

                    // 7. Authentication object oluştur
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 8. Security context'e set et
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.debug("User {} authenticated successfully via JWT", username);
                } else {
                    log.warn("❌ Invalid JWT token for user: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("❌ JWT authentication error: {}", e.getMessage());
        }

        // 9. Filter chain'e devam et
        filterChain.doFilter(request, response);
    }
}
