# 微服务架构设计

## 1. 用户服务 (user-service)
### 核心功能
- 用户注册、登录、认证
- 用户信息管理
- 用户关注关系
- 用户签到

### 相关表
- user (用户表)
- user_profile (用户资料表)
- user_follow (用户关注表)
- user_sign_in (签到记录表)

## 2. 题库服务 (question-service)
### 核心功能
- 题库分类管理
- 题目管理
- 题目评论
- 分类评论

### 相关表
- project_category (项目分类表)
- question_category (题库分类表)
- question (题目表)
- question_comment (题目评论表)
- category_comment (分类评论表)
- category_rating (题库评分表)

## 3. 学习记录服务 (study-service)
### 核心功能
- 答题记录
- 错题本
- 刷题统计

### 相关表
- user_question_record (答题记录表)
- wrong_question_book (错题本表)
- user_question_stats (用户刷题统计表)

## 4. 成就服务 (achievement-service)
### 核心功能
- 成就系统
- 成就获取与展示

### 相关表
- achievement (成就表)
- user_achievement (用户成就表)

## 5. 会员服务 (member-service)
### 核心功能
- 会员等级管理
- 会员购买与续费
- 积分系统

### 相关表
- member_level (会员等级表)
- user_member (用户会员表)
- user_points (用户积分表)
- points_record (积分变动记录表)

## 6. 权限服务 (permission-service)
### 核心功能
- 题库权限管理
- 用户权限管理

### 相关表
- question_permission (题库权限表)
- user_category_permission (用户题库权限表)

## 7. 管理服务 (admin-service)
### 核心功能
- 管理员账号管理
- 角色权限管理
- 后台操作日志

### 相关表
- admin_user (管理员表)
- admin_role (管理员角色表)
- admin_login_log (管理员登录日志表)

## 8. 网关服务 (gateway-service)
### 核心功能
- 请求路由
- 负载均衡
- 认证授权
- 限流熔断

## 9. 公共服务 (common-service)
### 核心功能
- 配置中心
- 服务注册与发现
- 日志收集
- 监控告警