SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for achievement
-- ----------------------------
DROP TABLE IF EXISTS `achievement`;
CREATE TABLE `achievement`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '成就ID',
    `name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '成就名称',
    `description`     varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '成就描述',
    `icon`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '成就图标URL',
    `condition_type`  tinyint                                                       NOT NULL COMMENT '条件类型 1:总题数 2:简单题 3:中等题 4:困难题',
    `condition_value` int                                                           NOT NULL COMMENT '达成条件数值',
    `status`          tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '成就表'
  ROW_FORMAT = Dynamic;


CREATE TABLE user_daily_submit_count
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id      BIGINT NOT NULL COMMENT '用户唯一标识',
    submit_count INT    NOT NULL DEFAULT 0 COMMENT '当天提交次数',
    submit_date  DATE   NOT NULL COMMENT '日期，年月日格式，例如：2025-06-21',
    update_time  DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间',
    create_time  DATETIME        DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    UNIQUE KEY uniq_user_date (user_id, submit_date)
) COMMENT ='用户每日提交次数记录表-热力图';


-- ----------------------------
-- Table structure for admin_login_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_login_log`;
CREATE TABLE `admin_login_log`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `admin_id`     bigint                                                        NOT NULL COMMENT '管理员ID',
    `login_ip`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '登录IP',
    `login_time`   datetime                                                      NOT NULL COMMENT '登录时间',
    `login_status` tinyint                                                       NOT NULL COMMENT '登录状态 1:成功 0:失败',
    `user_agent`   varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '浏览器信息',
    `create_time`  datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_admin` (`admin_id` ASC) USING BTREE,
    INDEX `idx_login_time` (`login_time` ASC) USING BTREE,
    CONSTRAINT `fk_log_admin` FOREIGN KEY (`admin_id`) REFERENCES `admin_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '管理员登录日志表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '角色名称',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '角色描述',
    `permissions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '权限集合(JSON格式)',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_name` (`name` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '管理员角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户名',
    `password`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码(加密存储)',
    `real_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '真实姓名',
    `email`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '邮箱',
    `phone`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '手机号',
    `role_id`         bigint                                                        NOT NULL COMMENT '角色ID',
    `last_login_ip`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '最后登录IP',
    `last_login_time` datetime                                                      NULL     DEFAULT NULL COMMENT '最后登录时间',
    `login_count`     int                                                           NOT NULL DEFAULT 0 COMMENT '登录次数',
    `status`          tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_username` (`username` ASC) USING BTREE,
    UNIQUE INDEX `idx_email` (`email` ASC) USING BTREE,
    UNIQUE INDEX `idx_phone` (`phone` ASC) USING BTREE,
    INDEX `idx_role` (`role_id` ASC) USING BTREE,
    CONSTRAINT `fk_admin_role` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '管理员表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category_comment
-- ----------------------------
DROP TABLE IF EXISTS `category_comment`;
CREATE TABLE `category_comment`
(
    `id`          bigint                                                NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `category_id` bigint                                                NOT NULL COMMENT '题库分类ID',
    `user_id`     bigint                                                NOT NULL COMMENT '评论用户ID',
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
    `parent_id`   bigint                                                NULL     DEFAULT NULL COMMENT '父评论ID，用于回复功能',
    `like_count`  int                                                   NULL     DEFAULT 0 COMMENT '点赞数',
    `status`      tinyint                                               NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_category` (`category_id` ASC) USING BTREE,
    INDEX `idx_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_parent` (`parent_id` ASC) USING BTREE,
    CONSTRAINT `fk_ccomment_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_ccomment_parent` FOREIGN KEY (`parent_id`) REFERENCES `category_comment` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_ccomment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题库分类评论表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category_rating
-- ----------------------------
DROP TABLE IF EXISTS `category_rating`;
CREATE TABLE `category_rating`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '评分ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `category_id` bigint   NOT NULL COMMENT '题库分类ID',
    `rating`      tinyint  NOT NULL COMMENT '评分(1-5分)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_category` (`user_id` ASC, `category_id` ASC) USING BTREE,
    INDEX `idx_category` (`category_id` ASC) USING BTREE,
    CONSTRAINT `fk_rating_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_rating_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题库评分表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_level
-- ----------------------------
DROP TABLE IF EXISTS `member_level`;
CREATE TABLE `member_level`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '等级ID',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '等级名称',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '等级描述',
    `price`       decimal(10, 2)                                                NOT NULL COMMENT '会员价格',
    `duration`    int                                                           NOT NULL COMMENT '有效期(天)',
    `icon`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '等级图标',
    `benefits`    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '会员权益说明',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '会员等级表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for points_record
-- ----------------------------
DROP TABLE IF EXISTS `points_record`;
CREATE TABLE `points_record`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id`     bigint                                                        NOT NULL COMMENT '用户ID',
    `points`      int                                                           NOT NULL COMMENT '变动积分',
    `type`        tinyint                                                       NOT NULL COMMENT '类型 1:获得 2:使用',
    `source`      tinyint                                                       NOT NULL COMMENT '来源 1:签到 2:答题 3:购买题库 4:其他',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '变动说明',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_record_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '积分变动记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_category
-- ----------------------------
DROP TABLE IF EXISTS `project_category`;
CREATE TABLE `project_category`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '项目分类ID',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '项目分类名称(如:Java面试题、软考)',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '项目描述',
    `sort_order`  int                                                           NOT NULL DEFAULT 0 COMMENT '排序序号',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '项目大分类表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`
(
    `id`               bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `project_id`       bigint                                                        NOT NULL COMMENT '项目ID',
    `category_id`      bigint                                                        NOT NULL COMMENT '分类ID',
    `title`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题目标题',
    `content`          text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '题目内容',
    `answer`           text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '题目答案',
    `difficulty`       tinyint                                                       NOT NULL DEFAULT 1 COMMENT '难度等级 1:简单 2:中等 3:困难',
    `type`             tinyint                                                       NOT NULL DEFAULT 1 COMMENT '题目类型 1:单选 2:多选 3:判断 4:简答',
    `tag_category_ids` json                                                          NOT NULL COMMENT '标签分类ID,用json格式',
    `status`           tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `view_count`       bigint                                                        NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `create_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_project` (`project_id` ASC) USING BTREE,
    INDEX `idx_category` (`category_id` ASC) USING BTREE,
    CONSTRAINT `fk_question_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_question_project` FOREIGN KEY (`project_id`) REFERENCES `project_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 274
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题库内容表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for question_category
-- ----------------------------
DROP TABLE IF EXISTS `question_category`;
CREATE TABLE `question_category`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `project_id`  bigint                                                        NOT NULL COMMENT '所属项目ID',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '分类名称(如:Java基础、多线程)',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '分类描述',
    `creator_id`  bigint                                                        NOT NULL COMMENT '创建者ID',
    `avatar_url`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '分类头像URL',
    `sort_order`  int                                                           NULL     DEFAULT 0 COMMENT '排序序号',
    `view_count`  bigint                                                        NOT NULL DEFAULT 0 COMMENT '访问数量',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_project` (`project_id` ASC) USING BTREE,
    INDEX `idx_creator` (`creator_id` ASC) USING BTREE,
    CONSTRAINT `fk_category_creator` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_category_project` FOREIGN KEY (`project_id`) REFERENCES `project_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 21
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题库分类表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for question_comment
-- ----------------------------
DROP TABLE IF EXISTS `question_comment`;
CREATE TABLE `question_comment`
(
    `id`          bigint                                                NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `question_id` bigint                                                NOT NULL COMMENT '题目ID',
    `user_id`     bigint                                                NOT NULL COMMENT '评论用户ID',
    `content`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
    `parent_id`   bigint                                                NULL     DEFAULT NULL COMMENT '父评论ID，用于回复功能',
    `like_count`  int                                                   NULL     DEFAULT 0 COMMENT '点赞数,冗余设计采取定时更新方案',
    `status`      tinyint                                               NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_question` (`question_id` ASC) USING BTREE,
    INDEX `idx_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_parent` (`parent_id` ASC) USING BTREE,
    CONSTRAINT `fk_qcomment_parent` FOREIGN KEY (`parent_id`) REFERENCES `question_comment` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_qcomment_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_qcomment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题目评论表'
  ROW_FORMAT = Dynamic;

# 评论点赞表
CREATE TABLE `question_comment_like`
(
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `comment_id`  BIGINT   NOT NULL COMMENT '评论ID',
    `user_id`     BIGINT   NOT NULL COMMENT '点赞用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_user_comment` (`comment_id`, `user_id`) USING BTREE,
    INDEX `idx_user` (`user_id`) USING BTREE,
    CONSTRAINT `fk_qclike_comment` FOREIGN KEY (`comment_id`) REFERENCES `question_comment` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_qclike_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题目评论点赞记录表';


-- ----------------------------
-- Table structure for question_permission
-- ----------------------------
DROP TABLE IF EXISTS `question_permission`;
CREATE TABLE `question_permission`
(
    `id`               bigint   NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `category_id`      bigint   NOT NULL COMMENT '题库分类ID',
    `points_required`  int      NOT NULL DEFAULT 0 COMMENT '所需积分',
    `is_member_free`   tinyint  NOT NULL DEFAULT 0 COMMENT '会员是否免费 1:是 0:否',
    `min_member_level` bigint   NULL     DEFAULT NULL COMMENT '最低会员等级',
    `status`           tinyint  NOT NULL DEFAULT 1 COMMENT '状态 1:公开 2:私有 3:关闭',
    `create_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_category` (`category_id` ASC) USING BTREE,
    INDEX `idx_member_level` (`min_member_level` ASC) USING BTREE,
    CONSTRAINT `fk_permission_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_permission_level` FOREIGN KEY (`min_member_level`) REFERENCES `member_level` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题库权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for question_tag_category
-- ----------------------------
DROP TABLE IF EXISTS `question_tag_category`;
CREATE TABLE `question_tag_category`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '标签分类ID',
    `project_id`  bigint                                                        NOT NULL COMMENT '所属项目分类ID',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '标签分类名称(如:Java,Mysql)',
    `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '标签分类描述',
    `sort_order`  int                                                           NOT NULL DEFAULT 0 COMMENT '排序序号',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_project` (`project_id` ASC) USING BTREE,
    CONSTRAINT `fk_tag_project` FOREIGN KEY (`project_id`) REFERENCES `project_category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 83
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '题目标签分类表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '用户名',
    `password`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码(加密存储)',
    `email`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '手机号',
    `status`      tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_username` (`username` ASC) USING BTREE,
    UNIQUE INDEX `idx_email` (`email` ASC) USING BTREE,
    UNIQUE INDEX `idx_phone` (`phone` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 52
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_achievement
-- ----------------------------
DROP TABLE IF EXISTS `user_achievement`;
CREATE TABLE `user_achievement`
(
    `id`             bigint   NOT NULL AUTO_INCREMENT COMMENT '用户成就ID',
    `user_id`        bigint   NOT NULL COMMENT '用户ID',
    `achievement_id` bigint   NOT NULL COMMENT '成就ID',
    `achieve_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
    `create_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_achievement` (`user_id` ASC, `achievement_id` ASC) USING BTREE,
    INDEX `idx_achievement` (`achievement_id` ASC) USING BTREE,
    CONSTRAINT `fk_ua_achievement` FOREIGN KEY (`achievement_id`) REFERENCES `achievement` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_ua_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户成就表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_category_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_category_permission`;
CREATE TABLE `user_category_permission`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '用户权限ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `category_id` bigint   NOT NULL COMMENT '题库分类ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_category` (`user_id` ASC, `category_id` ASC) USING BTREE,
    INDEX `idx_category` (`category_id` ASC) USING BTREE,
    CONSTRAINT `fk_upermission_category` FOREIGN KEY (`category_id`) REFERENCES `question_category` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_upermission_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户题库权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_follow
-- ----------------------------
DROP TABLE IF EXISTS `user_follow`;
CREATE TABLE `user_follow`
(
    `id`               bigint   NOT NULL AUTO_INCREMENT COMMENT '关注ID',
    `user_id`          bigint   NOT NULL COMMENT '关注者ID',
    `followed_user_id` bigint   NOT NULL COMMENT '被关注者ID',
    `create_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_followed` (`user_id` ASC, `followed_user_id` ASC) USING BTREE,
    INDEX `idx_followed_user` (`followed_user_id` ASC) USING BTREE,
    CONSTRAINT `fk_follow_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_followed_user` FOREIGN KEY (`followed_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `ck_self_follow` CHECK (`user_id` <> `followed_user_id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户关注表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_member
-- ----------------------------
DROP TABLE IF EXISTS `user_member`;
CREATE TABLE `user_member`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '用户会员ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `level_id`    bigint   NOT NULL COMMENT '会员等级ID',
    `start_time`  datetime NOT NULL COMMENT '会员开始时间',
    `end_time`    datetime NOT NULL COMMENT '会员结束时间',
    `status`      tinyint  NOT NULL DEFAULT 1 COMMENT '状态 1:正常 0:禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id` ASC) USING BTREE,
    INDEX `idx_level` (`level_id` ASC) USING BTREE,
    CONSTRAINT `fk_member_level` FOREIGN KEY (`level_id`) REFERENCES `member_level` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_member_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户会员表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_points
-- ----------------------------
DROP TABLE IF EXISTS `user_points`;
CREATE TABLE `user_points`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT '积分记录ID',
    `user_id`      bigint   NOT NULL COMMENT '用户ID',
    `points`       int      NOT NULL DEFAULT 0 COMMENT '当前积分',
    `total_points` int      NOT NULL DEFAULT 0 COMMENT '累计获得积分',
    `used_points`  int      NOT NULL DEFAULT 0 COMMENT '已使用积分',
    `create_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_points_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户积分表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`
(
    `id`                     bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '资料ID',
    `user_id`                bigint                                                        NOT NULL COMMENT '用户ID',
    `nickname`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '昵称',
    `avatar`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '头像URL',
    `gender`                 tinyint                                                       NULL     DEFAULT 0 COMMENT '性别 0:未知 1:男 2:女',
    `age`                    int                                                           NULL     DEFAULT NULL COMMENT '年龄',
    `birthday`               date                                                          NULL     DEFAULT NULL COMMENT '生日',
    `location`               varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '所属地（城市/国家）',
    `bio`                    varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '个人简介',
    `display_achievement_id` bigint                                                        NULL     DEFAULT NULL COMMENT '展示的成就ID',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `occupation`             int                                                           NULL     DEFAULT NULL COMMENT '职业（枚举类型）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_id` (`user_id` ASC) USING BTREE,
    INDEX `fk_profile_achievement` (`display_achievement_id` ASC) USING BTREE,
    CONSTRAINT `fk_profile_achievement` FOREIGN KEY (`display_achievement_id`) REFERENCES `achievement` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
    CONSTRAINT `fk_profile_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户个人资料表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_project_category
-- ----------------------------
DROP TABLE IF EXISTS `user_project_category`;
CREATE TABLE `user_project_category`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `user_id`     bigint   NOT NULL COMMENT '用户ID，外键关联 user 表',
    `project_id`  bigint   NOT NULL COMMENT '项目分类ID，外键关联 project_category 表',
    `uptime`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，记录最后修改时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，记录首次插入时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user` (`user_id` ASC) USING BTREE COMMENT '每个用户只能选择一个项目分类',
    INDEX `fk_project` (`project_id` ASC) USING BTREE,
    CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `project_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户项目分类表，记录用户选择的唯一项目分类'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Optimized Table structure for user_question_record
-- ----------------------------
DROP TABLE IF EXISTS `user_question_record`;
CREATE TABLE `user_question_record`
(
    `id`           bigint                                                NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id`      bigint                                                NOT NULL COMMENT '用户ID',
    `question_id`  bigint                                                NOT NULL COMMENT '题目ID',
    `user_answer`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户答案',
    `is_submitted` tinyint                                               NOT NULL DEFAULT 0 COMMENT '是否提交 1:已提交 0:未提交',
    `create_time`  datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_question` (`user_id` ASC, `question_id` ASC) USING BTREE,
    INDEX `idx_question` (`question_id` ASC) USING BTREE,
    INDEX `idx_create_time` (`create_time` ASC) USING BTREE,
    CONSTRAINT `fk_record_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_record_user_question` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '答题记录表'
  ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `user_answer_ai_analysis`;
CREATE TABLE `user_answer_ai_analysis`
(
    `id`             bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `record_id`      bigint                                                        NOT NULL COMMENT '答题记录ID（外键）',
    `accuracy_rate`  tinyint UNSIGNED                                              NOT NULL COMMENT 'AI判定的准确率（0~100 的整数）',
    `ai_explanation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT 'AI生成的解析内容',
    `ai_source`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'AI来源模型，如 deepseek R1 7B',
    `is_deleted`     tinyint(1)                                                    NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除（伪删除）',
    `create_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    CONSTRAINT `fk_ai_analysis_record` FOREIGN KEY (`record_id`) REFERENCES `user_question_record` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户答案AI分析表'
  ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for user_question_stats
-- ----------------------------
DROP TABLE IF EXISTS `user_question_stats`;
CREATE TABLE `user_question_stats`
(
    `id`            bigint   NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    `user_id`       bigint   NOT NULL COMMENT '用户ID',
    `total_solved`  int      NULL     DEFAULT 0 COMMENT '总刷题数',
    `easy_solved`   int      NULL     DEFAULT 0 COMMENT '简单题数量',
    `medium_solved` int      NULL     DEFAULT 0 COMMENT '中等题数量',
    `hard_solved`   int      NULL     DEFAULT 0 COMMENT '困难题数量',
    `create_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user` (`user_id` ASC) USING BTREE,
    CONSTRAINT `fk_stats_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户刷题统计表'
  ROW_FORMAT = Dynamic;

CREATE TABLE `user_sign_in`
(
    `id`          BIGINT                                                       NOT NULL AUTO_INCREMENT COMMENT '签到ID',
    `user_id`     BIGINT                                                       NOT NULL COMMENT '用户ID',
    `sign_date`   DATE                                                         NOT NULL COMMENT '签到日期',
    `sign_time`   DATETIME                                                     NOT NULL COMMENT '具体签到时间',
    `sign_ip`     VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签到IP地址',
    `create_time` DATETIME                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_date` (`user_id`, `sign_date`) USING BTREE,
    INDEX `idx_date` (`sign_date`) USING BTREE,
    INDEX `idx_sign_time` (`sign_time`) USING BTREE,
    CONSTRAINT `fk_sign_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
    COMMENT = '签到记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wrong_question_book
-- ----------------------------
DROP TABLE IF EXISTS `wrong_question_book`;
CREATE TABLE `wrong_question_book`
(
    `id`                bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id`           bigint                                                        NOT NULL COMMENT '用户ID',
    `question_id`       bigint                                                        NOT NULL COMMENT '题目ID',
    `wrong_count`       int                                                           NOT NULL DEFAULT 1 COMMENT '错误次数',
    `last_wrong_time`   datetime                                                      NOT NULL COMMENT '最后错误时间',
    `last_wrong_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL COMMENT '最后错误答案',
    `remark`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '用户备注',
    `create_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_user_question` (`user_id` ASC, `question_id` ASC) USING BTREE,
    INDEX `idx_question` (`question_id` ASC) USING BTREE,
    INDEX `idx_last_wrong_time` (`last_wrong_time` ASC) USING BTREE,
    CONSTRAINT `fk_wrong_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_wrong_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '错题本表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 索引优化
-- ----------------------------

-- 为question表添加常用筛选条件的组合索引
ALTER TABLE `question`
    ADD INDEX `idx_difficulty_type` (`difficulty`, `type`);

-- 为question表添加浏览量索引，用于热门问题排序
ALTER TABLE `question`
    ADD INDEX `idx_view_count` (`view_count` DESC);

-- 为question_category表创建排序序号索引，优化排序查询
ALTER TABLE `question_category`
    ADD INDEX `idx_sort_order` (`sort_order`);

-- 为问题标签表创建排序序号索引
ALTER TABLE `question_tag_category`
    ADD INDEX `idx_sort_order` (`sort_order`);

-- ----------------------------
-- 用户访问问题历史记录表(优化版)
-- ----------------------------
DROP TABLE IF EXISTS `user_question_history`;
CREATE TABLE `user_question_history`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `question_id` bigint   NOT NULL COMMENT '题目ID',
    `visit_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user` (`user_id` ASC) USING BTREE COMMENT '用户索引',
    INDEX `idx_user_time` (`user_id` ASC, `visit_time` DESC) USING BTREE COMMENT '用户最近浏览索引',
    INDEX `idx_question` (`question_id` ASC) USING BTREE COMMENT '问题索引',
    CONSTRAINT `fk_history_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_history_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户访问问题历史记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_favorite_question
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite_question`;
CREATE TABLE `user_favorite_question`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '收藏记录ID',
    `user_id`     bigint   NOT NULL COMMENT '用户ID',
    `question_id` bigint   NOT NULL COMMENT '题目ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uniq_user_question` (`user_id`, `question_id`) COMMENT '防止重复收藏',
    INDEX `idx_user` (`user_id`) USING BTREE COMMENT '用户维度查询',
    INDEX `idx_question` (`question_id`) USING BTREE COMMENT '题目维度查询',
    CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT `fk_favorite_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户题目收藏表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base_message
-- ----------------------------
DROP TABLE IF EXISTS `base_message`;
CREATE TABLE `base_message`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '消息记录ID（主键）',
    `user_id`       bigint       NOT NULL COMMENT '接收消息的用户ID',
    `type`          int          NOT NULL COMMENT '消息类型（枚举值）',
    `read_status`   tinyint      NOT NULL DEFAULT 0 COMMENT '阅读状态：0未读，1已读',
    `message_data`  text         NOT NULL COMMENT '消息内容，序列化后的JSON字符串',
    `created_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`) USING BTREE COMMENT '用户维度消息查询',
    INDEX `idx_created_time` (`created_time`) USING BTREE COMMENT '时间排序优化'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户消息记录表'
  ROW_FORMAT = Dynamic;


  -- 创建岗位招聘数据库表（移除申请状态字段）
CREATE TABLE job_items (
    -- 主键
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '岗位唯一标识符',
    
    -- 基本信息字段
    company_name VARCHAR(200) NOT NULL COMMENT '公司名称',
    job_name VARCHAR(200) NOT NULL COMMENT '岗位名称',
    job_description TEXT COMMENT '岗位描述，详细的工作内容和职责说明',
    job_type VARCHAR(50) NOT NULL COMMENT '岗位类型：校招、社招、暑期实习、寒假实习、春招、秋招、日常实习',
    
    -- 福利和地点
    benefits JSON COMMENT '福利待遇列表，存储为JSON数组',
    location VARCHAR(200) NOT NULL COMMENT '工作地点',
    salary_range VARCHAR(100) NOT NULL COMMENT '薪资范围',
    
    -- 时间字段
    publish_time DATETIME NOT NULL COMMENT '发布时间',
    end_time DATETIME NOT NULL COMMENT '申请截止时间',
    
    -- 申请相关（移除 application_status 字段）
    apply_url VARCHAR(500) NOT NULL COMMENT '申请链接',
    referral_code VARCHAR(100) COMMENT '推荐码',
    
    -- 审计字段
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    
    -- 软删除标记
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位招聘信息表';

-- 创建用户申请状态表
CREATE TABLE user_job_applications (
    -- 主键
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '申请记录唯一标识符',
    
    -- 关联字段
    user_id BIGINT NOT NULL COMMENT '用户ID',
    job_id BIGINT NOT NULL COMMENT '岗位ID，关联job_items表',
    
    -- 申请状态
    application_status VARCHAR(50) NOT NULL COMMENT '申请状态：待投递、投递中、待笔试、笔试中、一面、二面、三面',
    
    -- 申请相关信息
    apply_time DATETIME COMMENT '申请时间',
    status_update_time DATETIME COMMENT '状态最后更新时间',
 
    -- 审计字段
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    
    -- 软删除标记
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    
    -- 唯一约束：一个用户对同一个岗位只能有一条申请记录
    UNIQUE KEY uk_user_job (user_id, job_id),
    
    -- 外键约束
    FOREIGN KEY (job_id) REFERENCES job_items(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户岗位申请状态表';

-- ================================
-- 岗位表索引优化
-- ================================

-- 1. 关键词搜索相关索引（支持岗位名称、公司名称、工作地点的模糊查询）
CREATE INDEX idx_job_name ON job_items(job_name);
CREATE INDEX idx_company_name ON job_items(company_name);
CREATE INDEX idx_location ON job_items(location);

-- 2. 筛选条件索引
CREATE INDEX idx_job_type ON job_items(job_type);

-- 3. 时间排序索引（支持按发布时间和截止时间排序）
CREATE INDEX idx_publish_time ON job_items(publish_time DESC);
CREATE INDEX idx_end_time ON job_items(end_time DESC);

-- 4. 岗位类型和时间组合索引
CREATE INDEX idx_type_publish ON job_items(job_type, publish_time DESC);
CREATE INDEX idx_type_end ON job_items(job_type, end_time DESC);

-- 5. 全文搜索索引（用于岗位描述的全文检索）
CREATE FULLTEXT INDEX idx_fulltext_description ON job_items(job_description);

-- 6. 软删除相关索引
CREATE INDEX idx_deleted_type ON job_items(is_deleted, job_type);
CREATE INDEX idx_deleted_publish ON job_items(is_deleted, publish_time DESC);

-- 7. 时间范围查询优化索引
CREATE INDEX idx_publish_end_time ON job_items(publish_time, end_time);

-- 8. 有效岗位查询索引（未删除且未过期）
CREATE INDEX idx_active_jobs ON job_items(is_deleted, end_time, publish_time DESC);

-- ================================
-- 用户申请状态表索引优化
-- ================================

-- 1. 用户维度查询索引
CREATE INDEX idx_user_status ON user_job_applications(user_id, application_status);
CREATE INDEX idx_user_apply_time ON user_job_applications(user_id, apply_time DESC);

-- 2. 岗位维度查询索引
CREATE INDEX idx_job_status ON user_job_applications(job_id, application_status);

-- 3. 申请状态查询索引
CREATE INDEX idx_application_status ON user_job_applications(application_status);
CREATE INDEX idx_status_update_time ON user_job_applications(application_status, status_update_time DESC);

-- 4. 软删除相关索引
CREATE INDEX idx_deleted_user ON user_job_applications(is_deleted, user_id);
CREATE INDEX idx_deleted_status ON user_job_applications(is_deleted, application_status);

-- 5. 复合查询优化索引
CREATE INDEX idx_user_job_status ON user_job_applications(user_id, job_id, application_status);
CREATE INDEX idx_user_deleted_status ON user_job_applications(user_id, is_deleted, application_status);

SET FOREIGN_KEY_CHECKS = 1;
