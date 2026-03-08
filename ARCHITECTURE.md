# 🏗️ Event Booking System - Architecture & Design

## System Overview

The Event Booking System is built on a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────────────┐
│                       CLIENT LAYER (UI/API Consumers)               │
│  Web Browsers | Mobile Apps | Postman | REST Clients | cURL         │
└──────────────────────────┬──────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT APPLICATION                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  AUTHENTICATION & SECURITY LAYER                             │  │
│  │  • JWT Token Generation & Validation                         │  │
│  │  • JwtRequestFilter - Intercepts All Requests               │  │
│  │  • SecurityConfig - Authorization Rules                      │  │
│  │  • BCrypt Password Encoding                                  │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           ✓                                         │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  CONTROLLER LAYER (@RestController)                          │  │
│  │  • AuthController - Register/Login API                       │  │
│  │  • UserController - User CRUD API                            │  │
│  │  • EventController - Event CRUD API                          │  │
│  │  • BookingController - Booking CRUD API                      │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           ✓                                         │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  SERVICE LAYER (@Service)                                    │  │
│  │  • AuthService - Authentication Logic                        │  │
│  │  • EventService - Event Business Logic                       │  │
│  │  • UserService - User Business Logic                         │  │
│  │  • BookingService - Booking Business Logic                   │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           ✓                                         │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  REPOSITORY LAYER (@Repository)                              │  │
│  │  • UserRepository - Data Access for Users                    │  │
│  │  • EventRepository - Data Access for Events                  │  │
│  │  • BookingRepository - Data Access for Bookings              │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           ✓                                         │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  MODEL LAYER (@Entity)                                       │  │
│  │  • User - User Entity                                        │  │
│  │  • Event - Event Entity                                      │  │
│  │  • Booking - Booking Entity                                  │  │
│  └──────────────────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    MYSQL DATABASE                                   │
│  Database: eventbooking                                             │
│  Tables: users | events | bookings                                  │
│  Connection: root:root123 @ localhost:3306                          │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Layered Architecture Details

### 1️⃣ **Client Layer**
- **Purpose:** Interface for API consumers
- **Components:** Web browsers, mobile apps, API testing tools (Postman, cURL)
- **Communication:** HTTP/HTTPS REST requests

### 2️⃣ **Security & Authentication Layer**
- **JwtUtil** - Generates and validates JWT tokens
  - Algorithm: HMAC-SHA256
  - Secret Key: 512-bit (RFC 7518 compliant)
  - Expiration: 10 hours
  
- **JwtRequestFilter** - Request interceptor
  - Validates Bearer tokens on protected endpoints
  - Bypasses `/api/auth/**` (login/register)
  - Sets authentication context for request
  
- **SecurityConfig** - Spring Security configuration
  - Permits: `/`, `/index.html`, `/api/auth/**`, `/error`
  - Requires authentication: All other endpoints
  - Session management: STATELESS (no cookies, JWT only)
  - Password encoder: BCryptPasswordEncoder

### 3️⃣ **Controller Layer** (Request Handling)

#### AuthController
```
POST /api/auth/register  → Registers new user
POST /api/auth/login     → Authenticates and returns JWT token
```

#### UserController
```
GET /users              → List all users
GET /users/{id}         → Get user by ID
POST /users             → Create new user
PUT /users/{id}         → Update user
DELETE /users/{id}      → Delete user
```

#### EventController
```
GET /api/events         → List all events
GET /api/events/{id}    → Get event by ID
POST /api/events        → Create new event
PUT /api/events/{id}    → Update event
DELETE /api/events/{id} → Delete event
```

#### BookingController
```
GET /bookings           → List all bookings
POST /bookings          → Create new booking
DELETE /bookings/{id}   → Delete booking
```

### 4️⃣ **Service Layer** (Business Logic)

#### AuthService
- **register(RegisterRequest)** - Creates user with hashed password
  - Hashes password using BCrypt
  - Sets default role if not provided
  - Saves to database
  - Logging: tracks registration email
  
- **login(LoginRequest)** - Authenticates user
  - Validates credentials
  - Generates JWT token
  - Logging: tracks login attempts and success

#### EventService
- **createEvent(Event)** - Creates event with creator reference
- **getAllEvents()** - Fetches all events
- **getEventById(Long)** - Gets single event by ID
- **updateEvent(Long, Event)** - Updates event details
- **deleteEvent(Long)** - Removes event
- **Exception handling:** ResourceNotFoundException for missing events

#### UserService & BookingService
- Similar CRUD operations with business logic validation

### 5️⃣ **Repository Layer** (Data Access)

Uses **Spring Data JPA** for automatic CRUD operations:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // Custom query
}

public interface EventRepository extends JpaRepository<Event, Long> {}
public interface BookingRepository extends JpaRepository<Booking, Long> {}
```

### 6️⃣ **Model Layer** (Data Entities)

#### User Entity
```
@Entity Table: users
├── id (Long) - Primary Key
├── name (String) - Full name
├── email (String) - Unique, required
├── password (String) - BCrypt hashed
├── role (String) - "USER" or "ADMIN"
├── createdAt (LocalDateTime) - Auto-set on creation
├── events (List<Event>) - One-to-Many relationship
└── bookings (List<Booking>) - One-to-Many relationship
```

#### Event Entity
```
@Entity Table: events
├── id (Long) - Primary Key
├── title (String) - Event name
├── description (String) - Event details
├── location (String) - Event location
├── eventDate (LocalDateTime) - Event date/time
├── createdBy (User) - Many-to-One relationship
└── bookings (List<Booking>) - One-to-Many relationship
```

#### Booking Entity
```
@Entity Table: bookings
├── id (Long) - Primary Key
├── user (User) - Many-to-One relationship
├── event (Event) - Many-to-One relationship
├── bookingTime (LocalDateTime) - Auto-set on creation
└── status (String) - "CONFIRMED", "PENDING", etc.
```

### 7️⃣ **Database Layer**

**MySQL Database Structure:**

```sql
-- Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Events Table
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    event_date TIMESTAMP,
    created_by BIGINT NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- Bookings Table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (event_id) REFERENCES events(id)
);
```

---

## Request Flow Example

### Authentication Flow (Register → Login → Access Protected Resource)

```
┌─────────────────────────────────────────────────────────────────────┐
│ STEP 1: USER REGISTRATION                                           │
└─────────────────────────────────────────────────────────────────────┘

Client Request:
  POST /api/auth/register
  {
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePassword123"
  }
           ↓
  AuthController.register(RegisterRequest)
           ↓
  AuthService.register(RegisterRequest)
    - BCrypt hash password
    - Set default role: "USER"
           ↓
  UserRepository.save(User)
           ↓
  MySQL: INSERT INTO users (name, email, password, role, created_at)
           ↓
Server Response (201 Created):
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "password": "$2a$10$...", [Hashed]
    "role": "USER",
    "createdAt": "2024-03-08T10:30:00"
  }


┌─────────────────────────────────────────────────────────────────────┐
│ STEP 2: USER LOGIN                                                  │
└─────────────────────────────────────────────────────────────────────┘

Client Request:
  POST /api/auth/login
  {
    "email": "john@example.com",
    "password": "SecurePassword123"
  }
           ↓
  AuthController.login(LoginRequest)
           ↓
  AuthService.login(LoginRequest)
    - UserRepository.findByEmail(email)
           ↓
    Database query: SELECT * FROM users WHERE email = "john@example.com"
           ↓
    - BCrypt.matches(rawPassword, hashedPassword)
           ↓
    - If valid: JwtUtil.generateToken(email)
      • Algorithm: HMAC-SHA256
      • Subject: "john@example.com"
      • Expiration: +10 hours from now
           ↓
Server Response (200 OK):
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "message": "Login successful"
  }


┌─────────────────────────────────────────────────────────────────────┐
│ STEP 3: ACCESS PROTECTED RESOURCE WITH TOKEN                        │
└─────────────────────────────────────────────────────────────────────┘

Client Request:
  GET /api/events
  Headers: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
           ↓
  Spring receives request
           ↓
  JwtRequestFilter intercepts request
    - Skips check if path is /api/auth/**
    - Extracts token from Authorization header
    - JwtUtil.extractEmail(token)
           ↓
    - If token valid and not expired:
      • Extract email from token
      • Create UserDetails
      • Create UsernamePasswordAuthenticationToken
      • Set SecurityContext
           ↓
  Request continues to EventController.getAll()
           ↓
  EventService.getAllEvents()
           ↓
  EventRepository.findAll()
           ↓
  Database query: SELECT * FROM events
           ↓
Server Response (200 OK):
  [
    { Event 1 },
    { Event 2 },
    ...
  ]


┌─────────────────────────────────────────────────────────────────────┐
│ ERROR CASE: INVALID/EXPIRED TOKEN                                   │
└─────────────────────────────────────────────────────────────────────┘

Client Request:
  GET /api/events
  Headers: Authorization: Bearer invalid_token_xyz...
           ↓
  JwtRequestFilter intercepts
    - Tries to extract email from token
    - Token validation fails (expired or malformed)
           ↓
Server Response (401 Unauthorized):
  {
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid JWT token"
  }
```

---

## Entity Relationships

### User ↔ Event (One-to-Many)
- One user can create many events
- Each event has one creator (User)
- Foreign Key: `events.created_by` → `users.id`

### User ↔ Booking (One-to-Many)
- One user can make many bookings
- Each booking belongs to one user
- Foreign Key: `bookings.user_id` → `users.id`

### Event ↔ Booking (One-to-Many)
- One event can have many bookings
- Each booking is for one event
- Foreign Key: `bookings.event_id` → `events.id`

### Relationship Diagram
```
┌──────────┐                    ┌────────────┐
│  User    │ 1──────────────N   │   Event    │
│  -----   │ (creates)          │  ------    │
│ id(PK)   │                    │ id(PK)     │
│ name     │                    │ title      │
│ email    │◄─ created_by       │ created_by │
│ password │                    │ eventDate  │
│ role     │                    │            │
└──────────┘                    └────────────┘
     △                                △
     │                                │
     │ 1                              │ N
 (makes)│                         (for)│
     │                                │
     │ N                              │ 1
     │                                │
  User.bookings               Event.bookings
     └────────────┬───────────────────┘
                  │
              ┌───┴────────┐
              │  Booking   │
              │  -------   │
              │ id(PK)     │
              │ user_id(FK)│
              │ event_id(FK)
              │ bookingTime│
              │ status     │
              └────────────┘
```

---

## Technology Stack

### Backend Framework
- **Spring Boot 4.0.3** - Web framework
- **Spring Web** - REST controller support
- **Spring Data JPA** - Database abstraction

### Security
- **Spring Security 7.0.3** - Authentication & authorization
- **JJWT 0.11.5** - JWT token creation & validation
- **BCrypt** - Password hashing algorithm

### Database
- **MySQL 8.0+** - Relational database
- **Hibernate** - ORM (Object-Relational Mapping)

### Code Generation
- **Lombok 1.18.x** - Auto-generate getters/setters

### Build & Logging
- **Maven** - Build tool
- **SLF4J + Logback** - Logging framework

---

## Security Architecture

### Password Security
```
User Password Input: "SecurePassword123"
           ↓
BCryptPasswordEncoder (strength: 10)
           ↓
Hashed: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS..." (60 chars fixed)
           ↓
Stored in Database (never plain text)
           ↓
Login Request: User enters password again
           ↓
BCrypt.matches(rawPassword, storedHashedPassword)
           ↓
True → Token generated
False → 401 Unauthorized
```

### JWT Token Security
```
Token Generation:
  Header: { "alg": "HS256", "typ": "JWT" }
  Payload: { "sub": "john@example.com", "iat": 1234567890, "exp": 1234608690 }
  Secret: "mySecretKeyThatIsLongEnoughForJWT256BitsOrMoreToBeSecure" (512 bits)
           ↓
  HMAC-SHA256(Header.Payload, Secret)
           ↓
  Result: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

Token Validation:
  Received Header.Payload.Signature
           ↓
  Verify: HMAC-SHA256(Header.Payload, Secret) == Signature
           ↓
  Extract exp claim and check if expired
           ↓
  Valid → Grant access
  Invalid → 401 Unauthorized
```

### Authorization Rules
```
PERMITTED (No Authentication Required):
  GET  /                    (Welcome page)
  GET  /index.html          (Static content)
  POST /api/auth/register   (User registration)
  POST /api/auth/login      (User login)
  GET  /error               (Error page)

PROTECTED (Authentication Required):
  GET  /users               (List users)
  GET  /users/{id}          (Get user)
  POST /users               (Create user)
  PUT  /users/{id}          (Update user)
  DELETE /users/{id}        (Delete user)
  
  GET  /api/events          (List events)
  GET  /api/events/{id}     (Get event)
  POST /api/events          (Create event)
  PUT  /api/events/{id}     (Update event)
  DELETE /api/events/{id}   (Delete event)
  
  GET  /bookings            (List bookings)
  POST /bookings            (Create booking)
  DELETE /bookings/{id}     (Delete booking)
```

---

## Logging Architecture

### Log Levels
- **DEBUG** - Detailed information for debugging (application code, Spring Security)
- **INFO** - General informational messages (CRUD operations, login events)
- **WARN** - Warning messages (degraded service, missing resources)
- **ERROR** - Error messages (exceptions, authentication failures)

### Logged Components
1. **AuthService** - User registration and login attempts
2. **AuthController** - Login/register requests and responses
3. **UserController** - User CRUD operations
4. **EventController** - Event CRUD operations
5. **EventService** - Event business logic execution
6. **GlobalExceptionHandler** - Exception handling with appropriate levels

### Log Output Format
```
Console: %d{yyyy-MM-dd HH:mm:ss} - %msg%n
Example: 2024-03-08 10:30:00 - User registered successfully with email: john@example.com

File: %d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
Example: 2024-03-08 10:30:00 [main] INFO  com.susan.eventbooking.service.AuthService - User registered with email: john@example.com
```

---

## Deployment Architecture

### Development Environment
```
Local Machine
├── MySQL Server (localhost:3306)
├── Spring Boot Application (localhost:8080)
└── Client (Browser/Postman)
```

### Production Ready
```
Could be deployed to:
├── Docker Container (Containerized app)
├── Kubernetes Cluster (Orchestrated deployment)
├── Cloud Platforms (AWS, Azure, GCP)
└── Traditional Server
```

---

## Scalability Considerations

### Current Limitations
- Single instance (no horizontal scaling)
- MySQL database as single point of failure
- JWT tokens stored client-side (no session management)

### Future Improvements
- Load balance multiple Spring Boot instances
- Implement database replication (master-slave)
- Add caching layer (Redis)
- API rate limiting
- Database connection pooling optimization
- Message queue for async operations (RabbitMQ, Kafka)

---

## Error Handling Architecture

```
┌─────────────────────────────────────────┐
│ Exception Occurs                        │
└────────────┬────────────────────────────┘
             │
             ▼
┌─────────────────────────────────────────┐
│ GlobalExceptionHandler Catches          │
│ @ControllerAdvice                       │
└────────────┬────────────────────────────┘
             │
             ├─→ ResourceNotFoundException ─→ 404 Not Found (WARN)
             │
             ├─→ RuntimeException ─→ 400 Bad Request (ERROR)
             │
             └─→ General Exception ─→ 500 Internal Server Error (ERROR)
                                      
             ↓
         Logged to Console/File
             ↓
         Error Response returned to Client
```

---

## Summary

The Event Booking System architecture provides:
✅ **Clear separation of concerns** - Each layer has specific responsibility
✅ **Security** - JWT tokens + BCrypt password hashing
✅ **Maintainability** - Modular design easy to update/extend
✅ **Logging** - Observable system with DEBUG/INFO/ERROR levels
✅ **Scalability** - Spring Boot easily scales horizontally
✅ **Type Safety** - Java 17 with strong typing
✅ **Database Integrity** - ORM with foreign key relationships
✅ **Error Handling** - Centralized exception handling with appropriate HTTP status codes

