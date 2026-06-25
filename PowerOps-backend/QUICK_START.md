# PowerOps 快速启动指南

## 📋 前置条件

确保您的系统已安装：
- ✅ JDK 17 或更高版本
- ✅ Maven 3.6+
- ✅ Oracle 19c 数据库
- ✅ IntelliJ IDEA（推荐）或其他Java IDE

---

## 🚀 5分钟快速启动

### 步骤1: 配置Oracle数据库

#### 1.1 创建数据库用户（可选）

```sql
-- 以SYSDBA身份登录
CREATE USER powerops IDENTIFIED BY powerops123;
GRANT CONNECT, RESOURCE TO powerops;
GRANT CREATE SEQUENCE TO powerops;
GRANT CREATE VIEW TO powerops;
ALTER USER powerops QUOTA UNLIMITED ON USERS;
```

#### 1.2 执行初始化脚本

在 Navicat 或 SQL*Plus 中执行：

```
D:\backend\project\PowerOps\src\main\resources\sql\init.sql
```

该脚本会：
- ✅ 创建 `SYS_USER` 表
- ✅ 创建 `SEQ_SYS_USER` 序列
- ✅ 插入3个测试账号

#### 1.3 验证数据

```sql
SELECT * FROM SYS_USER;
```

应该看到3条记录：admin、operator1、merchant1

---

### 步骤2: 配置应用

打开 `src/main/resources/application.properties`，确认数据库连接信息：

```properties
# 根据您的Oracle配置修改
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:orcl
spring.datasource.username=powerops
spring.datasource.password=powerops123
```

**如果您的Oracle配置不同，请修改：**
- `localhost:1521:orcl` → 您的主机:端口:SID
- `powerops` → 您的用户名
- `powerops123` → 您的密码

---

### 步骤3: 启动项目

#### 方式1: 使用Maven命令

```bash
cd D:\backend\project\PowerOps
mvn spring-boot:run
```

#### 方式2: 使用IDEA

1. 用IDEA打开项目文件夹
2. 等待Maven依赖下载完成
3. 找到 `PowerOpsApplication.java`
4. 右键 → Run 'PowerOpsApplication'

---

### 步骤4: 验证启动

看到以下日志表示启动成功：

```
Started PowerOpsApplication in X.XXX seconds
```

访问：http://localhost:8080

---

### 步骤5: 测试API

#### 测试1: 用户登录

**使用curl:**
```bash
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

**使用Apifox/Postman:**
- 方法: `POST`
- URL: `http://localhost:8080/api/user/login`
- Body (JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**预期响应:**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1,
    "username": "admin",
    "realName": "系统管理员",
    "role": "ADMIN",
    "token": "a1b2c3d4e5f6..."
  }
}
```

---

#### 测试2: 查询用户列表

**使用curl:**
```bash
curl -X GET "http://localhost:8080/api/user/list?pageNum=1&pageSize=10"
```

**使用Apifox/Postman:**
- 方法: `GET`
- URL: `http://localhost:8080/api/user/list?pageNum=1&pageSize=10`

**预期响应:**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "userId": 1,
        "username": "admin",
        "realName": "系统管理员",
        "phone": "13800138000",
        "email": "admin@powerops.com",
        "role": "ADMIN",
        "status": 1,
        "createTime": "2026-06-17T10:00:00",
        "updateTime": "2026-06-17T10:00:00"
      }
    ],
    "total": 3,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

---

#### 测试3: 新增用户

**使用curl:**
```bash
curl -X POST http://localhost:8080/api/user \
  -H "Content-Type: application/json" \
  -d "{
    \"username\":\"testuser\",
    \"password\":\"test123\",
    \"realName\":\"测试用户\",
    \"phone\":\"13900139000\",
    \"email\":\"test@powerops.com\",
    \"role\":\"OPERATOR\",
    \"status\":1
  }"
```

**使用Apifox/Postman:**
- 方法: `POST`
- URL: `http://localhost:8080/api/user`
- Body (JSON):
```json
{
  "username": "testuser",
  "password": "test123",
  "realName": "测试用户",
  "phone": "13900139000",
  "email": "test@powerops.com",
  "role": "OPERATOR",
  "status": 1
}
```

**预期响应:**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 🔧 常见问题

### 问题1: 无法连接Oracle数据库

**错误信息:**
```
ORA-12505: TNS:listener does not currently know of SID given in connect descriptor
```

**解决方案:**
1. 检查Oracle服务是否启动
2. 确认SID是否正确（默认是 `orcl`）
3. 查看 `tnsnames.ora` 文件确认配置

---

### 问题2: Maven依赖下载失败

**解决方案:**
1. 检查网络连接
2. 配置国内Maven镜像（阿里云）
3. 删除 `.m2/repository` 中失败的依赖，重新下载

**阿里云镜像配置** (`settings.xml`):
```xml
<mirror>
  <id>aliyun</id>
  <mirrorOf>*</mirrorOf>
  <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

---

### 问题3: 端口8080被占用

**错误信息:**
```
Web server failed to start. Port 8080 was already in use.
```

**解决方案:**
修改 `application.properties`:
```properties
server.port=8081
```

---

### 问题4: Lombok不生效

**解决方案:**
1. IDEA安装Lombok插件
2. 启用注解处理器: 
   - Settings → Build → Compiler → Annotation Processors
   - 勾选 "Enable annotation processing"

---

## 📊 测试账号

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | ADMIN | 系统管理员 |
| operator1 | operator123 | OPERATOR | 运维人员 |
| merchant1 | merchant123 | MERCHANT | 商户用户 |

---

## 📝 下一步

框架已搭建完成，您可以：

1. ✅ 继续开发其他模块（商铺、设备、用电等）
2. ✅ 添加JWT认证
3. ✅ 添加密码加密（BCrypt）
4. ✅ 编写单元测试
5. ✅ 前端对接（Vue3 + Element Plus）

详细API文档请查看：[API_DOCUMENTATION.md](API_DOCUMENTATION.md)  
项目结构说明请查看：[PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)

---

## 💡 提示

- 所有API返回格式统一为 `{code, message, data}`
- 密码目前是明文存储，生产环境务必加密
- Token目前是简单UUID，生产环境建议使用JWT
- MyBatis XML位于 `src/main/resources/mapper/`
- 日志级别设置为DEBUG，可在控制台查看SQL语句

祝您开发顺利！🎉
