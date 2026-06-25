# PowerOps API 接口文档

## 基础信息

- **Base URL**: `http://localhost:8080/api`
- **返回格式**: JSON
- **统一返回结构**:
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

## 用户模块 (/api/user)

### 1. 用户登录

**接口**: `POST /api/user/login`

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应**:
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

### 2. 分页查询用户列表

**接口**: `GET /api/user/list`

**查询参数**:
- `username` (可选): 用户名模糊搜索
- `role` (可选): 角色筛选 (ADMIN/OPERATOR/MERCHANT)
- `status` (可选): 状态筛选 (0-禁用, 1-启用)
- `pageNum` (默认1): 页码
- `pageSize` (默认10): 每页数量

**示例**: `GET /api/user/list?role=ADMIN&pageNum=1&pageSize=10`

**响应**:
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
    "total": 1,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

---

### 3. 根据ID查询用户

**接口**: `GET /api/user/{id}`

**示例**: `GET /api/user/1`

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
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
}
```

---

### 4. 新增用户

**接口**: `POST /api/user`

**请求体**:
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

**字段验证**:
- `username`: 必填
- `password`: 必填
- `role`: 必填，只能是 ADMIN/OPERATOR/MERCHANT
- `phone`: 可选，需符合手机号格式
- `email`: 可选，需符合邮箱格式

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 5. 更新用户

**接口**: `PUT /api/user/{id}`

**示例**: `PUT /api/user/1`

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123",
  "realName": "系统管理员",
  "phone": "13800138000",
  "email": "admin@powerops.com",
  "role": "ADMIN",
  "status": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 6. 删除用户

**接口**: `DELETE /api/user/{id}`

**示例**: `DELETE /api/user/2`

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数验证失败 |
| 401 | 未授权（用户名或密码错误） |
| 403 | 禁止访问（账号被禁用） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 测试账号

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | admin123 | ADMIN | 系统管理员 |
| operator1 | operator123 | OPERATOR | 运维人员 |
| merchant1 | merchant123 | MERCHANT | 商户用户 |
