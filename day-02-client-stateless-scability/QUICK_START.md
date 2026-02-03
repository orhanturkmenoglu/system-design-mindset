# 🚀 QUICK START GUIDE - DAY 02 (Stateless/JWT)

## Hızlı Başlangıç (5 Dakika)

### Gereksinimler

- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ Terminal / Command Prompt

---

### Adım 1: Projeyi İndir

```bash
git clone <your-repo-url>
cd system-design-day02
```

---

### Adım 2: Çalıştır

```bash
./mvnw spring-boot:run

# Windows için:
mvnw.cmd spring-boot:run
```

Uygulama başladığında şu mesajı göreceksin:

```
╔════════════════════════════════════════════════════╗
║   SYSTEM DESIGN MINDSET - DAY 02                   ║
║   Stateless Architecture with JWT                  ║
╚════════════════════════════════════════════════════╝

🚀 Application started successfully!

📝 Endpoints:
   POST   /api/auth/login      - Login (JWT oluştur)
   GET    /api/account/balance - Balance (JWT gerekli)
   POST   /api/account/transfer- Transfer (JWT gerekli)

🔐 Authentication: JWT Token-based (Stateless)

✨ Day 01'den Farklar:
   ❌ Session yok (RAM'de state tutulmaz)
   ✅ JWT token (Client taşır, server doğrular)
   ✅ Horizontal scaling ready
   ✅ Multi-server çalışır!
```

---

### Adım 3: Test Et (JWT Workflow)

```bash
# 1. Login (JWT Token Al)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}'

# Response:
# {
#   "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjE...",
#   "type": "Bearer",
#   "username": "ali",
#   "message": "Login successful - JWT token generated (Stateless!)"
# }

# 2. Token'ı Kaydet
TOKEN="eyJhbGciOiJIUzI1NiJ9..."  # Token'ı buraya yapıştır

# 3. Balance Sorgula (JWT ile)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"

# Response:
# {
#   "username": "ali",
#   "balance": 5000,
#   "message": "Retrieved from JWT token-based authentication (Stateless!)"
# }

# 4. Transfer Yap
curl -X POST http://localhost:8080/api/account/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"toUsername":"ayse","amount":500}'

# Response:
# {
#   "message": "Transfer successful (processed via JWT auth)",
#   "fromUsername": "ali",
#   "toUsername": "ayse",
#   "amount": 500,
#   "newBalance": 4500
# }
```

**Tebrikler! 🎉** Stateless architecture çalışıyor!

---

### ✨ Ne Oldu? (Day 01'den Farklar)

| Day 01 (Stateful) | Day 02 (Stateless) |
|-------------------|---------------------|
| ❌ Session cookie | ✅ JWT token |
| ❌ Server RAM'de state | ✅ Server RAM temiz |
| ❌ `-c cookies.txt` | ✅ `Authorization: Bearer` |
| ❌ Restart → Session gider | ✅ Restart → Token geçerli |
| ❌ Multi-server → Fail | ✅ Multi-server → Success |

---

## 📁 Proje Yapısı

```
system-design-day02/
├── src/
│   ├── main/
│   │   ├── java/com/systemdesign/
│   │   │   ├── SystemDesignDay02Application.java   # Ana uygulama
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java             # Login → JWT
│   │   │   │   └── AccountController.java          # Balance/Transfer
│   │   │   ├── service/
│   │   │   │   └── UserService.java                # Business logic + UserDetailsService
│   │   │   ├── security/                          # ⭐ YENİ!
│   │   │   │   ├── JwtService.java                # JWT oluştur/validate
│   │   │   │   └── JwtAuthenticationFilter.java   # Her request'te JWT kontrol
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java            # Spring Security + JWT
│   │   │   │   └── DataInitializer.java           # Test data
│   │   │   ├── model/
│   │   │   │   └── User.java                      # JPA Entity + UserDetails
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java             # Token içerir
│   │   │   │   └── ...
│   │   │   └── repository/
│   │   │       └── UserRepository.java
│   │   └── resources/
│   │       └── application.properties              # JWT config
│   └── test/
├── pom.xml                         # JWT dependencies eklendi
├── README.md                       # Day 02 detaylı rehber
├── API_TESTS.md                    # JWT test örnekleri
├── QUICK_START.md                  # Bu dosya
└── PROJECT_STRUCTURE.md            # Proje yapısı
```

**Katmanlar:**
- **3 Controller** - Auth, Account
- **2 Security** - JwtService, JwtAuthenticationFilter
- **1 Service** - UserService (+ UserDetailsService)
- **1 Entity** - User (+ UserDetails)
- **5 DTO** - Request/Response
- **1 Repository** - Spring Data JPA
- **2 Config** - SecurityConfig, DataInitializer

---

## 🧪 Test Kullanıcıları

| Username | Password | Balance |
|----------|----------|---------|
| ali      | 1234     | 5000 TL |
| ayse     | 1234     | 3000 TL |
| mehmet   | 1234     | 10000 TL |

---

## 🔧 Multi-Server Test (Horizontal Scaling!)

### Terminal 1 - Server A (Port 8080):

```bash
./mvnw spring-boot:run
```

### Terminal 2 - Server B (Port 9090):

```bash
./mvnw spring-boot:run -Dserver.port=9090
```

### Test Senaryosu:

```bash
# 1. Server A'ya login, token al
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}'

# Token'ı kaydet
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# 2. Server A'dan balance (✅ Çalışır)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"

# 3. Server B'den balance (✅ ÇALIŞIR! 🎉)
curl http://localhost:9090/api/account/balance \
  -H "Authorization: Bearer $TOKEN"
```

### 🎯 Sonuç:

```
┌──────────────────────────────────────────────┐
│  Server A (8080)    Server B (9090)          │
│  ──────────────────  ──────────────────      │
│  JWT Validate ✅     JWT Validate ✅          │
│  Same Token         Same Token              │
│  Works!             Works!                   │
│                                              │
│  🎉 HER İKİ SERVER DE AYNI TOKEN'I           │
│     DOĞRULAYIP ÇALIŞIYOR!                    │
│                                              │
│  Day 01'de: 401 Unauthorized ❌              │
│  Day 02'de: Success! ✅                      │
└──────────────────────────────────────────────┘
```

---

## 🔄 Server Restart Test

```bash
# 1. Token al
TOKEN="..."

# 2. Balance sorgula (✅ Çalışır)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"

# 3. Server'ı RESTART et

# 4. Aynı token ile tekrar dene (✅ HALA ÇALIŞIYOR! 🎉)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"
```

**Sonuç:**
- Day 01: Session RAM'de, restart sonrası gitti ❌
- Day 02: Token client'ta, restart etkilemedi ✅

---

## 📝 Endpoints

### Authentication

- `POST /api/auth/login` - Login (JWT token al)
- `GET /api/auth/token-info` - Token bilgileri (debug)
- `GET /api/auth/compare` - Day 01 vs Day 02 karşılaştırma

### Account (JWT Gerekli)

- `GET /api/account/balance` - Bakiye sorgula
- `POST /api/account/transfer` - Transfer yap
- `GET /api/account/profile` - Profil bilgisi
- `GET /api/account/auth-debug` - Auth debug bilgisi

---

## 🗄️ H2 Database Console

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (boş)
```

---

## 🐛 Sık Sorulan Sorular

### Token nerede saklanır?

```
Client-side storage:
- LocalStorage (Web)
- SessionStorage (Web)
- Secure storage (Mobile)
- Environment variable (Test)

⚠️ XSS'e dikkat! HttpOnly cookie daha güvenli.
```

### Token expire oldu mu?

```bash
# Token info kontrol et
curl http://localhost:8080/api/auth/token-info \
  -H "Authorization: Bearer $TOKEN"

# Expired: true ise yeni login gerekir
```

### 403 Forbidden hatası?

```
Sebep 1: Token göndermemişsin
Çözüm: Authorization header ekle

Sebep 2: Token geçersiz/expired
Çözüm: Yeni login yap

Sebep 3: Token format yanlış
Çözüm: "Bearer " prefix ekle
```

### Spring Security devreye girmiyor?

```bash
# Log seviyesini artır
logging.level.org.springframework.security=DEBUG

# SecurityConfig kontrol et
# SessionCreationPolicy.STATELESS mi?
```

---

## 💡 İpuçları

1. 🔐 **Token'ı güvenli sakla** - Sensitive data!
2. 📋 **Postman kullan** - Environment variable ile token yönetimi kolay
3. 🕐 **Expiration kontrol et** - 24 saat sonra expire oluyor
4. 🔍 **jwt.io'da decode et** - Token içeriğini gör
5. 🧪 **Multi-server dene** - Horizontal scaling'i gör
6. 🔄 **Restart test et** - Stateless'ın gücünü gör

---

## 🎯 Sonraki Adımlar

### 1. Tüm Endpoint'leri Test Et

```bash
✓ POST /api/auth/login
✓ GET  /api/auth/token-info
✓ GET  /api/auth/compare
✓ GET  /api/account/balance
✓ POST /api/account/transfer
✓ GET  /api/account/profile
✓ GET  /api/account/auth-debug
```

### 2. Multi-Server Senaryosunu Dene

- İki terminal aç
- Farklı portlarda çalıştır
- Aynı token'ı her ikisinde kullan
- Day 01'den farkı gör!

### 3. JWT Token'ı İncele

- jwt.io'da decode et
- Payload'ı gör (userId, username, exp)
- Signature'ı anla
- Expiration test et

### 4. Kodu İncele

- `JwtService.java` - Token nasıl oluşturuluyor?
- `JwtAuthenticationFilter.java` - Her request nasıl kontrol ediliyor?
- `SecurityConfig.java` - STATELESS policy nasıl?
- `AuthController.java` - JWT nasıl dönüyor?

### 5. Day 01 ile Karşılaştır

```bash
curl http://localhost:8080/api/auth/compare
```

### 6. Day 03'e Hazırlan

- ✅ Stateless architecture anladın
- ✅ JWT kullanmayı öğrendin
- 🔜 Refresh token öğreneceksin
- 🔜 Token rotation göreceksin
- 🔜 Security best practices

---

## 📚 Öğrenme Yolu

### ✅ Day 01 - Stateful (Tamamlandı)
- Session-based auth
- Server RAM'de state
- Scalability problemleri

### ✅ Day 02 - Stateless (Şu an buradasın)
- JWT token-based auth
- Server RAM temiz
- Horizontal scaling
- Multi-server support

### 🔜 Day 03 - Advanced
- Refresh tokens
- Token rotation
- Token blacklist
- Security hardening

---

## 📖 Ek Kaynaklar

- 📄 **README.md** - Detaylı JWT rehberi
- 🧪 **API_TESTS.md** - Tüm test senaryoları
- 📁 **PROJECT_STRUCTURE.md** - Kod yapısı
- 🎯 **COMMIT_MESSAGES.md** - Git önerileri

---

## 🆘 Yardım Lazım?

1. README'yi oku - En detaylı kaynak
2. Log'lara bak - DEBUG seviyesi açık
3. Token'ı decode et - jwt.io
4. API_TESTS.md'ye bak - Örnekler var

---

**Başarılar! 🚀**

*"Day 01: Server hatırlar. Day 02: Token konuşur!"* 🧠

---

<div align="center">

Made with 🧠 for System Design Learners

**Day 01: Stateful → Day 02: Stateless → Day 03: Advanced**

</div>
