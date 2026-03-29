# 📚 Library Management System – Complete Guide

**JT Term Work | Spring Boot REST API**

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Project Structure](#2-project-structure)
3. [Prerequisites](#3-prerequisites)
4. [MySQL Setup (phpMyAdmin)](#4-mysql-setup-phpmyadmin)
5. [How to Run the Project](#5-how-to-run-the-project)
6. [Default Login Credentials](#6-default-login-credentials)
7. [API Endpoints Reference](#7-api-endpoints-reference)
8. [How to Use Postman](#8-how-to-use-postman)
9. [Entities and Relationships](#9-entities-and-relationships)
10. [Security – Role-Based Access](#10-security--role-based-access)
11. [Faculty Explanation Points](#11-faculty-explanation-points)

---

## 1. Project Overview

This is a **Library Management System** REST API built with:

| Technology        | Purpose                              |
|-------------------|--------------------------------------|
| Spring Boot 3.2   | Application framework                |
| Spring Data JPA   | Database operations (ORM)            |
| Spring Security   | Authentication & Role-based access   |
| MySQL             | Relational database                  |
| Hibernate         | JPA implementation                   |
| Lombok            | Reduces boilerplate (getters/setters)|
| Maven             | Build tool                           |

**What the system manages:**
- Authors and their books
- Library members (who can borrow books)
- Book borrowing and returning with copy tracking

---

## 2. Project Structure

```
src/
└── main/
    ├── java/com/library/lms/
    │   ├── LibraryManagementApplication.java  ← Main entry point
    │   ├── config/
    │   │   ├── SecurityConfig.java            ← Spring Security setup
    │   │   ├── DataInitializer.java           ← Seeds default users
    │   │   └── GlobalExceptionHandler.java    ← Error handling
    │   ├── entity/
    │   │   ├── Author.java                    ← Author table
    │   │   ├── Book.java                      ← Books table
    │   │   ├── Member.java                    ← Members table
    │   │   ├── BorrowRecord.java              ← Borrow tracking table
    │   │   └── User.java                      ← Login users table
    │   ├── repository/
    │   │   ├── AuthorRepository.java
    │   │   ├── BookRepository.java
    │   │   ├── MemberRepository.java
    │   │   ├── BorrowRecordRepository.java
    │   │   └── UserRepository.java
    │   ├── service/
    │   │   ├── AuthorService.java
    │   │   ├── BookService.java
    │   │   ├── MemberService.java
    │   │   ├── BorrowRecordService.java
    │   │   └── UserDetailsServiceImpl.java
    │   └── controller/
    │       ├── AuthorController.java
    │       ├── BookController.java
    │       ├── MemberController.java
    │       └── BorrowRecordController.java
    └── resources/
        └── application.properties             ← DB + server config
```

**Architecture (Layered):**
```
Client (Postman)
    ↓ HTTP Request
Controller  ← handles HTTP, calls service
    ↓
Service     ← business logic
    ↓
Repository  ← talks to database (Spring Data JPA)
    ↓
MySQL Database
```

---

## 3. Prerequisites

Make sure you have these installed:

| Requirement      | Version  | Download                              |
|------------------|----------|---------------------------------------|
| Java (JDK)       | 17+      | https://adoptium.net/                 |
| Maven            | 3.8+     | https://maven.apache.org/             |
| MySQL Server     | 8.0+     | https://dev.mysql.com/downloads/      |
| phpMyAdmin       | Any      | Comes with XAMPP / WAMP               |
| Postman          | Any      | https://www.postman.com/downloads/    |
| IntelliJ IDEA    | Any      | https://www.jetbrains.com/idea/       |

**Quick Check (run in terminal/CMD):**
```bash
java -version        # Should show 17+
mvn -version         # Should show 3.8+
```

---

## 4. MySQL Setup (phpMyAdmin)

### Option A – Using XAMPP (recommended for students)

1. Download and install **XAMPP** from https://www.apachefriends.org/
2. Open **XAMPP Control Panel**
3. Start **Apache** and **MySQL**
4. Open browser → go to `http://localhost/phpmyadmin`

### Creating the Database

1. In phpMyAdmin, click **"New"** in the left sidebar
2. Enter database name: `library_db`
3. Select Collation: `utf8mb4_unicode_ci`
4. Click **"Create"**

> ✅ **The application will auto-create all tables** when you run it the first time (thanks to `spring.jpa.hibernate.ddl-auto=update`).

### Configure Database Password

Open file: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root        ← Change this to YOUR MySQL root password
```

> **Default XAMPP MySQL password:** Usually empty (leave password blank) or `root`
> If XAMPP has no password, set: `spring.datasource.password=`

### Verify Tables in phpMyAdmin

After running the application once, you'll see these tables in `library_db`:
- `authors`
- `books`
- `members`
- `borrow_records`
- `users`

---

## 5. How to Run the Project

### Method 1 – IntelliJ IDEA (Recommended)

1. Open IntelliJ IDEA
2. Click **File → Open** → Select the project folder
3. Wait for Maven to download dependencies (bottom progress bar)
4. Open `LibraryManagementApplication.java`
5. Click the **green ▶ Run** button
6. You'll see: `Started LibraryManagementApplication on port 8080`

### Method 2 – Command Line (Terminal/CMD)

```bash
# Navigate to project folder
cd path/to/lms

# Build the project
mvn clean install

# Run the project
mvn spring-boot:run
```

### Method 3 – Run the JAR file

```bash
# Build
mvn clean package

# Run the generated JAR
java -jar target/lms-1.0.0.jar
```

### Verify the App is Running

Open browser and go to: `http://localhost:8080/api/books`
- A login popup will appear (it's secured)
- Enter `admin` / `admin123`
- You should see: `[]` (empty JSON array – no books yet)

---

## 6. Default Login Credentials

The application **automatically creates** these two users on first startup:

| Username | Password  | Role       | Access                              |
|----------|-----------|------------|-------------------------------------|
| `admin`  | `admin123`| ROLE_ADMIN | Full access (CRUD everything)       |
| `user`   | `user123` | ROLE_USER  | View books/authors, borrow/return   |

> Passwords are stored **BCrypt-encrypted** in the database. You can verify this in phpMyAdmin → `users` table.

---

## 7. API Endpoints Reference

**Base URL:** `http://localhost:8080`

### Author Endpoints

| Method | URL                          | Description           | Role        |
|--------|------------------------------|-----------------------|-------------|
| GET    | `/api/authors`               | Get all authors       | ADMIN, USER |
| GET    | `/api/authors/{id}`          | Get author by ID      | ADMIN, USER |
| GET    | `/api/authors/search?name=X` | Search by name        | ADMIN, USER |
| POST   | `/api/authors`               | Create new author     | ADMIN only  |
| PUT    | `/api/authors/{id}`          | Update author         | ADMIN only  |
| DELETE | `/api/authors/{id}`          | Delete author         | ADMIN only  |

### Book Endpoints

| Method | URL                              | Description              | Role        |
|--------|----------------------------------|--------------------------|-------------|
| GET    | `/api/books`                     | Get all books            | ADMIN, USER |
| GET    | `/api/books/{id}`                | Get book by ID           | ADMIN, USER |
| GET    | `/api/books/available`           | Get available books      | ADMIN, USER |
| GET    | `/api/books/search?title=X`      | Search by title          | ADMIN, USER |
| GET    | `/api/books/genre?genre=X`       | Filter by genre          | ADMIN, USER |
| GET    | `/api/books/author/{authorId}`   | Books by specific author | ADMIN, USER |
| POST   | `/api/books`                     | Create new book          | ADMIN only  |
| PUT    | `/api/books/{id}`                | Update book              | ADMIN only  |
| PUT    | `/api/books/{id}/author/{aId}`   | Assign author to book    | ADMIN only  |
| DELETE | `/api/books/{id}`                | Delete book              | ADMIN only  |

### Member Endpoints

| Method | URL                           | Description         | Role       |
|--------|-------------------------------|---------------------|------------|
| GET    | `/api/members`                | Get all members     | ADMIN only |
| GET    | `/api/members/{id}`           | Get member by ID    | ADMIN only |
| GET    | `/api/members/active`         | Get active members  | ADMIN only |
| GET    | `/api/members/search?name=X`  | Search by name      | ADMIN only |
| POST   | `/api/members`                | Create new member   | ADMIN only |
| PUT    | `/api/members/{id}`           | Update member       | ADMIN only |
| DELETE | `/api/members/{id}`           | Delete member       | ADMIN only |

### Borrow/Return Endpoints

| Method | URL                                   | Description              | Role        |
|--------|---------------------------------------|--------------------------|-------------|
| GET    | `/api/borrows`                        | Get all borrow records   | ADMIN, USER |
| GET    | `/api/borrows/{id}`                   | Get record by ID         | ADMIN, USER |
| GET    | `/api/borrows/member/{memberId}`      | Records by member        | ADMIN, USER |
| GET    | `/api/borrows/book/{bookId}`          | Records by book          | ADMIN, USER |
| GET    | `/api/borrows/status/{status}`        | Filter by status         | ADMIN, USER |
| POST   | `/api/borrows/borrow?bookId=1&memberId=2` | Borrow a book        | ADMIN, USER |
| PUT    | `/api/borrows/return/{id}`            | Return a book            | ADMIN, USER |
| DELETE | `/api/borrows/{id}`                   | Delete record            | ADMIN only  |

---

## 8. How to Use Postman

### Setting Up Postman

1. Download and install Postman from https://www.postman.com/downloads/
2. Open Postman

### Configure Basic Authentication (Do this once per request or use a Collection)

For every request, you need to set **Basic Auth**:

1. Open a new request in Postman
2. Click the **Authorization** tab
3. Select **Type → Basic Auth**
4. Enter:
   - Username: `admin`
   - Password: `admin123`

### Creating a Postman Collection (Recommended)

1. Click **Collections** → **+** (New Collection)
2. Name it: `Library Management System`
3. Click **Edit** on the collection → go to **Authorization** tab
4. Set **Type: Basic Auth**, Username: `admin`, Password: `admin123`
5. Now all requests in this collection use admin auth automatically

---

### Step-by-Step Postman Demo

#### STEP 1 – Create an Author

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/authors`
- **Auth:** Basic Auth (admin/admin123)
- **Headers:** `Content-Type: application/json`
- **Body (raw → JSON):**
```json
{
    "name": "J.K. Rowling",
    "email": "jkrowling@example.com",
    "bio": "British author of the Harry Potter series"
}
```
- **Expected Response (201 Created):**
```json
{
    "id": 1,
    "name": "J.K. Rowling",
    "email": "jkrowling@example.com",
    "bio": "British author of the Harry Potter series",
    "books": []
}
```

#### STEP 2 – Create a Book (assign to Author)

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/books`
- **Body:**
```json
{
    "title": "Harry Potter and the Philosopher's Stone",
    "isbn": "978-0-7475-3269-9",
    "genre": "Fantasy",
    "totalCopies": 5,
    "availableCopies": 5,
    "author": {
        "id": 1
    }
}
```
- **Expected Response (201 Created):**
```json
{
    "id": 1,
    "title": "Harry Potter and the Philosopher's Stone",
    "isbn": "978-0-7475-3269-9",
    "genre": "Fantasy",
    "totalCopies": 5,
    "availableCopies": 5
}
```

#### STEP 3 – Create a Member

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/members`
- **Body:**
```json
{
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "phone": "9876543210"
}
```
- **Expected Response (201 Created):**
```json
{
    "id": 1,
    "name": "Rahul Sharma",
    "email": "rahul@example.com",
    "phone": "9876543210",
    "membershipDate": "2024-03-20",
    "active": true
}
```

#### STEP 4 – Borrow a Book

- **Method:** `POST`
- **URL:** `http://localhost:8080/api/borrows/borrow?bookId=1&memberId=1`
- **Body:** (none)
- **Expected Response:**
```json
{
    "id": 1,
    "borrowDate": "2024-03-20",
    "dueDate": "2024-04-03",
    "returnDate": null,
    "status": "BORROWED"
}
```
> Notice: `availableCopies` of the book is now **4** (was 5)

#### STEP 5 – Return a Book

- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/borrows/return/1`
- **Body:** (none)
- **Expected Response:**
```json
{
    "id": 1,
    "borrowDate": "2024-03-20",
    "dueDate": "2024-04-03",
    "returnDate": "2024-03-20",
    "status": "RETURNED"
}
```
> Notice: `availableCopies` is back to **5**

#### STEP 6 – Get All Books

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/books`
- **Auth:** Basic Auth (admin/admin123)

#### STEP 7 – Search Books by Title

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/books/search?title=Harry`

#### STEP 8 – Get Available Books

- **Method:** `GET`
- **URL:** `http://localhost:8080/api/books/available`

#### STEP 9 – Test USER Role (limited access)

- Change Auth to: `user` / `user123`
- **GET** `/api/books` → ✅ Works
- **DELETE** `/api/books/1` → ❌ 403 Forbidden (USER cannot delete)
- **GET** `/api/members` → ❌ 403 Forbidden (USER cannot access members)

#### STEP 10 – Update a Book

- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/books/1`
- **Body:**
```json
{
    "title": "Harry Potter and the Philosopher's Stone",
    "isbn": "978-0-7475-3269-9",
    "genre": "Fantasy",
    "totalCopies": 10,
    "availableCopies": 10
}
```

#### STEP 11 – Delete an Author

- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/authors/1`
- **Expected Response:** `"Author deleted successfully."`

---

## 9. Entities and Relationships

```
Author (1) ────────── (*) Book
                           |
                           |
Member (1) ──── (*) BorrowRecord (*) ──── (1) Book
```

### Author
| Field           | Type    | Description          |
|-----------------|---------|----------------------|
| id              | Long    | Primary key (auto)   |
| name            | String  | Author's full name   |
| email           | String  | Unique email         |
| bio             | String  | Author biography     |
| books           | List    | Books written (1:N)  |

### Book
| Field           | Type    | Description             |
|-----------------|---------|-------------------------|
| id              | Long    | Primary key (auto)      |
| title           | String  | Book title              |
| isbn            | String  | Unique ISBN             |
| genre           | String  | Genre (Fantasy, etc.)   |
| totalCopies     | int     | Total copies in library |
| availableCopies | int     | Currently available     |
| author          | Author  | FK → Author (N:1)       |

### Member
| Field          | Type      | Description           |
|----------------|-----------|-----------------------|
| id             | Long      | Primary key (auto)    |
| name           | String    | Member's full name    |
| email          | String    | Unique email          |
| phone          | String    | Phone number          |
| membershipDate | LocalDate | Date joined (auto)    |
| active         | boolean   | Is member active      |

### BorrowRecord
| Field       | Type         | Description              |
|-------------|--------------|--------------------------|
| id          | Long         | Primary key (auto)       |
| borrowDate  | LocalDate    | Date borrowed (auto)     |
| dueDate     | LocalDate    | Due in 14 days (auto)    |
| returnDate  | LocalDate    | Actual return date       |
| status      | Enum         | BORROWED/RETURNED/OVERDUE|
| book        | Book         | FK → Book (N:1)          |
| member      | Member       | FK → Member (N:1)        |

### User (Security)
| Field    | Type    | Description                |
|----------|---------|----------------------------|
| id       | Long    | Primary key (auto)         |
| username | String  | Login username             |
| password | String  | BCrypt encoded password    |
| role     | String  | ROLE_ADMIN or ROLE_USER    |
| enabled  | boolean | Account active             |

---

## 10. Security – Role-Based Access

This project uses **HTTP Basic Authentication** with **BCrypt password encoding**.

```
┌─────────────────────────────────────────────────────┐
│                  Spring Security                    │
│                                                     │
│  HTTP Request → Check credentials → Check Role     │
│                                                     │
│  ROLE_ADMIN: Full access (CRUD all endpoints)       │
│  ROLE_USER:  Read books/authors + Borrow/Return     │
└─────────────────────────────────────────────────────┘
```

**How it works:**
1. Client sends request with `Authorization: Basic base64(username:password)` header
2. Spring Security calls `UserDetailsServiceImpl.loadUserByUsername()`
3. Loads user from MySQL database
4. Compares BCrypt hash of provided password with stored hash
5. Checks if the user's role allows the requested endpoint
6. Returns **401 Unauthorized** if credentials wrong
7. Returns **403 Forbidden** if role doesn't have access

**BCrypt encoding example:**
```
Plain password:  admin123
BCrypt stored:   $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

---

## 11. Faculty Explanation Points

### What is Spring Boot?
Spring Boot is a Java framework that simplifies creating REST APIs. It auto-configures the application so we don't write boilerplate XML configuration.

### What is JPA / Hibernate?
JPA (Java Persistence API) is a standard for ORM (Object-Relational Mapping). Hibernate is the implementation. It automatically converts Java objects to database tables and SQL queries.

### What is Layered Architecture?
```
Controller → Service → Repository
```
- **Controller**: Handles HTTP requests, sends responses
- **Service**: Contains business logic (e.g., check copies before borrowing)
- **Repository**: Database operations (CRUD via JPA)

### What are the 4 Entities?
1. **Author** – Stores author information
2. **Book** – Stores book details + copy count
3. **Member** – Library members who borrow books
4. **BorrowRecord** – Tracks every borrow/return transaction

### What are the Associations?
- **Author ↔ Book**: One-to-Many (one author writes many books)
- **Member ↔ BorrowRecord**: One-to-Many (one member has many borrow records)
- **Book ↔ BorrowRecord**: One-to-Many (one book can be borrowed many times)

### What is Role-Based Security?
- Two roles: **ADMIN** (full control) and **USER** (limited access)
- Passwords stored as **BCrypt hash** (cannot be reversed/decrypted)
- Configured in `SecurityConfig.java`
- Users loaded from MySQL `users` table via `UserDetailsServiceImpl`

### Why BCrypt?
BCrypt is a secure hashing algorithm. Even if the database is hacked, attackers cannot get the real passwords because hashes cannot be reversed.

### How does Borrow Book work?
1. Check if book has available copies
2. Check if member is active
3. Decrement `availableCopies` by 1
4. Create a `BorrowRecord` with status `BORROWED`
5. Due date is automatically set to **14 days** from today

### How does Return Book work?
1. Find the `BorrowRecord` by ID
2. Set `returnDate` to today
3. Set `status` to `RETURNED`
4. Increment `availableCopies` by 1
