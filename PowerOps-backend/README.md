# PowerOps - 商铺配电运维管理系统

## 项目简介

PowerOps 是一个面向商业综合体的配电运维管理平台，实现商铺、配电设备、用电记录、报警记录、运维工单等信息的数字化管理。

## 技术栈

- **后端**: Spring Boot 3.x + MyBatis + Oracle 19c
- **开发语言**: Java 17
- **构建工具**: Maven
- **其他**: Lombok, Spring Validation

## 项目结构

```
com.powerops
├── common              # 通用类
│   ├── Result.java     # 统一返回结果
│   ├── Code.java       # 状态码常量
│   └── BusinessException.java  # 业务异常
├── config              # 配置类
│   └── GlobalExceptionHandler.java  # 全局异常处理
├── controller          # 控制器层
│   └── UserController.java
├── service             # 服务层接口
│   └── UserService.java
├── service.impl        # 服务层实现
│   └── UserServiceImpl.java
├── mapper              # Mapper接口
│   └── UserMapper.java
├── entity              # 实体类
│   └── SysUser.java
├── dto                 # 数据传输对象
│   ├── LoginRequest.java
│   └── UserRequest.java
├── vo                  # 视图对象
│   ├── LoginVO.java
│   └── UserVO.java
└── PowerOpsApplication.java  # 启动类
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- Oracle 19c

### 2. 数据库配置

在 Oracle 数据库中执行初始化脚本：

```bash
src/main/resources/sql/init.sql
```

修改 `application.properties` 中的数据库连接信息：

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl
spring.datasource.username=powerops
spring.datasource.password=powerops123
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

或使用 IDE 直接运行 `PowerOpsApplication` 主类。

### 4. 访问接口

服务启动后访问：`http://localhost:8080`

API 文档详见：[API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## 核心功能模块

### 已完成模块

✅ **用户模块 (SYS_USER)**
- 用户登录
- 用户CRUD
- 分页查询
- 角色管理（ADMIN/OPERATOR/MERCHANT）

### 待开发模块

⏳ 商铺模块 (SHOP)  
⏳ 设备模块 (DEVICE)  
⏳ 用电模块 (POWER_RECORD)  
⏳ 账单模块 (BILL)  
⏳ 报警模块 (ALARM)  
⏳ 工单模块 (WORK_ORDER)  
⏳ 巡检模块 (INSPECTION_PLAN)  

## API 测试示例

### 登录接口

```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 查询用户列表

```bash
curl -X GET "http://localhost:8080/api/user/list?pageNum=1&pageSize=10"
```

### 新增用户

```bash
curl -X POST http://localhost:8080/api/user \
  -H "Content-Type: application/json" \
  -d '{
    "username":"testuser",
    "password":"test123",
    "realName":"测试用户",
    "phone":"13900139000",
    "email":"test@powerops.com",
    "role":"OPERATOR",
    "status":1
  }'
```

## 业务规则

### 1. 报警 → 工单联动

- 插入 ALARM 时自动生成 WORK_ORDER
- alarm_level = 紧急 → priority = 高
- alarm_type → order_type = 维修

### 2. 用电 → 账单生成

- 按 shop_id + 月份统计
- SUM power_record 计算总用电量
- 自动插入 bill 记录

### 3. 工单状态流转

待处理 → 处理中 → 已完成

## 注意事项

1. **密码加密**: 当前使用明文密码，生产环境建议使用 BCrypt 加密
2. **Token机制**: 当前使用简单UUID token，生产环境建议使用 JWT
3. **Oracle序列**: 使用 SEQUENCE + NEXTVAL 实现自增ID
4. **表名规范**: Oracle 表名全部大写

## 开发计划

- [ ] 完成商铺模块
- [ ] 完成设备模块
- [ ] 完成用电模块
- [ ] 完成账单模块
- [ ] 完成报警模块
- [ ] 完成工单模块
- [ ] 完成巡检模块
- [ ] 添加 JWT 认证
- [ ] 添加密码加密
- [ ] 添加单元测试

## 许可证

本项目仅用于学习和课程设计。
