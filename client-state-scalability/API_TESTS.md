# 🧪 API TEST EXAMPLES

## Postman / cURL ile Test

### 1. Login (Session Oluştur)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ali",
    "password": "1234"
  }' \
  -c cookies.txt
```

**Response:**
```json
{
  "message": "Login successful",
  "username": "ali",
  "sessionInfo": "Session ID: 3F2A1B4C..."
}
```

**Not:** `-c cookies.txt` ile cookie kaydedilir (JSESSIONID)

---

### 2. Balance Sorgula (Session Gerekli)

```bash
curl -X GET http://localhost:8080/api/account/balance \
  -b cookies.txt
```

**Response:**
```json
{
  "username": "ali",
  "balance": 5000,
  "message": "Retrieved from session-based authentication"
}
```

**Session yoksa:**
```
Unauthorized: Please login first
```

---

### 3. Transfer (Session Gerekli)

```bash
curl -X POST http://localhost:8080/api/account/transfer \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "toUsername": "ayse",
    "amount": 500
  }'
```

**Response:**
```json
{
  "message": "Transfer successful",
  "fromUsername": "ali",
  "toUsername": "ayse",
  "amount": 500,
  "newBalance": 4500
}
```

---

### 4. Profil Bilgisi (Session Gerekli)

```bash
curl -X GET http://localhost:8080/api/account/profile \
  -b cookies.txt
```

**Response:**
```
Profile: ali (ID: 1) - Balance: 4500 TL
```

---

### 5. Session Bilgisi

```bash
curl -X GET http://localhost:8080/api/auth/session-info \
  -b cookies.txt
```

**Response:**
```
Active Session - User: ali (ID: 1), Session ID: 3F2A1B4C...
```

---

### 6. Logout (Session Sil)

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -b cookies.txt
```

**Response:**
```
Logout successful
```

Session RAM'den silindi!

---

## 🥚 Easter Egg Endpoints

### 1. Session Truth

```bash
curl -X GET http://localhost:8080/api/easter-egg/session-truth \
  -b cookies.txt
```

### 2. Wisdom Mode

```bash
curl -X GET "http://localhost:8080/api/easter-egg/session-truth?wisdom=true" \
  -b cookies.txt
```

### 3. Konami Code

```bash
curl -X GET "http://localhost:8080/api/easter-egg/session-truth?konami=true" \
  -b cookies.txt
```

### 4. Session Death Simulator

```bash
curl -X GET http://localhost:8080/api/easter-egg/session-death \
  -b cookies.txt
```

### 5. Stateful vs Stateless Battle

```bash
curl -X GET http://localhost:8080/api/easter-egg/battle
```

---

## 🧪 Multi-Server Test (Session Problemi Gösterme)

### Terminal 1: Port 8080

```bash
./mvnw spring-boot:run
```

### Terminal 2: Port 9090

```bash
./mvnw spring-boot:run -Dserver.port=9090
```

### Test:

```bash
# Port 8080'de login ol
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}' \
  -c cookies-8080.txt

# Port 8080'de balance çek (✅ Çalışır)
curl -X GET http://localhost:8080/api/account/balance \
  -b cookies-8080.txt

# Port 9090'da balance çek (❌ Çalışmaz!)
curl -X GET http://localhost:9090/api/account/balance \
  -b cookies-8080.txt
```

**Sonuç:** Session paylaşılmıyor! Her server ayrı RAM kullanıyor.

---

## 📊 Postman Collection

Postman için JSON dosyası:

```json
{
  "info": {
    "name": "System Design Day 01",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"ali\",\n  \"password\": \"1234\"\n}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": {
          "raw": "http://localhost:8080/api/auth/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "auth", "login"]
        }
      }
    },
    {
      "name": "Get Balance",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/account/balance",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "account", "balance"]
        }
      }
    },
    {
      "name": "Transfer",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"toUsername\": \"ayse\",\n  \"amount\": 500\n}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": {
          "raw": "http://localhost:8080/api/account/transfer",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "account", "transfer"]
        }
      }
    }
  ]
}
```

---

## 🐛 Troubleshooting

### Session kayboldu?

```bash
# Server restart yaptın mı?
# Session RAM'de tutuluyor, restart sonrası gider!

# Çözüm: Tekrar login ol
curl -X POST http://localhost:8080/api/auth/login ...
```

### Cookie çalışmıyor?

```bash
# -b (read cookie) ve -c (write cookie) kullan
curl -c cookies.txt  # Cookie yaz
curl -b cookies.txt  # Cookie oku
```

### 401 Unauthorized?

```bash
# Login olmadın veya session expire oldu
# Tekrar login ol
```

---

**Happy Testing! 🎉**
