# Jendo Health App

## Overview

The Jendo Health App is a comprehensive healthcare management application designed for cardiovascular health monitoring. It offers features such as user authentication, doctor listings and appointment booking, medical records, health reports, wellness content, notifications, and user profile management. The project aims to provide a robust platform for users to manage their health proactively.

## User Preferences

- Standard TypeScript/React patterns
- Feature-based folder structure
- Environment-based configuration

## System Architecture

The application consists of a React Native (Expo) frontend and a Spring Boot backend.

### Frontend (Expo/React Native Web)
- **Framework**: Expo SDK 54 with React Native Web.
- **Key Features**: Expo Router for navigation, feature-based module organization (`src/features/`), shared components (`src/common/`), configuration management, React context providers, and API clients (`src/infrastructure/`).
- **UI/UX Decisions**: The application uses native icons (Ionicons, MaterialCommunityIcons, FontAwesome5) across all platforms for a consistent and performant experience, eliminating emoji fallbacks. Date and time displays are localized to the Sri Lankan timezone (Asia/Colombo). Modal popups are used for confirmations instead of native browser alerts.

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0 with Java 17.
- **Key Features**: RESTful API design, JWT-based authentication, Spring Data JPA for database interactions, and Swagger/OpenAPI for API documentation.
- **System Design**: Implements a hierarchical health report system with five levels: `ReportCategory` → `ReportSection` → `ReportItem` → `ReportItemValue` → `ReportAttachment`, supporting full CRUD operations and file attachments. User ownership is tracked for report values. Includes a secure OTP generation mechanism and refresh token validation for robust authentication.

### Technical Implementations
- **Authentication**: Features complete JWT authentication, Google OAuth integration, secure OTP generation, and a comprehensive password reset flow.
- **Data Management**: Utilizes PostgreSQL as the primary database.
- **Jendo Tests & Reports**: Complete Jendo test reports feature with list view (showing "Jendo Vascular Health Report" cards with scores and risk levels), detail view (Vascular Health Report with graphs, vital signs, and disclaimer), search/filter functionality, download capability, and auth-aware UX. Jendo tests now include SpO2 (blood oxygen saturation) tracking.
- **Learning Materials**: Public API endpoint (`/api/learning-materials/**`) for admin-managed educational content covering cardiovascular health topics (blood pressure, nutrition, fitness, wellness, cardiology). Accessible without authentication via Spring Security permitAll configuration.
- **Appointment Booking**: Users can book appointments with doctors using multiple consultation types (IN_PERSON, VIDEO, CHAT). The booking requires userId, doctorId, date, time, and type fields. Success notifications are displayed after successful bookings.
- **Home Dashboard**: Real-time dashboard showing user's latest Jendo test data including risk level, score, blood pressure, heart rate, SpO2, and a score history chart. Dates are displayed in Sri Lankan timezone (Asia/Colombo).
- **Deployment Strategy**: A Node.js proxy server is used in the Replit environment to manage traffic, routing API requests to the Spring Boot backend and other requests to the Expo frontend bundler, overcoming single-port limitations.

## External Dependencies

- **Database**: PostgreSQL (Replit's built-in database).
- **Authentication Services**: Google Identity Services (GIS) for Google Sign-In.
- **Frontend Libraries**: `react-native-paper` for Snackbar components.
- **Backend Libraries**: Spring Data JPA, Jackson for JSON processing.