# 🎟 Event Booking System

A Spring Boot backend application for managing events and bookings.

This project demonstrates the implementation of a clean relational database structure using Spring Data JPA and MySQL.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- MySQL
- Lombok
- Maven

---

## 📌 Project Overview

The Event Booking System allows:

- Users to register and log in
- Admins to create events
- Users to book events
- Users to cancel bookings

This project focuses on:
- Proper database design
- Clean JPA entity relationships
- Scalable backend structure

---

## 🗄 Database Schema

### 🧑 Users Table

| Column      | Type      | Description |
|------------|----------|------------|
| id         | BIGINT   | Primary Key |
| name       | VARCHAR  | User name |
| email      | VARCHAR  | Unique email |
| password   | VARCHAR  | Encrypted password |
| role       | VARCHAR  | USER / ADMIN |
| created_at | TIMESTAMP | Account creation time |

---

### 🎉 Events Table

| Column      | Type      | Description |
|------------|----------|------------|
| id         | BIGINT   | Primary Key |
| title      | VARCHAR  | Event title |
| description| TEXT     | Event description |
| location   | VARCHAR  | Event location |
| event_date | DATETIME | Date and time of event |
| created_by | BIGINT   | Foreign Key → Users(id) |

---

### 🎟 Bookings Table

| Column        | Type      | Description |
|--------------|----------|------------|
| id           | BIGINT   | Primary Key |
| user_id      | BIGINT   | Foreign Key → Users(id) |
| event_id     | BIGINT   | Foreign Key → Events(id) |
| booking_time | TIMESTAMP | Time of booking |
| status       | VARCHAR  | CONFIRMED / CANCELLED |

---

## 🔗 Entity Relationships

- One User can create multiple Events.
- One User can have multiple Bookings.
- One Event can have multiple Bookings.
- Each Booking belongs to one User and one Event.

---

## 🧠 JPA Annotations Used

- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@OneToMany`
- `@ManyToOne`
- `@JoinColumn`
- `@PrePersist`

---

## ⚙️ How to Run the Application

### 1️⃣ Clone the repository

```bash
git clone <your-repository-url>
cd eventbooking