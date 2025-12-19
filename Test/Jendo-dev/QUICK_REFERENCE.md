# 🚀 Jendo App - Quick Reference Card

## 📦 What Was Configured

### ✅ Frontend (Expo)
- ✓ Environment variables configured (`.env`)
- ✓ API client configured to connect to backend
- ✓ Connection test screen created (`/connection-test`)
- ✓ Test API service created

### ✅ Backend (Spring Boot)
- ✓ CORS configured for Expo development
- ✓ API endpoints accessible at `/api/*`
- ✓ Swagger UI available for testing

### ✅ Documentation
- ✓ Comprehensive setup guide
- ✓ Troubleshooting checklist
- ✓ Quick start script
- ✓ README with essentials

---

## ⚡ Quick Start Commands

### Start Everything (Automated)
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project"
.\start.ps1
```
Choose option 3 to start both backend and frontend.

### Start Manually

**Backend:**
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
mvn spring-boot:run
```

**Frontend:**
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
npm start
```

---

## 🔗 Important URLs

| Service | URL | Description |
|---------|-----|-------------|
| Backend API | http://localhost:5000/api | REST API endpoints |
| Swagger UI | http://localhost:5000/swagger-ui.html | API documentation |
| Expo DevTools | http://localhost:19002 | Development tools |
| Connection Test | Navigate to `/connection-test` in app | Test connectivity |

---

## 📋 First-Time Setup

1. **Ensure PostgreSQL is running** (port 5432)
2. **Create database:**
   ```sql
   CREATE DATABASE "jendo-app";
   ```
3. **Install frontend dependencies:**
   ```powershell
   cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
   npm install
   ```
4. **Build backend:**
   ```powershell
   cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
   mvn clean install
   ```
5. **Start services** (use quick start script or manual commands)

---

## 🧪 Testing the Connection

### Method 1: Using Test Screen (Recommended)
1. Start both backend and frontend
2. In the Expo app, navigate to `/connection-test`
3. Click "Test Connection" button
4. View results

### Method 2: Using Swagger UI
1. Start backend
2. Open http://localhost:5000/swagger-ui.html
3. Try any GET endpoint (like `/api/users`)

### Method 3: Using PowerShell
```powershell
# Test backend is running
Test-NetConnection -ComputerName localhost -Port 5000

# Test API endpoint
Invoke-RestMethod -Uri "http://localhost:5000/api/users" -Method GET
```

---

## 🔧 Configuration Files

### Frontend Configuration
**File:** `Expo/.env`
```env
EXPO_PUBLIC_API_URL=http://localhost:5000/api
```

**For Physical Device:** Replace `localhost` with your computer's IP
```env
EXPO_PUBLIC_API_URL=http://192.168.X.X:5000/api
```

### Backend Configuration
**File:** `Jendo-Backend/Jendo-Backend/src/main/resources/application.yml`
```yaml
server:
  port: 5000

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jendo-app
    username: postgres
    password: imandi2002
```

---

## 🎯 API Endpoints Quick Reference

### Authentication
```
POST   /api/auth/login          - Login
POST   /api/auth/register       - Register
POST   /api/auth/verify-otp     - Verify OTP
POST   /api/auth/forgot-password - Forgot password
```

### Users
```
GET    /api/users               - List users
GET    /api/users/{id}          - Get user
POST   /api/users               - Create user
PUT    /api/users/{id}          - Update user
DELETE /api/users/{id}          - Delete user
```

### Doctors
```
GET    /api/doctors             - List doctors
GET    /api/doctors/{id}        - Get doctor
POST   /api/doctors             - Create doctor
PUT    /api/doctors/{id}        - Update doctor
DELETE /api/doctors/{id}        - Delete doctor
```

### Appointments
```
GET    /api/appointments        - List appointments
GET    /api/appointments/{id}   - Get appointment
POST   /api/appointments        - Create appointment
PUT    /api/appointments/{id}   - Update appointment
DELETE /api/appointments/{id}   - Delete appointment
```

### Jendo Tests
```
GET    /api/jendo-tests         - List tests
GET    /api/jendo-tests/{id}    - Get test
POST   /api/jendo-tests         - Create test
PUT    /api/jendo-tests/{id}    - Update test
DELETE /api/jendo-tests/{id}    - Delete test
```

### Health Parameters
```
GET    /api/health-parameters         - List parameters
GET    /api/health-parameters/{id}    - Get parameter
POST   /api/health-parameters         - Create parameter
PUT    /api/health-parameters/{id}    - Update parameter
DELETE /api/health-parameters/{id}    - Delete parameter
```

### Notifications
```
GET    /api/notifications/user/{userId}              - User notifications
GET    /api/notifications/user/{userId}/unread       - Unread notifications
GET    /api/notifications/user/{userId}/unread/count - Unread count
POST   /api/notifications                            - Create notification
PUT    /api/notifications/{id}/read                  - Mark as read
```

### Learning Materials
```
GET    /api/learning-materials                    - List materials
GET    /api/learning-materials/{id}               - Get material
GET    /api/learning-materials/category/{category} - By category
GET    /api/learning-materials/search?q={query}   - Search materials
POST   /api/learning-materials                    - Create material
PUT    /api/learning-materials/{id}               - Update material
DELETE /api/learning-materials/{id}               - Delete material
```

---

## 🐛 Quick Troubleshooting

### Backend won't start?
```powershell
# Check PostgreSQL is running
Test-NetConnection -ComputerName localhost -Port 5432

# Check if port 5000 is available
Test-NetConnection -ComputerName localhost -Port 5000
```

### Frontend can't connect?
```powershell
# Verify .env file exists and has correct URL
Get-Content "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo\.env"

# Verify backend is running
Invoke-WebRequest -Uri "http://localhost:5000/swagger-ui.html"
```

### Database error?
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database if it doesn't exist
CREATE DATABASE "jendo-app";
```

### See full troubleshooting guide:
`TROUBLESHOOTING.md`

---

## 📁 Project Structure

```
jendo_project/
├── Expo (3)/Expo/              Frontend (React Native)
│   ├── .env                    ← Environment variables
│   ├── app/                    ← App screens
│   ├── src/
│   │   ├── config/             ← API & app config
│   │   ├── features/           ← Feature modules
│   │   ├── infrastructure/api/ ← HTTP client
│   │   └── services/           ← Business logic
│   └── package.json
│
├── Jendo-Backend/              Backend (Spring Boot)
│   └── Jendo-Backend/
│       ├── src/main/
│       │   ├── java/com/jendo/app/
│       │   │   ├── controller/ ← REST controllers
│       │   │   ├── domain/     ← Business logic
│       │   │   └── config/     ← App configuration
│       │   └── resources/
│       │       └── application.yml ← Backend config
│       └── pom.xml
│
├── README.md                   ← Quick overview
├── SETUP_GUIDE.md              ← Detailed setup
├── TROUBLESHOOTING.md          ← Problem solving
├── QUICK_REFERENCE.md          ← This file
└── start.ps1                   ← Quick start script
```

---

## 💡 Pro Tips

1. **Always start PostgreSQL first**, then backend, then frontend
2. **Use Swagger UI** to test backend before testing frontend
3. **Use connection test screen** during development
4. **Check backend logs** at `Jendo-Backend/Jendo-Backend/logs/jendo-app.log`
5. **Clear Expo cache** if you see weird errors: `npx expo start -c`
6. **For physical devices**, use your computer's IP instead of localhost

---

## 📚 Documentation Files

| File | Purpose |
|------|---------|
| `README.md` | Project overview and quick start |
| `SETUP_GUIDE.md` | Detailed setup instructions |
| `TROUBLESHOOTING.md` | Common issues and solutions |
| `QUICK_REFERENCE.md` | This quick reference card |

---

## 🔐 Default Credentials

**Database:**
- Username: `postgres`
- Password: `imandi2002`
- Database: `jendo-app`
- Port: `5432`

**Backend:**
- Port: `5000`
- API Base: `/api`

**Frontend:**
- Metro: Port `8081`
- DevTools: Port `19002`

---

## ✅ Verification Checklist

Before developing, verify:

- [ ] PostgreSQL is running on port 5432
- [ ] Backend starts without errors
- [ ] Swagger UI is accessible: http://localhost:5000/swagger-ui.html
- [ ] Frontend dependencies installed (`npm install` completed)
- [ ] `.env` file exists in Expo folder
- [ ] Expo dev server starts successfully
- [ ] Connection test screen shows success

---

## 🎓 Next Steps

1. **Test the connection** using the connection test screen
2. **Explore Swagger UI** to understand available APIs
3. **Start building features** in the frontend
4. **Create test data** in the database if needed
5. **Set up authentication** flow if not already done

---

## 📞 Need Help?

1. Check `TROUBLESHOOTING.md` for common issues
2. Review `SETUP_GUIDE.md` for detailed instructions
3. Use Swagger UI to test backend APIs directly
4. Check backend logs for error details
5. Use connection test screen to verify setup

---

**Happy Coding! 🎉**
