# 开店喵 API

餐饮选址评估平台后端服务

## 技术栈

- Java 17
- Spring Boot 3.2.3
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Redis
- JWT (jjwt 0.12.5)

## 项目结构

```
kaidianmiao-api/
├── src/main/java/com/kaidianmiao/
│   ├── KaidianmiaoApplication.java    # 启动类
│   ├── config/                        # 配置类
│   │   ├── CorsConfig.java           # CORS跨域配置
│   │   ├── GlobalExceptionHandler.java # 全局异常处理
│   │   ├── JacksonConfig.java        # JSON序列化配置
│   │   └── MybatisPlusConfig.java    # MyBatis-Plus配置
│   ├── controller/                    # 控制器
│   ├── service/                       # 服务层
│   ├── mapper/                        # MyBatis Mapper
│   ├── entity/                        # 实体类
│   ├── enums/                         # 枚举类
│   │   ├── TaskStatus.java           # 任务状态
│   │   ├── OrderStatus.java          # 订单状态
│   │   └── RiskLevel.java            # 风险等级
│   ├── dto/                           # 数据传输对象
│   ├── common/                        # 公共类
│   │   ├── Result.java               # 统一响应格式
│   │   ├── ErrorCode.java            # 错误码定义
│   │   └── BusinessException.java    # 业务异常
│   └── security/                      # 安全认证
│       ├── JwtAuthenticationFilter.java
│       └── JwtTokenProvider.java
├── src/main/resources/
│   ├── application.yml               # 主配置
│   ├── application-dev.yml           # 开发环境配置
│   └── db/
│       └── schema.sql                # 数据库初始化脚本
└── pom.xml                           # Maven依赖配置
```

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 数据库初始化

```bash
# 执行数据库初始化脚本
mysql -u root -p < src/main/resources/db/schema.sql
```

### 配置

修改 `application-dev.yml` 中的数据库和Redis配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/kaidianmiao
    username: root
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

### 运行

```bash
# 编译
mvn clean package -DskipTests

# 运行
java -jar target/kaidianmiao-api-1.0.0-SNAPSHOT.jar
```

服务启动后访问：http://localhost:8080

## API 响应格式

所有 API 返回统一格式：

```json
{
  "code": 0,
  "message": "success",
  "data": { ... }
}
```

错误响应：

```json
{
  "code": 40001,
  "message": "参数校验失败",
  "userMessage": "请检查输入内容",
  "traceId": "xxx"
}
```

---

## API 端点清单

### 认证接口 `/api/auth`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/wechat-login` | 微信登录 | 否 |

**请求示例：**
```json
{
  "code": "微信授权码"
}
```

**响应示例：**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "nickname": "用户昵称",
      "avatarUrl": "头像URL"
    }
  }
}
```

---

### 用户接口 `/api/user`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/user/me` | 获取当前用户信息 | 是 |
| PUT | `/api/user/me` | 更新用户信息 | 是 |
| GET | `/api/user/reports` | 获取用户报告列表 | 是 |
| GET | `/api/user/orders` | 获取用户订单列表 | 是 |

---

### 分析任务接口 `/api/analysis-task`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/analysis-task` | 创建分析任务 | 是 |
| GET | `/api/analysis-task/{id}/status` | 查询任务状态 | 否 |
| GET | `/api/analysis-task/{id}/report` | 获取报告详情 | 否 |

**创建任务请求示例：**
```json
{
  "city": "北京",
  "district": "朝阳区",
  "category": "茶饮",
  "budget": 100000,
  "area": 50
}
```

**创建任务响应示例：**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "taskId": 1
  }
}
```

---

### 订单接口 `/api/order`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/order` | 创建订单 | 是 |
| GET | `/api/order/{id}` | 获取订单详情 | 否 |
| POST | `/api/order/{id}/confirm-paid` | 用户确认支付 | 是 |

**创建订单请求示例：**
```json
{
  "taskId": 1,
  "productType": 1
}
```

**产品类型：**
- `1` - 基础版 (¥9.9)
- `2` - 完整版 (¥29.9)

---

### 管理后台接口 `/api/admin`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/admin/login` | 管理员登录 | 否 |
| GET | `/api/admin/dashboard` | 获取数据统计 | 是 (管理员) |
| GET | `/api/admin/orders` | 获取订单列表（分页） | 是 (管理员) |
| GET | `/api/admin/orders/pending` | 获取待审核订单 | 是 (管理员) |
| POST | `/api/admin/order/{id}/verify` | 审核订单 | 是 (管理员) |

**管理员登录请求：**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**数据统计响应示例：**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "totalOrders": 100,
    "pendingOrders": 5,
    "paidOrders": 80,
    "totalRevenue": 792.0,
    "totalUsers": 50,
    "totalReports": 75,
    "todayOrders": 3,
    "todayRevenue": 29.7
  }
}
```

**订单列表查询参数：**
- `page` - 页码（默认 1）
- `size` - 每页大小（默认 20）
- `status` - 订单状态筛选（可选）
  - `unpaid` - 待支付
  - `pending_verify` - 待审核
  - `paid` - 已支付
  - `refunded` - 已退款

**订单列表响应示例：**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "items": [
      {
        "id": 1,
        "orderNo": "KDM20260319143000001234",
        "userId": 1,
        "userNickname": "用户昵称",
        "taskId": 1,
        "productType": 1,
        "productName": "基础版",
        "amount": 9.9,
        "status": "pending_verify",
        "statusLabel": "待审核",
        "createdAt": "2026-03-19T14:30:00",
        "userConfirmTime": "2026-03-19T14:31:00",
        "adminVerifyTime": null
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 20
  }
}
```

**审核订单请求：**
```json
{
  "approve": true,
  "remark": "审核通过"
}
```

---

## 数据库表

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| analysis_task | 分析任务表 |
| order | 订单表 |
| report | 报告表 |
| payment_log | 支付流水表 |
| admin | 管理员表 |

## 订单状态流转

```
unpaid (待支付) 
    ↓ 用户确认支付
pending_verify (待审核)
    ↓ 管理员审核
    ├── paid (已支付) → 触发分析
    └── refunded (已退款)
```

## 默认管理员账号

- 用户名：admin
- 密码：admin123

---

*开发规范遵循 V2 版本*