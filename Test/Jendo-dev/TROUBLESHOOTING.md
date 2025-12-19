# Frontend-Backend Connection Troubleshooting Checklist

Use this checklist to diagnose and fix connection issues between your Expo frontend and Spring Boot backend.

## ✅ Pre-Flight Checklist

### 1. PostgreSQL Database
- [ ] PostgreSQL service is running
- [ ] Database `jendo-app` exists
- [ ] Can connect to database with credentials from `application.yml`
- [ ] Port 5432 is accessible

**Test:**
```powershell
# Check if PostgreSQL is listening
Test-NetConnection -ComputerName localhost -Port 5432
```

### 2. Backend (Spring Boot)
- [ ] Java 17+ is installed
- [ ] Maven is installed
- [ ] Backend builds without errors (`mvn clean install`)
- [ ] Backend starts successfully (`mvn spring-boot:run`)
- [ ] Backend is running on port 5000
- [ ] Swagger UI is accessible at http://localhost:5000/swagger-ui.html

**Test:**
```powershell
# Check if backend is running
Test-NetConnection -ComputerName localhost -Port 5000

# Test API endpoint
Invoke-WebRequest -Uri "http://localhost:5000/swagger-ui.html" -Method GET
```

### 3. Frontend (Expo)
- [ ] Node.js 18+ is installed
- [ ] Dependencies are installed (`npm install`)
- [ ] `.env` file exists with correct `EXPO_PUBLIC_API_URL`
- [ ] Expo dev server starts without errors (`npm start`)
- [ ] Metro bundler is running

**Test:**
```powershell
# Verify .env file exists
Test-Path "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo\.env"

# Check Node version
node --version
```

---

## 🔍 Common Issues & Solutions

### Issue 1: "Network Error" or "Connection Refused"

**Symptoms:**
- Frontend shows "Network Error" when making API calls
- Console shows "ERR_CONNECTION_REFUSED"

**Checklist:**
- [ ] Backend is actually running (check terminal)
- [ ] Backend is on port 5000 (not 8080 or other)
- [ ] `.env` has `EXPO_PUBLIC_API_URL=http://localhost:5000/api`
- [ ] No firewall blocking port 5000

**Solution:**
```powershell
# 1. Verify backend is running
Test-NetConnection -ComputerName localhost -Port 5000

# 2. If not running, start it
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
mvn spring-boot:run

# 3. Verify .env content
Get-Content "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo\.env"
```

---

### Issue 2: CORS Error

**Symptoms:**
- Browser console shows "CORS policy" error
- API calls fail with CORS-related messages

**Checklist:**
- [ ] `WebConfig.java` has CORS configuration
- [ ] Allowed origins include your frontend URL
- [ ] Backend restarted after CORS changes

**Solution:**
Verify `WebConfig.java` contains:
```java
.allowedOrigins(
    "http://localhost:8081",
    "http://localhost:19000",
    "*"
)
```

Restart backend after any CORS changes.

---

### Issue 3: "Cannot read property 'data' of undefined"

**Symptoms:**
- API calls complete but data processing fails
- Frontend crashes when handling response

**Checklist:**
- [ ] Backend is returning data in expected format
- [ ] API endpoint exists and is accessible
- [ ] Response structure matches frontend expectations

**Solution:**
Test API directly:
```powershell
# Test users endpoint
Invoke-RestMethod -Uri "http://localhost:5000/api/users" -Method GET | ConvertTo-Json
```

Check response structure matches what frontend expects.

---

### Issue 4: 404 Not Found on API calls

**Symptoms:**
- API calls return 404 error
- Endpoints not found

**Checklist:**
- [ ] API URL includes `/api` prefix
- [ ] Endpoint path is correct
- [ ] Controller has `@RequestMapping("/api/...")`

**Solution:**
```powershell
# List all available endpoints via Swagger
# Open: http://localhost:5000/swagger-ui.html
```

Verify your endpoints are registered and accessible.

---

### Issue 5: Database Connection Error

**Symptoms:**
- Backend fails to start with database error
- "Connection refused" to PostgreSQL

**Checklist:**
- [ ] PostgreSQL is running
- [ ] Database `jendo-app` exists
- [ ] Credentials in `application.yml` are correct
- [ ] Port 5432 is accessible

**Solution:**
```sql
-- Connect to PostgreSQL and create database if needed
CREATE DATABASE "jendo-app";

-- Verify connection
\c jendo-app
```

Update `application.yml` if credentials are wrong.

---

### Issue 6: Physical Device Can't Connect

**Symptoms:**
- App works on emulator but not on physical device
- Physical device shows connection errors

**Checklist:**
- [ ] Device is on same WiFi network as computer
- [ ] Using computer's IP address instead of localhost
- [ ] Firewall allows incoming connections on port 5000

**Solution:**
```powershell
# 1. Get your computer's IP address
ipconfig
# Look for IPv4 Address (e.g., 192.168.1.100)

# 2. Update .env
# EXPO_PUBLIC_API_URL=http://192.168.1.100:5000/api

# 3. Allow port 5000 through firewall
New-NetFirewallRule -DisplayName "Jendo Backend" -Direction Inbound -Port 5000 -Protocol TCP -Action Allow

# 4. Restart Expo
npx expo start -c
```

---

### Issue 7: 401 Unauthorized Error

**Symptoms:**
- API calls return 401 status
- "Unauthorized" errors

**Checklist:**
- [ ] User is logged in (if auth is required)
- [ ] Auth token is being sent in headers
- [ ] Token is valid and not expired
- [ ] `httpClient.ts` request interceptor is working

**Solution:**
Check if auth token is being included:
```typescript
// In httpClient.ts, verify request interceptor adds token
config.headers.Authorization = `Bearer ${token}`;
```

Test without auth on public endpoints first.

---

### Issue 8: Expo App Won't Start

**Symptoms:**
- `npm start` fails
- Metro bundler crashes
- Module not found errors

**Checklist:**
- [ ] All dependencies installed
- [ ] No conflicting packages
- [ ] Cache is clear

**Solution:**
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"

# Clear cache and reinstall
Remove-Item -Recurse -Force node_modules
Remove-Item package-lock.json -ErrorAction SilentlyContinue
npm install

# Start with cleared cache
npx expo start -c
```

---

### Issue 9: Backend Builds But Won't Start

**Symptoms:**
- Maven build succeeds
- Application fails to start
- Port already in use

**Checklist:**
- [ ] Port 5000 is not already in use
- [ ] All required properties in `application.yml`
- [ ] No syntax errors in configuration

**Solution:**
```powershell
# Check if port 5000 is in use
Get-NetTCPConnection -LocalPort 5000 -ErrorAction SilentlyContinue

# If in use, find and kill the process
$processId = (Get-NetTCPConnection -LocalPort 5000).OwningProcess
Stop-Process -Id $processId -Force

# Then restart backend
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
mvn spring-boot:run
```

---

### Issue 10: API Returns Empty Data

**Symptoms:**
- API call succeeds (200 OK)
- But returns empty array or null data

**Checklist:**
- [ ] Database has data
- [ ] Database connection is working
- [ ] JPA entities are properly configured
- [ ] No filter excluding all data

**Solution:**
```powershell
# Test API directly
Invoke-RestMethod -Uri "http://localhost:5000/api/users" -Method GET

# Check backend logs for SQL queries
# Look in: Jendo-Backend/Jendo-Backend/logs/jendo-app.log
```

If database is empty, create test data.

---

## 🧪 Diagnostic Commands

### Check All Ports
```powershell
# PostgreSQL
Test-NetConnection -ComputerName localhost -Port 5432

# Backend
Test-NetConnection -ComputerName localhost -Port 5000

# Expo Metro Bundler
Test-NetConnection -ComputerName localhost -Port 8081
```

### Test API Endpoints
```powershell
# Test backend health
Invoke-WebRequest -Uri "http://localhost:5000/swagger-ui.html"

# Test specific endpoint
Invoke-RestMethod -Uri "http://localhost:5000/api/users" -Method GET

# Test with verbose output
Invoke-WebRequest -Uri "http://localhost:5000/api/users" -Method GET -Verbose
```

### Check Logs
```powershell
# Backend logs
Get-Content "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend\logs\jendo-app.log" -Tail 50

# Expo Metro bundler - check terminal output

# PostgreSQL logs (if configured)
# Location varies by installation
```

### Environment Verification
```powershell
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Node version
node --version

# Check npm version
npm --version

# Check Expo CLI
npx expo --version
```

---

## 🎯 Quick Test Sequence

Run these tests in order to quickly identify where the issue is:

1. **Database Test:**
   ```powershell
   Test-NetConnection -ComputerName localhost -Port 5432
   ```

2. **Backend Test:**
   ```powershell
   Test-NetConnection -ComputerName localhost -Port 5000
   Invoke-WebRequest -Uri "http://localhost:5000/swagger-ui.html"
   ```

3. **API Test:**
   ```powershell
   Invoke-RestMethod -Uri "http://localhost:5000/api/users" -Method GET
   ```

4. **Frontend Config Test:**
   ```powershell
   Get-Content "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo\.env"
   ```

5. **Frontend Connection Test:**
   - Use the `/connection-test` screen in the app

---

## 📞 Still Having Issues?

If you've gone through this checklist and still have issues:

1. **Check Backend Logs:**
   ```
   Jendo-Backend/Jendo-Backend/logs/jendo-app.log
   ```

2. **Enable Verbose Logging:**
   In `application.yml`, set:
   ```yaml
   logging:
     level:
       com.jendo.app: DEBUG
       org.springframework.web: DEBUG
   ```

3. **Test with Connection Test Screen:**
   Navigate to `/connection-test` in your app

4. **Use Swagger UI:**
   Test APIs directly at http://localhost:5000/swagger-ui.html

5. **Check Network Tab:**
   In Expo DevTools, monitor actual network requests

---

## ✨ Best Practices

- Always start services in order: PostgreSQL → Backend → Frontend
- Use the `/connection-test` screen regularly during development
- Check Swagger UI to verify backend is working before testing frontend
- Keep backend logs open during development
- Use version control to track configuration changes
- Test on emulator/simulator before testing on physical devices

---

## 🔄 Complete Reset Procedure

If all else fails, try a complete reset:

```powershell
# 1. Stop all services (Ctrl+C in all terminals)

# 2. Clear frontend cache
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
Remove-Item -Recurse -Force node_modules
Remove-Item -Recurse -Force .expo
npm install

# 3. Rebuild backend
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
mvn clean install -U

# 4. Verify PostgreSQL is running
Test-NetConnection -ComputerName localhost -Port 5432

# 5. Start backend
mvn spring-boot:run

# 6. In new terminal, start frontend
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
npx expo start -c
```

This should resolve most persistent issues.
