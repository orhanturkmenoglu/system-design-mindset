# 🚀 QUICK START GUIDE

## Hızlı Başlangıç (5 Dakika)

### Gereksinimler

- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ Terminal / Command Prompt

### Adım 1: Projeyi İndir

```bash
git clone <your-repo-url>
cd system-design-mindset
```

### Adım 2: Çalıştır

```bash
./mvnw spring-boot:run

# Windows için:
mvnw.cmd spring-boot:run
```

### Adım 3: Test Et

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}' \
  -c cookies.txt

# Balance
curl http://localhost:8080/api/account/balance -b cookies.txt
```

**Tebrikler! 🎉** Stateful architecture çalışıyor!

---

## 📁 Proje Yapısı

```
system-design-mindset/
├── src/
│   ├── main/
│   │   ├── java/com/systemdesign/
│   │   │   ├── SystemDesignApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java       # Login/Logout
│   │   │   │   └── AccountController.java    # Balance/Transfer
│   │   │   ├── service/
│   │   │   │   └── UserService.java          # Business logic
│   │   │   ├── model/
│   │   │   │   └── User.java                 # Entity
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   └── ...
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java       # Database access
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java      # Test data
│   │   │   └── easteregg/
│   │   │       └── SessionTruthEasterEgg.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── README.md
├── API_TESTS.md
└── QUICK_START.md
```

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

## 🔧 Multi-Server Test

### Terminal 1:

```bash
./mvnw spring-boot:run
```

### Terminal 2:

```bash
./mvnw spring-boot:run -Dserver.port=9090
```

### Test:

```bash
# 8080'de login
curl -X POST http://localhost:8080/api/auth/login \
  -d '{"username":"ali","password":"1234"}' \
  -H "Content-Type: application/json" \
  -c cookies.txt

# 8080'de balance (✅ Çalışır)
curl http://localhost:8080/api/account/balance -b cookies.txt

# 9090'da balance (❌ Çalışmaz - Session yok!)
curl http://localhost:9090/api/account/balance -b cookies.txt
```

**Sonuç:** Session paylaşılmıyor! Bu Day 01'in ana problemi.

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
# Maven'i direkt kullan
mvn spring-boot:run
```

### Port 8080 kullanımda?

```bash
# Farklı port kullan
./mvnw spring-boot:run -Dserver.port=9090
```

### Session kayboldu?

```
Server restart yaptıysan normal!
Session RAM'de tutuluyor.
Tekrar login ol.
```

---

## 💡 İpuçları

1. **Postman kullan** - Cookie management otomatik
2. **H2 Console'a bak** - Database'i görsel incele
3. **Log'ları oku** - DEBUG seviyesi açık
4. **Easter egg'i keşfet** - `/api/easter-egg/session-truth`

---

## 🎯 Sonraki Adımlar

1. Tüm endpoint'leri test et
2. Multi-server senaryosunu dene
3. Session lifecycle'ı gözlemle
4. Day 02'ye hazırlan (Stateless migration)

---

**Başarılar! 🚀**

*Session ölür, ama öğrendiklerimiz kalır!* 🧠
