# PowerOps Development Log

## Project Structure (2026-06-25)

```
D:\backend\project\PowerOps\              ← Git root / monorepo root
├── PowerOps-backend\                      ← Backend (Spring Boot + MyBatis + Oracle)
│   ├── src/
│   ├── pom.xml
│   └── ...
├── PowerOps-frontend\                     ← Frontend (Vue 3 + Element Plus)
│   ├── src/
│   ├── package.json
│   └── ...
├── AGENTS.md                              ← Dev log
├── .gitignore                             ← Root-level ignores
└── .gitattributes
```

## Session Summary (2026-06-25)

### 目录重组
从扁平结构改为 monorepo：后端 → `PowerOps-backend/`，前端 → `PowerOps-frontend/`，根目录保留 `.git`。

### Bug 修复
- **DeviceRequest** 补 `status` 字段 — 编辑设备时 status 被丢弃导致 Oracle ORA-17004
- **DeviceRequest** 补 `@NotBlank` on `deviceNo` — 前端校验比后端严
- **application.properties** 加 `jdbc-type-for-null=VARCHAR` — 防 null 值触发 Oracle 错误
- **UserRequest** 密码改用 `CreateGroup` 校验组 — 编辑用户时密码可选
- **UserMapper.xml** update 语句用 `<if>` 跳过空密码 — 不再覆盖原密码
- **UserController** createUser 用 `@Validated(CreateGroup.class)` — 仅新增时校验密码
- **PowerRecordController** 新增 `GET /api/power/list` 分页接口 — 全量查询
- **PowerRecordMapper** 新增 `listAll`/`countAll` 方法
- **WorkOrderController** `updateOrder` 改为 `@PutMapping("/{id}")` — RESTful 一致性
- **pom.xml** 删除重复 ojdbc11 依赖 — 消除 Maven 编译警告
- **device/List.vue** 补 `变压器` 设备类型选项（5 种对齐后端）
- **workorder/List.vue** 表格补 `deviceId` 列 — 可查看关联设备
- **PowerRecordMapper.java** 删除多余 `BigDecimal` import

### 前端优化
- **device/List.vue** 商铺字段改为 `el-select filterable` — 从 API 加载商铺列表
- **user/List.vue** 编辑弹窗密码改为可选，提示"留空则不修改"
- **power/List.vue** 改为调用 `GET /api/power/list` 全量分页查询，不再强制 shopId

### 文档更新
- **README.md** — 完整重写：项目结构、技术栈、功能模块表、数据库设计、快速开始
- **API_DOCUMENTATION.md** — 完整重写：6 模块全部接口文档
- **init.sql** — 修正注释：工单 "12条"→"11条"，"60条"→"73条总计"

### 文档审计修复（对照数据库实际结构）
通过 `sqlplus` 直连 Oracle 验证：
- **README.md** — 重写：补全 6 表字段定义（类型/可空/说明）、表关系、测试数据计数（73 条）
- **API_DOCUMENTATION.md** — 重写：补全 3 个遗漏接口（`GET /user/{id}`, `GET /shop/{id}`, `PUT /workorder/{id}/status`）、修正字段名 `role`→`roleCode`、PowerRecord POST 示例 shopId 改为数值型、修正测试数据计数
- **init.sql** — 修正工单注释 "12条"→"11条"、"60条"→"73条总计"

### 全面审计结果
- 6 个实体类与 init.sql DDL 47/47 字段完全一致
- 6 个 Mapper XML 与实体字段完全匹配
- 6 个 Controller/Service 参数与 DTO 完全匹配
- 7 个前端视图 API 调用与后端接口完全匹配
- 后端 42 源文件编译成功，前端构建成功

## Session Summary (2026-06-23)

Major cleanup pass — entities, mapper XMLs, init.sql were NEVER actually simplified by prior sessions. This session fixed all remaining inconsistencies.

### Changes Made

#### Backend - Entities (truly simplified)
- **Device.java**: 16→6 fields (removed ratedVoltage, ratedCurrent, ratedPower, healthScore, parentDeviceId)
- **PowerRecord.java**: 9→4 fields (removed meterId, startReading, endReading; added consumption)
- **Shop.java**: 12→10 fields (removed ratedCapacity, supplyCircuit)
- **Alarm.java**, **WorkOrder.java**, **SysUser.java**: Already clean

#### Backend - Mapper XMLs
- All 6 rewritten to match simplified entities (DeviceMapper, PowerRecordMapper, ShopMapper + 3 clean ones)

#### Backend - Service Interfaces & DTOs
- Created DeviceService, AlarmService, WorkOrderService interfaces + implementations
- Fixed DeviceRequest, ShopRequest, ShopVO to remove old fields

#### Backend - Deletions
- Removed Bill, InspectionPlan entirely (entities, mappers, XMLs, controllers)

#### Frontend
- power/List.vue: Single consumption column
- shop/List.vue: No ratedCapacity/supplyCircuit
- device/List.vue: No ratedVoltage/ratedCurrent/healthScore
- dashboard/Index.vue: Uses `consumption` instead of `endReading - startReading`

#### Database
- init.sql: 6-table DDL (no BILL/INSPECTION_PLAN) + 60 test data records

### Verification
- Backend: `mvn clean compile` — BUILD SUCCESS (42 sources)
- Frontend: `npm run build` — all 7 views built successfully

## Next Steps
1. Run init.sql against Oracle (POWEROPS/123456@//localhost:1521/ORCLPDB)
2. Start backend: `mvn spring-boot:run` (from PowerOps-backend/)
3. Start frontend: `npm run dev` (from PowerOps-frontend/)
4. Login with admin/admin123

## Key Architecture Decisions
- Device status: Chinese strings ("运行"/"故障"/"检修")
- Alarm flow: 未处理 → 已确认 → 已解决
- WorkOrder flow: 待处理 → 处理中 → 已完成
- PowerRecord: single `consumption` field, no meterId
- Frontend Axios interceptor unwraps `res.data`
