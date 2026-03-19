-- =============================================
-- 开店喵数据库初始化脚本
-- 版本: 1.0
-- 创建时间: 2026-03-19
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS kaidianmiao 
    DEFAULT CHARACTER SET utf8mb4 
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE kaidianmiao;

-- =============================================
-- 用户表
-- =============================================
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    openid VARCHAR(64) UNIQUE COMMENT '微信openid',
    phone VARCHAR(20) COMMENT '手机号',
    nickname VARCHAR(64) COMMENT '昵称',
    avatar_url VARCHAR(256) COMMENT '头像URL',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_openid (openid),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 分析任务表
-- =============================================
CREATE TABLE IF NOT EXISTS analysis_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    task_type TINYINT DEFAULT 1 COMMENT '任务类型：1-选址评估',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending/analyzing/completed/failed',
    input_json JSON COMMENT '用户输入JSON',
    result_json JSON COMMENT 'AI分析结果JSON',
    score INT COMMENT '评分0-100',
    risk_level VARCHAR(10) COMMENT '风险等级：low/medium/high',
    order_id BIGINT COMMENT '关联订单ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    completed_at DATETIME COMMENT '完成时间',
    INDEX idx_status (status),
    INDEX idx_user (user_id),
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分析任务表';

-- =============================================
-- 订单表
-- =============================================
CREATE TABLE IF NOT EXISTS `order` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(32) UNIQUE NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    task_id BIGINT COMMENT '关联任务ID',
    product_type TINYINT DEFAULT 1 COMMENT '产品类型：1-基础版9.9，2-完整版29.9',
    amount DECIMAL(10,2) COMMENT '金额',
    status VARCHAR(20) DEFAULT 'unpaid' COMMENT '状态：unpaid/pending_verify/paid/refunded',
    user_confirm_time DATETIME COMMENT '用户确认支付时间',
    admin_verify_time DATETIME COMMENT '管理员审核时间',
    admin_id BIGINT COMMENT '审核管理员ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_status (status),
    INDEX idx_user (user_id),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- =============================================
-- 报告表
-- =============================================
CREATE TABLE IF NOT EXISTS report (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    task_id BIGINT UNIQUE COMMENT '关联任务ID',
    pdf_url VARCHAR(256) COMMENT 'PDF报告URL',
    share_image_url VARCHAR(256) COMMENT '分享图片URL',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_task (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报告表';

-- =============================================
-- 支付流水表
-- =============================================
CREATE TABLE IF NOT EXISTS payment_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    amount DECIMAL(10,2) COMMENT '金额',
    payment_method VARCHAR(32) COMMENT '支付方式',
    paid_at DATETIME COMMENT '支付时间',
    remark VARCHAR(256) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order (order_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付流水表';

-- =============================================
-- 管理员表
-- =============================================
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(32) UNIQUE NOT NULL COMMENT '用户名',
    password_hash VARCHAR(128) NOT NULL COMMENT '密码哈希',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- =============================================
-- 初始数据：默认管理员账号
-- 用户名: admin
-- 密码: admin123 (BCrypt加密)
-- =============================================
INSERT INTO admin (username, password_hash) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6v5EH');

-- =============================================
-- 验证表创建
-- =============================================
SELECT 'Tables created successfully!' AS message;
SHOW TABLES;