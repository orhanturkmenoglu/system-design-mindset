# 🧠 SYSTEM DESIGN MINDSET

> **30 Günlük Pratik System Design Öğrenme Programı**  
> *"Teori değil, çalışan kod. Soru değil, cevap."*

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

---

## 🎯 Bu Repo Nedir?

System design **teorisini** biliyorsun, ama **production'da** ne demek bilmiyor musun?

- ✅ "Stateless olmalı" diyorlar → Ama kodda nasıl?
- ✅ "Load balancer kullan" → Peki session ne olacak?
- ✅ "Horizontal scaling" → Database connection pool patlarsa?

**Bu repo**, her gün bir system design prensibini **çalışan Spring Boot projesiyle** öğretir.

---

## 📚 İçerik Yapısı (Fazlara Göre)

```
system-design-mindset/
│
├── 📄 README.md                           # Ana repo rehberi (bu dosya)
│
├── 📁 phase-01-request-origin/            # 🔵 FAZ 1: Request'in Doğuşu
│   │
│   ├── 📁 day-01-client-and-state/        # Client nedir, State nedir?
│   │   ├── src/                           # Session-based authentication
│   │   ├── README.md                      # Stateful architecture detayları
│   │   ├── QUICK_START.md                 # 5 dakikada çalıştır
│   │   ├── API_TESTS.md                   # Test senaryoları
│   │   └── PROJECT_STRUCTURE.md           # Kod yapısı
│   │
│   ├── 📁 day-02-stateless-jwt/           # Stateless Architecture
│   │   ├── src/                           # JWT-based authentication
│   │   ├── README.md                      # JWT deep dive
│   │   ├── QUICK_START.md                 # Hızlı başlangıç
│   │   ├── API_TESTS.md                   # Multi-server testleri
│   │   └── PROJECT_STRUCTURE.md           # Kod yapısı
│   │
│   ├── 📁 day-03-refresh-tokens/          # 🔜 Token Rotation
│   ├── 📁 day-04-http-deep-dive/          # 🔜 Idempotency & Retry
│   └── 📁 day-05-connection-pooling/      # 🔜 Database Connections
│
├── 📁 phase-02-traffic-edge/              # 🟢 FAZ 2: Traffic & Edge
│   │
│   ├── 📁 day-06-load-balancing/          # 🔜 L4 vs L7
│   ├── 📁 day-07-api-gateway/             # 🔜 Request Aggregation
│   ├── 📁 day-08-rate-limiting/           # 🔜 Token Bucket + Redis
│   ├── 📁 day-09-cdn-caching/             # 🔜 Cache Invalidation
│   └── 📁 day-10-edge-failures/           # 🔜 Multi-Region Failover
│
├── 📁 phase-03-application/               # 🟡 FAZ 3: Application Architecture
│   │
│   ├── 📁 day-11-clean-architecture/      # 🔜 Layered vs Hexagonal
│   ├── 📁 day-12-concurrency/             # 🔜 Thread Safety
│   ├── 📁 day-13-transactions/            # 🔜 Isolation Levels
│   ├── 📁 day-14-id-generation/           # 🔜 Distributed ID
│   └── 📁 day-15-idempotency/             # 🔜 Idempotent APIs
│
├── 📁 phase-04-data/                      # 🟠 FAZ 4: Data Architecture
│   │
│   ├── 📁 day-16-database-selection/      # 🔜 CAP Theorem
│   ├── 📁 day-17-indexing/                # 🔜 Query Optimization
│   ├── 📁 day-18-sharding/                # 🔜 Partitioning
│   ├── 📁 day-19-caching/                 # 🔜 Cache Strategies
│   └── 📁 day-20-replication/             # 🔜 Backup & Recovery
│
├── 📁 phase-05-distributed/               # 🔴 FAZ 5: Distributed Systems
│   │
│   ├── 📁 day-21-microservices/           # 🔜 Bounded Context
│   ├── 📁 day-22-communication/           # 🔜 REST vs gRPC
│   ├── 📁 day-23-event-driven/            # 🔜 Kafka
│   ├── 📁 day-24-saga-pattern/            # 🔜 Distributed Transactions
│   └── 📁 day-25-service-discovery/       # 🔜 Eureka
│
└── 📁 phase-06-scale-real-world/          # ⚫ FAZ 6: Scale & Real Systems
    │
    ├── 📁 day-26-observability/           # 🔜 Logs, Metrics, Tracing
    ├── 📁 day-27-resilience/              # 🔜 Circuit Breaker
    ├── 📁 day-28-capacity-planning/       # 🔜 RPS Calculation
    ├── 📁 day-29-twitter-design/          # 🔜 Timeline & Fan-out
    └── 📁 day-30-netflix-uber/            # 🔜 Streaming & Geo-routing
```

### 📁 Her Gün Klasöründe Ne Var?

```
day-XX-topic-name/
├── 📂 src/
│   └── main/
│       ├── java/com/systemdesign/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── model/
│       │   ├── dto/
│       │   ├── repository/
│       │   ├── config/
│       │   └── security/              # (Varsa)
│       └── resources/
│           └── application.properties
│
├── 📄 README.md                      # Detaylı anlatım
│   ├─ Neden bu pattern?
│   ├─ Nasıl çalışıyor?
│   ├─ Alternatifler neler?
│   └─ 1M user'da ne olur?
│
├── 📄 QUICK_START.md                 # 5 dakikada başla
│   ├─ Kurulum
│   ├─ Çalıştırma
│   ├─ İlk testler
│   └─ Beklenen sonuçlar
│
├── 📄 API_TESTS.md                   # Test senaryoları
│   ├─ cURL komutları
│   ├─ Postman collection
│   ├─ Multi-server testleri
│   └─ Failure senaryoları
│
├── 📄 PROJECT_STRUCTURE.md           # Kod yapısı
│   ├─ Dosya ağacı
│   ├─ Katman açıklamaları
│   ├─ Kod istatistikleri
│   └─ Dependency'ler
│
└── 📄 pom.xml                        # Maven dependencies
```

---

## 🔥 ANA PRENSİP

Her gün **4 soruya** cevap veriyoruz:

```
1. Bu parça neden var?
2. Olmasaydı ne olurdu?
3. Alternatifleri ne?
4. 1M user'da nerede patlar?
```

---

## 📅 PROGRAM ROADMAP

### 🔵 FAZ 1 — REQUEST'İN DOĞUŞU (Gün 1–5)

| Gün | Konu | Proje | Durum |
|-----|------|-------|-------|
| **1** | **Client & State** | Session-based Auth | ✅ Tamamlandı |
| **2** | **Stateless Architecture** | JWT Token | ✅ Tamamlandı |
| **3** | **Refresh Tokens** | Token Rotation | 🔜 Yakında |
| **4** | **HTTP Deep Dive** | Idempotency & Retry | 🔜 Yakında |
| **5** | **Connection Pooling** | Database Connections | 🔜 Yakında |

### 🟢 FAZ 2 — TRAFFIC & EDGE (Gün 6–10)

| Gün | Konu | Proje | Durum |
|-----|------|-------|-------|
| **6** | **Load Balancing** | L4 vs L7 Simulation | 🔜 Yakında |
| **7** | **API Gateway** | Request Aggregation | 🔜 Yakında |
| **8** | **Rate Limiting** | Token Bucket + Redis | 🔜 Yakında |
| **9** | **CDN & Caching** | Cache Invalidation | 🔜 Yakında |
| **10** | **Edge Failures** | Multi-Region Failover | 🔜 Yakında |

### 🟡 FAZ 3 — APPLICATION ARCHITECTURE (Gün 11–15)

| Gün | Konu | Durum |
|-----|------|-------|
| **11** | Clean Architecture | 🔜 Yakında |
| **12** | Concurrency & Thread Safety | 🔜 Yakında |
| **13** | Transactions & Isolation | 🔜 Yakında |
| **14** | Distributed ID Generation | 🔜 Yakında |
| **15** | Idempotency Patterns | 🔜 Yakında |

### 🟠 FAZ 4 — DATA ARCHITECTURE (Gün 16–20)

| Gün | Konu | Durum |
|-----|------|-------|
| **16** | Database Selection (CAP) | 🔜 Yakında |
| **17** | Index & Query Optimization | 🔜 Yakında |
| **18** | Sharding & Partitioning | 🔜 Yakında |
| **19** | Cache Strategies | 🔜 Yakında |
| **20** | Replication & Backup | 🔜 Yakında |

### 🔴 FAZ 5 — DISTRIBUTED SYSTEMS (Gün 21–25)

| Gün | Konu | Durum |
|-----|------|-------|
| **21** | Microservices Boundaries | 🔜 Yakında |
| **22** | REST vs gRPC | 🔜 Yakında |
| **23** | Event-Driven (Kafka) | 🔜 Yakında |
| **24** | Distributed Transactions (Saga) | 🔜 Yakında |
| **25** | Service Discovery | 🔜 Yakında |

### ⚫ FAZ 6 — SCALE & REAL WORLD (Gün 26–30)

| Gün | Konu | Durum |
|-----|------|-------|
| **26** | Observability (Logs, Metrics, Tracing) | 🔜 Yakında |
| **27** | Resilience Patterns (Circuit Breaker) | 🔜 Yakında |
| **28** | Capacity Planning | 🔜 Yakında |
| **29** | Twitter System Design | 🔜 Yakında |
| **30** | Netflix/Uber Case Study | 🔜 Yakında |

---

## 🚀 HIZLI BAŞLANGIÇ

### Day 01 - Stateful Architecture (Session)

```bash
# 1. Clone
git clone https://github.com/yourusername/system-design-mindset.git
cd system-design-mindset/phase-01-request-origin/day-01-client-and-state

# 2. Çalıştır
./mvnw spring-boot:run

# 3. Test
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}' \
  -c cookies.txt

curl http://localhost:8080/api/account/balance -b cookies.txt
```

**Sonuç:** ✅ Session-based authentication çalışıyor!

**Problem:** ❌ Multi-server'da patlar → Day 02'ye geç

---

### Day 02 - Stateless Architecture (JWT)

```bash
cd ../day-02-stateless-jwt

# Çalıştır
./mvnw spring-boot:run

# Login (JWT al)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}'

# Token'ı kaydet
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Balance (JWT ile)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"
```

**Sonuç:** ✅ Stateless! Multi-server çalışıyor!

---

## 📖 HER MODÜLDE NE VAR?

### Faz Yapısı

Her faz kendi klasöründe 5 gün içerir:

```
phase-XX-topic/
├── day-01-subtopic/
├── day-02-subtopic/
├── day-03-subtopic/
├── day-04-subtopic/
└── day-05-subtopic/
```

### Gün Yapısı

Her gün klasöründe:

```
day-XX-topic-name/
├── 📄 README.md              # Detaylı anlatım (Neden? Nasıl? Ne olurdu?)
├── 📄 QUICK_START.md         # 5 dakikada çalıştır
├── 📄 API_TESTS.md           # Test senaryoları (curl, Postman)
├── 📄 PROJECT_STRUCTURE.md   # Kod yapısı açıklamaları
├── 📂 src/                   # Tam çalışır Spring Boot projesi
└── 📄 pom.xml                # Dependencies
```

### README.md İçeriği

Her günün README'si şu soruları cevaplıyor:

1. ✅ **Bu parça neden var?** - Problem tanımı
2. ✅ **Olmasaydı ne olurdu?** - Alternatif senaryolar
3. ✅ **Alternatifleri ne?** - Farklı çözümler
4. ✅ **1M user'da nerede patlar?** - Scalability analizi

---

## 🎓 KİMLER İÇİN?

### ✅ Senin için mükemmel:

- "Load balancer biliyorum ama kodda nasıl?" diyorsan
- Teorik bilgin var, pratik eksiksen
- System design interview hazırlanıyorsan
- Production problemlerini önceden görmek istiyorsan
- Junior'dan Mid'e, Mid'den Senior'a yükselenler

### ❌ Senin için değil:

- Java/Spring Boot bilmiyorsan (önce temel öğren)
- Sadece teori istiyorsan (kitap oku)
- Copy-paste arıyorsan (anlamadan kullanma)

---

## 💡 NASIL KULLANILIR?

### 1️⃣ Sıralı Git (Önerilen)

```bash
# Faz 1: Request'in Doğuşu
phase-01-request-origin/
  day-01-client-and-state      → Stateful
  day-02-stateless-jwt          → Stateless
  day-03-refresh-tokens         → Token Rotation
  day-04-http-deep-dive         → Idempotency
  day-05-connection-pooling     → DB Connections

# Faz 2: Traffic & Edge
phase-02-traffic-edge/
  day-06-load-balancing         → L4 vs L7
  ...
```

Her gün öncekinin üzerine inşa ediyor. Atlamadan git.

### 2️⃣ İlgilendiğin Konuya Atla

```bash
# Rate limiting öğrenmek istiyorsan
cd phase-02-traffic-edge/day-08-rate-limiting

# JWT öğrenmek istiyorsan
cd phase-01-request-origin/day-02-stateless-jwt
```

⚠️ Ama önkoşulları bil (her README'de belirtilmiş).

### 3️⃣ Karşılaştırmalı Öğren

```bash
# Aynı problemi farklı çözümlerle gör
diff phase-01-request-origin/day-01-client-and-state \
     phase-01-request-origin/day-02-stateless-jwt
```

Aynı banking API'si, farklı authentication yaklaşımları.

### 4️⃣ Faz Bazlı İlerle

Her fazı bitir, sonrakine geç:

```
Week 1: Faz 1 (Request Origin)      ✅
Week 2: Faz 2 (Traffic & Edge)      🔜
Week 3: Faz 3 (Application)         🔜
Week 4: Faz 4 (Data)                🔜
Week 5: Faz 5 (Distributed)         🔜
Week 6: Faz 6 (Scale & Real World)  🔜
```

---

## 🧪 TEST KULLANICILARI

Her projede varsayılan olarak:

| Username | Password | Balance |
|----------|----------|---------|
| ali      | 1234     | 5000 TL |
| ayse     | 1234     | 3000 TL |
| mehmet   | 1234     | 10000 TL |

---

## 🔧 TEKNİK STACK

- **Java**: 17+
- **Spring Boot**: 3.2.1
- **Database**: H2 (In-Memory)
- **Security**: Spring Security + JWT
- **Build**: Maven
- **Testing**: cURL, Postman

---

## 📊 ÖĞRENME YAKLAŞIMI

### Her Gün:

1. 📖 **README oku** - Problem ve çözümü anla
2. 🚀 **QUICK_START ile çalıştır** - Kodu görüntüle
3. 🧪 **API_TESTS ile test et** - Davranışı gözle
4. 🔍 **Kod incele** - Nasıl çalıştığını anla
5. 🔨 **Kendi değişikliklerini yap** - Deneyerek öğren

### Örnek Akış (Faz 1, Gün 1-2):

```
1. phase-01-request-origin/day-01-client-and-state/README.md oku
   → "Session nedir, neden riskli?" anla
   
2. QUICK_START.md ile çalıştır
   → curl ile login yap, balance sorgula
   
3. Multi-server test et (2 terminal)
   → Session'ın nerede patladığını GÖR
   → 8080'de login, 9090'da balance → 401 Unauthorized!
   
4. day-02-stateless-jwt'ye geç
   → JWT ile aynı problemi çöz
   → 8080'de login, 9090'da balance → 200 OK! ✅
   
5. Karşılaştır
   → "Stateless neden?" sorusuna cevap verebiliyorsun
   → Production'da neden JWT kullanıldığını GÖRÜYORSUN
```

### Haftalık Plan

```
📅 Hafta 1: Faz 1 - Request Origin (Gün 1-5)
   - Client, state, session, JWT, refresh tokens

📅 Hafta 2: Faz 2 - Traffic & Edge (Gün 6-10)
   - Load balancing, API gateway, rate limiting, CDN

📅 Hafta 3: Faz 3 - Application (Gün 11-15)
   - Clean architecture, concurrency, transactions

📅 Hafta 4: Faz 4 - Data (Gün 16-20)
   - Database, indexing, sharding, caching

📅 Hafta 5: Faz 5 - Distributed (Gün 21-25)
   - Microservices, events, saga, service discovery

📅 Hafta 6: Faz 6 - Scale & Real (Gün 26-30)
   - Observability, resilience, real-world cases
```

---

## 🤝 KATKI SAĞLAMA

Katkı yapmanı çok isterim! 

### Nasıl Katkı Sağlanır?

1. Fork yap
2. Feature branch oluştur (`git checkout -b feature/day-XX`)
3. Commit'le (`git commit -m 'Add Day XX: Topic'`)
4. Push yap (`git push origin feature/day-XX`)
5. Pull Request aç

### Katkı Fikirleri:

- 🐛 Bug fix
- 📝 Dokümantasyon iyileştirme
- ✨ Yeni gün ekle
- 🌐 İngilizce çeviri
- 🧪 Test senaryosu ekle

---

## 📜 LİSANS

MIT License - Özgürce kullan, öğren, paylaş!

---

## 🌟 YILDIZ VER!

Bu repo işine yaradıysa, ⭐ vermeyi unutma!

---

## 📞 İLETİŞİM & DESTEK

- 🐛 **Bug Report:** [Issues](https://github.com/yourusername/system-design-mindset/issues)
- 💡 **Feature Request:** [Issues](https://github.com/yourusername/system-design-mindset/issues)
- 💬 **Soru Sor:** [Discussions](https://github.com/yourusername/system-design-mindset/discussions)

---

## 🎯 HEDEF

Bu 30 günü bitirince:

```
✅ System design interview'de ezilmezsin
✅ "1M user kaldırır mı?" sorusuna matematikle cevap verirsin
✅ Senior'la aynı dili konuşursun
✅ Production'da ne patlayacağını önceden görürsün
✅ LinkedIn'de otorite olursun
```

---

## 🗓️ GÜNCEL DURUM

| Kategori | Tamamlanan | Toplam | İlerleme |
|----------|------------|--------|----------|
| **Faz 1 (Client & Request)** | 2 | 5 | ████░░░░░░ 40% |
| **Faz 2 (Traffic & Edge)** | 0 | 5 | ░░░░░░░░░░ 0% |
| **Faz 3 (Application)** | 0 | 5 | ░░░░░░░░░░ 0% |
| **Faz 4 (Data)** | 0 | 5 | ░░░░░░░░░░ 0% |
| **Faz 5 (Distributed)** | 0 | 5 | ░░░░░░░░░░ 0% |
| **Faz 6 (Scale & Real)** | 0 | 5 | ░░░░░░░░░░ 0% |
| **TOPLAM** | **2** | **30** | **██░░░░░░░░ 6.6%** |

**Son Güncelleme:** 03 Şubat 2026

---

## 🏆 TAMAMLADIĞINDA

Bu repo'yu bitirdiğinde:

1. ✅ Pull Request aç → "30 Gün Tamamlandı" badge kazanan ilk kişi ol
2. ✅ LinkedIn'de paylaş → "#SystemDesignMindset"
3. ✅ Başkalarına öğret → Knowledge sharing

---

## 🙏 TEŞEKKÜRLER

- Spring Boot ekibine
- System design topluluğuna
- Bu repo'ya yıldız veren herkese
- Katkı sağlayan tüm contributorlara

---

<div align="center">

**🧠 Made with Brain for Engineers 🧠**

*"Teori öğretmiyoruz, sistem düşünmeyi öğreniyoruz."*

---

**[⬆ Başa Dön](#-system-design-mindset)**

</div>
