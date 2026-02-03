# 🧪 API TEST EXAMPLES - DAY 02 (JWT/Stateless)

## Day 01'den Farklar

| Aspect | Day 01 (Stateful) | Day 02 (Stateless) |
|--------|-------------------|---------------------|
| **Auth Method** | Session Cookie | JWT Token |
| **Storage** | `-c cookies.txt` | Save token manually |
| **Header** | Cookie auto-sent | `Authorization: Bearer <token>` |
| **Server RAM** | Session stored | Nothing stored |

---

## 1. Login (JWT Token Al)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ali",
    "password": "1234"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWxpIiwic3ViIjoiYWxpIiwiaWF0IjoxNzA0MDY3MjAwLCJleHAiOjE3MDQxNTM2MDB9.xyz123...",
  "type": "Bearer",
  "username": "ali",
  "message": "Login successful - JWT token generated (Stateless!)"
}
```

### Token'ı Kaydet

```bash
# Linux/Mac
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Windows PowerShell
$TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Ya da dosyaya kaydet
echo "eyJhbGciOiJIUzI1NiJ9..." > token.txt
```

---

## 2. Balance Sorgula (JWT ile)

```bash
# Token variable ile
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"

# Ya da direkt token
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

**Response:**
```json
{
  "username": "ali",
  "balance": 5000,
  "message": "Retrieved from JWT token-based authentication (Stateless!)"
}
```

**Day 01'den Fark:**
- ❌ Cookie yok
- ✅ Authorization header var
- ❌ Session RAM'de yok
- ✅ Token self-contained

---

## 3. Transfer (JWT ile)

```bash
curl -X POST http://localhost:8080/api/account/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "toUsername": "ayse",
    "amount": 500
  }'
```

**Response:**
```json
{
  "message": "Transfer successful (processed via JWT auth)",
  "fromUsername": "ali",
  "toUsername": "ayse",
  "amount": 500,
  "newBalance": 4500
}
```

---

## 4. Profil Bilgisi (JWT ile)

```bash
curl http://localhost:8080/api/account/profile \
  -H "Authorization: Bearer $TOKEN"
```

**Response:**
```
📋 Profile (via JWT):
- Username: ali
- User ID: 1
- Balance: 4500 TL

✅ Auth method: JWT Token (Stateless)
❌ No session used
✅ Server didn't store anything in RAM
```

---

## 5. Token Info (Debug)

```bash
curl http://localhost:8080/api/auth/token-info \
  -H "Authorization: Bearer $TOKEN"
```

**Response:**
```
📋 JWT Token Info:
- Username: ali
- User ID: 1
- Expired: false
- Type: Bearer (JWT)

✅ This is STATELESS!
Server didn't look up anything in RAM.
All info came from the token itself.
```

---

## 6. Auth Debug

```bash
curl http://localhost:8080/api/account/auth-debug \
  -H "Authorization: Bearer $TOKEN"
```

---

## 7. Day 01 vs Day 02 Karşılaştırma

```bash
curl http://localhost:8080/api/auth/compare
```

**Response:**
```
╔════════════════════════════════════════════════════╗
║        DAY 01 vs DAY 02 - COMPARISON              ║
╚════════════════════════════════════════════════════╝

DAY 01 (STATEFUL):
─────────────────────────────────────────────────────
❌ Session ID in cookie
❌ Server stores session in RAM
❌ Session lost on restart
❌ Multi-server requires sticky sessions
❌ Cannot scale horizontally

DAY 02 (STATELESS):
─────────────────────────────────────────────────────
✅ JWT token (self-contained)
✅ Server stores NOTHING in RAM
✅ Restart doesn't affect tokens
✅ Multi-server works perfectly
✅ Infinite horizontal scaling
```

---

## 🧪 Multi-Server Test (Horizontal Scaling)

### Senaryo: İki Server, Aynı Token

### Terminal 1: Server A (Port 8080)

```bash
./mvnw spring-boot:run
```

### Terminal 2: Server B (Port 9090)

```bash
./mvnw spring-boot:run -Dserver.port=9090
```

### Test:

```bash
# 1. Server A'ya login
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

# 4. Server A'dan transfer
curl -X POST http://localhost:8080/api/account/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"toUsername":"ayse","amount":100}'

# 5. Server B'den balance kontrol (✅ Güncel! 🎉)
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
│  Aynı token, farklı server                   │
│  = HER İKİSİ DE ÇALIŞIYOR! 🎉                │
└──────────────────────────────────────────────┘
```

**Day 01'de:**
- ❌ Server B session'ı bilmiyor
- ❌ 401 Unauthorized

**Day 02'de:**
- ✅ Token self-contained
- ✅ Her server aynı şekilde doğrular
- ✅ Horizontal scaling!

---

## 🔄 Server Restart Test

```bash
# 1. Login ve token al
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ali","password":"1234"}' \
  | jq -r '.token')

# 2. Balance sorgula (✅ Çalışır)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"

# 3. Server'ı RESTART et (Ctrl+C, tekrar ./mvnw spring-boot:run)

# 4. Aynı token ile balance sorgula (✅ HALA ÇALIŞIYOR! 🎉)
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer $TOKEN"
```

**Day 01'de:**
- ❌ Session RAM'de, restart sonrası gider

**Day 02'de:**
- ✅ Token istemcide, restart etkilemez!

---

## ❌ Hatalı Durumlar

### 1. Token Eksik

```bash
curl http://localhost:8080/api/account/balance
```

**Response:** 403 Forbidden

### 2. Geçersiz Token

```bash
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer invalidtoken123"
```

**Response:** 403 Forbidden

### 3. Expired Token

(24 saat sonra token expire olur)

```bash
curl http://localhost:8080/api/account/balance \
  -H "Authorization: Bearer <expired-token>"
```

**Response:** 403 Forbidden

---

## 📊 Postman Collection

### Login Request

```json
{
  "name": "Login (JWT)",
  "request": {
    "method": "POST",
    "header": [
      {
        "key": "Content-Type",
        "value": "application/json"
      }
    ],
    "body": {
      "mode": "raw",
      "raw": "{\n  \"username\": \"ali\",\n  \"password\": \"1234\"\n}"
    },
    "url": {
      "raw": "http://localhost:8080/api/auth/login",
      "protocol": "http",
      "host": ["localhost"],
      "port": "8080",
      "path": ["api", "auth", "login"]
    }
  },
  "response": []
}
```

### Postman: Token'ı Otomatik Kaydet

**Tests sekmesine:**

```javascript
// Login response'undan token'ı al
var jsonData = pm.response.json();
pm.environment.set("jwt_token", jsonData.token);
```

### Balance Request (Token ile)

```json
{
  "name": "Get Balance (JWT)",
  "request": {
    "method": "GET",
    "header": [
      {
        "key": "Authorization",
        "value": "Bearer {{jwt_token}}"
      }
    ],
    "url": {
      "raw": "http://localhost:8080/api/account/balance",
      "protocol": "http",
      "host": ["localhost"],
      "port": "8080",
      "path": ["api", "account", "balance"]
    }
  }
}
```

---

## 🔐 JWT Token Anatomisi

Token'ı decode et: https://jwt.io

```
eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiYWxpIiwic3ViIjoiYWxpIiwiaWF0IjoxNzA0MDY3MjAwLCJleHAiOjE3MDQxNTM2MDB9.xyz...

Header:
{
  "alg": "HS256"
}

Payload:
{
  "userId": 1,
  "username": "ali",
  "sub": "ali",
  "iat": 1704067200,
  "exp": 1704153600
}

Signature:
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

---

## 💡 İpuçları

1. **Token'ı güvenli sakla** - LocalStorage veya güvenli yerde
2. **Authorization header** - Her request'te gönder
3. **Token expire** - 24 saat sonra yeni login
4. **Multi-server** - Aynı token her yerde çalışır
5. **Postman** - Environment variable kullan ({{jwt_token}})

---

**Happy Testing! 🚀**

*Day 01: Session gider, Day 02: Token kalır!* 🧠
