# 🚀 MDD API - Full Stack Developer Network

A modern, full-stack application built with Spring Boot and Angular, connecting developers through articles, comments, and theme-based subscriptions.

## 🛠️ Tech Stack

### 🔙 Backend
- 🍃 Spring Boot 3.2.2
- 🐬 MySQL Database
- 🔒 Spring Security with JWT
- ⚡ Lombok for code generation
- 📊 Spring Data JPA
- 📝 SpringDoc OpenAPI for API documentation

### 🖥️ Frontend
- 🅰️ Angular 14.1.3
- 🎨 Angular Material UI
- 🔄 RxJS for reactive programming
- � TypeScript for type safety

## ✨ Features

### 👥 User Management
- 📝 User registration and authentication
- 🔐 Secure JWT-based authentication
- 👤 Profile management
- 🔑 Password hashing and security

### 📚 Content Management
- ✍️ Article creation and management
- 💬 Comment system with real-time updates
- 🏷️ Theme-based organization
- 🔍 Search functionality

### 🤝 Social Features
- 📌 Theme subscriptions
- 💬 User interactions
- 📈 Activity tracking
- 📊 Analytics

## 🛠️ Setup Instructions

### 📋 Prerequisites
- ☕ Java 17 or higher
- 🏗️ Maven 3.6.3 or higher
- 🟩 Node.js 16.x or higher
- 🐬 MySQL 8.0.33

### ⚙️ Backend Setup
1. 📥 Clone the repository
2. 📂 Navigate to the backend directory
3. ⚡ Run `mvn spring-boot:run`
4. 🌐 The backend will start on `http://localhost:8080`

### 🎮 Frontend Setup
1. 📂 Navigate to the frontend directory
2. 📦 Run `npm install` to install dependencies
3. ⚡ Run `ng serve` to start the development server
4. 🌐 The frontend will be available at `http://localhost:4200`

## 📄 API Documentation

The API documentation is available at:
- 📚 Swagger UI: `http://localhost:8080/swagger-ui.html`
- 📜 OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🔧 Environment Variables

Create a `.env` file in the backend directory with the following variables:
Database Configuration
DB_URL=jdbc:mysql://localhost:3306/mdd_api
DB_USERNAME=root
DB_PASSWORD=your_password

JWT Configuration
JWT_SECRET=your_jwt_secret_key
JWT_EXPIRATION=86400000

Server Configuration
SERVER_PORT=8080

## 🔄 Development Workflow

1. ✏️ Make changes in the appropriate module
2. 🧪 Run backend tests: `mvn test`
3. 🧪 Run frontend tests: `ng test`
4. 🏗️ Build the application: `mvn clean install`
5. 🚀 Deploy to production

## 🌐 API Endpoints

### 🔐 Authentication
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - User login
- GET `/api/auth/me` - Get current user info

### 📰 Articles
- GET `/api/articles` - List all articles
- POST `/api/articles` - Create new article
- GET `/api/articles/:id` - Get article by ID
- PUT `/api/articles/:id` - Update article
- DELETE `/api/articles/:id` - Delete article

### 💬 Comments
- GET `/api/comments` - List comments
- POST `/api/comments` - Create comment
- DELETE `/api/comments/:id` - Delete comment

### 📌 Subscriptions
- POST `/api/subscriptions` - Subscribe to theme
- DELETE `/api/subscriptions/:id` - Unsubscribe from theme
- GET `/api/subscriptions/user/:userId` - Get user subscriptions

## 🔒 Security

- 🛡️ JWT-based authentication
- 🔑 Password hashing with BCrypt
- 👥 Role-based access control
- 🛡️ CSRF protection
- 🛡️ XSS protection
- 🛡️ SQL injection prevention

## License

This project is licensed under the MIT License - see the LICENSE file for details.