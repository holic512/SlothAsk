-- 设置SQL模式，确保SQL语句的严格性和兼容性
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 初始化管理员角色数据
INSERT INTO `admin_role` (`id`, `name`, `description`, `permissions`, `status`, `create_time`, `update_time`) VALUES
(1, 'super_admin', '超级管理员', '{
    "system": ["view", "edit", "delete"],
    "user": ["view", "edit", "delete", "ban"],
    "question": ["view", "edit", "delete", "audit"],
    "category": ["view", "edit", "delete", "audit"],
    "member": ["view", "edit", "delete"],
    "achievement": ["view", "edit", "delete"],
    "points": ["view", "edit"],
    "statistics": ["view"],
    "log": ["view"]
}', 1, NOW(), NOW()),
(2, 'content_admin', '内容管理员', '{
    "question": ["view", "edit", "audit"],
    "category": ["view", "edit", "audit"],
    "achievement": ["view", "edit"],
    "statistics": ["view"]
}', 1, NOW(), NOW()),
(3, 'user_admin', '用户管理员', '{
    "user": ["view", "edit", "ban"],
    "member": ["view", "edit"],
    "points": ["view", "edit"],
    "statistics": ["view"]
}', 1, NOW(), NOW());

-- 初始化超级管理员账号
-- 密码使用MD5加密，这里的密码是：SlothAsk@2024
-- 实际应用中应该使用更安全的加密方式，比如bcrypt
INSERT INTO `admin_user` (`id`, `username`, `password`, `real_name`, `email`, `phone`, `role_id`, `status`, `create_time`, `update_time`) VALUES
(1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'admin@slothask.com', '13800138000', 1, 1, NOW(), NOW()),
(2, 'content_manager', 'e10adc3949ba59abbe56e057f20f883e', '内容管理员', 'content@slothask.com', '13800138001', 2, 1, NOW(), NOW()),
(3, 'user_manager', 'e10adc3949ba59abbe56e057f20f883e', '用户管理员', 'user@slothask.com', '13800138002', 3, 1, NOW(), NOW());

-- 记录初始管理员的登录日志
INSERT INTO `admin_login_log` (`admin_id`, `login_ip`, `login_time`, `login_status`, `user_agent`, `create_time`) VALUES
(1, '127.0.0.1', NOW(), 1, 'System Initialization', NOW());

SET FOREIGN_KEY_CHECKS = 1;

-- 请注意：
-- 1. 实际部署时请修改管理员的邮箱、电话和密码
-- 2. 密码应该使用更安全的加密方式
-- 3. 权限配置应根据实际业务需求调整 