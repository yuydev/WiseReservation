# WiseReservation

## 智慧物流云台小程序 - 功能与 API 文档

### 项目概述

- **项目名称**：智慧物流云台
- **平台**：微信小程序
- **后端 API 基础地址**：`https://localhost:7153`
- **认证方式**：`Authorization: GnBearer {token}`

## 1. 认证与用户管理

### 1.1 微信授权登录
- 页面：`pages/login/login`
- API：`POST /api/Auth/wechat/login`
- 请求：`{ code }`
- 响应：`{ token }`

### 1.2 获取用户信息
- API：`GET /api/Auth/me`
- 请求头：`Authorization: GnBearer {token}`
- 返回：用户基础信息、角色列表、调拨权限字段。

### 1.3 身份绑定
- 页面：`pages/identity-binding/identity-binding`
- API：`POST /api/Auth/bind`
- 规则：
  - `Roles` 支持多选
  - `SupplierCode` 在选择“供应商”时必填
  - `EmpNo` 在选择“事业部人员”时必填
  - 绑定成功前不可返回，仅可退出登录

### 1.4 用户名密码登录
- API：`POST /api/Auth/login`
- 请求：`{ username, password }`
- 响应：`{ token }`

## 2. 首页工作台

- 页面：`pages/home/home`
- 根据角色显示快捷入口。
- 待办预警当前为前端模拟，后端接口：`GET /api/Home/alerts?role={role}`。

## 3. 供应商预约

- 页面：`pages/supplier-booking/supplier-booking`
- 分步骤录入：配送信息 → 车辆司机 → 送货单信息 → 自带货
- 待实现 API：`POST /api/Booking/supplier`

## 4. 车辆调度管理

- 页面：`pages/vehicle-scheduling/vehicle-scheduling`
- 功能：按状态查看调拨单、单条/合并排车、指派司机与路线
- 待实现 API：
  - `GET /api/Scheduling/orders?status={status}`
  - `POST /api/Scheduling/assign`

## 5. 内部调拨

- 页面：`pages/transfer/transfer`
- 功能：事业部提交调拨申请
- 待实现 API：`POST /api/Transfer/apply`

## 6. 订单管理

- 页面：`pages/order-list/order-list`
- 功能：状态筛选、关键词搜索
- 待实现 API：`GET /api/Order/list?status={status}&keyword={keyword}`

## 7. 抢单大厅

- 页面：`pages/order-pool/order-pool`
- 功能：司机抢单，按奖励或距离排序
- 待实现 API：
  - `GET /api/Order/pool?sortBy={sortBy}`
  - `POST /api/Order/grab`

## 8. 入场审批

- 页面：`pages/entry-approval/entry-approval`
- 待实现 API：
  - `GET /api/Approval/entry?status={status}`
  - `POST /api/Approval/entry/approve`
  - `POST /api/Approval/entry/reject`

## 9. 安保核验

- 页面：`pages/security-check/security-check`
- 待实现 API：
  - `GET /api/Security/check?status={status}`
  - `POST /api/Security/check/approve`
  - `POST /api/Security/check/reject`

## 10. 司机通行码

- 页面：`pages/driver-qrcode/driver-qrcode`
- 待实现 API：`GET /api/Driver/passcode`

## 11. 消息通知

- 页面：`pages/notifications/notifications`
- 待实现 API：
  - `GET /api/Notification/list?type={type}&page={page}&pageSize={pageSize}`
  - `POST /api/Notification/mark-read`
  - `POST /api/Notification/mark-all-read`

## 12. 个人中心

- 页面：`pages/user-center/user-center`
- 待实现 API：
  - `GET /api/User/profile`
  - `GET /api/User/stats`

## 13. 其他功能（界面已实现，功能待开发）

- `pages/digital-twin/digital-twin`
- `pages/ehs-home/ehs-home`
- `pages/logistics-tracking/logistics-tracking`
- `pages/safety-exam/safety-exam`
- `pages/training-module/training-module`
- `pages/help-center/help-center`

## 14. 数据模型

### 用户角色枚举

```csharp
public enum UserRole
{
    管理员 = 8,
    内物流车队长 = 2,
    内物流司机 = 3,
    内物流车队主管 = 5,
    事业部人员 = 6,
    月台调度 = 88,
    供应商 = 89,
    司机 = 12,
    安保 = 51
}
```

### 订单状态

```csharp
public enum OrderStatus
{
    Pending = 0,
    Processing = 1,
    Completed = 2,
    Cancelled = 3
}
```

### 调拨单状态

```csharp
public enum TransferStatus
{
    Pending = 0,
    Scheduled = 1,
    Transporting = 2,
    Completed = 3,
    Returned = 4
}
```

## 15. API 汇总

### 认证相关

- `POST /api/Auth/wechat/login`
- `POST /api/Auth/login`
- `GET /api/Auth/me`
- `POST /api/Auth/bind`

### 业务相关

- `POST /api/Booking/supplier`
- `GET /api/Scheduling/orders`
- `POST /api/Scheduling/assign`
- `POST /api/Transfer/apply`
- `GET /api/Order/list`
- `GET /api/Order/pool`
- `POST /api/Order/grab`
- `GET /api/Approval/entry`
- `POST /api/Approval/entry/approve`
- `POST /api/Approval/entry/reject`
- `GET /api/Security/check`
- `POST /api/Security/check/approve`
- `POST /api/Security/check/reject`
- `GET /api/Driver/passcode`
- `GET /api/Notification/list`
- `POST /api/Notification/mark-read`
- `POST /api/Notification/mark-all-read`
- `GET /api/User/profile`
- `GET /api/User/stats`

## 16. 开发说明

- 小程序使用 `wx.request` 请求后端。
- 登录流程：`wx.login` 获取 `code` → 换取 `token` → 调 `me` 获取用户信息。
- 请求封装需自动带 `Authorization: GnBearer {token}`。
- 错误处理：
  - `401` 清理 token 并跳登录
  - 网络错误提示“网络请求失败”
  - 业务错误展示后端 message

## 17. 待实现功能清单

### 高优先级
- 供应商预约提交 API
- 车辆调度管理 API
- 内部调拨申请 API
- 订单管理列表 API
- 抢单大厅 API
- 入场审批 API
- 安保核验 API
- 消息通知 API

### 中优先级
- 司机通行码 API
- 用户信息统计 API
- 物流跟踪功能
- 数字孪生功能
- EHS 管理功能

### 低优先级
- 安全考试功能
- 培训学习功能
- 帮助中心功能
- 系统设置功能

## 18. 附录

### 页面路由（均来自需求）

- `/pages/login/login`（无需登录）
- `/pages/identity-binding/identity-binding`
- `/pages/home/home`
- `/pages/supplier-booking/supplier-booking`
- `/pages/vehicle-scheduling/vehicle-scheduling`
- `/pages/transfer/transfer`
- `/pages/order-list/order-list`
- `/pages/order-pool/order-pool`
- `/pages/user-center/user-center`
- `/pages/entry-approval/entry-approval`
- `/pages/security-check/security-check`
- `/pages/driver-qrcode/driver-qrcode`
- `/pages/notifications/notifications`
