# 🛒 E-Commerce REST API (Spring Boot)

Full-featured backend for an online store built with Spring Boot.

## 📌 Features

- ✅ JWT Authentication & Authorization
- ✅ Role-based access control: USER, SELLER, ADMIN
- ✅ Product & Category CRUD (with seller ownership)
- ✅ Cart management per user (add/remove items, total price)
- ✅ User registration with encrypted passwords (BCrypt)
- ✅ Address management (many-to-many with users)
- ✅ Global Exception Handling
- ✅ Entity validation (using JSR-303 annotations)
- ✅ Secure REST endpoints via Spring Security
- ✅ Preloaded test users & roles with CommandLineRunner
- ✅ DTOs and layered architecture (Controller → Service → Repository)

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL
- Maven

## 🚀 Getting Started

1. **Clone the repository**
2. Configure your `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
   spring.datasource.username=root
   spring.datasource.password=yourpassword

   spring.jpa.hibernate.ddl-auto=update
   spring.app.jwtSecret=YourJWTSecretKey
   spring.app.jwtExpirationMs=86400000
   spring.raxrot.jwtCookieName=your_jwt_cookie
   ```
3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. Access Swagger docs (if enabled):
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## 👥 Preloaded Users

| Username | Password   | Role(s)            |
|----------|------------|--------------------|
| user1    | password1  | USER               |
| seller1  | password2  | SELLER             |
| admin    | adminPass  | ADMIN, USER, SELLER|

## 📁 Project Structure

```
├── model/
├── repository/
├── service/
├── controller/
├── security/
├── dto/
└── exception/
```

---

## 📦 Endpoints (Example)

- `POST /api/auth/signin` – Login
- `POST /api/auth/signup` – Register
- `GET /api/products` – List all products
- `POST /api/products` – Add product (SELLER only)
- `POST /api/cart/add` – Add to cart (USER only)

---

## 🧪 Tests

Test users and roles are automatically created on startup via `CommandLineRunner`.

---

## ⚠️ License

MIT
