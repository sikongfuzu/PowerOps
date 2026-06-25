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

Reorganized from flat layout to monorepo structure: backend → `PowerOps-backend/`, frontend → `PowerOps-frontend/`, both under `D:\backend\project\PowerOps\`.

Fixed WorkOrderController `updateOrder` path to `@PutMapping("/{id}")` for RESTful consistency. Removed duplicate ojdbc11 dependency from pom.xml to clear Maven warning.

All 7 course-design requirements confirmed in place:
1. ✅ Unified Result return class (`common/Result.java`)
2. ✅ Global exception handler (`config/GlobalExceptionHandler.java`)
3. ✅ Pagination on all list endpoints (`GET /list` with pageNum/pageSize)
4. ✅ RESTful Controller style
5. ✅ Oracle 19c compatible (Sequence + ROWNUM)
6. ✅ No extra business modules (6 only)
7. ✅ No Redis/MQ/Spring Security

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
