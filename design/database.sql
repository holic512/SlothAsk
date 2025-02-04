-- 设置默认字符集和存储引擎
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 用户信息表（基础表）
CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(32) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '密码(加密存储)',
    `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`),
    UNIQUE KEY `idx_email` (`email`),
    UNIQUE KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';

-- 管理员角色表（基础表）
CREATE TABLE `admin_role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
    `permissions` text NOT NULL COMMENT '权限集合(JSON格式)',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员角色表';

-- 会员等级表（基础表）
CREATE TABLE `member_level` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级ID',
    `name` varchar(50) NOT NULL COMMENT '等级名称',
    `description` varchar(200) DEFAULT NULL COMMENT '等级描述',
    `price` decimal(10, 2) NOT NULL COMMENT '会员价格',
    `duration` int(11) NOT NULL COMMENT '有效期(天)',
    `icon` varchar(255) DEFAULT NULL COMMENT '等级图标',
    `benefits` varchar(500) DEFAULT NULL COMMENT '会员权益说明',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员等级表';

-- 成就表（基础表）
CREATE TABLE `achievement` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '成就ID',
    `name` varchar(50) NOT NULL COMMENT '成就名称',
    `description` varchar(200) NOT NULL COMMENT '成就描述',
    `icon` varchar(255) DEFAULT NULL COMMENT '成就图标URL',
    `condition_type` tinyint(4) NOT NULL COMMENT '条件类型 1:总题数 2:简单题 3:中等题 4:困难题',
    `condition_value` int(11) NOT NULL COMMENT '达成条件数值',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='成就表';

-- 项目大分类表（基础表）
CREATE TABLE `project_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目分类ID',
    `name` varchar(50) NOT NULL COMMENT '项目分类名称(如:Java面试题、软考)',
    `description` varchar(200) DEFAULT NULL COMMENT '项目描述',
    `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序序号',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目大分类表';

-- 管理员表（依赖admin_role）
CREATE TABLE `admin_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username` varchar(32) NOT NULL COMMENT '用户名',
    `password` varchar(128) NOT NULL COMMENT '密码(加密存储)',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `email` varchar(64) NOT NULL COMMENT '邮箱',
    `phone` varchar(20) NOT NULL COMMENT '手机号',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `last_login_ip` varchar(64) DEFAULT NULL COMMENT '最后登录IP',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `login_count` int(11) NOT NULL DEFAULT '0' COMMENT '登录次数',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`),
    UNIQUE KEY `idx_email` (`email`),
    UNIQUE KEY `idx_phone` (`phone`),
    KEY `idx_role` (`role_id`),
    CONSTRAINT `fk_admin_role` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员表';

-- 用户个人资料表（依赖user和achievement）
CREATE TABLE `user_profile` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资料ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `gender` tinyint(4) DEFAULT '0' COMMENT '性别 0:未知 1:男 2:女',
    `age` int(11) DEFAULT NULL COMMENT '年龄',
    `bio` varchar(500) DEFAULT NULL COMMENT '个人简介',
    `display_achievement_id` bigint(20) DEFAULT NULL COMMENT '展示的成就ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_profile_achievement` FOREIGN KEY (`display_achievement_id`) REFERENCES `achievement` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户个人资料表';

-- 题库分类表（依赖project_category）
CREATE TABLE `question_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `project_id` bigint(20) NOT NULL COMMENT '所属项目ID',
    `name` varchar(50) NOT NULL COMMENT '分类名称(如:Java基础、多线程)',
    `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
    `creator_id` bigint(20) NOT NULL COMMENT '创建者ID',
    `avatar_url` varchar(255) DEFAULT NULL COMMENT '分类头像URL',
    `sort_order` int(11) DEFAULT 0 COMMENT '排序序号',
    `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '访问数量',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_project` (`project_id`),
    KEY `idx_creator` (`creator_id`),
    CONSTRAINT `fk_category_project` FOREIGN KEY (`project_id`) REFERENCES `project_category` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_category_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题库分类表';

-- 题库内容表（依赖question_category）
CREATE TABLE `question` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `category_id` bigint(20) NOT NULL COMMENT '分类ID',
    `title` varchar(500) NOT NULL COMMENT '题目标题',
    `content` text NOT NULL COMMENT '题目内容',
    `answer` text NOT NULL COMMENT '题目答案',
    `difficulty` tinyint(4) NOT NULL DEFAULT '1' COMMENT '难度等级 1:简单 2:中等 3:困难',
    `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '题目类型 1:单选 2:多选 3:判断 4:简答',
    `tag_category_id` bigint(20) NOT NULL COMMENT '标签分类ID',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `view_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '浏览次数',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_tag_category` (`tag_category_id`),
    CONSTRAINT `fk_question_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_question_tag` FOREIGN KEY (`tag_category_id`) REFERENCES `question_tag_category` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题库内容表';

-- 题目评论表（依赖question和user）
CREATE TABLE `question_comment` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `question_id` bigint(20) NOT NULL COMMENT '题目ID',
    `user_id` bigint(20) NOT NULL COMMENT '评论用户ID',
    `content` text NOT NULL COMMENT '评论内容',
    `parent_id` bigint(20) DEFAULT NULL COMMENT '父评论ID，用于回复功能',
    `like_count` int(11) DEFAULT '0' COMMENT '点赞数',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_question` (`question_id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_parent` (`parent_id`),
    CONSTRAINT `fk_qcomment_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_qcomment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_qcomment_parent` FOREIGN KEY (`parent_id`) REFERENCES `question_comment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题目评论表';

-- 题库分类评论表（依赖question_category和user）
CREATE TABLE `category_comment` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `category_id` bigint(20) NOT NULL COMMENT '题库分类ID',
    `user_id` bigint(20) NOT NULL COMMENT '评论用户ID',
    `content` text NOT NULL COMMENT '评论内容',
    `parent_id` bigint(20) DEFAULT NULL COMMENT '父评论ID，用于回复功能',
    `like_count` int(11) DEFAULT '0' COMMENT '点赞数',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_parent` (`parent_id`),
    CONSTRAINT `fk_ccomment_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ccomment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ccomment_parent` FOREIGN KEY (`parent_id`) REFERENCES `category_comment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题库分类评论表';

-- 用户刷题统计表（依赖user）
CREATE TABLE `user_question_stats` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `total_solved` int(11) DEFAULT '0' COMMENT '总刷题数',
    `easy_solved` int(11) DEFAULT '0' COMMENT '简单题数量',
    `medium_solved` int(11) DEFAULT '0' COMMENT '中等题数量',
    `hard_solved` int(11) DEFAULT '0' COMMENT '困难题数量',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user` (`user_id`),
    CONSTRAINT `fk_stats_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户刷题统计表';

-- 用户成就表（依赖user和achievement）
CREATE TABLE `user_achievement` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户成就ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `achievement_id` bigint(20) NOT NULL COMMENT '成就ID',
    `achieve_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_achievement` (`user_id`, `achievement_id`),
    KEY `idx_achievement` (`achievement_id`),
    CONSTRAINT `fk_ua_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_ua_achievement` FOREIGN KEY (`achievement_id`) REFERENCES `achievement` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户成就表';

-- 用户会员表（依赖user和member_level）
CREATE TABLE `user_member` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户会员ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `level_id` bigint(20) NOT NULL COMMENT '会员等级ID',
    `start_time` datetime NOT NULL COMMENT '会员开始时间',
    `end_time` datetime NOT NULL COMMENT '会员结束时间',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_level` (`level_id`),
    CONSTRAINT `fk_member_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_member_level` FOREIGN KEY (`level_id`) REFERENCES `member_level` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户会员表';

-- 用户积分表（依赖user）
CREATE TABLE `user_points` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '积分记录ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `points` int(11) NOT NULL DEFAULT '0' COMMENT '当前积分',
    `total_points` int(11) NOT NULL DEFAULT '0' COMMENT '累计获得积分',
    `used_points` int(11) NOT NULL DEFAULT '0' COMMENT '已使用积分',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user` (`user_id`),
    CONSTRAINT `fk_points_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户积分表';

-- 积分变动记录表（依赖user）
CREATE TABLE `points_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `points` int(11) NOT NULL COMMENT '变动积分',
    `type` tinyint(4) NOT NULL COMMENT '类型 1:获得 2:使用',
    `source` tinyint(4) NOT NULL COMMENT '来源 1:签到 2:答题 3:购买题库 4:其他',
    `description` varchar(200) DEFAULT NULL COMMENT '变动说明',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    CONSTRAINT `fk_record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='积分变动记录表';

-- 题库权限表（依赖question_category和member_level）
CREATE TABLE `question_permission` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `category_id` bigint(20) NOT NULL COMMENT '题库分类ID',
    `points_required` int(11) NOT NULL DEFAULT '0' COMMENT '所需积分',
    `is_member_free` tinyint(4) NOT NULL DEFAULT '0' COMMENT '会员是否免费 1:是 0:否',
    `min_member_level` bigint(20) DEFAULT NULL COMMENT '最低会员等级',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:公开 2:私有 3:关闭',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_category` (`category_id`),
    KEY `idx_member_level` (`min_member_level`),
    CONSTRAINT `fk_permission_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_permission_level` FOREIGN KEY (`min_member_level`) REFERENCES `member_level` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题库权限表';

-- 用户题库权限表（依赖user和question_category）
CREATE TABLE `user_category_permission` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户权限ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `category_id` bigint(20) NOT NULL COMMENT '题库分类ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_category` (`user_id`, `category_id`),
    KEY `idx_category` (`category_id`),
    CONSTRAINT `fk_upermission_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_upermission_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户题库权限表';

-- 签到记录表（依赖user）
CREATE TABLE `user_sign_in` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '签到ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `sign_date` date NOT NULL COMMENT '签到日期',
    `sign_time` datetime NOT NULL COMMENT '具体签到时间',
    `sign_ip` varchar(64) NOT NULL COMMENT '签到IP地址',
    `continuous_days` int(11) NOT NULL DEFAULT '1' COMMENT '连续签到天数',
    `points_earned` int(11) NOT NULL DEFAULT '0' COMMENT '获得积分',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_date` (`user_id`, `sign_date`),
    KEY `idx_date` (`sign_date`),
    KEY `idx_sign_time` (`sign_time`),
    CONSTRAINT `fk_sign_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='签到记录表';

-- 答题记录表（依赖user和question）
CREATE TABLE `user_question_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `question_id` bigint(20) NOT NULL COMMENT '题目ID',
    `user_answer` text NOT NULL COMMENT '用户答案',
    `is_correct` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否正确 1:正确 0:错误',
    `answer_time` int(11) NOT NULL DEFAULT '0' COMMENT '答题用时(秒)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_question` (`user_id`, `question_id`),
    KEY `idx_question` (`question_id`),
    KEY `idx_create_time` (`create_time`),
    CONSTRAINT `fk_record_user_question` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_record_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='答题记录表';

-- 题库评分表（依赖user和question_category）
CREATE TABLE `category_rating` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评分ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `category_id` bigint(20) NOT NULL COMMENT '题库分类ID',
    `rating` tinyint(4) NOT NULL COMMENT '评分(1-5分)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_category` (`user_id`, `category_id`),
    KEY `idx_category` (`category_id`),
    CONSTRAINT `fk_rating_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_rating_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题库评分表';

-- 管理员登录日志表（依赖admin_user）
CREATE TABLE `admin_login_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `admin_id` bigint(20) NOT NULL COMMENT '管理员ID',
    `login_ip` varchar(64) NOT NULL COMMENT '登录IP',
    `login_time` datetime NOT NULL COMMENT '登录时间',
    `login_status` tinyint(4) NOT NULL COMMENT '登录状态 1:成功 0:失败',
    `user_agent` varchar(500) DEFAULT NULL COMMENT '浏览器信息',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_admin` (`admin_id`),
    KEY `idx_login_time` (`login_time`),
    CONSTRAINT `fk_log_admin` FOREIGN KEY (`admin_id`) REFERENCES `admin_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员登录日志表';

-- 错题本表（依赖user和question）
CREATE TABLE `wrong_question_book` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `question_id` bigint(20) NOT NULL COMMENT '题目ID',
    `wrong_count` int(11) NOT NULL DEFAULT '1' COMMENT '错误次数',
    `last_wrong_time` datetime NOT NULL COMMENT '最后错误时间',
    `last_wrong_answer` text NOT NULL COMMENT '最后错误答案',
    `remark` varchar(500) DEFAULT NULL COMMENT '用户备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_question` (`user_id`, `question_id`),
    KEY `idx_question` (`question_id`),
    KEY `idx_last_wrong_time` (`last_wrong_time`),
    CONSTRAINT `fk_wrong_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_wrong_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='错题本表';

-- 用户关注表（依赖user）
CREATE TABLE `user_follow` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关注ID',
    `user_id` bigint(20) NOT NULL COMMENT '关注者ID',
    `followed_user_id` bigint(20) NOT NULL COMMENT '被关注者ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_followed` (`user_id`, `followed_user_id`),
    KEY `idx_followed_user` (`followed_user_id`),
    CONSTRAINT `fk_follow_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_followed_user` FOREIGN KEY (`followed_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `ck_self_follow` CHECK (`user_id` != `followed_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户关注表';

-- 题目标签分类表（依赖project_category）
CREATE TABLE `question_tag_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签分类ID',
    `project_id` bigint(20) NOT NULL COMMENT '所属项目分类ID',
    `name` varchar(50) NOT NULL COMMENT '标签分类名称(如:Java,Mysql)',
    `description` varchar(200) DEFAULT NULL COMMENT '标签分类描述',
    `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序序号',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_project` (`project_id`),
    CONSTRAINT `fk_tag_project` FOREIGN KEY (`project_id`) REFERENCES `project_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='题目标签分类表';

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;