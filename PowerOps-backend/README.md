# PowerOps - 商铺配电运维管理系统

## 项目简介

PowerOps 是一个面向商业综合体的配电运维管理平台，实现商铺、配电设备、用电记录、报警记录、运维工单等信息的数字化管理。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.5.x |
| 数据库 | Oracle | 19c |
| 数据访问 | MyBatis 3 + XML Mapper | 3.0.5 |
| 语言 | Java | 17 |
| 前端 | Vue 3 + Element Plus + Vite | 最新 |
| 工具 | Lombok, Spring Validation | — |

## 项目结构

```
PowerOps/
├── PowerOps-backend/                         # 后端
│   ├── src/main/java/com/example/powerops/
│   │   ├── common/           Result.java, Code.java, BusinessException.java
│   │   ├── config/           GlobalExceptionHandler.java, CorsConfig.java
│   │   ├── controller/       6 个 Controller（User/Shop/Device/Alarm/WorkOrder/PowerRecord）
│   │   ├── service/          6 个 Service（Device/Alarm/WorkOrder 有接口+实现，其余为 @Service 类）
│   │   ├── mapper/           6 个 Mapper 接口
│   │   ├── entity/           6 个实体类（对应 6 张表）
│   │   ├── dto/              LoginRequest, UserRequest, ShopRequest, DeviceRequest
│   │   └── vo/               LoginVO, UserVO, ShopVO
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── mapper/           6 个 XML 映射文件
│   │   └── sql/init.sql      数据库初始化脚本（6 表 DDL + 73 条测试数据）
│   └── pom.xml
├── PowerOps-frontend/                        # 前端
│   ├── src/
│   │   ├── api/              6 个 API 模块（user/shop/device/alarm/workOrder/power）
│   │   ├── views/            7 个页面（登录 + 6 个功能页）
│   │   ├── router/           路由配置（7 条路由）
│   │   ├── store/            用户状态（Pinia）
│   │   └── utils/            Axios 封装（request.js）
│   ├── package.json
│   └── vite.config.js
├── AGENTS.md                                 # 开发日志
└── .gitignore
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- Oracle 19c
- Node.js 16+

### 2. 数据库初始化

连接到 Oracle 数据库后执行 init.sql 脚本。

### 3. 启动后端

```bash
cd PowerOps-backend
mvn spring-boot:run
```

### 4. 启动前端

```bash
cd PowerOps-frontend
npm install
npm run dev
```

### 5. 登录系统

使用 init.sql 中预置的测试账号登录。

## 数据库设计

6 张表，Oracle 序列（SEQ_表名）自增主键，VARCHAR2 状态字段。

### SYS_USER — 系统用户表

| 列名 | 类型 | 可空 | 说明 |
|------|------|------|------|
| USER_ID | NUMBER | N | 主键 |
| USERNAME | VARCHAR2(50) | N | 用户名，UNIQUE |
| PASSWORD | VARCHAR2(100) | N | — |
| REAL_NAME | VARCHAR2(50) | Y | 真实姓名 |
| PHONE | VARCHAR2(20) | Y | 手机号 |
| ROLE_CODE | VARCHAR2(20) | N | 角色：ADMIN / OPERATOR / MERCHANT |
| STATUS | NUMBER | Y | 状态：0-禁用, 1-启用 |
| CREATE_TIME | DATE | Y | 创建时间 |

### SHOP — 商铺表

| 列名 | 类型 | 可空 | 说明 |
|------|------|------|------|
| SHOP_ID | NUMBER | N | 主键 |
| SHOP_NO | VARCHAR2(50) | Y | 商铺编号 |
| SHOP_NAME | VARCHAR2(100) | N | 商铺名称 |
| BUILDING | VARCHAR2(50) | Y | 楼栋 |
| FLOOR_NO | VARCHAR2(20) | Y | 楼层 |
| BUSINESS_TYPE | VARCHAR2(50) | Y | 业态：餐饮 / 零售 / 服务 |
| CONTACT_PERSON | VARCHAR2(50) | Y | 联系人 |
| CONTACT_PHONE | VARCHAR2(20) | Y | 联系电话 |
| STATUS | NUMBER | Y | 状态：0-停用, 1-营业中, 2-装修中 |
| CREATE_TIME | DATE | Y | 创建时间 |

### DEVICE — 设备表

| 列名 | 类型 | 可空 | 说明 |
|------|------|------|------|
| DEVICE_ID | NUMBER | N | 主键 |
| DEVICE_NO | VARCHAR2(50) | Y | 设备编号 |
| DEVICE_NAME | VARCHAR2(100) | N | 设备名称 |
| DEVICE_TYPE | VARCHAR2(50) | N | 类型：变压器 / 配电柜 / 断路器 / 智能电表 / 漏电保护器 |
| SHOP_ID | NUMBER | Y | 所属商铺ID，FK → SHOP |
| STATUS | VARCHAR2(20) | Y | 状态：运行 / 故障 / 检修 |

### ALARM — 报警表

| 列名 | 类型 | 可空 | 说明 |
|------|------|------|------|
| ALARM_ID | NUMBER | N | 主键 |
| DEVICE_ID | NUMBER | N | 设备ID，FK → DEVICE |
| ALARM_TYPE | VARCHAR2(50) | N | 类型：过压 / 欠压 / 过流 / 过载 / 漏电 / 温度过高 |
| ALARM_LEVEL | VARCHAR2(20) | N | 等级：一般 / 严重 / 紧急 |
| ALARM_TIME | DATE | Y | 报警时间 |
| STATUS | VARCHAR2(20) | Y | 状态：未处理 / 已确认 / 已解决 |
| HANDLER_ID | NUMBER | Y | 处理人ID |
| HANDLE_TIME | DATE | Y | 处理时间 |

### WORK_ORDER — 工单表

| 列名 | 类型 | 可空 | 说明 |
|------|------|------|------|
| ORDER_ID | NUMBER | N | 主键 |
| ORDER_NO | VARCHAR2(50) | N | 工单编号（自动生成），UNIQUE |
| ORDER_TYPE | VARCHAR2(20) | N | 类型：维修 / 抢修 |
| DEVICE_ID | NUMBER | Y | 设备ID，FK → DEVICE |
| APPLICANT_ID | NUMBER | Y | 申请人ID |
| HANDLER_ID | NUMBER | Y | 处理人ID |
| PRIORITY | VARCHAR2(20) | Y | 优先级：普通 / 高 / 紧急 |
| DESCRIPTION | VARCHAR2(1000) | Y | 描述 |
| STATUS | VARCHAR2(20) | Y | 状态：待处理 / 处理中 / 已完成 |
| CREATE_TIME | DATE | Y | 创建时间 |
| FINISH_TIME | DATE | Y | 完成时间 |

### POWER_RECORD — 用电记录表

| 列名 | 类型 | 可空 | 说明 |
|------|------|------|------|
| RECORD_ID | NUMBER | N | 主键 |
| SHOP_ID | NUMBER | N | 商铺ID，FK → SHOP |
| RECORD_DATE | DATE | N | 记录日期 |
| CONSUMPTION | NUMBER(10,2) | Y | 用电量（kWh） |

### 表关系

```
SHOP 1:N DEVICE → DEVICE 1:N ALARM / WORK_ORDER
SHOP 1:N POWER_RECORD
```

### 测试数据

| 表名 | 行数 | 说明 |
|------|------|------|
| SYS_USER | 10 | 管理员 + 运维人员 + 商户 |
| SHOP | 10 | 星巴克/优衣库/万达影城等 |
| DEVICE | 16 | 配电柜/智能电表/断路器等 |
| ALARM | 10 | 各级别报警 |
| WORK_ORDER | 11 | 各状态工单 |
| POWER_RECORD | 15 | 2026-06-01 ~ 06-04 用电数据 |
| **合计** | **73** | |

## API 接口

详见 [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## 核心功能模块

| 模块 | 功能 | 状态 |
|------|------|------|
| 用户管理 | CRUD + 分页 + 角色管理（ADMIN/OPERATOR/MERCHANT） | ✅ |
| 商铺管理 | CRUD + 分页 + 按楼栋/类型筛选 | ✅ |
| 设备管理 | CRUD + 分页 + 状态变更（运行/故障/检修） + 按商铺关联 | ✅ |
| 报警中心 | 分页 + 确认/处理流程（未处理→已确认→已解决） | ✅ |
| 工单管理 | 创建 + 指派 + 完成 + 删除 + 按状态/优先级筛选 | ✅ |
| 用电管理 | 分页查询 + 按日期范围筛选 + 新增记录 | ✅ |
| 首页仪表盘 | 统计卡片（商铺数/设备数/未处理报警/待处理工单） | ✅ |

## 状态流转

- **报警**：未处理 → 已确认 → 已解决
- **工单**：待处理 → 处理中 → 已完成
- **设备**：运行 / 故障 / 检修

## 许可证

本项目仅用于学习和课程设计。
