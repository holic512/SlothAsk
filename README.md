<div align="center">
  <img src="./image-removebg-preview.png" alt="SlothAsk Logo" width="200">
  
  <h1>SlothAsk - 树懒问答</h1>
  
  <p><strong>基于AI驱动的智能知识问答平台</strong></p>
  
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
    <a href="https://slothask.online/">🌐 官方网站</a> |
    <a href="#快速开始">🚀 快速开始</a> |
    <a href="#功能特性">✨ 功能特性</a> |
    <a href="#技术栈">🛠️ 技术栈</a>
  </p>
  
</div>

---

## 📖 目录

- [介绍](#介绍)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [快速开始](#快速开始)
- [部署指南](#部署指南)
- [项目结构](#项目结构)
- [贡献指南](#贡献指南)
- [Star History](#star-history)
- [许可证](#许可证)

## 🎯 介绍

SlothAsk 是一个基于 **Java Spring Boot 微服务架构** 和 **Vue3 前端技术栈** 构建的智能知识问答平台。

### 🌟 核心特色

- 🤖 **AI 驱动**：深度集成人工智能技术，提供智能问答和内容生成
- 🔍 **语义搜索**：基于 Qdrant 向量数据库的高精度语义检索
- 🏗️ **微服务架构**：分布式系统设计，高可用、高并发
- 📱 **现代化界面**：基于 Vue3 + TypeScript 的响应式前端
- 🔐 **安全认证**：支持多种第三方登录方式

## ✨ 功能特性

### 🎯 核心功能

- 🧠 **智能问答系统**
  - AI 驱动的自然语言理解
  - 上下文感知的对话能力
  - 多轮对话支持

- 📚 **知识库管理**
  - 个人知识库构建
  - 知识点关联分析
  - 智能标签分类

- 🔍 **语义搜索引擎**
  - 基于向量数据库的语义检索
  - 模糊匹配和精确查找
  - 实时搜索建议

- 🤖 **AI 内容生成**
  - 智能问题生成
  - 答案自动补全
  - 内容摘要提取

- 📊 **数据分析**
  - 学习进度追踪
  - 知识图谱可视化
  - 个性化推荐

### 🛠️ 技术特性

- **分布式实时通信**：基于 Redis 广播与队列实现分布式 SSE 推送与离线消息处理，通过双向心跳机制维护连接稳定性与用户状态一致性
- **智能语义搜索**：集成 Qdrant 向量数据库与嵌入式语义模型，构建高维向量语义搜索引擎，实现智能语义匹配与相似度计算
- **分布式安全认证**：通过 Sa-Token 技术结合 Spring Gateway 实现分布式鉴权体系，支持 GitHub、邮箱登录等第三方免密登录
- **智能任务调度**：引入分布式锁机制协调定时任务执行，避免多服务重复调度，确保任务执行的准确性和一致性
- **AI 智能评估**：通过特定的 AI Prompt 技术实现对用户回答内容的准确性判断与解析生成，提升答题反馈的智能化程度
- **高并发处理**：引入 RabbitMQ + Redis 构建削峰填谷机制，有效解决高并发写入瓶颈，保障系统稳定运行
- **用户行为分析**：设计类 GitHub 热力图功能，优化活跃度聚合查询，高效展现用户行为分布与学习轨迹
- **异步消息通知**：基于 RabbitMQ 实现异步邮件与消息通知系统，保障消息可靠传递，降低模块间耦合度


## 🛠️ 技术栈

### 后端技术
- **核心框架**：Java 17 + Spring Boot 3.x + Spring Cloud
- **微服务**：Spring Gateway + Eureka + Sa-Token
- **数据存储**：MySQL + Redis + Qdrant 向量数据库
- **消息队列**：RabbitMQ
- **文件存储**：MinIO
- **容器化**：Docker + Docker Compose

### 前端技术
- **框架**：Vue 3 + TypeScript + Vite
- **UI 组件**：Element Plus
- **状态管理**：Pinia
- **HTTP 客户端**：Axios
- **动画库**：GSAP
- **构建工具**：Vite + ESBuild

### 运维部署
- **Web 服务器**：Nginx
- **容器编排**：Docker Compose
- **监控**：Spring Boot Actuator
- **日志**：Logback + ELK Stack

## 🚀 快速开始

### 环境要求

- Java 17+
- Node.js 16+
- Docker & Docker Compose
- MySQL 8.0+
- Redis 6.0+

### 本地开发

1. **克隆项目**
```bash
git clone https://github.com/holic512/SlothAsk.git
cd SlothAsk
```

2. **启动基础服务**
```bash
docker-compose up -d mysql redis rabbitmq qdrant
```

3. **配置数据库**
 ```bash
 # 导入数据库脚本
 mysql -u root -p < design/database.sql
 ```

4. **启动后端服务**
 ```bash
 # 启动注册中心
 cd Service-Eureka
 mvn spring-boot:run
 
 # 启动网关服务
 cd ../Service-Gateway
 mvn spring-boot:run
 
 # 启动其他微服务
 cd ../Service-User && mvn spring-boot:run &
 cd ../Service-Question && mvn spring-boot:run &
 cd ../Service-Ai && mvn spring-boot:run &
 # ... 其他服务
 ```
 
 5. **启动前端**
 ```bash
 # 启动用户端前端
 cd frontend-user-web
 npm install
 npm run dev
 
 # 启动管理端前端（可选）
 cd ../frontend-admin-web
 npm install
 npm run dev
 ```

6. **访问应用**
- 前端地址：http://localhost:3000
- API 网关：http://localhost:8080
- 注册中心：http://localhost:8761

### Docker 部署

```bash
# 一键启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 📁 项目结构

```
SlothAsk/
├── Service-Eureka/          # 服务注册中心
├── Service-Gateway/         # API 网关
├── Service-User/            # 用户服务
├── Service-Question/        # 问题服务
├── Service-Ai/              # AI 服务
├── Service-Image/           # 图片服务
├── Service-Notification/    # 通知服务
├── Service-Admin/           # 管理服务
├── Service-Common/          # 公共模块
├── frontend-user-web/       # 用户端前端应用
├── frontend-admin-web/      # 管理端前端应用
├── design/                  # 设计文档
│   ├── database.sql         # 数据库脚本
│   ├── database_design.mmd  # 数据库设计图
│   └── 数据库er图.png       # ER图
├── LICENSE                  # 开源协议
├── README.md               # 项目说明
└── README-en.md            # 英文说明
```

## 🤝 贡献指南

我们欢迎所有形式的贡献！请遵循以下步骤：

### 贡献流程

1. **Fork 项目**
2. **创建特性分支** (`git checkout -b feature/AmazingFeature`)
3. **提交更改** (`git commit -m 'Add some AmazingFeature'`)
4. **推送到分支** (`git push origin feature/AmazingFeature`)
5. **创建 Pull Request**

### 开发规范

- 遵循现有的代码风格
- 添加适当的测试用例
- 更新相关文档
- 确保所有测试通过

### 问题反馈

- 🐛 [报告 Bug](https://github.com/holic512/SlothAsk/issues/new?template=bug_report.md)
- 💡 [功能建议](https://github.com/holic512/SlothAsk/issues/new?template=feature_request.md)
- 📖 [文档改进](https://github.com/holic512/SlothAsk/issues/new?template=documentation.md)

## 📈 Star History

[![Star History Chart](https://api.star-history.com/svg?repos=holic512/SlothAsk&type=Date)](https://star-history.com/#holic512/SlothAsk&Date)

## 📄 许可证

本项目基于 [Apache 2.0 License](LICENSE) 开源协议。

## 🙏 致谢

感谢所有为 SlothAsk 项目做出贡献的开发者们！

### 特别感谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的 Java 框架
- [Vue.js](https://vuejs.org/) - 渐进式 JavaScript 框架
- [Qdrant](https://qdrant.tech/) - 高性能向量数据库
- [Element Plus](https://element-plus.org/) - 优秀的 Vue 3 UI 组件库

---

<div align="center">
  
  **如果这个项目对您有帮助，请给我们一个 ⭐️ Star！**
  
  <p>
    <a href="https://github.com/holic512/SlothAsk/stargazers">
      <img src="https://img.shields.io/github/stars/holic512/SlothAsk?style=social" alt="GitHub stars">
    </a>
    <a href="https://github.com/holic512/SlothAsk/network/members">
      <img src="https://img.shields.io/github/forks/holic512/SlothAsk?style=social" alt="GitHub forks">
    </a>
  </p>
  
  <p>
    <strong>联系我们</strong><br>
    📧 <a href="mailto:holic-x@outlook.com">holic512@163.com</a><br>
    🌐 <a href="https://slothask.online/">官方网站</a>
  </p>
  
</div>
