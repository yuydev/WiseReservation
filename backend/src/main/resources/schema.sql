-- 智慧预约管理系统数据库初始化脚本
-- WiseReservation Database Schema

CREATE DATABASE IF NOT EXISTS wise_reservation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wise_reservation;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `openid` VARCHAR(128) NOT NULL COMMENT '微信openid',
    `name` VARCHAR(64) DEFAULT NULL COMMENT '姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    `company` VARCHAR(128) DEFAULT NULL COMMENT '公司名称',
    `role` VARCHAR(32) NOT NULL DEFAULT 'driver' COMMENT '角色: admin, driver, security, dispatcher, department',
    `role_name` VARCHAR(32) DEFAULT NULL COMMENT '角色名称',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 预约表
CREATE TABLE IF NOT EXISTS `reservation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '预约单号',
    `supplier_id` BIGINT DEFAULT NULL COMMENT '供应商ID',
    `supplier_name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
    `material_name` VARCHAR(128) NOT NULL COMMENT '物料名称',
    `plate_number` VARCHAR(20) DEFAULT NULL COMMENT '车牌号',
    `driver_name` VARCHAR(64) DEFAULT NULL COMMENT '司机姓名',
    `driver_phone` VARCHAR(20) DEFAULT NULL COMMENT '司机电话',
    `appointment_time` DATETIME NOT NULL COMMENT '预约时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending, approved, rejected, completed, cancelled',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `approved_by` BIGINT DEFAULT NULL COMMENT '审批人ID',
    `approved_time` DATETIME DEFAULT NULL COMMENT '审批时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_status` (`status`),
    KEY `idx_supplier_name` (`supplier_name`),
    KEY `idx_appointment_time` (`appointment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预约表';

-- 运输订单表
CREATE TABLE IF NOT EXISTS `transport_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(32) NOT NULL COMMENT '订单编号',
    `title` VARCHAR(128) NOT NULL COMMENT '标题',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `destination` VARCHAR(256) NOT NULL COMMENT '目的地',
    `material_info` VARCHAR(256) DEFAULT NULL COMMENT '物料信息',
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '运费',
    `distance` DOUBLE DEFAULT NULL COMMENT '距离(km)',
    `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
    `type` VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '类型: normal, urgent, delivery',
    `status` VARCHAR(20) NOT NULL DEFAULT 'available' COMMENT '状态: available, grabbed, in_transit, completed, cancelled',
    `publisher_id` BIGINT DEFAULT NULL COMMENT '发布者ID',
    `driver_id` BIGINT DEFAULT NULL COMMENT '接单司机ID',
    `grabbed_time` DATETIME DEFAULT NULL COMMENT '抢单时间',
    `completed_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_status` (`status`),
    KEY `idx_driver_id` (`driver_id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运输订单表';

-- 预警/通知表
CREATE TABLE IF NOT EXISTS `alert` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预警ID',
    `title` VARCHAR(128) NOT NULL COMMENT '标题',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `icon` VARCHAR(32) DEFAULT NULL COMMENT '图标',
    `type` VARCHAR(20) NOT NULL DEFAULT 'info' COMMENT '类型: warning, pending, info',
    `status` VARCHAR(20) NOT NULL DEFAULT 'unread' COMMENT '状态: unread, read, handled',
    `user_id` BIGINT DEFAULT NULL COMMENT '所属用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_status` (`user_id`, `status`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预警通知表';

-- 插入测试数据
INSERT INTO `user` (`openid`, `name`, `phone`, `company`, `role`, `role_name`) VALUES
('mock_openid_001', '张明', '13800138001', '智慧物流有限公司', 'dispatcher', '调度管理员'),
('mock_openid_002', '李强', '13800138002', '华安建设集团', 'driver', '司机'),
('mock_openid_003', '王芳', '13800138003', '智慧物流有限公司', 'admin', '管理员');

INSERT INTO `reservation` (`order_no`, `supplier_name`, `material_name`, `plate_number`, `driver_name`, `driver_phone`, `appointment_time`, `status`, `created_by`) VALUES
('YY20260531001', '华安建设集团', '消防系统配件', '苏A·88888', '李强', '13800138002', '2026-05-31 14:00:00', 'pending', 1),
('YY20260531002', '江苏建材有限公司', '钢筋 HRB400', '苏B·66666', '赵刚', '13800138003', '2026-05-31 15:30:00', 'approved', 1),
('YY20260530003', '南京混凝土公司', 'C30混凝土', '苏A·12345', '孙磊', '13800138004', '2026-05-30 08:00:00', 'completed', 1);

INSERT INTO `transport_order` (`order_no`, `title`, `description`, `destination`, `material_info`, `price`, `distance`, `deadline`, `type`, `status`, `publisher_id`) VALUES
('TO20260531001', '钢筋配送', 'HRB400 Φ16 钢筋 20吨', '江宁区建设工地A区', '钢筋 HRB400 / 20吨', 2800.00, 15.2, '2026-05-31 16:00:00', 'delivery', 'available', 1),
('TO20260531002', '混凝土供应', 'C30混凝土 50方 急需', '鼓楼区市政工程', 'C30混凝土 / 50方', 5500.00, 8.7, '2026-05-31 14:30:00', 'urgent', 'available', 1),
('TO20260531003', '管材运输', 'PE给水管 DN200 500米', '浦口区新城开发区', 'PE管材 / 500米', 1600.00, 22.5, '2026-06-01 10:00:00', 'normal', 'available', 1);

INSERT INTO `alert` (`title`, `description`, `icon`, `type`, `status`, `user_id`) VALUES
('车辆异常停留', '苏A·88888 在 A3 道口停留超过 45 分钟', '🚨', 'warning', 'unread', 1),
('送货预约待批', '华安建设集团 - 消防系统配件入库', '🕐', 'pending', 'unread', 1),
('订单完成通知', '订单TO20260530001已完成配送', '✅', 'info', 'read', 1);
