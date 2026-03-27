# 💰 Budget Buddy — Backend Application

A **powerful and scalable RESTful API** built with **Spring Boot** that powers the *Budget Buddy* personal finance application.
It handles **authentication, transaction management, analytics, and automation**, providing a complete backend solution for financial tracking.

---

## 🚀 Key Features

### 👤 Authentication & Users

* Secure user registration with **email activation**
* **JWT-based authentication & authorization**
* Protected endpoints with Spring Security

### 💸 Financial Management

* Full CRUD for **incomes & expenses**
* Smart **categorization system**
* Track and organize personal finances efficiently

### 📊 Analytics & Insights

* Real-time **dashboard aggregation**

   * Total balance
   * Total income
   * Total expenses
   * Recent transactions
* Clean and optimized API responses

### 🔍 Advanced Filtering

* Filter transactions by:

   * Name
   * Date range
   * Amount
* Server-side sorting for better performance

### 📤 Data Export & Emailing

* Export data to **Excel (Apache POI)**
* Send reports directly via email
* Automated email workflows:

   * 📅 Daily reminders
   * 📈 Daily summaries

### 🐳 DevOps Ready

* Fully containerized with **Docker**
* Easy deployment to platforms like Railway / Render

---

## 🧰 Tech Stack

| Layer      | Technology                           |
| ---------- | ------------------------------------ |
| Backend    | Spring Boot 3                        |
| Language   | Java 21                              |
| Security   | Spring Security, JWT                 |
| Database   | PostgreSQL / MySQL (JPA + Hibernate) |
| Build Tool | Maven                                |
| Excel      | Apache POI                           |
| Deployment | Docker                               |

---

## ⚙️ Getting Started

### 📌 Prerequisites

* Java 21+
* Maven 3.6+
* Git
* MySQL or PostgreSQL
* Docker *(optional)*

---

### 📥 Installation

```bash
git clone https://github.com/SalemNabeelSalem/budget-buddy-application-backend.git
cd budget-buddy-application-backend
```

---

### 🔐 Environment Setup

```bash
cp .env.example .env
```

Update `.env` with your:

* Database credentials
* Email provider (Brevo / SMTP)
* JWT secret key

---

### 🛠️ Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

📍 Server runs at:

```
http://localhost:8080
```

---

## 🔧 Configuration

The app uses environment variables via `.env`.

| Variable              | Description           |
|-----------------------|-----------------------|
| `BACK_END_BASE_URL`   | Backend base URL      |
| `FRONT_END_BASE_URL`  | Frontend base URL     |
| `BREVO_FROM_EMAIL`    | Sender email          |
| `BREVO_USERNAME`      | SMTP username         |
| `BREVO_PASSWORD`      | SMTP password/API key |
| `BREVO_PORT`          | SMTP port             |
| `JWT_SECRET`          | JWT signing key       |
| `RAILWAY_DB_HOST`     | Railway DB host       |
| `RAILWAY_DB_USERNAME` | Railway DB username   |
| `RAILWAY_DB_PASSWORD` | Railway DB password   |
| `RAILWAY_DB_PORT`     | Railway DB port       |
| `RAILWAY_DB_NAME`     | Railway DB name       |
| `RENDER_DB_HOST`      | Render DB host        |
| `RENDER_DB_USERNAME`  | Render DB username    |
| `RENDER_DB_PASSWORD`  | Render DB password    |
| `RENDER_DB_PORT`      | Render DB port        |
| `RENDER_DB_NAME`      | Render DB name        |
---

## 📡 API Overview

All endpoints are prefixed with:

```
/api/v1
```

### 🔓 Public Endpoints

| Endpoint            | Method | Description      |
| ------------------- | ------ | ---------------- |
| `/status`           | GET    | App status       |
| `/health`           | GET    | Health check     |
| `/profile/register` | POST   | Register user    |
| `/profile/activate` | GET    | Activate account |
| `/profile/login`    | POST   | Login            |

---

### 🔐 Secured Endpoints

#### 👤 User

| Endpoint      | Method | Description       |
| ------------- | ------ | ----------------- |
| `/profile/me` | GET    | Current user info |

#### 💰 Transactions

| Endpoint                          | Method       |
| --------------------------------- | ------------ |
| `/incomes`, `/expenses`           | GET / POST   |
| `/incomes/{id}`, `/expenses/{id}` | PUT / DELETE |
| `/incomes/top`, `/expenses/top`   | GET          |

#### 🗂️ Categories

| Endpoint         | Method     |
| ---------------- | ---------- |
| `/category`      | GET / POST |
| `/category/{id}` | PUT        |

#### 📊 Dashboard

| Endpoint     | Method |
| ------------ | ------ |
| `/dashboard` | GET    |

#### 🔍 Filtering

| Endpoint   | Method |
| ---------- | ------ |
| `/filters` | POST   |

#### 📤 Export & Email

| Endpoint                   | Method |
| -------------------------- | ------ |
| `/download/excel-incomes`  | GET    |
| `/download/excel-expenses` | GET    |
| `/email/excel-incomes`     | GET    |
| `/email/excel-expenses`    | GET    |

---

## ⏰ Scheduled Jobs

Automated background tasks (Asia/Dubai timezone):

* 🕙 **10:00 PM** — Daily reminder to log transactions
* 🕚 **11:00 PM** — Daily financial summary email

---

## 🐳 Docker Usage

### Build Image

```bash
docker build -t budget-buddy-backend .
```

### Run Container

```bash
docker run -p 8080:8080 --env-file .env budget-buddy-backend
```

---

## 🏗️ Project Structure

```
src/main/java/isalem/dev/budget_buddy/
├── configs/        # Security & CORS configs
├── controllers/    # REST controllers
├── dtos/           # Data Transfer Objects
├── entities/       # JPA entities
├── repositories/   # Data access layer
├── security/       # JWT & auth logic
├── services/       # Business logic
└── utils/          # Helpers (JWT, etc.)
```

---

## ✨ Summary

**Budget Buddy Backend** is a **production-ready API** that combines:

* 🔐 Strong security
* ⚡ High performance
* 📊 Smart financial insights
* 📦 Clean architecture

Perfect for building **modern fintech or personal finance applications**.

---