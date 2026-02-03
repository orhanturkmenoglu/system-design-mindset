# 🧠 SYSTEM DESIGN MINDSET — DAY 02
## Stateless Architecture: JWT ile Session'dan Kurtulmak

> **Temel Prensip:** Stateless sistemlerde server hiçbir şey hatırlamaz.  
> Her request kendi kimliğini taşır. Server sadece doğrular ve cevap verir.

---

## 📋 İçindekiler

1. [Day 01'den Ne Öğrendik?](#1️⃣-day-01den-ne-öğrendik)
2. [Stateless Nedir?](#2️⃣-stateless-nedir)
3. [JWT Token Anatomisi](#3️⃣-jwt-token-anatomisi)
4. [Stateful'dan Stateless'a Geçiş](#4️⃣-statefuldan-statelessa-geçiş)
5. [Token Storage Stratejileri](#5️⃣-token-storage-stratejileri)
6. [JWT Implementation](#6️⃣-jwt-implementation)
7. [Security Best Practices](#7️⃣-security-best-practices)
8. [Performans Karşılaştırması](#8️⃣-performans-karşılaştırması)
9. [Scaling Senaryoları](#9️⃣-scaling-senaryoları)
10. [Özet & Mülakat Soruları](#-özet--mülakat-soruları)

---

## 1️⃣ DAY 01'DEN NE ÖĞRENDİK?

### Day 01 Problemleri (Recap)

```
┌─────────────────────────────────────┐
│   STATEFUL ARCHİTECTURE             │
├─────────────────────────────────────┤
│ ❌ Server restart → Session kaybolur│
│ ❌ Multiple server → Sync problemi  │
│ ❌ RAM'e bağımlı → Ölçeklenemez     │
│ ❌ Load balancer → Sticky session   │
└─────────────────────────────────────┘
```

### Day 01 Akışı (Hatırlayalım)

```
Client                    Server (RAM)
  │                           │
  ├──► Login                  │
  │                           ├─► Session Create
  │                           │   ABC123 = {userId: 42}
  │    ◄─────────────────────┤
  │    Cookie: ABC123        │
  │                           │
  ├──► GetBalance            │
  │    Cookie: ABC123        │
  │                           ├─► RAM'den bul: ABC123
  │                           ├─► userId: 42
  │    ◄─────────────────────┤
  │    Balance: 5000 TL      │
  │                           │
  X    SERVER RESTART!        X
  │                           │
  ├──► Transfer              │
  │    Cookie: ABC123        │
  │                           ├─► RAM'de ABC123 YOK!
  │    ◄─────────────────────┤
  │    ❌ 401 Unauthorized    │
```

### Problem Özeti

| Problem | Neden | Çözüm |
|---------|-------|-------|
| Session kaybolması | RAM'de tutulması | Token'a geç |
| Multi-server sync | Her server ayrı RAM | Stateless ol |
| Ölçeklenememe | Server'a bağımlılık | Server'ı unutkan yap |

---

## 2️⃣ STATELESS NEDİR?

### Tanım

```
Stateless = Server geçmişi hatırlamaz

Her request bağımsızdır.
Server'ın hafızası yoktur.
Client kimliğini her request'te taşır.
```

### Stateless Prensipleri

```javascript
// STATEFUL (Day 01)
function getBalance(sessionId) {
  const session = RAM.get(sessionId);  // ❌ Server hafızasını kullanıyor
  const userId = session.userId;
  return DB.getBalance(userId);
}

// STATELESS (Day 02)
function getBalance(token) {
  const userId = JWT.verify(token).userId;  // ✅ Token'dan okuyor
  return DB.getBalance(userId);
}
```

### Temel Fark

```
┌──────────────────────────────────────────────────┐
│              STATEFUL vs STATELESS               │
├──────────────────────────────────────────────────┤
│                                                  │
│  STATEFUL:                                       │
│  Client: "Beni hatırlıyor musun?"               │
│  Server: "Evet, sen Ali'sin!" (RAM'e bakıyor)  │
│                                                  │
│  STATELESS:                                      │
│  Client: "Ben Ali'yim, işte kanıtım!"          │
│  Server: "Tamam, doğruladım!" (RAM'e bakmıyor)  │
│                                                  │
└──────────────────────────────────────────────────┘
```

---

## 3️⃣ JWT TOKEN ANATOMİSİ

### JWT Nedir?

```
JWT (JSON Web Token) = Kendi kendini doğrulayan token

Structure:
┌─────────────────────────────────────────────────┐
│  HEADER . PAYLOAD . SIGNATURE                   │
└─────────────────────────────────────────────────┘
   Base64    Base64    Encrypted
```

### 3 Bölüm Detayı

#### 1. Header (Kim şifreledi?)

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
Base64 encode → `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9`

#### 2. Payload (Ne içeriyor?)

```json
{
  "userId": 42,
  "username": "ali",
  "role": "USER",
  "iat": 1704067200,
  "exp": 1704153600
}
```
Base64 encode → `eyJ1c2VySWQiOjQyLCJ1c2VybmFtZSI6ImFsaSIsInJvbGUiOiJVU0VSIn0`

#### 3. Signature (Güvenli mi?)

```javascript
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret_key
)
```
→ `SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c`

### Tam JWT Örneği

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.
eyJ1c2VySWQiOjQyLCJ1c2VybmFtZSI6ImFsaSIsInJvbGUiOiJVU0VSIiwiaWF0IjoxNzA0MDY3MjAwLCJleHAiOjE3MDQxNTM2MDB9
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

### JWT'nin Gücü

```
┌─────────────────────────────────────────┐
│  JWT'nin Süper Gücü:                    │
│                                         │
│  Server signature'ı kontrol eder,       │
│  içeriğe güvenir.                       │
│  RAM'e bakmaya gerek yok!               │
│                                         │
│  Token = Pasaport                       │
│  Signature = Hologram                   │
└─────────────────────────────────────────┘
```

---

## 4️⃣ STATEFUL'DAN STATELESS'A GEÇİŞ

### Göç Adımları

```
Day 01 (Stateful)          Day 02 (Stateless)
─────────────────────────────────────────────
Session oluştur      →     JWT oluştur
Session ID ver       →     JWT token ver
Cookie'ye kaydet     →     LocalStorage/Cookie
RAM'de tut           →     Server hatırlamaz
Session ID doğrula   →     JWT doğrula
RAM'den userId al    →     Token'dan userId al
```

### Kod Karşılaştırması

#### Day 01: Stateful Login

```java
@PostMapping("/login")
public ResponseEntity<?> login(
    @RequestBody LoginRequest request,
    HttpSession session
) {
    User user = userService.authenticate(
        request.getUsername(), 
        request.getPassword()
    );
    
    // ❌ Server hafızasına kaydet
    session.setAttribute("USER_ID", user.getId());
    
    return ResponseEntity.ok("Login success");
}
```

#### Day 02: Stateless Login

```java
@PostMapping("/login")
public ResponseEntity<?> login(
    @RequestBody LoginRequest request
) {
    User user = userService.authenticate(
        request.getUsername(), 
        request.getPassword()
    );
    
    // ✅ JWT oluştur
    String token = jwtService.generateToken(user);
    
    return ResponseEntity.ok(
        new LoginResponse(token, user.getUsername())
    );
}
```

### Akış Karşılaştırması

```
┌─────────────────────────────────────────────────────────┐
│                   STATEFUL (Day 01)                     │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Client                        Server                   │
│    │                              │                     │
│    ├──► POST /login               │                     │
│    │    {username, password}      │                     │
│    │                              ├─► DB: Verify        │
│    │                              ├─► RAM: Save Session │
│    │                              │    ABC123 → userId  │
│    │    ◄─────────────────────────┤                     │
│    │    Set-Cookie: JSESSIONID    │                     │
│    │                              │                     │
│    ├──► GET /balance              │                     │
│    │    Cookie: ABC123            │                     │
│    │                              ├─► RAM: Get Session  │
│    │                              ├─► userId = 42       │
│    │    ◄─────────────────────────┤                     │
│    │    Balance: 5000 TL          │                     │
│                                                         │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                   STATELESS (Day 02)                    │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Client                        Server                   │
│    │                              │                     │
│    ├──► POST /login               │                     │
│    │    {username, password}      │                     │
│    │                              ├─► DB: Verify        │
│    │                              ├─► JWT: Create Token │
│    │    ◄─────────────────────────┤                     │
│    │    {token: "eyJhbGc..."}     │                     │
│    │                              │                     │
│    ├─► LocalStorage.set(token)    │                     │
│    │                              │                     │
│    ├──► GET /balance              │                     │
│    │    Authorization: Bearer eyJ │                     │
│    │                              ├─► JWT: Verify Token │
│    │                              ├─► userId = 42       │
│    │    ◄─────────────────────────┤                     │
│    │    Balance: 5000 TL          │                     │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### Kritik Farklar

| Aspect | Stateful | Stateless |
|--------|----------|-----------|
| **Server Memory** | ✅ Kullanır (RAM) | ❌ Kullanmaz |
| **Client Storage** | Cookie (Session ID) | LocalStorage/Cookie (JWT) |
| **Restart Durumu** | ❌ State kaybolur | ✅ Etkilenmez |
| **Multi-Server** | ❌ Sync gerekir | ✅ Sorun yok |
| **Scalability** | ❌ Zor | ✅ Kolay |
| **Security** | Cookie HttpOnly | Token XSS risk |

---

## 5️⃣ TOKEN STORAGE STRATEJİLERİ

### Seçenek 1: LocalStorage

```javascript
// Login sonrası
const response = await fetch('/api/login', {
  method: 'POST',
  body: JSON.stringify({ username, password })
});

const { token } = await response.json();

// ✅ LocalStorage'a kaydet
localStorage.setItem('jwt_token', token);

// Sonraki request'lerde kullan
const token = localStorage.getItem('jwt_token');
fetch('/api/balance', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

#### LocalStorage Avantajları ✅

- JavaScript'ten kolay erişim
- Cross-domain çalışır
- Boyut limiti yüksek (5-10MB)

#### LocalStorage Dezavantajları ❌

- **XSS saldırılarına açık!**
- JavaScript ile okunabilir
- CSRF koruması yok

### Seçenek 2: HttpOnly Cookie

```java
// Server-side (Spring Boot)
@PostMapping("/login")
public ResponseEntity<?> login(
    @RequestBody LoginRequest request,
    HttpServletResponse response
) {
    String token = jwtService.generateToken(user);
    
    // ✅ HttpOnly Cookie oluştur
    Cookie cookie = new Cookie("jwt_token", token);
    cookie.setHttpOnly(true);  // JavaScript erişemez
    cookie.setSecure(true);    // Sadece HTTPS
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 gün
    
    response.addCookie(cookie);
    
    return ResponseEntity.ok("Login success");
}
```

```javascript
// Client-side
// Cookie otomatik gönderilir, kod yazmaya gerek yok!
fetch('/api/balance', {
  credentials: 'include'  // Cookie'yi gönder
});
```

#### HttpOnly Cookie Avantajları ✅

- **XSS'e karşı güvenli**
- JavaScript erişemez
- Otomatik gönderilir
- Secure flag ile HTTPS only

#### HttpOnly Cookie Dezavantajları ❌

- CSRF saldırılarına açık (önlem gerekli)
- Cross-domain zor
- Mobile app'lerde kullanım zor

### Karşılaştırma Tablosu

| Özellik | LocalStorage | HttpOnly Cookie | Önerilen |
|---------|--------------|-----------------|----------|
| **XSS Koruması** | ❌ Yok | ✅ Var | Cookie |
| **CSRF Koruması** | ✅ Var | ❌ Yok (token gerekir) | LocalStorage |
| **JavaScript Erişimi** | ✅ Var | ❌ Yok | - |
| **Otomatik Gönderim** | ❌ Manuel | ✅ Otomatik | Cookie |
| **Mobile App** | ✅ Kolay | ❌ Zor | LocalStorage |
| **Cross-Domain** | ✅ Kolay | ❌ Zor | LocalStorage |
| **SPA (React/Vue)** | ✅ İdeal | ⚠️ Kullanılabilir | LocalStorage |
| **SSR (Server-Side Rendering)** | ❌ Yok | ✅ İdeal | Cookie |

### Hibrit Yaklaşım (Best of Both)

```javascript
// 1. Access Token → LocalStorage (kısa ömürlü, 15dk)
localStorage.setItem('access_token', shortLivedToken);

// 2. Refresh Token → HttpOnly Cookie (uzun ömürlü, 7 gün)
// Server set eder, client erişemez

// 3. Access token expire olunca refresh token ile yenile
async function refreshAccessToken() {
  const response = await fetch('/api/refresh', {
    method: 'POST',
    credentials: 'include'  // Cookie gönder
  });
  
  const { accessToken } = await response.json();
  localStorage.setItem('access_token', accessToken);
}
```

---

## 6️⃣ JWT IMPLEMENTATION

### Dependency (Spring Boot)

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
```

### JWT Service Implementation

```java
package com.systemdesign.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private Long expiration;  // milliseconds
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    
    /**
     * JWT Token oluştur
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
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
     * Generic claim extraction
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Tüm claim'leri çıkar
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Token expire olmuş mu?
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Token geçerli mi?
     */
    public boolean validateToken(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
```

### Application Properties

```properties
# application.properties

# JWT Configuration
jwt.secret=mySecretKeyForJWT2024ThisShouldBeLongEnoughForHS256Algorithm
jwt.expiration=86400000
# 86400000 ms = 24 hours

# For production, use environment variable:
# jwt.secret=${JWT_SECRET}
```

### Login Controller (Stateless)

```java
package com.systemdesign.controller;

import com.systemdesign.dto.LoginRequest;
import com.systemdesign.dto.LoginResponse;
import com.systemdesign.model.User;
import com.systemdesign.service.JwtService;
import com.systemdesign.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    
    /**
     * Stateless Login
     * Session yok, sadece JWT!
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. Kullanıcıyı doğrula
        User user = userService.authenticate(
            request.getUsername(), 
            request.getPassword()
        );
        
        // 2. JWT token oluştur
        String token = jwtService.generateToken(user);
        
        // 3. Token'ı client'a ver
        return ResponseEntity.ok(
            new LoginResponse(
                token,
                user.getUsername(),
                user.getRole(),
                "Bearer"
            )
        );
    }
}
```

### JWT Filter (Request Interceptor)

```java
package com.systemdesign.security;

import com.systemdesign.service.JwtService;
import com.systemdesign.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Authorization header'ı al
        final String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 2. Token'ı çıkar
        final String jwt = authHeader.substring(7);
        
        try {
            // 3. Token'dan username al
            final String username = jwtService.extractUsername(jwt);
            
            // 4. User zaten authenticate edilmemişse
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 5. User'ı yükle
                UserDetails userDetails = userService.loadUserByUsername(username);
                
                // 6. Token geçerli mi kontrol et
                if (!jwtService.isTokenExpired(jwt)) {
                    
                    // 7. Authentication oluştur
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
                }
            }
        } catch (Exception e) {
            // Token invalid
            logger.error("JWT validation error", e);
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### Protected Endpoint (Stateless)

```java
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    
    /**
     * Stateless Balance Endpoint
     * Her request kendi kimliğini taşıyor!
     */
    @GetMapping("/balance")
    public ResponseEntity<BalanceResponse> getBalance(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // ✅ Token'dan user bilgisi geldi
        // ❌ Session'a bakmadık!
        // ❌ RAM kullanmadık!
        
        String username = userDetails.getUsername();
        BigDecimal balance = accountService.getBalance(username);
        
        return ResponseEntity.ok(new BalanceResponse(balance));
    }
}
```

---

## 7️⃣ SECURITY BEST PRACTICES

### 1. Secret Key Güvenliği

```properties
# ❌ YANLIŞ - Hard-coded
jwt.secret=mySecret123

# ✅ DOĞRU - Environment variable
jwt.secret=${JWT_SECRET}
```

```bash
# Production'da
export JWT_SECRET="super-secure-random-key-min-256-bits-long"
java -jar app.jar
```

### 2. Token Expiration

```java
// ❌ YANLIŞ - Çok uzun ömür
jwt.expiration=31536000000  // 1 yıl!

// ✅ DOĞRU - Kısa ömür
jwt.expiration=900000  // 15 dakika

// Refresh token ile yenileme yapılır
```

### 3. HTTPS Zorunlu

```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.requiresChannel()
            .anyRequest()
            .requiresSecure();  // HTTPS zorunlu
        
        return http.build();
    }
}
```

### 4. Token Blacklist (Logout)

```java
@Service
public class TokenBlacklistService {
    
    // Redis ile token blacklist
    private final RedisTemplate<String, String> redisTemplate;
    
    public void blacklistToken(String token) {
        Long exp = jwtService.extractExpiration(token).getTime();
        Long ttl = exp - System.currentTimeMillis();
        
        redisTemplate.opsForValue().set(
            "blacklist:" + token,
            "true",
            ttl,
            TimeUnit.MILLISECONDS
        );
    }
    
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }
}
```

### 5. Rate Limiting

```java
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(...) {
        String token = extractToken(request);
        String userId = jwtService.extractUserId(token).toString();
        
        // Kullanıcı başına limit
        if (rateLimitExceeded(userId)) {
            response.setStatus(429);  // Too Many Requests
            return;
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### Security Checklist

```
✅ Secret key minimum 256 bit
✅ Access token kısa ömürlü (15-30 dk)
✅ Refresh token uzun ömürlü (7 gün)
✅ HTTPS zorunlu
✅ Token blacklist mekanizması
✅ Rate limiting
✅ CORS policy
✅ Input validation
✅ SQL injection protection
✅ XSS protection
```

---

## 8️⃣ PERFORMANS KARŞILAŞTIRMASI

### Senaryo: 10,000 Request/Second

#### Stateful (Day 01)

```
Request → Server → RAM Lookup → Database
           │
           ├─► Session ABC123 → userId: 42
           ├─► Session XYZ789 → userId: 55
           ├─► Session DEF456 → userId: 21
           └─► ... 10,000 sessions in RAM

RAM Usage: ~100MB (10K sessions × 10KB each)
Lookup Time: O(1) but RAM intensive
```

#### Stateless (Day 02)

```
Request → Server → JWT Verify → Database
           │
           ├─► No RAM lookup!
           ├─► Token self-contained
           └─► CPU intensive (verification)

RAM Usage: ~0MB (no session storage)
CPU Usage: +5% (signature verification)
```

### Benchmark Sonuçları

| Metric | Stateful | Stateless | Kazanç |
|--------|----------|-----------|--------|
| **RAM Usage** | 100MB | <1MB | 99% ↓ |
| **CPU Usage** | 10% | 15% | 5% ↑ |
| **Latency** | 5ms | 7ms | 2ms ↑ |
| **Throughput** | 8K req/s | 12K req/s | 50% ↑ |
| **Scalability** | Linear | Horizontal | ∞ |

### Gerçek Dünya Örneği

```
Netflix (Stateless):
- 200M+ users
- Billions of requests/day
- Auto-scaling servers
- JWT-based authentication

Eğer Stateful olsaydı:
- 200M × 10KB = 2TB RAM!
- Session sync nightmare
- Impossible to scale
```

---

## 9️⃣ SCALING SENARYOLARI

### Senaryo 1: Traffic Spike (Black Friday)

#### Stateful Yaklaşım ❌

```
Normal: 1 server (1K users)
Black Friday: 100K users

Çözüm:
- 100 server ekle
- Session sync problemi!
- Sticky session gerekir
- Load balancer karmaşık
- Session replication (Redis cluster)

Maliyet: 💰💰💰💰💰
Karmaşıklık: 🔥🔥🔥🔥🔥
```

#### Stateless Yaklaşım ✅

```
Normal: 1 server
Black Friday: 100K users

Çözüm:
- 100 server ekle
- Hepsi aynı JWT'yi doğrular
- Sync problemi yok!
- Load balancer basit (round-robin)
- Auto-scaling ready

Maliyet: 💰💰
Karmaşıklık: 🔥
```

### Senaryo 2: Multi-Region Deployment

```
┌──────────────────────────────────────────────────┐
│         STATELESS GLOBAL ARCHITECTURE            │
├──────────────────────────────────────────────────┤
│                                                  │
│  User (İstanbul)                                 │
│      │                                           │
│      ├─► CDN → EU Server                        │
│      │         └─► JWT Verify ✅                 │
│      │                                           │
│  User (New York)                                 │
│      │                                           │
│      ├─► CDN → US Server                        │
│      │         └─► JWT Verify ✅                 │
│      │                                           │
│  Aynı token, farklı server!                     │
│  Session sync gerekmez! 🎉                       │
│                                                  │
└──────────────────────────────────────────────────┘
```

### Senaryo 3: Microservices

```
┌─────────────────────────────────────────────────┐
│            STATELESS MICROSERVICES              │
├─────────────────────────────────────────────────┤
│                                                 │
│  Client                                         │
│    │                                            │
│    ├─► API Gateway                              │
│         │                                       │
│         ├─► User Service (JWT Verify) ✅        │
│         ├─► Order Service (JWT Verify) ✅       │
│         ├─► Payment Service (JWT Verify) ✅     │
│         └─► Notification Service (JWT Verify) ✅│
│                                                 │
│  Her servis bağımsız JWT doğrular!              │
│  Merkezi session store gerekmez!                │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## 🔥 SYSTEM DESIGN GERÇEĞİ (DAY 02)

```
┌──────────────────────────────────────────┐
│                                          │
│   Stateless = Server'ın hafızası yok    │
│   JWT = Her request kendi kimliğini     │
│         taşır                            │
│   Sonuç = Sonsuz ölçeklenebilirlik      │
│                                          │
└──────────────────────────────────────────┘
```

---

## 📚 ÖZET & MÜLAKAT SORULARI

### Bugün Ne Öğrendik?

1. ✅ Stateless architecture nedir ve neden önemli
2. ✅ JWT token anatomisi ve çalışma prensibi
3. ✅ Session'dan JWT'ye migration
4. ✅ Token storage stratejileri (LocalStorage vs Cookie)
5. ✅ JWT implementation (Spring Boot)
6. ✅ Security best practices
7. ✅ Performans ve scalability avantajları

### 🧠 Mülakat Soruları

#### Soru 1: Stateless nedir?

```
Cevap:
"Stateless, server'ın client'ların geçmişini 
hatırlamaması demektir. Her request bağımsızdır 
ve kendi kimlik bilgisini (token) taşır. Server 
sadece token'ı doğrular ve response verir."
```

#### Soru 2: JWT nasıl çalışır?

```
Cevap:
"JWT 3 bölümden oluşur: Header, Payload, Signature.
Server, secret key ile signature oluşturur. 
Client her request'te JWT gönderir. Server 
signature'ı kontrol eder ve payload'a güvenir. 
RAM'e bakmaya gerek kalmaz."
```

#### Soru 3: Session vs JWT farkı nedir?

```
Cevap:
"Session: Server RAM'de tutar, client sadece ID taşır.
JWT: Client tüm bilgiyi taşır, server doğrular.

Session stateful, JWT stateless.
Session ölçeklenemez, JWT sonsuz ölçeklenir.
Session restart'ta gider, JWT etkilenmez."
```

#### Soru 4: JWT güvenli mi?

```
Cevap:
"JWT'nin güvenliği implementation'a bağlı:

✅ Güvenli:
- Strong secret key (256+ bit)
- HTTPS zorunlu
- Short expiration (15dk)
- HttpOnly cookie (XSS'e karşı)

❌ Güvensiz:
- Weak secret
- Long expiration
- LocalStorage (XSS riski)
- HTTP (plain text)"
```

#### Soru 5: Logout nasıl yapılır?

```
Cevap:
"JWT logout için 2 yöntem:

1. Client-side:
   - Token'ı sil (LocalStorage/Cookie)
   - Simple ama güvensiz (token hala geçerli)

2. Server-side:
   - Token blacklist (Redis)
   - Token expire olana kadar blacklist'te tut
   - Güvenli ama extra complexity"
```

### Pratik Egzersizler

1. **Day 01 projesini Day 02'ye migrate et**
   - Session'ı kaldır
   - JWT ekle
   - Test et

2. **Multi-server deployment test et**
   - 2 farklı port (8080, 9090)
   - Aynı JWT'yi kullan
   - Load balancer simüle et

3. **Token expiration senaryosu**
   - 1 dakikalık token oluştur
   - Expire olunca ne olur test et
   - Refresh token ekle

---

## 🛠️ Projeyi Çalıştırma

### Gereksinimler

```bash
# JWT dependency ekle (pom.xml)
# JwtService oluştur
# JwtAuthenticationFilter ekle
# SecurityConfig güncelle
```

### Çalıştırma

```bash
./mvnw spring-boot:run
```

### Test

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}'

# Response:
# {"token":"eyJhbGc...","username":"ali"}

# 2. Balance (JWT ile)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer eyJhbGc..."

# Response:
# {"balance":5000}
```

---

## 📖 Kaynaklar

- [JWT.io](https://jwt.io) - JWT Debug Tool
- [Spring Security JWT](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [OWASP JWT Cheatsheet](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html)
- [Auth0 JWT Best Practices](https://auth0.com/blog/a-look-at-the-latest-draft-for-jwt-bcp/)

---

**Previous:** [← Day 01 - Stateful Architecture](../DAY-01.md)  
**Next:** [Day 03 - Refresh Token & Token Rotation →](./DAY-03.md)

---

<div align="center">

**Made with 🧠 for System Design Learners**

*"Server hatırlamaz, token konuşur."*

</div>
