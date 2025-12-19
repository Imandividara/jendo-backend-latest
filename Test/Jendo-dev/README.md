# Jendo Health App

A comprehensive health management application with React Native (Expo) frontend and Spring Boot backend.

## 🚀 Quick Start

### Option 1: Using Quick Start Script (Recommended)
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project"
.\start.ps1
```

### Option 2: Manual Start

**Start Backend:**
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Jendo-Backend\Jendo-Backend"
mvn spring-boot:run
```

**Start Frontend:**
```powershell
cd "c:\Users\HP\OneDrive\Desktop\jendo_project\Expo (3)\Expo"
npm start
```

## 📋 Prerequisites

- **Java 17+** - For backend
- **Maven 3.6+** - For building backend
- **Node.js 18+** - For frontend
- **PostgreSQL** - Database (running on port 5432)
- **Expo CLI** - For React Native development

## 🔧 Configuration

### Backend
- **Port:** 5000
- **Database:** PostgreSQL on localhost:5432
- **Database Name:** jendo-app
- **API Docs:** http://localhost:5000/swagger-ui.html

### Frontend
- **Metro Bundler:** Port 8081
- **API URL:** http://localhost:5000/api
- **Config:** `.env` file in `Expo` folder

## 📱 Testing Connection

### Via Test Screen
Navigate to `/connection-test` in the app to access the connection testing screen.

### Via Script
Run the quick start script and choose option 4 to test backend connection.

### Via Browser
Visit http://localhost:5000/swagger-ui.html to see API documentation.

## 🗂️ Project Structure

```
jendo_project/
├── Expo (3)/Expo/          # React Native frontend
│   ├── app/                # App screens
│   ├── src/                # Source code
│   │   ├── config/         # Configuration files
│   │   ├── features/       # Feature modules
│   │   ├── infrastructure/ # API client, storage
│   │   └── services/       # Business logic
│   └── .env                # Environment variables
│
├── Jendo-Backend/          # Spring Boot backend
│   └── Jendo-Backend/
│       ├── src/main/
│       │   ├── java/       # Java source code
│       │   └── resources/  # Configuration files
│       └── pom.xml         # Maven configuration
│
├── SETUP_GUIDE.md          # Detailed setup instructions
├── start.ps1               # Quick start script
└── README.md               # This file
```

## 🔑 Key Endpoints

| Endpoint | Description |
|----------|-------------|
| `POST /api/auth/login` | User login |
| `POST /api/auth/register` | User registration |
| `GET /api/users` | List users |
| `GET /api/doctors` | List doctors |
| `GET /api/appointments` | List appointments |
| `GET /api/jendo-tests` | List health tests |
| `GET /api/notifications/user/{userId}` | User notifications |
| `GET /api/learning-materials` | Learning materials |

## 📚 Documentation

- **Detailed Setup Guide:** See `SETUP_GUIDE.md`
- **API Documentation:** http://localhost:5000/swagger-ui.html (when backend is running)
- **Expo Documentation:** https://docs.expo.dev/

## 🐛 Troubleshooting

### Backend won't start
- Ensure PostgreSQL is running
- Check if port 5000 is available
- Verify database credentials in `application.yml`

### Frontend can't connect
- Verify backend is running on port 5000
- Check `.env` file has correct API URL
- For physical devices, use computer's IP instead of localhost

### Database connection failed
- Ensure PostgreSQL is running
- Verify database "jendo-app" exists
- Check credentials in `application.yml`

## 🔗 Quick Links

- **Backend:** http://localhost:5000
- **Swagger UI:** http://localhost:5000/swagger-ui.html
- **Expo DevTools:** http://localhost:19002

## 📝 Environment Variables

Create `.env` in `Expo` folder:
```env
EXPO_PUBLIC_API_URL=http://localhost:5000/api
EXPO_PUBLIC_APP_NAME=Jendo Health App
EXPO_PUBLIC_APP_VERSION=1.0.0
EXPO_PUBLIC_ENV=development
```

## 💡 Tips

1. Always start PostgreSQL before the backend
2. Use the connection test screen to verify setup
3. Check Swagger UI for API documentation
4. Monitor backend logs for debugging
5. Use `npx expo start -c` to clear Expo cache if needed

## 🤝 Support

For detailed instructions, troubleshooting, and production deployment, see `SETUP_GUIDE.md`.
