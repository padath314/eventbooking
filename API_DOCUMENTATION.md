# Event Booking API Documentation

**Base URL:** `http://localhost:8080`

**Authentication:** JWT Bearer Token (required for all endpoints except `/api/auth/**`)

---

## Table of Contents

1. [Authentication APIs](#authentication-apis)
2. [User APIs](#user-apis)
3. [Event APIs](#event-apis)
4. [Booking APIs](#booking-apis)
5. [Error Responses](#error-responses)
6. [Authentication Flow Examples](#authentication-flow-examples)

---

## Authentication APIs

### 1. Register User

**Endpoint:** `POST /api/auth/register`

**Description:** Creates a new user account with email and password. If no role is provided, defaults to "USER".

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123",
  "role": "USER"
}
```

**Path Parameters:** None

**Query Parameters:** None

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-03-08T10:30:00",
  "events": [],
  "bookings": []
}
```

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| id | Long | User unique identifier (auto-generated) |
| name | String | User's full name |
| email | String | User's email (unique, required) |
| password | String | BCrypt-hashed password |
| role | String | User role ("USER" or "ADMIN") |
| createdAt | DateTime | Account creation timestamp |
| events | Array | List of events created by user |
| bookings | Array | List of bookings made by user |

---

### 2. Login User

**Endpoint:** `POST /api/auth/login`

**Description:** Authenticates user and returns a JWT token valid for 10 hours.

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA5ODk0NjAwLCJleHAiOjE3MDk5MzA2MDB9.abc123...",
  "message": "Login successful"
}
```

**Fields:**
| Field | Type | Description |
|-------|------|-------------|
| token | String | JWT Bearer token (valid for 10 hours) |
| message | String | Success confirmation message |

**Token Details:**
- **Algorithm:** HMAC-SHA256
- **Subject:** User email
- **Expiration:** 10 hours from generation
- **Usage:** Include in Authorization header as `Bearer {token}`

---

## User APIs

### 1. Get All Users

**Endpoint:** `GET /users`

**Description:** Retrieves all registered users.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "password": "$2a$10$...",
    "role": "USER",
    "createdAt": "2024-03-08T10:30:00",
    "events": [],
    "bookings": []
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "password": "$2a$10$...",
    "role": "ADMIN",
    "createdAt": "2024-03-08T11:15:00",
    "events": [],
    "bookings": []
  }
]
```

---

### 2. Get User By ID

**Endpoint:** `GET /users/{id}`

**Description:** Retrieves a specific user by their ID.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-03-08T10:30:00",
  "events": [],
  "bookings": []
}
```

---

### 3. Create User

**Endpoint:** `POST /users`

**Description:** Creates a new user directly (alternative to registration).

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "password": "Password123",
  "role": "USER"
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "name": "Alice Johnson",
  "email": "alice@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-03-08T12:45:00",
  "events": [],
  "bookings": []
}
```

---

### 4. Update User

**Endpoint:** `PUT /users/{id}`

**Description:** Updates user information.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Request Body:**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "password": "NewPassword123",
  "role": "ADMIN"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com",
  "password": "$2a$10$...",
  "role": "ADMIN",
  "createdAt": "2024-03-08T10:30:00",
  "events": [],
  "bookings": []
}
```

---

### 5. Delete User

**Endpoint:** `DELETE /users/{id}`

**Description:** Deletes a user by their ID.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | User ID |

**Response (204 No Content):** Empty response body on success

---

## Event APIs

### 1. Create Event

**Endpoint:** `POST /api/events`

**Description:** Creates a new event. User must be the creator.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "title": "Tech Conference 2024",
  "description": "Annual technology conference featuring keynote speakers and workshops",
  "location": "San Francisco Convention Center",
  "eventDate": "2024-06-15T09:00:00",
  "createdBy": {
    "id": 1
  }
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Tech Conference 2024",
  "description": "Annual technology conference featuring keynote speakers and workshops",
  "location": "San Francisco Convention Center",
  "eventDate": "2024-06-15T09:00:00",
  "createdBy": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  },
  "bookings": []
}
```

**Fields:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| title | String | Yes | Event title |
| description | String | No | Event description |
| location | String | No | Event location |
| eventDate | DateTime | No | Event date and time (ISO 8601 format) |
| createdBy | User Object | Yes | User who created the event |

---

### 2. Get All Events

**Endpoint:** `GET /api/events`

**Description:** Retrieves all events in the system.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Tech Conference 2024",
    "description": "Annual technology conference featuring keynote speakers and workshops",
    "location": "San Francisco Convention Center",
    "eventDate": "2024-06-15T09:00:00",
    "createdBy": {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "role": "USER"
    },
    "bookings": []
  },
  {
    "id": 2,
    "title": "Music Festival",
    "description": "Three-day music festival with local and international artists",
    "location": "Central Park",
    "eventDate": "2024-07-20T18:00:00",
    "createdBy": {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane@example.com",
      "role": "ADMIN"
    },
    "bookings": []
  }
]
```

---

### 3. Get Event By ID

**Endpoint:** `GET /api/events/{id}`

**Description:** Retrieves a specific event by its ID.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Event ID |

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Tech Conference 2024",
  "description": "Annual technology conference featuring keynote speakers and workshops",
  "location": "San Francisco Convention Center",
  "eventDate": "2024-06-15T09:00:00",
  "createdBy": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  },
  "bookings": [
    {
      "id": 1,
      "user": {
        "id": 2,
        "name": "Jane Smith"
      },
      "bookingTime": "2024-03-08T10:30:00",
      "status": "CONFIRMED"
    }
  ]
}
```

---

### 4. Update Event

**Endpoint:** `PUT /api/events/{id}`

**Description:** Updates an existing event's details.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Event ID |

**Request Body:**
```json
{
  "title": "Tech Conference 2024 - Updated",
  "description": "Updated description for the conference",
  "location": "San Francisco Marriott Marquis",
  "eventDate": "2024-06-15T09:00:00",
  "createdBy": {
    "id": 1
  }
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Tech Conference 2024 - Updated",
  "description": "Updated description for the conference",
  "location": "San Francisco Marriott Marquis",
  "eventDate": "2024-06-15T09:00:00",
  "createdBy": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  },
  "bookings": []
}
```

---

### 5. Delete Event

**Endpoint:** `DELETE /api/events/{id}`

**Description:** Deletes an event by its ID.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Event ID |

**Response (204 No Content):** Empty response body on success

---

## Booking APIs

### 1. Create Booking

**Endpoint:** `POST /bookings`

**Description:** Creates a new booking for a user to attend an event.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

**Request Body:**
```json
{
  "user": {
    "id": 2
  },
  "event": {
    "id": 1
  },
  "status": "CONFIRMED"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "user": {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "role": "USER"
  },
  "event": {
    "id": 1,
    "title": "Tech Conference 2024",
    "description": "Annual technology conference",
    "location": "San Francisco Convention Center",
    "eventDate": "2024-06-15T09:00:00"
  },
  "bookingTime": "2024-03-08T14:22:30",
  "status": "CONFIRMED"
}
```

**Fields:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| user | User Object | Yes | User making the booking |
| event | Event Object | Yes | Event being booked |
| status | String | No | Booking status (e.g., "CONFIRMED", "PENDING", "CANCELLED") |
| bookingTime | DateTime | Auto | Timestamp of booking creation |

---

### 2. Get All Bookings

**Endpoint:** `GET /bookings`

**Description:** Retrieves all bookings in the system.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "user": {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane@example.com",
      "role": "USER"
    },
    "event": {
      "id": 1,
      "title": "Tech Conference 2024",
      "location": "San Francisco Convention Center",
      "eventDate": "2024-06-15T09:00:00"
    },
    "bookingTime": "2024-03-08T14:22:30",
    "status": "CONFIRMED"
  },
  {
    "id": 2,
    "user": {
      "id": 3,
      "name": "Alice Johnson",
      "email": "alice@example.com",
      "role": "USER"
    },
    "event": {
      "id": 2,
      "title": "Music Festival",
      "location": "Central Park",
      "eventDate": "2024-07-20T18:00:00"
    },
    "bookingTime": "2024-03-08T15:00:00",
    "status": "PENDING"
  }
]
```

---

### 3. Delete Booking

**Endpoint:** `DELETE /bookings/{id}`

**Description:** Cancels/deletes a booking by its ID.

**Request Headers:**
```
Authorization: Bearer {JWT_TOKEN}
```

**Path Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Long | Yes | Booking ID |

**Response (204 No Content):** Empty response body on success

---

## Error Responses

### Common HTTP Status Codes

| Status Code | Meaning | Description |
|-------------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request successful, no content to return |
| 400 | Bad Request | Invalid request parameters or format |
| 401 | Unauthorized | Missing or invalid JWT token |
| 403 | Forbidden | Authenticated user lacks permission |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server error |

### 404 Not Found

**Response (404 Not Found):**
```json
{
  "timestamp": "2024-03-08T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found with id: 999",
  "path": "/api/events/999"
}
```

### 400 Bad Request

**Response (400 Bad Request):**
```json
{
  "timestamp": "2024-03-08T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request parameters",
  "path": "/api/auth/register"
}
```

### 401 Unauthorized (Missing Token)

**Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-03-08T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication required",
  "path": "/api/events"
}
```

### 401 Unauthorized (Invalid Token)

**Response (401 Unauthorized):**
```json
{
  "timestamp": "2024-03-08T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid JWT token",
  "path": "/api/events"
}
```

### 500 Internal Server Error

**Response (500 Internal Server Error):**
```json
{
  "timestamp": "2024-03-08T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/events"
}
```

---

## Authentication Flow Examples

### Complete Authentication Flow with cURL

#### Step 1: Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePassword123",
    "role": "USER"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "role": "USER",
  "createdAt": "2024-03-08T10:30:00",
  "events": [],
  "bookings": []
}
```

#### Step 2: Login and Get JWT Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePassword123"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA5ODk0NjAwLCJleHAiOjE3MDk5MzA2MDB9.abc123xyz",
  "message": "Login successful"
}
```

#### Step 3: Use JWT Token to Access Protected Endpoints

**Create Event:**
```bash
curl -X POST http://localhost:8080/api/events \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA5ODk0NjAwLCJleHAiOjE3MDk5MzA2MDB9.abc123xyz" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tech Conference 2024",
    "description": "Annual technology conference",
    "location": "San Francisco Convention Center",
    "eventDate": "2024-06-15T09:00:00",
    "createdBy": {
      "id": 1
    }
  }'
```

**Response:**
```json
{
  "id": 1,
  "title": "Tech Conference 2024",
  "description": "Annual technology conference",
  "location": "San Francisco Convention Center",
  "eventDate": "2024-06-15T09:00:00",
  "createdBy": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  },
  "bookings": []
}
```

**Get All Events:**
```bash
curl -X GET http://localhost:8080/api/events \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA5ODk0NjAwLCJleHAiOjE3MDk5MzA2MDB9.abc123xyz"
```

---

### Example: Using JWT in Postman

#### Headers Required for All Protected Endpoints:

```
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Step-by-step in Postman:**

1. First, call `POST /api/auth/login` to get the token:
   - Body (JSON): 
     ```json
     {
       "email": "john@example.com",
       "password": "SecurePassword123"
     }
     ```

2. Copy the token from the response:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "message": "Login successful"
   }
   ```

3. For any protected endpoint (e.g., `GET /api/events`):
   - Go to **Headers** tab
   - Add header:
     - **Key:** `Authorization`
     - **Value:** `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

4. Click **Send** - request will be authenticated

---

## Things to Remember

✅ **Token Format:** Always use `Bearer {token}` in the Authorization header

✅ **Token Expiration:** JWT tokens expire after 10 hours

✅ **Password Security:** Passwords are hashed using BCrypt and never returned in plain text

✅ **Email Uniqueness:** Email addresses are unique in the system

✅ **User ID Auto-generated:** When creating resources, IDs are auto-generated by the database

✅ **Timestamps Auto-populated:** `createdAt` and `bookingTime` are automatically set by the server

✅ **Null Relationships:** Relationships without data return as `null` or empty arrays in the response

---

## Running the Application

**Start the server:**
```bash
mvn spring-boot:run
```

**Default database:**
- Database: MySQL
- Host: localhost:3306
- Database: eventbooking
- Username: root
- Password: root123

**Server runs on:** `http://localhost:8080`

---

## Support & Troubleshooting

| Issue | Solution |
|-------|----------|
| 401 Unauthorized | Ensure JWT token is included in Authorization header |
| Token expired | Login again to get a new token |
| 404 Not Found | Check if resource ID exists or endpoint URL is correct |
| 400 Bad Request | Verify request JSON format and required fields |
| 403 Forbidden | User may lack permission or token may be invalid |
| Connection refused | Ensure MySQL is running and server is started |

---

**Last Updated:** March 8, 2024
**API Version:** 1.0
