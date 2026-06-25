# PowerOps 数据库表与前端字段映射说明

## 📊 表结构对应关系

本文档说明数据库表字段与前端页面字段的对应关系，确保前后端数据一致性。

---

## 1. SYS_USER (用户表)

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| USER_ID | NUMBER | userId | 用户ID |
| USERNAME | VARCHAR2 | username | 用户名 |
| PASSWORD | VARCHAR2 | password | 密码（加密存储） |
| REAL_NAME | VARCHAR2 | realName | 真实姓名 |
| PHONE | VARCHAR2 | phone | 联系电话 |
| ROLE_CODE | VARCHAR2 | roleCode | 角色代码（ADMIN/OPERATOR/MERCHANT） |
| STATUS | NUMBER | status | 状态（1-启用，0-禁用） |
| CREATE_TIME | TIMESTAMP | createTime | 创建时间 |

---

## 2. SHOP (商铺表)

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| SHOP_ID | NUMBER | shopId | 商铺ID |
| SHOP_NO | VARCHAR2 | shopNo | 商铺编号 |
| SHOP_NAME | VARCHAR2 | shopName | 商铺名称 |
| BUILDING | VARCHAR2 | building | 楼栋 |
| FLOOR_NO | VARCHAR2 | floorNo | 楼层 |
| BUSINESS_TYPE | VARCHAR2 | businessType | 业务类型 |
| RATED_CAPACITY | NUMBER | ratedCapacity | 额定容量(kW) |
| SUPPLY_CIRCUIT | VARCHAR2 | supplyCircuit | 供电回路 |
| CONTACT_PERSON | VARCHAR2 | contactPerson | 联系人 |
| CONTACT_PHONE | VARCHAR2 | contactPhone | 联系电话 |
| STATUS | NUMBER | status | 状态（1-营业中，0-未营业） |
| CREATE_TIME | TIMESTAMP | createTime | 创建时间 |

---

## 3. DEVICE (设备表)

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| DEVICE_ID | NUMBER | deviceId | 设备ID |
| DEVICE_NO | VARCHAR2 | deviceNo | 设备编号 |
| DEVICE_NAME | VARCHAR2 | deviceName | 设备名称 |
| DEVICE_TYPE | VARCHAR2 | deviceType | 设备类型（变压器/配电柜/电表） |
| PARENT_DEVICE_ID | NUMBER | parentDeviceId | 父设备ID |
| MODEL | VARCHAR2 | model | 型号 |
| MANUFACTURER | VARCHAR2 | manufacturer | 制造商 |
| INSTALLATION_DATE | DATE | installationDate | 安装日期 |
| SHOP_ID | NUMBER | shopId | 所属商铺ID |
| LOCATION | VARCHAR2 | location | 安装位置 |
| RATED_VOLTAGE | NUMBER | ratedVoltage | 额定电压(V) |
| RATED_CURRENT | NUMBER | ratedCurrent | 额定电流(A) |
| RATED_POWER | NUMBER | ratedPower | 额定功率(kW) |
| HEALTH_SCORE | NUMBER | healthScore | 健康评分(0-100) |
| STATUS | VARCHAR2 | status | 状态（运行/停机/维修） |
| LAST_MAINTAIN_DATE | DATE | lastMaintainDate | 最后维护日期 |
| NEXT_MAINTAIN_DATE | DATE | nextMaintainDate | 下次维护日期 |

---

## 4. POWER_RECORD (用电记录表)

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| RECORD_ID | NUMBER | recordId | 记录ID |
| METER_ID | NUMBER | meterId | 电表ID（关联DEVICE） |
| SHOP_ID | NUMBER | shopId | 商铺ID |
| RECORD_DATE | DATE | recordDate | 记录日期 |
| START_READING | NUMBER | startReading | 起始读数(kWh) |
| END_READING | NUMBER | endReading | 结束读数(kWh) |
| PEAK_CONSUMPTION | NUMBER | peakConsumption | 峰时用电量(kWh) |
| VALLEY_CONSUMPTION | NUMBER | valleyConsumption | 谷时用电量(kWh) |
| CREATE_TIME | TIMESTAMP | createTime | 创建时间 |

**计算字段**：
- 总用电量 = END_READING - START_READING

---

## 5. BILL (电费账单表) ⚠️ 重要修改

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| BILL_ID | NUMBER | billId | 账单ID |
| SHOP_ID | NUMBER | shopId | 商铺ID |
| BILL_MONTH | VARCHAR2 | billMonth | 账单月份（YYYY-MM） |
| TOTAL_CONSUMPTION | NUMBER | **totalConsumption** | 总用电量(kWh) |
| PEAK_PRICE | NUMBER | **peakPrice** | 峰时单价(元/kWh)，默认0.80 |
| VALLEY_PRICE | NUMBER | **valleyPrice** | 谷时单价(元/kWh)，默认0.40 |
| PEAK_FEE | NUMBER | **peakFee** | 峰时电费(元) |
| VALLEY_FEE | NUMBER | **valleyFee** | 谷时电费(元) |
| TOTAL_FEE | NUMBER | **totalFee** | 总电费(元) |
| PAY_STATUS | VARCHAR2 | **payStatus** | 缴费状态（已缴费/未缴费） |
| PAY_TIME | TIMESTAMP | payTime | 缴费时间 |
| CREATE_TIME | TIMESTAMP | createTime | 创建时间 |

**注意**：
- ❌ 旧字段：`totalPower`, `unitPrice`, `totalAmount`, `status`(数字)
- ✅ 新字段：`totalConsumption`, `peakPrice`, `valleyPrice`, `peakFee`, `valleyFee`, `totalFee`, `payStatus`(字符串)

---

## 6. ALARM (报警表) ⚠️ 重要修改

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| ALARM_ID | NUMBER | alarmId | 报警ID |
| DEVICE_ID | NUMBER | deviceId | 设备ID |
| ALARM_TYPE | VARCHAR2 | alarmType | 报警类型（过压/欠压/过流/温度异常） |
| ALARM_LEVEL | VARCHAR2 | alarmLevel | 报警等级（紧急/严重/一般） |
| ALARM_TIME | TIMESTAMP | alarmTime | 报警时间 |
| ALARM_VALUE | NUMBER | **alarmValue** | 报警值 |
| THRESHOLD_VALUE | NUMBER | **thresholdValue** | 阈值 |
| STATUS | VARCHAR2 | **status** | 状态（未处理/已确认/已处理） |
| HANDLER_ID | NUMBER | handlerId | 处理人ID |
| HANDLE_TIME | TIMESTAMP | handleTime | 处理时间 |

**注意**：
- ❌ 旧字段：`currentValue`, `threshold`, `status`(数字0/1/2)
- ✅ 新字段：`alarmValue`, `thresholdValue`, `status`(字符串"未处理"/"已确认"/"已处理")

---

## 7. WORK_ORDER (工单表) ⚠️ 重要修改

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| ORDER_ID | NUMBER | orderId | 工单ID |
| ORDER_NO | VARCHAR2 | **orderNo** | 工单编号（唯一） |
| ORDER_TYPE | VARCHAR2 | orderType | 工单类型（维修/巡检/保养） |
| DEVICE_ID | NUMBER | deviceId | 设备ID |
| APPLICANT_ID | NUMBER | applicantId | 申请人ID |
| HANDLER_ID | NUMBER | handlerId | 处理人ID |
| PRIORITY | VARCHAR2 | **priority** | 优先级（紧急/高/普通/低） |
| POWER_OUTAGE_REQUIRED | NUMBER | powerOutageRequired | 是否需停电（1-是，0-否） |
| DESCRIPTION | VARCHAR2 | description | 工单描述 |
| STATUS | VARCHAR2 | **status** | 状态（待处理/处理中/已完成/已关闭） |
| CREATE_TIME | TIMESTAMP | createTime | 创建时间 |
| FINISH_TIME | TIMESTAMP | **finishTime** | 完成时间 |
| SOLUTION | VARCHAR2 | solution | 解决方案 |

**注意**：
- ❌ 旧字段：`orderId`(作为编号), `priority`(数字1/2/3), `status`(数字0/1/2/3), `completeTime`
- ✅ 新字段：`orderNo`, `priority`(字符串), `status`(字符串), `finishTime`

---

## 8. WORK_ORDER_SHOP (工单-商铺关联表)

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| ID | NUMBER | id | 关联ID |
| ORDER_ID | NUMBER | orderId | 工单ID |
| SHOP_ID | NUMBER | shopId | 商铺ID |

---

## 9. INSPECTION_PLAN (巡检计划表) ⚠️ 重要修改

| 数据库字段 | 类型 | 前端字段 | 说明 |
|-----------|------|---------|------|
| PLAN_ID | NUMBER | planId | 计划ID |
| PLAN_NAME | VARCHAR2 | planName | 计划名称 |
| INSPECTION_CYCLE | VARCHAR2 | **inspectionCycle** | 巡检周期（每日/每周/每月） |
| DEVICE_ID | NUMBER | deviceId | 设备ID |
| EXECUTOR_ID | NUMBER | executorId | 执行人ID |
| NEXT_TIME | TIMESTAMP | **nextTime** | 下次巡检时间 |
| STATUS | VARCHAR2 | status | 状态（启用/禁用） |
| CREATE_TIME | TIMESTAMP | createTime | 创建时间 |

**注意**：
- ❌ 旧字段：`cycle`, `assigneeId`, `nextInspectionTime`
- ✅ 新字段：`inspectionCycle`, `executorId`, `nextTime`

---

## 🔧 已修改的前端文件

### 1. 视图文件（Views）
- ✅ `src/views/bill/List.vue` - 更新账单字段
- ✅ `src/views/alarm/List.vue` - 更新报警字段
- ✅ `src/views/workorder/List.vue` - 更新工单字段
- ✅ `src/views/inspection/List.vue` - 更新巡检字段

### 2. API文件
- ✅ `src/api/bill.js` - 完善账单API接口

---

## 📝 开发注意事项

### 1. 状态字段类型变化
- **之前**：使用数字（0, 1, 2, 3）
- **现在**：使用字符串（"未处理", "已确认", "已处理"等）

### 2. 优先级字段变化
- **之前**：数字（1-低，2-中，3-高）
- **现在**：字符串（"紧急", "高", "普通", "低"）

### 3. 缴费状态变化
- **之前**：`status` (0-未缴费，1-已缴费)
- **现在**：`payStatus` ("未缴费", "已缴费")

### 4. 时间字段命名
- **之前**：`completeTime`（工单完成时间）
- **现在**：`finishTime`（工单完成时间）

---

## 🎯 后端API对接建议

### RESTful API设计示例

```javascript
// 账单管理
GET    /api/bill/list?shopId=1&month=2024-06&payStatus=未缴费
GET    /api/bill/{billId}
POST   /api/bill/generate
PUT    /api/bill/{billId}/pay

// 报警管理
GET    /api/alarm/list?status=未处理&level=紧急
GET    /api/alarm/{alarmId}
PUT    /api/alarm/{alarmId}/confirm
PUT    /api/alarm/{alarmId}/handle
POST   /api/alarm/{alarmId}/workorder

// 工单管理
GET    /api/workorder/list?status=待处理&priority=紧急
GET    /api/workorder/{orderId}
POST   /api/workorder
PUT    /api/workorder/{orderId}/accept
PUT    /api/workorder/{orderId}/complete

// 巡检计划
GET    /api/inspection/list?status=启用
GET    /api/inspection/{planId}
POST   /api/inspection
PUT    /api/inspection/{planId}
```

---

## 📊 数据类型对照

| Oracle类型 | JavaScript类型 | 说明 |
|-----------|---------------|------|
| NUMBER | Number | 数字类型 |
| VARCHAR2 | String | 字符串类型 |
| TIMESTAMP | String | 时间戳（ISO格式字符串） |
| DATE | String | 日期（YYYY-MM-DD格式字符串） |

---

**更新日期**：2024-06-17  
**版本**：v1.1.0（根据实际表结构调整）
