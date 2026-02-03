package com.systemdesign.mindset.security;

import com.systemdesign.mindset.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Service - STATELESS Authentication'ın Kalbi
 * <p>
 * Bu service:
 * - JWT token oluşturur
 * - JWT token'ı validate eder
 * - Token'dan bilgi çıkarır
 * <p>
 * ⚠️ DİKKAT: Server hiçbir şey hatırlamaz!
 * Token self-contained (kendi kendine yeterli)
 */
@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Secret key'i SecretKey objesine çevir
     */

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
    /**
     * JWT Token Oluştur
     *
     * Token içinde:
     * - userId
     * - username
     * - Issued at (ne zaman oluşturuldu)
     * - Expiration (ne zaman bitecek)
     *
     * ✅ STATELESS: Server RAM'de hiçbir şey saklamaz!
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());

        String token = Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();

        log.debug("✅ JWT token generated for user: {} (Token will expire in {} ms)",
                user.getUsername(), expiration);
        log.debug("Token (first 20 chars): {}...", token.substring(0, Math.min(20, token.length())));

        return token;
    }

    /**
     * Token'dan username çıkar
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Token'dan userId çıkar
     */
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Token'dan expiration date çıkar
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic claim extractor
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *  Tüm claim'leri çıkar
     * ⚠️ Signature doğrulaması burada yapılır!
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.error("❌ JWT validation failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Token expire olmuş mu?
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Token geçerli mi?
     *
     * Kontroller:
     * 1. Username eşleşiyor mu?
     * 2. Token expire olmamış mı?
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean usernameMatches = username.equals(userDetails.getUsername());
            boolean notExpired = !isTokenExpired(token);

            if (usernameMatches && notExpired) {
                log.debug("✅ Token valid for user: {}", username);
                return true;
            } else {
                log.warn("❌ Token validation failed: username match={}, not expired={}",
                        usernameMatches, notExpired);
                return false;
            }
        } catch (Exception e) {
            log.error("❌ Token validation error: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Token'dan user bilgilerini debug için göster
     */
    public void logTokenInfo(String token) {
        try {
            String username = extractUsername(token);
            Long userId = extractUserId(token);
            Date expiration = extractExpiration(token);

            log.debug("📋 Token Info:");
            log.debug("   Username: {}", username);
            log.debug("   User ID: {}", userId);
            log.debug("   Expires at: {}", expiration);
            log.debug("   Is expired: {}", isTokenExpired(token));
        } catch (Exception e) {
            log.error("Cannot parse token: {}", e.getMessage());
        }
    }

}
