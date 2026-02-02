# 🚀 QUICK START GUIDE

## Hızlı Başlangıç (5 Dakika)

### Gereksinimler

- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ Terminal / Command Prompt

### Adım 1: Projeyi İndir

```bash
git clone <your-repo-url>
cd system-design-day01
```

### Adım 2: Çalıştır

```bash
./mvnw spring-boot:run

# Windows için:
mvnw.cmd spring-boot:run
```

Uygulama başladığında şu mesajı göreceksin:

```
╔════════════════════════════════════════════════════╗
║   SYSTEM DESIGN MINDSET - DAY 01                   ║
║   Stateful Architecture Learning Project           ║
╚════════════════════════════════════════════════════╝

🚀 Application started successfully!

📝 Endpoints:
   POST   /api/auth/login      - Login (Session oluştur)
   GET    /api/account/balance - Balance (Session gerekli)
   POST   /api/account/transfer- Transfer (Session gerekli)
   POST   /api/auth/logout     - Logout (Session sil)

🗄️  H2 Console: http://localhost:8080/h2-console
```

### Adım 3: Test Et

```bash
# 1. Login (Session oluştur)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}' \
  -c cookies.txt

# Response:
# {
#   "message": "Login successful",
#   "username": "ali",
#   "sessionInfo": "Session ID: 3F2A1B4C..."
# }

# 2. Balance Sorgula (Session kullanarak)
curl http://localhost:8080/api/account/balance -b cookies.txt

# Response:
# {
#   "username": "ali",
#   "balance": 5000,
#   "message": "Retrieved from session-based authentication"
# }

# 3. Transfer Yap
curl -X POST http://localhost:8080/api/account/transfer \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{"toUsername":"ayse","amount":500}'

# Response:
# {
#   "message": "Transfer successful",
#   "fromUsername": "ali",
#   "toUsername": "ayse",
#   "amount": 500,
#   "newBalance": 4500
# }
```

**Tebrikler! 🎉** Stateful architecture çalışıyor!

**Ne Oldu?**
1. ✅ Login yaptın → Server RAM'de session oluşturdu
2. ✅ Balance sorguladın → Session'dan userId aldı
3. ✅ Transfer yaptın → Session doğruladı, transfer geçti

**Session nerede?**
- Client: Cookie (JSESSIONID)
- Server: RAM (USER_ID, USERNAME)

---

## 📁 Proje Yapısı

```
system-design-day01/
├── src/
│   ├── main/
│   │   ├── java/com/systemdesign/
│   │   │   ├── SystemDesignApplication.java    # Ana uygulama
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java         # Login/Logout
│   │   │   │   └── AccountController.java      # Balance/Transfer
│   │   │   ├── service/
│   │   │   │   └── UserService.java            # Business logic
│   │   │   ├── model/
│   │   │   │   └── User.java                   # JPA Entity
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── BalanceResponse.java
│   │   │   │   ├── TransferRequest.java
│   │   │   │   └── TransferResponse.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java         # Database access
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java        # Test data
│   │   │   
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml                    # Maven dependencies
├── README.md                  # Detaylı öğrenme rehberi
├── API_TESTS.md              # API test örnekleri
├── QUICK_START.md            # Hızlı başlangıç (bu dosya)
└── PROJECT_STRUCTURE.md      # Proje yapısı detayları
```

**Katmanlar:**
- **3 Controller** - REST API endpoints
- **1 Service** - İş mantığı
- **1 Entity** - User (JPA/Hibernate)
- **5 DTO** - Request/Response nesneleri
- **1 Repository** - Spring Data JPA
- **1 Config** - Test verisi oluşturucu

---

## 🧪 Test Kullanıcıları

| Username | Password | Balance |
|----------|----------|---------|
| ali      | 1234     | 5000 TL |
| ayse     | 1234     | 3000 TL |
| mehmet   | 1234     | 10000 TL |

---

## 📝 Endpoints

### Authentication

- `POST /api/auth/login` - Login (Session oluştur)
- `POST /api/auth/logout` - Logout (Session sil)
- `GET /api/auth/session-info` - Session durumu

### Account

- `GET /api/account/balance` - Bakiye sorgula
- `POST /api/account/transfer` - Transfer yap
- `GET /api/account/profile` - Profil bilgisi

### Easter Egg 🥚

- `GET /api/easter-egg/session-truth` - Session keşfi
- `GET /api/easter-egg/battle` - Stateful vs Stateless
- `GET /api/easter-egg/session-death` - Session öldür

---


## 🗄️ H2 Database Console

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (boş)
```

---

## 🔧 Multi-Server Test (Session Problemi)

### Senaryo: İki Server Çalıştır

### Terminal 1 - Server A (Port 8080):

```bash
./mvnw spring-boot:run
```

### Terminal 2 - Server B (Port 9090):

```bash
./mvnw spring-boot:run -Dserver.port=9090
```

### Test Adımları:

```bash
# 1. Server A'ya login ol
curl -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"ali","password":"1234"}' \
  -H "Content-Type: application/json" \
  -c cookies.txt

# ✅ Response: Login successful
# Session: ABC123 (Server A'nın RAM'inde)

# 2. Server A'dan balance sorgula
curl http://localhost:8080/api/account/balance -b cookies.txt

# ✅ Çalışır!
# Response: {"username":"ali","balance":5000}
# Çünkü: Server A'nın RAM'inde session var

# 3. Server B'den balance sorgula
curl http://localhost:9090/api/account/balance -b cookies.txt

# ❌ ÇALIŞMAZ!
# Response: "Unauthorized: Please login first"
# Çünkü: Server B'nin RAM'inde session yok!
```

### 🔥 Problem Gösterildi!

```
┌─────────────────────────────────────────┐
│  Server A (8080)    Server B (9090)     │
│  ─────────────────  ─────────────────   │
│  Session ABC123     Session YOK!        │
│  └─> Ali            └─> ???             │
│                                         │
│  Aynı cookie, farklı server             │
│  = Session bulunamadı!                  │
└─────────────────────────────────────────┘
```

**Sonuç:**
- Session paylaşılmıyor!
- Bu Day 01'in ana problemi.
- Day 02'de JWT ile çözeceğiz! 🚀

---

## 📚 Öğrenme Yolu

1. ✅ **Day 01** - Stateful Architecture (Şu an buradasın)
   - Session-based auth
   - Scalability problemleri
   - Multi-server challenges

2. 🔜 **Day 02** - Stateless Architecture
   - JWT tokens
   - Horizontal scaling
   - Microservices ready

3. 🔜 **Day 03** - Advanced Topics
   - Refresh tokens
   - Token rotation
   - Security best practices

---

## 🐛 Sık Sorulan Sorular

### Maven wrapper çalışmıyor?

```bash
# Çözüm 1: Maven'i direkt kullan
mvn spring-boot:run

# Çözüm 2: Wrapper'ı yeniden indir
mvn wrapper:wrapper
```

### Port 8080 kullanımda?

```bash
# Farklı port kullan
./mvnw spring-boot:run -Dserver.port=9090

# Veya application.properties'te değiştir:
# server.port=9090
```

### Session kayboldu?

```
Neden: Server restart yaptın!
Session RAM'de tutuluyor, restart sonrası gider.

Çözüm: Tekrar login ol
curl -X POST http://localhost:8080/api/auth/login ...
```

### 401 Unauthorized hatası?

```
Sebep 1: Login olmadın
Çözüm: Önce /api/auth/login endpoint'ine istek at

Sebep 2: Cookie göndermedin
Çözüm: curl'de -b cookies.txt kullan

Sebep 3: Session timeout oldu (30 dakika)
Çözüm: Tekrar login ol
```

### H2 Console açılmıyor?

```
Kontrol et:
1. Uygulama çalışıyor mu? (localhost:8080)
2. URL doğru mu? http://localhost:8080/h2-console
3. JDBC URL: jdbc:h2:mem:testdb
4. Username: sa
5. Password: (boş bırak)
```

### Lombok hatası alıyorum?

```bash
# IntelliJ IDEA:
# Settings → Plugins → Lombok plugin yükle
# Settings → Annotation Processors → Enable

# Eclipse:
# Download lombok.jar
# java -jar lombok.jar
```

---

## 💡 İpuçları

1. 📮 **Postman kullan** - Cookie management otomatik, test koleksiyonu oluştur
2. 🗄️ **H2 Console'a bak** - Database'i görsel incele, SQL sorguları dene
3. 📋 **Log'ları oku** - DEBUG seviyesi açık, her işlem loglanıyor
4. 🥚 **Easter egg'i keşfet** - `/api/easter-egg/session-truth` eğlenceli!
5. 🔧 **Multi-server dene** - Session problemini canlı gör
6. 📝 **README oku** - Detaylı sistem tasarımı anlatımı var

---

## 🎯 Sonraki Adımlar

### 1. Tüm Endpoint'leri Test Et
```bash
# Auth endpoints
✓ POST /api/auth/login
✓ GET  /api/auth/session-info
✓ POST /api/auth/logout

# Account endpoints
✓ GET  /api/account/balance
✓ POST /api/account/transfer
✓ GET  /api/account/profile


```

### 2. Multi-Server Senaryosunu Dene
- İki terminal aç
- Farklı portlarda çalıştır (8080 & 9090)
- Session paylaşılmadığını gör

### 3. Session Lifecycle'ı Gözlemle
- Login yap
- 30 dakika bekle
- Session timeout'u test et

### 4. Kodu İncele
- `AuthController.java` - Session nasıl oluşturuluyor?
- `AccountController.java` - Session nasıl kullanılıyor?
- `UserService.java` - Business logic nasıl?

### 5. Day 02'ye Hazırlan
- ✅ Stateful problemlerini gördün
- ✅ Session limitation'ları anladın
- 🔜 Stateless architecture öğreneceksin
- 🔜 JWT token kullanmayı göreceksin

---

## 📚 Öğrenme Yolu

### ✅ Day 01 - Stateful Architecture (Şu an buradasın)
- Session-based authentication
- HttpSession kullanımı
- Server RAM'de state tutma
- Scalability problemleri
- Multi-server challenges

### 🔜 Day 02 - Stateless Architecture
- JWT token nedir?
- Token-based authentication
- Horizontal scaling
- Microservices ready architecture

### 🔜 Day 03 - Advanced Topics
- Refresh tokens
- Token rotation
- Security best practices
- Production deployment

---

## 📖 Ek Kaynaklar

- 📄 **README.md** - Detaylı sistem tasarımı rehberi
- 🧪 **API_TESTS.md** - Tüm API test örnekleri
- 📁 **PROJECT_STRUCTURE.md** - Kod yapısı açıklamaları
- 🎯 **COMMIT_MESSAGES.md** - Git commit önerileri

---

## 🆘 Yardım Lazım?

1. README'yi oku - En detaylı kaynak
2. Log'lara bak - Ne olduğunu gösterir
3. H2 Console'a gir - Database'i incele
4. Easter egg'i dene - Eğlenerek öğren!

---

**Başarılar! 🚀**

*"Session ölür, ama öğrendiklerimiz kalır!"* 🧠

---

<div align="center">

Made with 🧠 for System Design Learners

**Day 01: Stateful** → Day 02: Stateless → Day 03: Advanced

</div>