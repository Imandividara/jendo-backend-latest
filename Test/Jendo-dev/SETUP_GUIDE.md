# Jendo App - Frontend & Backend Connection Guide

## Overview
This guide helps you connect the Expo (React Native) frontend with the Spring Boot backend.

## Architecture
- **Frontend**: Expo (React Native) - Port 8081 (Metro Bundler)
- **Backend**: Spring Boot - Port 5000
- **Database**: PostgreSQL - Port 5432

---

## Prerequisites

### Backend Requirements
- Java 17 or higher
- Maven 3.6+
- PostgreSQL database running on `localhost:5432`

### Frontend Requirements
- Node.js 18+ and npm/yarn
- Expo CLI

---

## Setup Instructions

### 1. Database Setup

Ensure PostgreSQL is running and create the database:

```sql
CREATE DATABASE "jendo-app";
```

Update database credentials in `Jendo-Backend/Jendo-Backend/src/main/resources/application.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jendo-app?sslmode=disable
    username: postgres
    password: YOUR_PASSWORD
```

### 2. Backend Setup

Navigate to the backend directory:

```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
```

Build and run the backend:

```powershell
mvn clean install
mvn spring-boot:run
```

Or run the main Application class from your IDE.

**Verify backend is running:**
- Open browser: http://localhost:5000/swagger-ui.html
- You should see the Swagger API documentation

### 3. Frontend Setup

Navigate to the frontend directory:

```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
```

Install dependencies:

```powershell
npm install
```

Start the Expo development server:

```powershell
npm start
```

Or run on specific platform:

```powershell
npm run android  # For Android
npm run ios      # For iOS
npm run web      # For Web
```

---

## Configuration Files

### Frontend Configuration

**`.env` file** (already created):
```env
EXPO_PUBLIC_API_URL=http://localhost:5000/api
EXPO_PUBLIC_APP_NAME=Jendo Health App
EXPO_PUBLIC_APP_VERSION=1.0.0
EXPO_PUBLIC_ENV=development
```

**For Physical Device Testing:**

If testing on a physical device, replace `localhost` with your computer's IP address:

```env
EXPO_PUBLIC_API_URL=http://192.168.X.X:5000/api
```

Find your IP:
```powershell
ipconfig
```
Look for "IPv4 Address" under your active network adapter.

### Backend Configuration

The backend is configured to accept requests from:
- http://localhost:8081 (Expo Metro Bundler)
- http://localhost:19000-19002 (Expo DevTools)
- Expo mobile clients

**CORS is configured in:** `WebConfig.java`

---

## API Endpoints

The backend exposes the following API groups (all prefixed with `/api`):

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/verify-otp` - Verify OTP
- `POST /api/auth/forgot-password` - Request password reset

### Users
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Doctors
- `GET /api/doctors` - List doctors
- `GET /api/doctors/{id}` - Get doctor details
- `POST /api/doctors` - Create doctor
- `PUT /api/doctors/{id}` - Update doctor
- `DELETE /api/doctors/{id}` - Delete doctor

### Appointments
- `GET /api/appointments` - List appointments
- `GET /api/appointments/{id}` - Get appointment details
- `POST /api/appointments` - Create appointment
- `PUT /api/appointments/{id}` - Update appointment
- `DELETE /api/appointments/{id}` - Delete appointment

### Jendo Tests
- `GET /api/jendo-tests` - List all tests
- `GET /api/jendo-tests/{id}` - Get test details
- `POST /api/jendo-tests` - Create test
- `PUT /api/jendo-tests/{id}` - Update test
- `DELETE /api/jendo-tests/{id}` - Delete test

### Health Parameters
- `GET /api/health-parameters` - List health parameters
- `GET /api/health-parameters/{id}` - Get parameter details
- `POST /api/health-parameters` - Create parameter
- `PUT /api/health-parameters/{id}` - Update parameter

### Notifications
- `GET /api/notifications/user/{userId}` - Get user notifications
- `GET /api/notifications/user/{userId}/unread` - Get unread notifications
- `GET /api/notifications/user/{userId}/unread/count` - Get unread count
- `POST /api/notifications` - Create notification
- `PUT /api/notifications/{id}/read` - Mark as read

### Learning Materials
- `GET /api/learning-materials` - List materials
- `GET /api/learning-materials/{id}` - Get material details
- `GET /api/learning-materials/category/{category}` - Get by category
- `GET /api/learning-materials/search?q={query}` - Search materials

---

## Testing the Connection

### 1. Test Backend API Directly

Using PowerShell:

```powershell
# Test health check or any GET endpoint
Invoke-WebRequest -Uri "http://localhost:5000/api/users" -Method GET
```

Or use a REST client like Postman or Insomnia.

### 2. Test from Frontend

The frontend already has API integration set up. Check these files:
- `src/infrastructure/api/httpClient.ts` - HTTP client with axios
- `src/features/*/services/*Api.ts` - Feature-specific API calls

Example API call in your React Native components:

```typescript
import { httpClient } from '@/infrastructure/api';

// GET request
const users = await httpClient.get('/users');

// POST request
const newUser = await httpClient.post('/users', {
  name: 'John Doe',
  email: 'john@example.com'
});
```

### 3. Monitor Network Requests

In Expo:
- Open Expo DevTools (usually at http://localhost:19002)
- Use the "Network" tab to monitor API calls

In Backend:
- Check console logs for incoming requests
- Spring Boot logs all API calls at DEBUG level

---

## Troubleshooting

### Connection Refused
- Ensure backend is running on port 5000
- Check if PostgreSQL is running
- Verify no firewall is blocking the connection

### CORS Errors
- Check `WebConfig.java` CORS configuration
- Ensure frontend URL is in allowed origins
- For physical device, add your computer's IP to allowed origins

### 401 Unauthorized
- Check if authentication token is being sent
- Verify token is stored in AsyncStorage
- Check `httpClient.ts` request interceptor

### Database Connection Failed
- Verify PostgreSQL is running
- Check database credentials in `application.yml`
- Ensure database "jendo-app" exists

### Cannot Find Module
Frontend:
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
rm -rf node_modules
npm install
```

Backend:
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
mvn clean install -U
```

---

## Development Workflow

1. **Start PostgreSQL** (if not running as service)
2. **Start Backend**:
   ```powershell
   cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
   mvn spring-boot:run
   ```
3. **Start Frontend**:
   ```powershell
   cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
   npm start
   ```
4. **Access Application**:
   - Scan QR code with Expo Go app (mobile)
   - Press 'w' for web browser
   - Press 'a' for Android emulator
   - Press 'i' for iOS simulator

---

## Additional Resources

- **Swagger API Docs**: http://localhost:5000/swagger-ui.html
- **Expo DevTools**: http://localhost:19002
- **Backend Logs**: `Jendo-Backend/Jendo-Backend/logs/jendo-app.log`

---

## Production Deployment

When deploying to production:

1. Update `.env`:
   ```env
   EXPO_PUBLIC_API_URL=https://your-production-api.com/api
   EXPO_PUBLIC_ENV=production
   ```

2. Update `WebConfig.java` CORS:
   - Remove wildcard `*`
   - Add specific production frontend URLs

3. Secure database:
   - Use environment variables for credentials
   - Enable SSL mode

4. Configure proper authentication:
   - Implement JWT token refresh
   - Add proper session management
   - Enable HTTPS

---

## Quick Reference Commands

### Backend
```powershell
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run tests
mvn test

# Package
mvn package
```

### Frontend
```powershell
# Install dependencies
npm install

# Start dev server
npm start

# Run on Android
npm run android

# Run on iOS
npm run ios

# Run on Web
npm run web

# Clear cache
npx expo start -c
```

---

## Support

For issues or questions:
1. Check backend logs in `logs/jendo-app.log`
2. Check Expo Metro bundler console
3. Use Swagger UI for API testing: http://localhost:5000/swagger-ui.html
