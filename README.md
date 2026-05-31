# WiseReservation - 智慧预约管理系统

智慧物流预约管理系统，包含微信小程序端和Spring Boot后端服务。

## 项目结构

```
WiseReservation/
├── mini-program/          # 微信小程序前端
│   ├── pages/
│   │   ├── workspace/     # 工作台页面
│   │   ├── reservations/  # 预约单页面
│   │   ├── orderHall/     # 抢单大厅页面
│   │   └── profile/       # 我的页面
│   ├── utils/             # 工具函数
│   ├── static/            # 静态资源
│   ├── app.js             # 小程序入口
│   ├── app.json           # 小程序配置
│   └── app.wxss           # 全局样式
├── backend/               # Spring Boot后端
│   ├── src/main/java/com/wise/reservation/
│   │   ├── controller/    # 控制器层
│   │   ├── service/       # 服务层
│   │   ├── model/         # 数据模型
│   │   ├── repository/    # 数据访问层
│   │   ├── config/        # 配置类
│   │   └── common/        # 公共类
│   └── src/main/resources/
│       ├── application.yml  # 应用配置
│       └── schema.sql       # 数据库脚本
└── README.md
```

## 功能模块

### 小程序端
- **工作台**: 角色切换（管理/司机/安保/调度/事业部）、今日数据概览、核心能力入口、待办预警
- **预约单**: 预约单列表、状态筛选、搜索、新建预约、审批操作
- **抢单大厅**: 可用运输订单列表、筛选（全部/附近/紧急/高价值）、抢单
- **我的**: 个人信息、数据统计、功能菜单、系统设置

### 后端API
- `POST /api/auth/login` - 微信登录
- `GET /api/dashboard/overview` - 工作台数据概览
- `GET /api/reservations` - 预约单列表
- `POST /api/reservations` - 创建预约
- `PUT /api/reservations/{id}/status` - 更新预约状态
- `GET /api/orders/available` - 可抢订单列表
- `POST /api/orders/{id}/grab` - 抢单
- `GET /api/user/profile` - 用户信息
- `GET /api/user/stats` - 用户统计数据

## 技术栈

### 前端
- 微信小程序原生开发
- WXML + WXSS + JavaScript

### 后端
- Java 17
- Spring Boot 3.2
- MyBatis-Plus 3.5
- MySQL 8.0
- Redis
- JWT 认证

## 快速开始

### 后端启动
1. 创建 MySQL 数据库，执行 `backend/src/main/resources/schema.sql`
2. 修改 `backend/src/main/resources/application.yml` 中的数据库配置
3. 运行 `mvn spring-boot:run`

### 小程序端
1. 使用微信开发者工具打开 `mini-program` 目录
2. 配置 `app.js` 中的 `baseUrl` 为后端服务地址
3. 编译并预览

## 数据库

系统使用 MySQL 数据库，主要表：
- `user` - 用户表
- `reservation` - 预约表
- `transport_order` - 运输订单表
- `alert` - 预警通知表