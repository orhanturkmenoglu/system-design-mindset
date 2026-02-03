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

## 📚 İçerik Yapısı

```
system-design-mindset/
│
├── 📁 day-01-stateful/          # Session-based authentication
│   ├── src/                     # Tam çalışır Spring Boot projesi
│   ├── README.md                # Detaylı anlatım
│   ├── QUICK_START.md           # 5 dakikada çalıştır
│   └── API_TESTS.md             # Test senaryoları
│
├── 📁 day-02-stateless/         # JWT-based authentication
│   ├── src/                     # Stateless mimari
│   ├── README.md                # JWT deep dive
│   ├── QUICK_START.md           # Hızlı başlangıç
│   └── API_TESTS.md             # Multi-server testleri
│
├── 📁 day-03-refresh-tokens/    # Token rotation (Coming soon)
├── 📁 day-04-load-balancing/    # L4 vs L7 (Coming soon)
├── 📁 day-05-rate-limiting/     # Token bucket impl (Coming soon)
│
└── 📄 README.md                 # Bu dosya
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
cd system-design-mindset/day-01-stateful

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
cd day-02-stateless

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

Her proje klasöründe:

```
day-XX-topic/
├── 📄 README.md              # Detaylı anlatım (Neden? Nasıl? Ne olurdu?)
├── 📄 QUICK_START.md         # 5 dakikada çalıştır
├── 📄 API_TESTS.md           # Test senaryoları (curl, Postman)
├── 📄 PROJECT_STRUCTURE.md   # Kod yapısı açıklamaları
├── 📂 src/                   # Tam çalışır Spring Boot projesi
└── 📄 pom.xml                # Dependencies
```

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
Day 01 → Day 02 → Day 03 → ...
```

Her gün öncekinin üzerine inşa ediyor. Atlamadan git.

### 2️⃣ İlgilendiğin Konuya Atla

```bash
"Rate limiting nasıl yapılır?" → day-08-rate-limiting
```

Ama önkoşulları bil (README'de belirtilmiş).

### 3️⃣ Karşılaştırmalı Öğren

```bash
day-01-stateful vs day-02-stateless
```

Aynı problemi farklı çözümlerle gör.

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

### Örnek Akış (Day 01):

```
1. day-01-stateful/README.md oku
   → "Session nedir?" anla
   
2. QUICK_START.md ile çalıştır
   → curl ile test et
   
3. Multi-server test et
   → Session'ın nerede patladığını GÖR
   
4. Day 02'ye geç
   → JWT ile aynı problemi çöz
   
5. Karşılaştır
   → Artık "stateless neden?" sorusuna cevap verebiliyorsun
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

*"Teori öğretmiyoruz, sistem düşünmeyi öğretiyoruz."*

---

**[⬆ Başa Dön](#-system-design-mindset)**

</div>
