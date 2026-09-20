# Reglog Full-Stack Authentication System

A full-stack, enterprise-grade authentication solution consisting of:
1. **Reglog Frontend App**: Built with **React.js** (Vite), featuring `Signup.jsx`, `Login.jsx`, and `Home.jsx`, rich dark-mode glassmorphic styling, and JWT-guarded routes.
2. **Reglog Backend App**: Built with **Spring Boot 3**, structured into distinct **User Service** and **Authentication Service** layers (Controller, Service, and Repository each).
3. **Database Storage**: **MySQL running on `localhost:3306`**, storing users in the `users` table and session tokens in the `JWT_tokens` table with BCrypt encrypted passwords.

---

## Architecture Overview

```
authentication-project/
├── backend/                                   # Reglog Backend App (Spring Boot)
│   ├── pom.xml                                # Maven configuration & dependencies
│   ├── mvnw.cmd / .mvn/                       # Maven wrapper
│   └── src/main/
│       ├── resources/
│       │   ├── application.properties         # MySQL & JWT configuration
│       │   └── schema.sql                     # DDL for MySQL (users & JWT_tokens)
│       └── java/com/reglog/
│           ├── ReglogBackendApplication.java  # Main Application class
│           ├── config/
│           │   ├── SecurityConfig.java        # Spring Security, BCrypt bean, route security
│           │   ├── JwtAuthenticationFilter.java# Intercepts Bearer tokens
│           │   └── CorsConfig.java            # CORS configuration for React frontend
│           ├── user/                          # User Service (3 layers)
│           │   ├── controller/UserController.java
│           │   ├── service/UserService.java & UserServiceImpl.java (BCrypt hashing)
│           │   ├── repository/UserRepository.java
│           │   ├── model/User.java            # Mapped to `users` table
│           │   └── dto/SignupRequest.java, UserResponse.java
│           └── auth/                          # Authentication Service (3 layers)
│               ├── controller/AuthController.java
│               ├── service/AuthService.java & AuthServiceImpl.java (Login, BCrypt check, JWT)
│               ├── repository/JwtTokenRepository.java
│               ├── model/JwtToken.java        # Mapped to `JWT_tokens` table
│               ├── util/JwtUtils.java         # HMAC-SHA256 token generation & validation
│               └── dto/LoginRequest.java, AuthResponse.java, TokenValidationResponse.java
│
├── frontend/                                  # Reglog Frontend App (React.js + Vite)
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.jsx
│       ├── App.jsx                            # Router configuration
│       ├── index.css                          # Design tokens, Google Fonts, theme
│       ├── App.css                            # Glassmorphism, animations, cards, badges
│       ├── services/
│       │   └── api.js                         # REST client with JWT storage & headers
│       └── components/
│           ├── Signup.jsx                     # Name, password, confirm, email, phone, link
│           ├── Login.jsx                      # User name, password, login button, link
│           ├── Home.jsx                       # Logged-in user name display, profile, logout
│           └── ProtectedRoute.jsx             # JWT guard redirecting unauthenticated users
└── README.md
```

---

## Database Schema (MySQL on `localhost:3306`)

Database name: `reglog_db`

### 1. `users` Table
Stores user accounts. Passwords are **always encrypted using BCrypt** before being inserted. Plain-text passwords are never stored.

| Column | Data Type | Modifiers | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY AUTO_INCREMENT | Unique User identifier |
| `name` | VARCHAR(100) | NOT NULL | User name |
| `password` | VARCHAR(255) | NOT NULL | BCrypt encrypted password hash |
| `email` | VARCHAR(100) | NOT NULL UNIQUE | User email address |
| `phone_no` | VARCHAR(20) | NULL | User phone number |

### 2. `JWT_tokens` Table
Maintains active tokens for validation, session audit, and logout invalidation.

| Column | Data Type | Modifiers | Description |
|---|---|---|---|
| `tid` | BIGINT | PRIMARY KEY AUTO_INCREMENT | Token ID |
| `uid` | BIGINT | NOT NULL, FOREIGN KEY | User ID reference (`users.id`) |
| `token` | VARCHAR(512) | NOT NULL | Signed JWT token string |
| `cat` | DATETIME | NOT NULL | Created At timestamp |
| `eat` | DATETIME | NOT NULL | Expires At timestamp |

---

## API Endpoints

### User Service (`/api/users`)
- `POST /api/users/register` (Public): Registers a new user. Hashes the password using `BCryptPasswordEncoder` and persists user to `users` table.
- `GET /api/users/me` (Protected): Retrieves profile details of the currently authenticated user based on the `Authorization: Bearer <token>` header.

### Authentication Service (`/api/auth`)
- `POST /api/auth/login` (Public): Validates user credentials using BCrypt verification, generates signed JWT token with claims, records token in `JWT_tokens` table (`tid, uid, token, cat, eat`), and returns token + user data.
- `POST /api/auth/validate` (Public): Checks cryptographic signature and verifies active status in `JWT_tokens` table.
- `POST /api/auth/logout` (Public/Protected): Deletes the token entry from `JWT_tokens` table.

---

## How to Run

### Prerequisites
- Node.js (v18+) & npm
- Java JDK (version 17 or higher)
- MySQL Server running on `localhost:3306`

### 1. MySQL Setup
Ensure your MySQL server is running on `localhost:3306`. Create database `reglog_db` (or allow Spring Boot auto-creation):
```sql
CREATE DATABASE IF NOT EXISTS reglog_db;
```
Check or update credentials in `backend/src/main/resources/application.properties` (default: `username=root`, `password=root`).

### 2. Start the Backend Application
Open a terminal in `backend/`:
```bash
# Using Maven wrapper (Windows)
mvnw.cmd spring-boot:run

# Or with installed Maven:
mvn spring-boot:run
```
The backend server will start at `http://localhost:8080`.

### 3. Start the Frontend Application
Open a terminal in `frontend/`:
```bash
npm install
npm run dev
```
The frontend will start at `http://localhost:5173`.

---

## Frontend Pages & Features

1. **Signup Page (`/signup`)**:
   - Fields: **user name**, **password**, **confirm password**, **email**, **phone**, and **Sign Up button**.
   - Hyperlink: *"Already have an account? Login"* pointing to `/login`.
   - Real-time client-side validation for passwords matching, valid email pattern, and required fields.
2. **Login Page (`/login`)**:
   - Fields: **user name**, **password**, and **Login button**.
   - Hyperlink: *"Don't have an account? Sign Up"* pointing to `/signup`.
   - Handles BCrypt credential checks via `/api/auth/login` and stores token upon success.
3. **Home Dashboard (`/`)**:
   - Protected route: automatically redirects unauthenticated users to `/login`.
   - Prominently displays the logged-in user's name: *"Welcome back, [Name]!"*.
   - Displays user details (Email, Phone, Authentication status).
   - Includes **Logout button** that revokes token from backend and clears local storage.
