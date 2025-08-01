<div align="center">
  <img src="./image-removebg-preview.png" alt="SlothAsk Logo" width="200">
  
  <h1>SlothAsk - Intelligent Q&A Platform</h1>
  
  <p><strong>AI-Driven Intelligent Knowledge Q&A Platform</strong></p>
  
  <p>
    <a href="./README.md">简体中文</a> |
    <a href="./README-en.md">English</a>
  </p>
  
  <p>
    <img src="https://img.shields.io/badge/Java-17-orange" alt="Java 17">
    <img src="https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen" alt="Spring Boot">
    <img src="https://img.shields.io/badge/Vue-3.x-4FC08D" alt="Vue 3">
    <img src="https://img.shields.io/badge/License-Apache%202.0-blue" alt="License">
    <img src="https://img.shields.io/badge/Version-1.0.0-orange" alt="Version">
  </p>
  
  <p>
    <a href="https://slothask.online/">🌐 Official Website</a> |
    <a href="#quick-start">🚀 Quick Start</a> |
    <a href="#features">✨ Features</a> |
    <a href="#tech-stack">🛠️ Tech Stack</a>
  </p>
  
</div>

---

## 📖 Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start)
- [Deployment Guide](#deployment-guide)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [Star History](#star-history)
- [License](#license)

## 🎯 Introduction

SlothAsk is an intelligent knowledge Q&A platform built on **Java Spring Boot microservice architecture** and **Vue3 frontend technology stack**.

### 🌟 Core Features

- 🤖 **AI-Driven**: Deep integration of artificial intelligence technology for intelligent Q&A and content generation
- 🔍 **Semantic Search**: High-precision semantic retrieval based on Qdrant vector database
- 🏗️ **Microservice Architecture**: Distributed system design with high availability and high concurrency
- 📱 **Modern Interface**: Responsive frontend based on Vue3 + TypeScript
- 🔐 **Secure Authentication**: Support for multiple third-party login methods

## ✨ Features

### 🎯 Core Functions

- 🧠 **Intelligent Q&A System**
  - AI-driven natural language understanding
  - Context-aware conversation capabilities
  - Multi-turn dialogue support

- 📚 **Knowledge Base Management**
  - Personal knowledge base construction
  - Knowledge point correlation analysis
  - Intelligent tag classification

- 🔍 **Semantic Search Engine**
  - Vector database-based semantic retrieval
  - Fuzzy matching and exact search
  - Real-time search suggestions

- 🤖 **AI Content Generation**
  - Intelligent question generation
  - Answer auto-completion
  - Content summary extraction

- 👥 **Collaboration & Sharing**
  - Multi-user real-time collaboration
  - Knowledge sharing community
  - Permission management system

- 📊 **Data Analytics**
  - Learning progress tracking
  - Knowledge graph visualization
  - Personalized recommendations

### 🛠️ Technical Features

- **Distributed Real-time Communication**: Implements distributed SSE push and offline message processing based on Redis broadcast and queues, maintaining connection stability and user state consistency through bidirectional heartbeat mechanisms
- **Intelligent Semantic Search**: Integrates Qdrant vector database with embedded semantic models to build high-dimensional vector semantic search engines for intelligent semantic matching and similarity computation
- **Distributed Security Authentication**: Implements distributed authentication system through Sa-Token technology combined with Spring Gateway, supporting third-party passwordless login via GitHub, email, etc.
- **Intelligent Task Scheduling**: Introduces distributed lock mechanisms to coordinate scheduled task execution, avoiding duplicate scheduling across multiple services
- **AI Intelligent Assessment**: Implements accuracy judgment and parsing generation of user answer content through specific AI Prompt technology
- **High Concurrency Processing**: Introduces RabbitMQ + Redis to build peak-shaving and valley-filling mechanisms, effectively solving high-concurrency write bottlenecks
- **User Behavior Analysis**: Designs GitHub-like heatmap functionality for efficient display of user behavior distribution and learning trajectories
- **Asynchronous Message Notification**: Implements asynchronous email and message notification systems based on RabbitMQ


## 🛠️ Tech Stack

### Backend Technologies
- **Core Framework**: Java 17 + Spring Boot 3.x + Spring Cloud
- **Microservices**: Spring Gateway + Eureka + Sa-Token
- **Data Storage**: MySQL + Redis + Qdrant Vector Database
- **Message Queue**: RabbitMQ
- **File Storage**: MinIO
- **Containerization**: Docker + Docker Compose

### Frontend Technologies
- **Framework**: Vue 3 + TypeScript + Vite
- **UI Components**: Element Plus
- **State Management**: Pinia
- **HTTP Client**: Axios
- **Animation Library**: GSAP
- **Build Tools**: Vite + ESBuild

### DevOps & Deployment
- **Web Server**: Nginx
- **Container Orchestration**: Docker Compose
- **Monitoring**: Spring Boot Actuator
- **Logging**: Logback + ELK Stack

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Node.js 16+
- Docker & Docker Compose
- MySQL 8.0+
- Redis 6.0+

### Local Development

1. **Clone the project**
```bash
git clone https://github.com/holic512/SlothAsk.git
cd SlothAsk
```

2. **Start infrastructure services**
```bash
docker-compose up -d mysql redis rabbitmq qdrant
```

3. **Configure database**
```bash
# Import database script
mysql -u root -p < design/database.sql
```

4. **Start backend services**
```bash
# Start service registry
cd Service-Eureka
mvn spring-boot:run

# Start gateway service
cd ../Service-Gateway
mvn spring-boot:run

# Start other microservices
cd ../Service-User && mvn spring-boot:run &
cd ../Service-Question && mvn spring-boot:run &
cd ../Service-Ai && mvn spring-boot:run &
# ... other services
```

5. **Start frontend**
```bash
# Start user frontend
cd frontend-user-web
npm install
npm run dev

# Start admin frontend (optional)
cd ../frontend-admin-web
npm install
npm run dev
```

6. **Access the application**
- Frontend: http://localhost:3000
- API Gateway: http://localhost:8080
- Service Registry: http://localhost:8761

### Docker Deployment

```bash
# Start all services with one command
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f
```

## 📁 Project Structure

```
SlothAsk/
├── Service-Eureka/          # Service Registry
├── Service-Gateway/         # API Gateway
├── Service-User/            # User Service
├── Service-Question/        # Question Service
├── Service-Ai/              # AI Service
├── Service-Image/           # Image Service
├── Service-Notification/    # Notification Service
├── Service-Admin/           # Admin Service
├── Service-Common/          # Common Module
├── frontend-user-web/       # User Frontend Application
├── frontend-admin-web/      # Admin Frontend Application
├── design/                  # Design Documents
│   ├── database.sql         # Database Script
│   ├── database_design.mmd  # Database Design Diagram
│   └── 数据库er图.png       # ER Diagram
├── LICENSE                  # Open Source License
├── README.md               # Project Documentation
└── README-en.md            # English Documentation
```

## 🤝 Contributing

We welcome all forms of contributions! Please follow these steps:

### Contribution Process

1. **Fork the project**
2. **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the branch** (`git push origin feature/AmazingFeature`)
5. **Create a Pull Request**

### Development Guidelines

- Follow existing code style
- Add appropriate test cases
- Update relevant documentation
- Ensure all tests pass

### Issue Reporting

- 🐛 [Report Bug](https://github.com/holic512/SlothAsk/issues/new?template=bug_report.md)
- 💡 [Feature Request](https://github.com/holic512/SlothAsk/issues/new?template=feature_request.md)
- 📖 [Documentation Improvement](https://github.com/holic512/SlothAsk/issues/new?template=documentation.md)

## 📈 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=holic512/SlothAsk&type=Date)](https://star-history.com/#holic512/SlothAsk&Date)

## 📄 License

This project is licensed under the [Apache 2.0 License](LICENSE).

## 🙏 Acknowledgments

Thanks to all developers who have contributed to the SlothAsk project!

### Special Thanks

- [Spring Boot](https://spring.io/projects/spring-boot) - Powerful Java Framework
- [Vue.js](https://vuejs.org/) - Progressive JavaScript Framework
- [Qdrant](https://qdrant.tech/) - High-Performance Vector Database
- [Element Plus](https://element-plus.org/) - Excellent Vue 3 UI Component Library

---

<div align="center">
  
  **If this project helps you, please give us a ⭐️ Star!**
  
  <p>
    <a href="https://github.com/holic512/SlothAsk/stargazers">
      <img src="https://img.shields.io/github/stars/holic512/SlothAsk?style=social" alt="GitHub stars">
    </a>
    <a href="https://github.com/holic512/SlothAsk/network/members">
      <img src="https://img.shields.io/github/forks/holic512/SlothAsk?style=social" alt="GitHub forks">
    </a>
  </p>
  
  <p>
    <strong>Contact Us</strong><br>
    📧 <a href="mailto:holic-x@outlook.com">holic-x@outlook.com</a><br>
    🌐 <a href="https://slothask.online/">Official Website</a>
  </p>
  
</div>
