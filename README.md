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
│   └── utils/                         # 工具类
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

## 数据库表

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| analysis_task | 分析任务表 |
| order | 订单表 |
| report | 报告表 |
| payment_log | 支付流水表 |
| admin | 管理员表 |

## 默认管理员账号

- 用户名：admin
- 密码：admin123

---

*开发规范遵循 V2 版本*