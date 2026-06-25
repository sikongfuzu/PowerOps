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

## 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数验证失败 |
| 401 | 未授权（登录失败） |
| 403 | 禁止访问（账号被禁用） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 用户模块 `/api/user`

### POST `/api/user/login` — 登录

**请求体**:
```json
{ "username": "string", "password": "string" }
```

**响应 data** (`LoginVO`):
```json
{
  "userId": 1,
  "username": "admin",
  "realName": "系统管理员",
  "roleCode": "ADMIN",
  "token": "a1b2c3d4e5f6..."
}
```

---

### GET `/api/user/list` — 分页查询

**查询参数**:
| 参数 | 必填 | 说明 |
|------|------|------|
| username | 否 | 用户名模糊搜索 |
| roleCode | 否 | 角色筛选：ADMIN / OPERATOR / MERCHANT |
| status | 否 | 状态筛选：0-禁用, 1-启用 |
| pageNum | 否 | 页码，默认 1 |
| pageSize | 否 | 每页数量，默认 10 |

**响应 data**:
```json
{
  "list": [
    {
      "userId": 1,
      "username": "admin",
      "realName": "系统管理员",
      "phone": "13800138000",
      "roleCode": "ADMIN",
      "status": 1,
      "createTime": "2026-06-17T10:00:00"
    }
  ],
  "total": 10,
  "pageNum": 1,
  "pageSize": 10
}
```

---

### GET `/api/user/{id}` — 根据 ID 查询用户

**响应 data** (`UserVO`):
```json
{
  "userId": 1,
  "username": "admin",
  "realName": "系统管理员",
  "phone": "13800138000",
  "roleCode": "ADMIN",
  "status": 1,
  "createTime": "2026-06-17T10:00:00"
}
```

---

### POST `/api/user` — 新增用户

**请求体** (`UserRequest`):
```json
{
  "username": "string",
  "password": "string",
  "realName": "string",
  "phone": "string",
  "roleCode": "OPERATOR",
  "status": 1
}
```

**字段验证**:
| 字段 | 规则 |
|------|------|
| username | 必填 |
| password | 必填（仅新增时校验），≥6 位 |
| roleCode | 必填，只能是 ADMIN / OPERATOR / MERCHANT |
| phone | 可选，需符合手机号格式 `1[3-9]XXXXXXXXX` |
| status | 可选，默认 1 |

---

### PUT `/api/user/{id}` — 修改用户

**请求体** (`UserRequest`):
```json
{
  "username": "admin",
  "realName": "系统管理员",
  "phone": "13800138000",
  "roleCode": "ADMIN",
  "status": 1
}
```

---

### DELETE `/api/user/{id}` — 删除用户

---

## 商铺模块 `/api/shop`

### GET `/api/shop/list` — 分页查询

**查询参数**:
| 参数 | 必填 | 说明 |
|------|------|------|
| shopName | 否 | 商铺名称模糊搜索 |
| building | 否 | 楼栋筛选 |
| businessType | 否 | 业态筛选：餐饮 / 零售 / 服务 |
| status | 否 | 状态筛选：0-停用, 1-营业中, 2-装修中 |
| pageNum | 否 | 页码，默认 1 |
| pageSize | 否 | 每页数量，默认 10 |

**响应 data**:
```json
{
  "list": [
    {
      "shopId": 1,
      "shopNo": "S001",
      "shopName": "星巴克咖啡",
      "building": "A栋",
      "floorNo": "1层",
      "businessType": "餐饮",
      "contactPerson": "张三",
      "contactPhone": "13900139001",
      "status": 1,
      "createTime": "2026-06-17T10:00:00"
    }
  ],
  "total": 10,
  "pageNum": 1,
  "pageSize": 10
}
```

---

### GET `/api/shop/{id}` — 根据 ID 查询商铺

**响应 data** (`ShopVO`): 同列表中的对象结构。

---

### POST `/api/shop` — 新增商铺

**请求体** (`ShopRequest`):
```json
{
  "shopName": "新商铺",
  "shopNo": "S011",
  "building": "A栋",
  "floorNo": "1层",
  "businessType": "餐饮",
  "contactPerson": "张三",
  "contactPhone": "13900139001",
  "status": 1
}
```

**字段验证**:
| 字段 | 规则 |
|------|------|
| shopName | 必填 |
| contactPhone | 可选，需符合手机号格式 |
| status | 可选，默认 1 |

---

### PUT `/api/shop/{id}` — 修改商铺

请求体同新增。

---

### DELETE `/api/shop/{id}` — 删除商铺

---

## 设备模块 `/api/device`

### GET `/api/device/list` — 分页查询

**查询参数**:
| 参数 | 必填 | 说明 |
|------|------|------|
| deviceName | 否 | 设备名称模糊搜索 |
| deviceType | 否 | 设备类型：变压器 / 配电柜 / 断路器 / 智能电表 / 漏电保护器 |
| shopId | 否 | 所属商铺 ID |
| status | 否 | 状态：运行 / 故障 / 检修 |
| pageNum | 否 | 页码，默认 1 |
| pageSize | 否 | 每页数量，默认 10 |

**响应 data**:
```json
{
  "list": [
    {
      "deviceId": 1,
      "deviceNo": "D001",
      "deviceName": "星巴克配电柜",
      "deviceType": "配电柜",
      "shopId": 1,
      "status": "运行"
    }
  ],
  "total": 16,
  "pageNum": 1,
  "pageSize": 10
}
```

---

### GET `/api/device/{id}` — 根据 ID 查询设备

---

### GET `/api/device/shop/{shopId}` — 按商铺查询设备

**响应 data** (`List<Device>`):
```json
[
  { "deviceId": 1, "deviceNo": "D001", "deviceName": "星巴克配电柜", "deviceType": "配电柜", "shopId": 1, "status": "运行" },
  { "deviceId": 2, "deviceNo": "D002", "deviceName": "星巴克电表", "deviceType": "智能电表", "shopId": 1, "status": "运行" }
]
```

---

### POST `/api/device` — 新增设备

**请求体** (`DeviceRequest`):
```json
{
  "deviceName": "新设备",
  "deviceNo": "D017",
  "deviceType": "配电柜",
  "shopId": 1,
  "status": "运行"
}
```

**字段验证**:
| 字段 | 规则 |
|------|------|
| deviceName | 必填 |
| deviceNo | 必填 |
| deviceType | 必填 |
| shopId | 必填 |
| status | 可选，默认 "运行" |

---

### PUT `/api/device/{id}` — 修改设备

请求体同新增。

---

### PUT `/api/device/{id}/status` — 更新设备状态

**查询参数**: `status`（运行 / 故障 / 检修）

示例: `PUT /api/device/1/status?status=故障`

---

### DELETE `/api/device/{id}` — 删除设备

---

## 报警模块 `/api/alarm`

### GET `/api/alarm/list` — 分页查询

**查询参数**:
| 参数 | 必填 | 说明 |
|------|------|------|
| deviceId | 否 | 设备 ID |
| alarmType | 否 | 报警类型：过压 / 欠压 / 过流 / 过载 / 漏电 / 温度过高 |
| alarmLevel | 否 | 报警等级：一般 / 严重 / 紧急 |
| status | 否 | 状态：未处理 / 已确认 / 已解决 |
| pageNum | 否 | 页码，默认 1 |
| pageSize | 否 | 每页数量，默认 10 |

**响应 data**:
```json
{
  "list": [
    {
      "alarmId": 1,
      "deviceId": 2,
      "alarmType": "过压",
      "alarmLevel": "严重",
      "alarmTime": "2026-06-18T10:00:00",
      "status": "未处理",
      "handlerId": null,
      "handleTime": null
    }
  ],
  "total": 10,
  "pageNum": 1,
  "pageSize": 10
}
```

---

### GET `/api/alarm/{id}` — 根据 ID 查询报警

---

### POST `/api/alarm` — 新增报警

**请求体** (`Alarm`):
```json
{
  "deviceId": 2,
  "alarmType": "过压",
  "alarmLevel": "严重"
}
```

**注意**: 新增报警时 `status` 默认为 "未处理"，`alarmTime` 默认为当前时间。

---

### PUT `/api/alarm/{id}/confirm` — 确认报警

**查询参数**: `userId`（确认人 ID）

状态变更: 未处理 → 已确认

示例: `PUT /api/alarm/1/confirm?userId=2`

---

### PUT `/api/alarm/{id}/handle` — 处理报警

**查询参数**: `userId`（处理人 ID）

状态变更: 已确认 → 已解决

示例: `PUT /api/alarm/1/handle?userId=2`

---

## 工单模块 `/api/workorder`

### GET `/api/workorder/list` — 分页查询

**查询参数**:
| 参数 | 必填 | 说明 |
|------|------|------|
| orderType | 否 | 工单类型：维修 / 抢修 |
| status | 否 | 状态：待处理 / 处理中 / 已完成 |
| handlerId | 否 | 处理人 ID |
| priority | 否 | 优先级：普通 / 高 / 紧急 |
| pageNum | 否 | 页码，默认 1 |
| pageSize | 否 | 每页数量，默认 10 |

**响应 data**:
```json
{
  "list": [
    {
      "orderId": 1,
      "orderNo": "WO20260618001",
      "orderType": "维修",
      "deviceId": 4,
      "applicantId": 4,
      "handlerId": 2,
      "priority": "高",
      "description": "优衣库配电柜过压报警",
      "status": "处理中",
      "createTime": "2026-06-18T10:00:00",
      "finishTime": null
    }
  ],
  "total": 11,
  "pageNum": 1,
  "pageSize": 10
}
```

---

### GET `/api/workorder/{id}` — 根据 ID 查询工单

---

### POST `/api/workorder` — 创建工单

**请求体** (`WorkOrder`):
```json
{
  "orderType": "维修",
  "deviceId": 4,
  "applicantId": 4,
  "priority": "高",
  "description": "优衣库配电柜过压报警"
}
```

**注意**: `orderNo` 由后端自动生成（格式: WO + yyyyMMdd + 4 位流水号），`status` 默认为 "待处理"。

---

### PUT `/api/workorder/{id}/assign` — 指派工单

**查询参数**: `handlerId`（处理人 ID）

状态变更: 待处理 → 处理中

示例: `PUT /api/workorder/1/assign?handlerId=2`

---

### PUT `/api/workorder/{id}/status` — 更新工单状态

**查询参数**: `status`（新状态值）

示例: `PUT /api/workorder/1/status?status=已完成`

---

### PUT `/api/workorder/{id}/complete` — 完成工单

状态变更: 处理中 → 已完成

---

### PUT `/api/workorder/{id}` — 修改工单信息

**请求体** (`WorkOrder`): 同创建。

---

### DELETE `/api/workorder/{id}` — 删除工单

---

## 用电模块 `/api/power`

### GET `/api/power/list` — 分页查询全部用电记录

**查询参数**:
| 参数 | 必填 | 说明 |
|------|------|------|
| startDate | 否 | 开始日期，格式 YYYY-MM-DD |
| endDate | 否 | 结束日期，格式 YYYY-MM-DD |
| pageNum | 否 | 页码，默认 1 |
| pageSize | 否 | 每页数量，默认 10 |

**响应 data**:
```json
{
  "list": [
    {
      "recordId": 1,
      "shopId": 1,
      "recordDate": "2026-06-01",
      "consumption": 120.5
    }
  ],
  "total": 15,
  "pageNum": 1,
  "pageSize": 10
}
```

---

### GET `/api/power/shop/{shopId}` — 按商铺查询用电记录

**查询参数**: `startDate`, `endDate`（可选）

**响应 data** (`List<PowerRecord>`):
```json
[
  { "recordId": 1, "shopId": 1, "recordDate": "2026-06-01", "consumption": 120.5 },
  { "recordId": 7, "shopId": 1, "recordDate": "2026-06-03", "consumption": 125.7 }
]
```

---

### POST `/api/power` — 新增用电记录

**请求体** (`PowerRecord`):
```json
{
  "shopId": 1,
  "recordDate": "2026-06-05",
  "consumption": 135.2
}
```

**字段验证**:
| 字段 | 规则 |
|------|------|
| shopId | 必填 |
| recordDate | 必填，格式 YYYY-MM-DD |
| consumption | 必填 |
